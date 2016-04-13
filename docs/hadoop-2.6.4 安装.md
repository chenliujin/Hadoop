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

#### 2.4.5 验证
```
$ ssh slave01
$ ssh slave02
```

## 3. JAVA
```
$ yum install java
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
* core-site.xml
```
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://master:9000</value>
  </property>
</configuration>
```
* hdfs-site.xml
```
<configuration>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>/data/hadoop/name/</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>/data/hadoop/data/</value>
  </property>
</configuration>
```
* mapred-site.xml
```
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
```
* slaves
```
slave01
slave02
```

### 4.2 格式化 namenode
```
$ $HADOOP_HOME/bin/hdfs namenode -format
```

### 4.3 启动 dfs
```
$ $HADOOP_HOME/sbin/start-dfs.sh
```
### 4.4 启动 yarn
```
$ $HADOOP_HOME/sbin/start-yarn.sh
```

## 参考文献
* [hadoop 2.6全分布安装](http://www.cnblogs.com/yjmyzz/p/4280069.html)
