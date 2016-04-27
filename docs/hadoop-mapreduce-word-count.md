## mvn
```
$ mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=org.chenliujin.hadoop.mr -DartifactId=hadoop -DpackageName=org.chenliujin.hadoop.mr -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```

## 加载 hadoop 依赖（pom.xml）
```
<dependencies>
  <dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-client</artifactId>
    <version>2.6.4</version>
  </dependency>
</dependencies>
```

## WordCount
```
$ /opt/hadoop/bin/hadoop jar /opt/hadoop/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.6.4-sources.jar org.apache.hadoop.examples.WordCount /data/wordcount /output/wordcount


/opt/hadoop/bin/hadoop jar /root/Hadoop/examples/access_log/target/temperature-1.0-SNAPSHOT.jar hdfs://master:9000/input hdfs://master:9000/output
```




## 参考文献
* [用Maven构建Hadoop项目](http://blog.fens.me/hadoop-maven-eclipse/)
* [在hadoop2.3下运行WordCount程序](http://blog.itpub.net/21819287/viewspace-1119313/)
