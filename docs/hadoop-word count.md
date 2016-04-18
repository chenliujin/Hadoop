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

## 伪分布式测试
```
$ /opt/hadoop/bin/hadoop jar /opt/hadoop/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.6.4-sources.jar org.apache.hadoop.examples.WordCount /data/wordcount /output/wordcount
```




## 参考文献
* [用Maven构建Hadoop项目](http://blog.fens.me/hadoop-maven-eclipse/)
