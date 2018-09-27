# hadoop Safe mode is ON

由于系统断电，内存不足等原因导致dataNode丢失超过设置的丢失百分比，系统自动进入安全模式

解决办法(Solution)*

安装HDFS客户端，并执行如下命令：

步骤 1     执行命令退出安全模式：hadoop dfsadmin -safemode leave

步骤 2     执行健康检查，删除损坏掉的block。  


```
hdfs fsck /
```

先自检，如果自检不通过，加上 `-delete` 参数。

注意: 这种方式会出现数据丢失，损坏的block会被删掉

## Ambari 异常

### HBase file layout needs to be upgraded. You have version null and I want version 8

```
# su hdfs
# hdfs dfs -rm -r /apps/hbase
```
