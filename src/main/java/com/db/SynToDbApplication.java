package com.db;


import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.source.MySqlSourceBuilder;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import com.ververica.cdc.connectors.oracle.OracleSource;

import javax.naming.directory.NoSuchAttributeException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class SynToDbApplication {
   static Logger logger = Logger.getLogger(SynToDbApplication.class.getName());
    public static String type;

    public static void main(String... args) {

        try {
            if (args.length <= 0) {
                throw new NoSuchAttributeException("请按照要求填写数据库链接信息,key=value");
            }
            Properties properties = new Properties();
            for (String arg : args) {
                String[] split = arg.split("=");
                if (split.length != 2) {
                    throw new NoSuchAttributeException("请按照要求填写数据库链接信息,key=value");
                } else {
                    properties.put(split[0], split[1]);
                }
            }
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            //2.Flink-CDC 将读取 binlog 的位置信息以状态的方式保存在 CK,如果想要做到断点续传,需要从 Checkpoint 或者 Savepoint 启动程序
            //2.1 开启 Checkpoint,每隔 5 秒钟做一次 CK
            env.enableCheckpointing(3000);
            //2.3 设置任务关闭的时候保留最后一次 CK 数据
            //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            //2.4 指定从 CK 自动重启策略
            //env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));
            DataStreamSource<String> sourceDs = null;
            type = properties.getProperty("type");
            if(type.equals("mysql")){
                MySqlSource<String> mySqlSource = mysqlSourceInit(properties);
                sourceDs = env
                        .fromSource(mySqlSource, WatermarkStrategy.forMonotonousTimestamps(), "MySQL Source");
            }else if (type.equals("oracle")){
                SourceFunction<String> stringSourceFunction = oracleSourceInit(properties);
                sourceDs = env.addSource(stringSourceFunction);
            }else {
                throw new NoSuchAttributeException("请填写源数据库类型，mysql or oracle");
            }
            Map<String, String> sinkMap = sinkInit(properties);
            sourceDs.addSink(new DbSink(sinkMap));
            String jobName = properties.getProperty("job_name");
            if(jobName==null){
                throw new NoSuchAttributeException("请填写任务名称，job_name=？");
            }
//            mySqlSourceDs.print().setParallelism(10);
            env.execute(jobName);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }




    private static MySqlSource<String> mysqlSourceInit(Properties properties) throws NoSuchAttributeException {
        MySqlSourceBuilder<String> builder = MySqlSource.<String>builder();

        String sourceHostname = properties.getProperty("source_hostname", null);
        if(sourceHostname !=null){
            builder.hostname(sourceHostname);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_hostname");
        }
        String sourcePort = properties.getProperty("source_port", null);
        if(sourcePort!=null){
            builder.port(Integer.parseInt(sourcePort));
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_port");
        }
        String sourceDatabase = properties.getProperty("source_database", null);
        if(sourceDatabase!=null){
            builder.databaseList(sourceDatabase);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_database");
        }
        String sourceTable = properties.getProperty("source_table", null);
        if(sourceTable!=null){
            builder.tableList(sourceTable);
        }else {
            logger.warning("未指定表,默认迁移库下所有表信息");
            builder.tableList(sourceDatabase+".*");
        }
        String sourceUsername = properties.getProperty("source_username", null);
        if(sourceUsername!=null){
            builder.username(sourceUsername);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_username");
        }
        String sourcePassword = properties.getProperty("source_password", null);
        if(sourcePassword!=null){
            builder.password(sourcePassword);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_password");
        }

        String sourceSplitSize = properties.getProperty("source_splitSize", null);
        if(sourceSplitSize!=null){
            builder.splitSize(Integer.parseInt(sourceSplitSize));
        }else {
            logger.warning("未指定分割大小,使用默认值");
        }

        Properties propertiesDebezium = new Properties();
        propertiesDebezium.setProperty("decimal.handling.mode","double");
        propertiesDebezium.setProperty("binary.handling.mode","bytes");
        String columnExclude = properties.getProperty("column_exclude");
        if(columnExclude!=null){
            propertiesDebezium.setProperty("column.exclude.list",columnExclude);
        }

        String columnInclude = properties.getProperty("column_include");
        if(columnInclude!=null){
            propertiesDebezium.setProperty("column.include.list",columnInclude);
        }

        return builder
                .deserializer(new JsonDebeziumDeserializationSchema())
                .serverTimeZone("Asia/Shanghai")
                .debeziumProperties(propertiesDebezium)
                .scanNewlyAddedTableEnabled(true)
                .startupOptions(StartupOptions.latest())
                .includeSchemaChanges(true)
                .build();
    }


    private static SourceFunction<String> oracleSourceInit(Properties properties) throws NoSuchAttributeException {
        OracleSource.Builder<String> builder = OracleSource.<String>builder();
        String sourceHostname = properties.getProperty("source_hostname", null);
        if(sourceHostname !=null){
            //builder.hostname(sourceHostname);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_hostname");
        }
        String sourcePort = properties.getProperty("source_port", null);
        if(sourcePort!=null){
            builder.port(Integer.parseInt(sourcePort));
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_port");
        }
        String sourceDatabase = properties.getProperty("source_database", null);
        if(sourceDatabase!=null){
            builder.database(sourceDatabase);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_database");
        }
        builder.url("jdbc:oracle:thin:@"+sourceHostname+":"+sourcePort+":"+sourceDatabase);
        String sourceTable = properties.getProperty("source_table", null);
        if(sourceTable!=null){
            builder.tableList(sourceTable);
        }else {
            logger.warning("未指定表,默认迁移库下所有表信息");
            builder.tableList(sourceDatabase+".*");
        }
        String sourceUsername = properties.getProperty("source_username", null);
        if(sourceUsername!=null){
            builder.username(sourceUsername);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_username");
        }
        String sourceSchema = properties.getProperty("source_schema", null);
        if(sourceSchema!=null){
            builder.schemaList(sourceSchema);
        }else {
            logger.warning("未指定模式,默认迁移用户同名模式下所有表信息");
            builder.schemaList(sourceUsername);
        }
        String sourcePassword = properties.getProperty("source_password", null);
        if(sourcePassword!=null){
            builder.password(sourcePassword);
        }else {
            throw new NoSuchAttributeException("缺失 source 链接信息 source_password");
        }
        Properties deb = new Properties();
     /*   deb.setProperty("log.mining.strategy", "online_catalog");
        deb.setProperty("log.mining.continuous.mine", "true"); */
     /*   deb.setProperty("execution.checkpointing.interval", "10min");
        deb.setProperty("execution.checkpointing.tolerable-failed-checkpoints", "100");
        deb.setProperty("restart-strategy", "fixed-delay");
        deb.setProperty("restart-strategy.fixed-delay.attempts", "2147483647");*/
        deb.setProperty("database.tablename.case.insensitive", "false");
        deb.setProperty("decimal.handling.mode","string");

        /*String columnExclude = properties.getProperty("column_exclude");
        if(columnExclude!=null){
            deb.setProperty("column.exclude.list",columnExclude);
        }

        String columnInclude = properties.getProperty("column_include");
        if(columnInclude!=null){
            deb.setProperty("column.include.list",columnInclude);
        }*/
        return builder
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                //只读取增量的  注意：不设置默认是先全量读取表然后增量读取日志中的变化
                .debeziumProperties(deb)
                .startupOptions(com.ververica.cdc.connectors.base.options.StartupOptions.initial())
                .build();
    }

    private static Map<String,String> sinkInit(Properties properties) throws NoSuchAttributeException {
        Map<String,String> sinkMap = new HashMap<>(20);
        String sinkHostname = properties.getProperty("sink_hostname", null);
        if(sinkHostname!=null){
            sinkMap.put("sink_hostname",sinkHostname);
        }else {
            throw new NoSuchAttributeException("缺失 sink 链接信息 sink_hostname");
        }
        String sinkPort = properties.getProperty("sink_port", null);
        if(sinkPort!=null){
            sinkMap.put("sink_port",sinkPort);
        }else {
            throw new NoSuchAttributeException("缺失 sink 链接信息 sink_port");
        }
        String sinkDatabase = properties.getProperty("sink_database", null);
        if(sinkDatabase!=null){
            sinkMap.put("sink_database",sinkDatabase);
        }else {
            throw new NoSuchAttributeException("缺失 sink 链接信息 sink_database");
        }
        String sinkUsername = properties.getProperty("sink_username", null);
        if(sinkUsername!=null){
            sinkMap.put("sink_username",sinkUsername);
        }else {
            throw new NoSuchAttributeException("缺失 sink 链接信息 sink_username");
        }
        String sinkPassword = properties.getProperty("sink_password", null);
        if(sinkPassword!=null){
            sinkMap.put("sink_password",sinkPassword);
        }else {
            throw new NoSuchAttributeException("缺失 sink 链接信息 sink_password");
        }
        return sinkMap;
    }

}
