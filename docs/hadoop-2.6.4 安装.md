## 版权
* 作者：<a href="http://www.chenliujin.com">陈柳锦</a>
* 主页：<a href="http://www.chenliujin.com">http://www.chenliujin.com</a>
* 邮箱：liujin.chen@qq.com

## 1. 环境
* CentOS 7
* hadoop-2.6.4

## 2. 服务器

| Hostname| IP             |
| --      | --             |
| master  | 192.168.85.130 |
| slave01 | 192.168.85.132 |
| slave02 | 192.168.85.133 |

### 2.1 hosts
```
$ vim /etc/hosts
192.168.85.130 master
192.168.85.132 slave01
192.168.85.133 slave02
```
### 2.2 hostname
```
$ ssh master
$ hostnamectl --static set-hostname master

$ ssh slave01
$ hostnamectl --static set-hostname slave01

$ ssh slave02
$ hostnamectl --static set-hostname slave02
```
### 2.3 Hadoop 用户
```
$ useradd hadoop
$ passwd hadoop
```

### 2.4 配置 SSH 无密码登录
hadoop工作时，各节点要相互通讯，正常情况下linux之间通讯要提供用户名、密码（目的是保证通讯安全），如果需要人工干预输入密码，显然不方便，做这一步的目的，是让各节点能自动通过安全认证，不影响正常通讯。
#### 2.4.1 先在master上，生成公钥、私钥对
```
# 以hadoop身份登录到系统
$ ssh-keygen -t rsa
```
#### 2.4.2 导入公钥
```
$ cd .ssh
$ cat id_rsa.pub >> authorized_keys
$ chmod 600 authorized_keys
```
测试下 ssh localhost ，如果不需要输入密码，就连接成功，表示ok。

#### 2.4.3 导入 slave01, slave02 的公钥到 master
```
$ cd
$ rsync slave01:.ssh/id_rsa.pub ./
$ cat id_rsa.pub >> .ssh/authorized_keys
$ rm -f id_rsa.pub

$ rsync slave02:.ssh/id_rsa.pub ./
$ cat id_rsa.pub >> .ssh/authorized_keys
$ rm -f id_rsa.pub
```

#### 2.4.4 将 master 的总公钥文件同步到 slave
```
$ rsync ~/.ssh/authorized_keys slave01:.ssh/
$ rsync ~/.ssh/authorized_keys slave02:.ssh/
```

#### 2.4.5 相互之间验证，没有提示信息登录成功才可以
```
# master
$ ssh slave01
$ ssh slave02

# slave01
$ ssh master
$ ssh slave02

# slave02
$ ssh master
$ ssh slave01
```

## 3. JAVA
```
$ yum install java java-1.8.0-openjdk-devel
$ vim /etc/profile
export JAVA_HOME=/usr
```

## 4. 安装 Hadoop
```
$ cd /opt/
$ ln -s hadoop-2.6.4 hadoop
$ chown hadoop:hadoop -R hadoop-2.6.4
```

### 4.1 配置
* hadoop-env.sh
```
export JAVA_HOME=/usr
```
* yarn-env.sh
```
export JAVA_HOME=/usr
```
* core-site.xml<br />
[core-site.xml 默认值](http://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-common/core-default.xml)
```
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://master:9000</value>
  </property>
</configuration>
```
* hdfs-site.xml<br />
[hdfs-site.xml 默认值](http://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)
```
<configuration>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>/data/hadoop/dfs/name/</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>/data/hadoop/dfs/data/</value>
  </property>
</configuration>
```
* mapred-site.xml<br />
[mapred-site.xml 默认值](http://hadoop.apache.org/docs/r2.6.0/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml)
```
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
```
* yarn-site.xml<br />
[yarn-site.xml 默认值](http://hadoop.apache.org/docs/r2.6.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)
```
<configuration>
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
</configuration>
```
* masters （没有则创建）
```
master
```
* slaves
```
slave01
slave02
```

### 4.2 同步 hadoop 到 slave
```
$ rsync /opt/hadoop slave01:/opt/
$ rsync /opt/hadoop slave02:/opt/
```

### 4.3 关闭防火墙或放行相应端口
```
$ systemctl stop firewalld.service
```

### 4.4 格式化 namenode
```
$ $HADOOP_HOME/bin/hdfs namenode -format
```

### 4.5 启动 dfs
```
$ $HADOOP_HOME/sbin/start-dfs.sh
```
### 4.6 启动 yarn
```
$ $HADOOP_HOME/sbin/start-yarn.sh
```

### 4.7 验证
* hdfs 使用情况
```
$ /opt/hadoop/bin/hdfs dfsadmin -report
Configured Capacity: 107321753600 (99.95 GB)
Present Capacity: 103961427968 (96.82 GB)
DFS Remaining: 103961415680 (96.82 GB)
DFS Used: 12288 (12 KB)
DFS Used%: 0.00%
Under replicated blocks: 0
Blocks with corrupt replicas: 0
Missing blocks: 0

-------------------------------------------------
Live datanodes (2):

Name: 192.168.85.132:50010 (slave01)
Hostname: slave01
Decommission Status : Normal
Configured Capacity: 53660876800 (49.98 GB)
DFS Used: 8192 (8 KB)
Non DFS Used: 1681076224 (1.57 GB)
DFS Remaining: 51979792384 (48.41 GB)
DFS Used%: 0.00%
DFS Remaining%: 96.87%
Configured Cache Capacity: 0 (0 B)
Cache Used: 0 (0 B)
Cache Remaining: 0 (0 B)
Cache Used%: 100.00%
Cache Remaining%: 0.00%
Xceivers: 1
Last contact: Fri Apr 15 23:32:35 EDT 2016


Name: 192.168.85.133:50010 (slave02)
Hostname: slave02
Decommission Status : Normal
Configured Capacity: 53660876800 (49.98 GB)
DFS Used: 4096 (4 KB)
Non DFS Used: 1679249408 (1.56 GB)
DFS Remaining: 51981623296 (48.41 GB)
DFS Used%: 0.00%
DFS Remaining%: 96.87%
Configured Cache Capacity: 0 (0 B)
Cache Used: 0 (0 B)
Cache Remaining: 0 (0 B)
Cache Used%: 100.00%
Cache Remaining%: 0.00%
Xceivers: 1
Last contact: Fri Apr 15 23:32:36 EDT 2016
```
* Hadoop UI：http://master:50070
* Yarn UI：http://master:8088

## 参考文献
* [hadoop 2.6全分布安装](http://www.cnblogs.com/yjmyzz/p/4280069.html)
* [CentOS 7.0 hadoop 2.6 安装与配置](http://www.jianshu.com/p/859e10af9796)
