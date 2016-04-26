## mvn
```
$ mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=com.chenliujin.hadoop.mr -DartifactId=temperature
```

## run
* 权限：检查 temperature-1.0-SNAPSHOT.jar 所属的用户和用户组（hadoop:hadoop）
```
$ /opt/hadoop/bin/hdfs dfs -rm -r /output
$ /opt/hadoop/bin/hadoop jar temperature-1.0-SNAPSHOT.jar hdfs://master:9000/input hdfs://master:9000/output
```

## 参考文献
* [Hadoop MapReduce Examples - 最高气温统计](http://my.oschina.net/yanjianhai/blog/261728?fromerr=nwDNQmfz)
