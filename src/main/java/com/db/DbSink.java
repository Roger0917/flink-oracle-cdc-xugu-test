package com.db;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DbSink extends RichSinkFunction<String> {
    static String driver = "com.xugu.cloudjdbc.Driver";

    static String mysqldriver = "com.mysql.jdbc.Driver";

    private String sinkUrl;

    private String sinkUsername;

    private String sinkPassword;
    //private static DruidDataSource dataSource;
    private PreparedStatement ps;
    private Connection connection;

    //AtomicInteger num = new AtomicInteger(0);

    Map<String, TableInfo> tableNameAndColumn = new ConcurrentHashMap<>(1000);

    Map<String,PreparedStatement> insertSqlMap = new ConcurrentHashMap<>(1000);
    Map<String,String> deleteSqlMap = new ConcurrentHashMap<>(1000);

    Map<String,String> updateSqlMap = new ConcurrentHashMap<>(1000);

    /*static {
       *//* try {
            Class.forName("com.xugu.cloudjdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*//*
        //dataSource = new HikariDataSource();
        //dataSource = new DruidDataSource();
        //dataSource.setDriverClassName("com.xugu.cloudjdbc.Driver");
       *//* dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(false);
        dataSource.setValidationQuery("select 1");
        dataSource.setInitialSize(50);
        dataSource.setMaxActive(600);
        dataSource.setMaxWait(6000);
        dataSource.setMinIdle(100);*//*
        super.open(parameters);
        connection = getConnection();

    }*/
    public DbSink(Map<String,String> map) {
        String sinkHostname = map.get("sink_hostname");
        String sinkPort = map.get("sink_port");
        String sinkDatabase = map.get("sink_database");
        this.sinkUrl = "jdbc:mysql://"+sinkHostname+":"+sinkPort+"/"+sinkDatabase+"?useUnicode=true&characterEncoding=UTF-8&serverTimeZone=UTC";
        //this.sinkUrl = "jdbc:xugu://"+sinkHostname+":"+sinkPort+"/"+sinkDatabase;
        this.sinkUsername = map.get("sink_username");
        this.sinkPassword = map.get("sink_password");
       // dataSource.setJdbcUrl("jdbc:xugu://"+sinkHostname+":"+sinkPort+"/"+sinkDatabase);
//        dataSource.setUsername(map.get("sink_username"));
//        dataSource.setPassword(map.get("sink_password"));
//        dataSource.setMaximumPoolSize(100);
//        dataSource.setMaxLifetime(3000000);
//        dataSource.setKeepaliveTime(30000);
        //dataSource.setUsername(map.get("sink_username"));
        //dataSource.setPassword(map.get("sink_password"));
        //dataSource.setDriverClassName(driver);
        //dataSource.setDriverClassName(mysqldriver);
        //dataSource.setUrl("jdbc:mysql://"+sinkHostname+":"+sinkPort+"/"+sinkDatabase+"?serverTimeZone=UTC");
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        ps = connection.prepareStatement("select 1 from dual");
        getMysqlTableInfo();
        //getTableInfo();
        initSql();
        //System.out.println("num: "+num.incrementAndGet());
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (ps != null) {
            ps.close();
        }
        if (connection != null) { //关闭连接和释放资源
            connection.close();
        }
    }

    @Override
    public void invoke(String value, Context context) throws Exception {
        Thread t = Thread.currentThread();
        JSONObject parse = (JSONObject) JSON.parse(value);
        JSONObject after = parse.getJSONObject("after");
        JSONObject before = parse.getJSONObject("before");
        JSONObject source = parse.getJSONObject("source");
        String tableName = source.getString("table");
        String op = parse.getString("op");
        TableInfo tableInfo = tableNameAndColumn.get(tableName.toUpperCase());
        List<List<String>> columns = tableInfo.getColumns();
        int size = columns.size();
        List<String> primaryKeys = tableInfo.getPrimaryKeys();
        String sql ="";
        ps = insertSqlMap.getOrDefault(tableName.toUpperCase(), null);
        try {
            if (op != null) {
                switch (op) {
                    case "c":
                    case "r":
                        if (sql == null) {
                            break;
                        }
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            String type = columns.get(i).get(1);
                            Object obj = after.get(column.toUpperCase());
                            if (obj == null) {
                                obj = after.get(column.toLowerCase());
                            }
                            obj = getFieldData2Pre(obj, type);
                            ps.setObject(i + 1, obj);
                        }
                        //ps.addBatch();
                        //num.getAndIncrement();
                        //if (num.get()%500==0) {
                        //    ps.executeBatch();
                        //}
                        ps.execute();
                        //ps.close();
                        break;
                    case "u":
                        sql = updateSqlMap.getOrDefault(tableName.toUpperCase(), null);
                        if (sql == null) {
                            break;
                        }
                        System.out.println("u :" + sql);
                        ps = connection.prepareStatement(sql);
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            String type = columns.get(i).get(1);
                            Object obj = after.get(column.toUpperCase());
                            if (obj == null) {
                                obj = after.get(column.toLowerCase());
                            }
                            obj = getFieldData2Pre(obj, type);
                            ps.setObject(i + 1, obj);
                        }
                        for (int i = 0; i < primaryKeys.size(); i++) {
                            String key = primaryKeys.get(i);
                            Object obj = before.get(key.toUpperCase());
                            if (obj == null) {
                                obj = before.get(key.toLowerCase());
                            }
                            ps.setObject(i + 1 + size, obj);
                        }
                        ps.executeUpdate();
                        //ps.close();
                        break;
                    case "d":
                        sql = deleteSqlMap.getOrDefault(tableName.toUpperCase(), null);
                        if (sql == null) {
                            break;
                        }
                        System.out.println("d :" + sql);
                        ps = connection.prepareStatement(sql);
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            Object obj = before.get(column.toUpperCase());
                            if (obj == null) {
                                obj = before.get(column.toLowerCase());
                            }
                            ps.setObject(i + 1, obj);
                        }
                        ps.executeUpdate();
                        //ps.close();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println(op + " " + t.getId() + ": " + t.getName() + e.getMessage() + "  " + value);
        }


        /*if (ps != null) {
            sql = insertSqlMap.getOrDefault(tableName.toUpperCase(), null);
            ps = connection.prepareStatement(sql);
        }*/
        /*try {
            if (op != null) {
                switch (op) {
                    case "c":
                    case "r":
                        sql = insertSqlMap.getOrDefault(tableName.toUpperCase(), null);
                        if (sql == null) {
                            break;
                        }
                        System.out.println(t.getId() + ": " + t.getName() + "  " + "c/r :" + sql);
                        ps = connection.prepareStatement(sql);
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            String type = columns.get(i).get(1);
                            Object obj = after.get(column.toUpperCase());
                            if (obj == null) {
                                obj = after.get(column.toLowerCase());
                            }
                            obj = getFieldData2Pre(obj, type);
                            ps.setObject(i + 1, obj);
                        }
                        //ps.addBatch();
                        //num.getAndIncrement();
                        //if (num.get()%500==0) {
                        //    ps.executeBatch();
                        //}
                        ps.execute();
                        ps.close();
                        break;
                    case "u":
                        sql = updateSqlMap.getOrDefault(tableName.toUpperCase(), null);
                        if (sql == null) {
                            break;
                        }
                        System.out.println("u :" + sql);
                        ps = connection.prepareStatement(sql);
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            String type = columns.get(i).get(1);
                            Object obj = after.get(column.toUpperCase());
                            if (obj == null) {
                                obj = after.get(column.toLowerCase());
                            }
                            obj = getFieldData2Pre(obj, type);
                            ps.setObject(i + 1, obj);
                        }
                        for (int i = 0; i < primaryKeys.size(); i++) {
                            String key = primaryKeys.get(i);
                            Object obj = before.get(key.toUpperCase());
                            if (obj == null) {
                                obj = before.get(key.toLowerCase());
                            }
                            ps.setObject(i + 1 + size, obj);
                        }
                        ps.executeUpdate();
                        ps.close();
                        break;
                    case "d":
                        sql = deleteSqlMap.getOrDefault(tableName.toUpperCase(), null);
                        if (sql == null) {
                            break;
                        }
                        System.out.println("d :" + sql);
                        ps = connection.prepareStatement(sql);
                        for (int i = 0; i < size; i++) {
                            String column = columns.get(i).get(0);
                            Object obj = before.get(column.toUpperCase());
                            if (obj == null) {
                                obj = before.get(column.toLowerCase());
                            }
                            ps.setObject(i + 1, obj);
                        }
                        ps.executeUpdate();
                        ps.close();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println(op + " " + t.getId() + ": " + t.getName() + e.getMessage() + "  " + value);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }*/
    }


    /**
     * 获取到库下面所有表的字段信息 与主键信息
     * @throws SQLException
     */
    public void getTableInfo() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            //  查询到该库下面的所有表 以及表的字段  用来拼接sql语句
            String sqlTables = "SELECT table_id, table_name FROM user_tables";
            String sqlColumns = "SELECT table_id,col_no,col_name,type_name FROM user_columns ORDER BY table_id,col_no";
            String sqlPrimary = "SELECT table_id ,keys FROM user_indexes where is_primary is true";
            ps = connection.prepareStatement(sqlTables);
            rs = ps.executeQuery();
            Map<String, String> tableIdAndTableName = new ConcurrentHashMap<>(500);

            // 拿到表名 与表ID
            while (rs.next()){
                String tableId = rs.getString("table_id");
                String tableName = rs.getString("table_name");
                tableIdAndTableName.put(tableId,tableName);
            }
            // 通过表ID 获取到表的列信息
            ps = connection.prepareStatement(sqlColumns);
            rs = ps.executeQuery();
            while (rs.next()){
                List<String> colAndType = new ArrayList<>();
                String tableId = rs.getString("table_id");
                String tableName = tableIdAndTableName.getOrDefault(tableId,null);
                if(tableName!=null){
                    TableInfo tableInfo = tableNameAndColumn.getOrDefault(tableName.toUpperCase(), new TableInfo());
                    List<List<String>> columns = tableInfo.getColumns();
                    if(columns==null){
                        columns=new ArrayList<List<String>>();
                    }
                    colAndType.add(rs.getString("col_name"));
                    colAndType.add(rs.getString("type_name"));
                    columns.add(rs.getInt("col_no"),colAndType);
                    tableInfo.setTableName(tableName);
                    tableInfo.setColumns(columns);
                    tableNameAndColumn.put(tableName.toUpperCase(),tableInfo);
                }
            }

            // 通过表ID 获取到表的主键信息
            ps =  connection.prepareStatement(sqlPrimary);
            rs = ps.executeQuery();
            while (rs.next()){
                String tableId = rs.getString("table_id");
                String tableName = tableIdAndTableName.getOrDefault(tableId,null);
                if(tableName!=null){
                    TableInfo tableInfo = tableNameAndColumn.getOrDefault(tableName.toUpperCase(), new TableInfo());
                    List<String> primaryKeys = tableInfo.getPrimaryKeys();
                    if(primaryKeys == null){
                        primaryKeys = new ArrayList<>();
                    }
                    String keys = rs.getString("keys").replace("\"","");
                    String[] key = keys.split(",");
                    primaryKeys.addAll(Arrays.asList(key));
                    tableInfo.setTableName(tableName);
                    tableInfo.setPrimaryKeys(primaryKeys);
                    tableNameAndColumn.put(tableName.toUpperCase(),tableInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
    }

    public void getMysqlTableInfo() throws SQLException {
        ResultSet rs = null;
        try{
            //  查询到该库下面的所有表 以及表的字段  用来拼接sql语句
            String sqlTables = "select table_id,name from information_schema.INNODB_SYS_TABLES where name like 'lj_sync_test%';";
            String sqlColumns = "select column_name,data_type from information_schema.`COLUMNS` where table_schema = (select database()) and table_name=?;";
            String sqlPrimary = "select column_name from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where constraint_schema=(select database()) and table_name=? " +
                    "and constraint_name=(select constraint_name from information_schema.TABLE_CONSTRAINTS where CONSTRAINT_schema=(select database()) " +
                    "and table_name=? and constraint_type='PRIMARY KEY');";
            ps = connection.prepareStatement(sqlTables);
            rs = ps.executeQuery();
            Map<String, String> tableIdAndTableName = new ConcurrentHashMap<>(500);

            // 拿到表名 与表ID
            while (rs.next()){
                String tableId = rs.getString("table_id");
                String tableName = rs.getString("name");
                tableIdAndTableName.put(tableId,tableName.substring(tableName.indexOf("/")+1));
            }

            for(Map.Entry<String,String> enrty:tableIdAndTableName.entrySet()){
                ps = connection.prepareStatement(sqlColumns);
                String tablename = enrty.getValue();
                ps.setString(1,tablename);
                rs = ps.executeQuery();
                while (rs.next()){
                    List<String> colAndType = new ArrayList<>();
                    TableInfo tableInfo = tableNameAndColumn.getOrDefault(tablename.toUpperCase(), new TableInfo());
                    List<List<String>> columns = tableInfo.getColumns();
                    if(columns==null){
                        columns=new ArrayList<List<String>>();
                    }
                    colAndType.add(rs.getString("column_name"));
                    colAndType.add(rs.getString("data_type"));
                    columns.add(colAndType);
                    tableInfo.setTableName(tablename);
                    tableInfo.setColumns(columns);
                    tableNameAndColumn.put(tablename.toUpperCase(),tableInfo);
                }
            }
           /* // 通过表ID 获取到表的列信息
            ps = connection.prepareStatement(sqlColumns);
            rs = ps.executeQuery();
            while (rs.next()){
                List<String> colAndType = new ArrayList<>();
                String tableId = rs.getString("table_id");
                String tableName = tableIdAndTableName.getOrDefault(tableId,null);
                if(tableName!=null){
                    TableInfo tableInfo = tableNameAndColumn.getOrDefault(tableName.toUpperCase(), new TableInfo());
                    List<List<String>> columns = tableInfo.getColumns();
                    if(columns==null){
                        columns=new ArrayList<List<String>>();
                    }
                    colAndType.add(rs.getString("name"));
                    colAndType.add(rs.getString("type_name"));
                    columns.add(colAndType);
                    tableInfo.setTableName(tableName);
                    tableInfo.setColumns(columns);
                    tableNameAndColumn.put(tableName.toUpperCase(),tableInfo);
                }
            }*/

            for(Map.Entry<String,String> enrty:tableIdAndTableName.entrySet()){
                ps =  connection.prepareStatement(sqlPrimary);
                String tablename = enrty.getValue();
                ps.setString(1,tablename);
                ps.setString(2,tablename);
                rs = ps.executeQuery();
                TableInfo tableInfo = tableNameAndColumn.getOrDefault(enrty.getValue().toUpperCase(), new TableInfo());
                List<String> primaryKeys = tableInfo.getPrimaryKeys();
                if(primaryKeys == null){
                    primaryKeys = new ArrayList<>();
                }
                while(rs.next()){
                    primaryKeys.add(rs.getString(1));
                }
                tableInfo.setTableName(tablename);
                tableInfo.setPrimaryKeys(primaryKeys);
                tableNameAndColumn.put(tablename.toUpperCase(),tableInfo);
            }
           /* // 通过表ID 获取到表的主键信息
            ps =  connection.prepareStatement(sqlPrimary);
            ps.setString(1,);
            rs = ps.executeQuery();
            while (rs.next()){
                String tableId = rs.getString("table_id");
                String tableName = tableIdAndTableName.getOrDefault(tableId,null);
                if(tableName!=null){
                    TableInfo tableInfo = tableNameAndColumn.getOrDefault(tableName.toUpperCase(), new TableInfo());
                    List<String> primaryKeys = tableInfo.getPrimaryKeys();
                    if(primaryKeys == null){
                        primaryKeys = new ArrayList<>();
                    }
                    String keys = rs.getString("keys").replace("\"","");
                    String[] key = keys.split(",");
                    primaryKeys.addAll(Arrays.asList(key));
                    tableInfo.setTableName(tableName);
                    tableInfo.setPrimaryKeys(primaryKeys);
                    tableNameAndColumn.put(tableName.toUpperCase(),tableInfo);
                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 拼装SQL语句   insert  delete
     */
    public void initSql(){
        try {
            Collection<TableInfo> tableInfos = tableNameAndColumn.values();
            StringBuilder insertSb = new StringBuilder();
            StringBuilder updateSb = new StringBuilder();
            StringBuilder deleteSb = new StringBuilder();
            for (TableInfo tableInfo : tableInfos) {
                if(tableInfo == null){
                    continue;
                }
                String tableName = tableInfo.getTableName();
                List<List<String>> columns = tableInfo.getColumns();
                // insert
                insertSb.setLength(0);
                insertSb.append("insert into ")
                        .append(tableName)
                        .append("(");
                StringBuilder insertFixed = new StringBuilder();
                insertFixed.append(" values (");
                int columnCount = columns.size();
                for (int i = 0; i < columnCount; i++) {
                    if (i == columnCount - 1) {
                        insertSb.append(columns.get(i).get(0)).append(")");
                        insertFixed.append("?)");
                    } else {
                        insertSb.append(columns.get(i).get(0)).append(",");
                        insertFixed.append("?,");
                    }
                }
                insertSb.append(insertFixed);
                PreparedStatement ps = connection.prepareStatement(insertSb.toString());
                insertSqlMap.put(tableName.toUpperCase(),ps);

                // update
                List<String> primaryKeys = tableInfo.getPrimaryKeys();
                if(primaryKeys!=null){
                    updateSb.setLength(0);
                    updateSb.append("update ")
                            .append(tableName)
                            .append(" set ");
                    for (int i = 0; i < columnCount; i++) {
                        if (i == columnCount - 1) {
                            updateSb.append(columns.get(i).get(0)).append("=").append("?");
                        } else {
                            updateSb.append(columns.get(i).get(0)).append("=").append("?,");
                        }
                    }
                    updateSb.append(" where ");
                    for (int i = 0; i < primaryKeys.size(); i++) {
                        if (i == primaryKeys.size() - 1) {
                            updateSb.append(columns.get(i).get(0)).append("=").append("?");
                        } else {
                            updateSb.append(columns.get(i).get(0)).append("=").append("? and ");
                        }
                    }
                    updateSqlMap.put(tableName.toUpperCase(),updateSb.toString());
                }

                // delete
                deleteSb.setLength(0);
                deleteSb.append("delete from ")
                        .append(tableName).append(" where ");
                for (int i = 0; i < columnCount; i++) {
                    if (i == columnCount - 1) {
                        deleteSb.append(columns.get(i).get(0)).append("=? ");
                    } else {
                        deleteSb.append(columns.get(i).get(0)).append("=? ").append(" and ");
                    }
                }
                deleteSqlMap.put(tableName.toUpperCase(),deleteSb.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 数据处理
     */
    public Object getFieldData2Pre(Object data,String type) throws ParseException {
        String typeLower = type.toUpperCase();
        switch (typeLower) {
            case "DATE":
                if (data!=null) {
                    data = Util.getDate(Integer.valueOf((Integer) data));
                }
                return data;
            case "TIME":
                if (data!=null){
                    data = Util.getTime(Long.valueOf((Long) data));
                }
                return data;
            case "DATETIME":
            case "TIMESTAMP":
                if (data!=null&&!data.toString().contains(":")) {
                    return DateUtil.date((Long)data/1000);
                }
            case "BLOB":
//                byte[] bData = (byte[]) data;
//                int lobSize=bData.length;
//                ByteString.copyFrom(bData, 0, lobSize);
                return data;
            default:
                return data;
        }
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            //Class.forName(driver);
            Class.forName(mysqldriver);
            con =
                    DriverManager.getConnection(
                            this.sinkUrl, this.sinkUsername, this.sinkPassword);
        } catch (Exception e) {
            System.out.println("-----------mysql get connection has exception , msg = " + e.getMessage());
        }
        return con;
    }
}

