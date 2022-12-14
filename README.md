# MysqlToXuguSyncer

启动参数 mysql 
```shell
type=mysql source_hostname=127.0.0.1 source_port=3306 source_database=test1 source_table=test1.t1 source_username=root source_password=mysql source_splitSize=10000 column_exclude=test1.t1.id,test1.t1.age column_include=test1.t1.name sink_hostname=127.0.0.1 sink_port=5138 sink_database=test1 sink_username=SYSDBA sink_password=SYSDBA job_name=mysql-binlog
```


启动参数 oracle
```shell
type=oracle source_hostname=127.0.0.1 source_port=5121 source_database=test1 source_table=test1.t1 source_username=SYSDBA source_password=SYSDBA column_exclude=test1.t1.id,test1.t1.age column_include=test1.t1.name sink_hostname=127.0.0.1 sink_port=5138 sink_database=test1 sink_username=SYSDBA sink_password=SYSDBA job_name=mysql-binlog
```

## 参数解释

- type=mysql    源数据库类型  可选 oracle/mysql
- source_hostname=127.0.0.1    源数据库的连接地址
- source_port=3306             源数据库的连接端口
- source_database=test1        迁移的源数据库
- source_table=test1.t1        迁移的源数据库中的表,多个表可用逗号隔开，如: test1.t1,test.t2,若是该库下所有表,则用\*表示,即: test1.\*
- source_username=root         源数据库的连接用户名
- source_password=mysql        源数据库的连接用户名的密码
- source_splitSize=10000       可选,使用mysql时才需要使用
- column_exclude=test1.t1.id,test1.t1.age   可选  迁移过程中,忽略的表列名. column_exclude与column_include 不同时使用
- column_include=test1.t1.name              可选  迁移过程中,迁移的表列名. column_exclude与column_include 不同时使用
- sink_hostname=127.0.0.1      目标数据库的连接地址
- sink_port=5138               目标数据库的连接端口
- sink_database=test1          迁移的目标数据库
- sink_username=SYSDBA         目标数据库的连接用户名
- sink_password=SYSDBA         目标数据库的连接用户名的密码
- job_name=mysql-binlog        迁移任务的名称

