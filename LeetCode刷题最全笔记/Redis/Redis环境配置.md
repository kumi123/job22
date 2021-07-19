# Redis环境启动

## 启动Redis服务

在`/usr/local/bin`目录下，输入，这个就是wsl的默认初始目录

```bash
redis-server rconfig/redis.conf
```

- 后面为配置文件地址，表示通过指定的配置文件启动
  ![image-20200731121612605](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9naXRlZS5jb20vemhvbmdfc2lydS9pbWFnZXMvcmF3L21hc3Rlci8vaW1nL2ltYWdlLTIwMjAwNzMxMTIxNjEyNjA1LnBuZw?x-oss-process=image/format,png)

## 使用Redis客户端连接测试

```bash
redis-cli -p 6379	#通过指定端口连接
ping	#测试
set name zsr
get name
keys *	#查看所有的key
12345
```

![image-20200731122122997](https://gitee.com/zhong_siru/images/raw/master//img/image-20200731122122997.png)

## 12、查看Redis的进程是否开启

```bash
ps -ef|grep redis
1
```

![image-20200731122450419](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9naXRlZS5jb20vemhvbmdfc2lydS9pbWFnZXMvcmF3L21hc3Rlci8vaW1nL2ltYWdlLTIwMjAwNzMxMTIyNDUwNDE5LnBuZw?x-oss-process=image/format,png)

## 13、怎么关闭Redis服务？

```bash
shutdown
exit
12
```

![image-20200731122620479](https://gitee.com/zhong_siru/images/raw/master//img/image-20200731122620479.png)

然后我们在查看Redis进程，发现已经关闭

![image-20200731122704790](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9naXRlZS5jb20vemhvbmdfc2lydS9pbWFnZXMvcmF3L21hc3Rlci8vaW1nL2ltYWdlLTIwMjAwNzMxMTIyNzA0NzkwLnBuZw?x-oss-process=image/format,png)