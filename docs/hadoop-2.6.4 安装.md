## 安装
```
$ cd /opt/
$ ln -s hadoop-2.6.4 hadoop
```

## 服务器
| Hostname| IP             |
| --      | --             |
| master  | 192.168.85.130 |
| slave01 | 192.168.85.132 |
| slave02 | 192.168.85.133 |

### hosts
```
$ vim /etc/hosts
192.168.85.130 master
192.168.85.132 slave01
192.168.85.133 slave02
```
### hostname
```
$ ssh master
$ hostnamectl --static set-hostname master

$ ssh slave01
$ hostnamectl --static set-hostname slave01

$ ssh slave02
$ hostnamectl --static set-hostname slave02
```
## Hadoop 用户

## SSH 无密码登录

## 参考文献
* [hadoop 2.6全分布安装](http://www.cnblogs.com/yjmyzz/p/4280069.html)
