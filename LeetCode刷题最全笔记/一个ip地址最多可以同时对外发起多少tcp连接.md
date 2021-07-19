# 一个ip地址最多可以同时对外发起多少tcp连接

# 前言

这是一道面试题,主要考察的是linux和网络协议相关基础知识.
**如何标识一个TCP连接**
在确定最大连接数之前,先来看看系统如何标识一个tcp连接.系统用一个四元组来唯一标识一个TCP连接:{local ip, local port,remote ip,remote port}.
我们知道在网络通信过程中服务端监听一个固定的端口,==客户端主动发起连接请求后要经过三次握手才能与服务器建立起一个tcp连接==.客户端每次发起一个tcp连接时,系统会随机选取一个空闲的端口,该端口是独占的不能与其他tcp连接共享,因此==理论上一台机器有多少空闲的端口,就能对外发起多少个tcp连接==,根据tcp/ip协议,==端口port使用16位无符号整数unsigned short来存储,因此本地端口一共有2^16=65536个==,即0-65535,其中==0~1023是预留端口,0有特殊含义不能使用,1024以下端口都是超级管理员用户(如root)才可以使用==,因此就算使用root权限,一台机器最多能使用的端口也只有65535个(除去一些保留的和已被占用的端口,实际可能不足这个数).
那么一台机是否真的可以发起65535个tcp连接呢?
下面我们用netty写了一个测试的客户端和服务端,来看看在单机linux下能发起的最大tcp连接数,其中代码使用了非阻塞 io,所以不用开大量的线程.
**Server端主要代码如下:**



```java
        System.out.println("server starting....");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        bootstrap.childHandler(new ConnectionCountHandler());
        int port = 8100;
        bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
            System.out.println("bind success in port: " + port);
        });
        System.out.println("server started!");
```

**服务端Handler代码如下:**



```java
@ChannelHandler.Sharable
public class ConnectionCountHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger nConnection = new AtomicInteger();

    public ConnectionCountHandler() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("connections: " + nConnection.get());
        }, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        nConnection.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    }
}
```

**Client端主要代码如下:**



```java
        System.out.println("client starting....");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        while (!Thread.interrupted()) {
            try {
                ChannelFuture channelFuture = bootstrap.connect("192.168.56.101", 8100);
                channelFuture.addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        System.out.println("connect failed, exit!:" + future.cause().getMessage());
                    }
                });
                channelFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
```

**客户端Handler代码如下:**



```java
@ChannelHandler.Sharable//该类的实例可以被多个channel共享
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //在到服务器的连接已经建立之后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    //当从服务器收到一条消息之后被调用
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
    }

    //处理过程中引发异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```

我们使用maven将server和client的代码打成两个jar包,放到同一台linux服务器下并使用以下命令分别启动两个服务:



```css
java -jar server.jar
java -jar client.jar
```

很快客户端便抛出了大量拒绝连接的异常,而服务端显示出来的已连接数只有1000多,于是猜测可能是==服务端达到了所能接收的最大tcp连接数(即文件描述符个数),因为受服务器资源、操作系统的限制,linux内核默认文件描述符最大值是1024,也就是说默认支持最大并发连接是1024个==(每个tcp连接都要占用一定内存,每个socket就是一个文件描述符)
我们输入以下命令查看linux内核参数:



```swift
[root@bogon local]# ulimit -a
core file size          (blocks, -c) 0
data seg size           (kbytes, -d) unlimited
scheduling priority             (-e) 0
file size               (blocks, -f) unlimited
pending signals                 (-i) 7271
max locked memory       (kbytes, -l) 64
max memory size         (kbytes, -m) unlimited
open files                      (-n) 1024
pipe size            (512 bytes, -p) 8
POSIX message queues     (bytes, -q) 819200
real-time priority              (-r) 0
stack size              (kbytes, -s) 8192
cpu time               (seconds, -t) unlimited
max user processes              (-u) 7271
virtual memory          (kbytes, -v) unlimited
file locks                      (-x) unlimited
```

可以看到其中的open files是1024,接着我们调整内核参数:



```bash
ulimit -n 102400
```

**注意以上命令只能临时生效,系统重启后又恢复成默认值.**
要想永久生效,在/etc/security/limits.conf文件中配置如下两行：

- hard nofile 102400
- soft nofile 102400

soft和hard为两种限制方式,其中soft表示警告的限制,hard表示真正限制,nofile表示打开的最大文件数.
但这是针对于单个进程的修改,若想修改系统全局的限制,可以执行下面的操作:

- cat /proc/sys/fs/file-max
- etc/sysctl.conf

cat /proc/sys/fs/file-max查看我所有进程能够打开的最大文件数是多少,每一个tcp连接代表一个文件,局部的不能大于全局的限制,然后vi etc/sysctl.conf,在里面添加fs.file-max = 102400,file-max表示全局文件句柄数的限制.
**最后看看执行结果:当server端连接数到达28232时,client开始抛出大量异常java.net.BindException: 无法指定被请求的地址.**
server端:



```undefined
connections: 2627
connections: 4688
connections: 5722
connections: 8593
connections: 10048
connections: 13383
connections: 16013
connections: 19736
connections: 22628
connections: 26571
connections: 28232
connections: 28232
connections: 28232
```

client端：



```bash
Cconnect failed, exit!:无法指定被请求的地址: /192.168.56.101:8100
java.util.concurrent.ExecutionException: io.netty.channel.AbstractChannel$AnnotatedSocketException: 无法指定被请求的地址: /192.168.56.101:8100
    at io.netty.util.concurrent.AbstractFuture.get(AbstractFuture.java:41)
    at demo.test.TestClientApplication.client3(TestClientApplication.java:214)
    at demo.test.TestClientApplication.main(TestClientApplication.java:250)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48)
    at org.springframework.boot.loader.Launcher.launch(Launcher.java:87)
    at org.springframework.boot.loader.Launcher.launch(Launcher.java:50)
    at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51)
Caused by: io.netty.channel.AbstractChannel$AnnotatedSocketException: 无法指定被请求的地址: /192.168.56.101:8100
    at sun.nio.ch.Net.connect0(Native Method)
    at sun.nio.ch.Net.connect(Net.java:454)
    at sun.nio.ch.Net.connect(Net.java:446)
    at sun.nio.ch.SocketChannelImpl.connect(SocketChannelImpl.java:648)
    at io.netty.util.internal.SocketUtils$3.run(SocketUtils.java:83)
    at io.netty.util.internal.SocketUtils$3.run(SocketUtils.java:80)
    at java.security.AccessController.doPrivileged(Native Method)
    at io.netty.util.internal.SocketUtils.connect(SocketUtils.java:80)
    at io.netty.channel.socket.nio.NioSocketChannel.doConnect(NioSocketChannel.java:312)
    at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.connect(AbstractNioChannel.java:254)
    at io.netty.channel.DefaultChannelPipeline$HeadContext.connect(DefaultChannelPipeline.java:1366)
    at io.netty.channel.AbstractChannelHandlerContext.invokeConnect(AbstractChannelHandlerContext.java:545)
    at io.netty.channel.AbstractChannelHandlerContext.connect(AbstractChannelHandlerContext.java:530)
    at io.netty.channel.AbstractChannelHandlerContext.connect(AbstractChannelHandlerContext.java:512)
    at io.netty.channel.DefaultChannelPipeline.connect(DefaultChannelPipeline.java:1024)
    at io.netty.channel.AbstractChannel.connect(AbstractChannel.java:259)
    at io.netty.bootstrap.Bootstrap$3.run(Bootstrap.java:252)
    at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:163)
    at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:404)
    at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:464)
    at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:884)
    at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
    at java.lang.Thread.run(Thread.java:748)
Caused by: java.net.BindException: 无法指定被请求的地址
```

从测试的结果可以证明,==linux对外随机分配的端口是有限制,理论上单机对外端口数可达65535,但实际对外可建立的连接默认最大只有28232个==,在linux下执行以下命令可知:



```bash
[root@bogon local]#  cat /proc/sys/net/ipv4/ip_local_port_range
32768   60999
```

也就是在这个区间内的端口可以使用,所以单个IP对外最多只能发送28232个tcp请求.
通过以下命令可以临时调整这个区间的范围,但是系统重启后会还原成默认值.



```ruby
echo "1024 65535"> /proc/sys/net/ipv4/ip_local_port_range
```

要想永远生效可以修改/etc/sysctl.conf文件,增加一行:



```undefined
net.ipv4.ip_local_port_range= 1024 65535
```

然后再执行:



```undefined
sysctl -p
```

这样就永远生效了.现在单ip可以发起64510个tcp连接了.
**扩展**
	==单机最多有6w多连接,若想模拟实现百万级别的客户端连接,至少需要十几台机器以上,当然也可以在单机上使用多个虚拟ip来模拟实现.==





前面研究的是client能建立的最大tcp连接数,而==server能接收的最大tcp连接数又是多少呢?==
==理论上是无上限的==,server通常固定在某个本地端口上监听client的连接请求.不考虑地址重用(unix的SO_REUSEADDR选项)的情况下,即使server端有多个ip,本地监听端口也是独占的,因此server端tcp连接4元组中只有==remote ip(也就是client ip)和remote port(客户端port)是可变的==,因此==最大tcp连接为客户端ip数×客户端port数==,对于IPV4,不考虑ip地址分类等因素,最大tcp连接数约为==2的32次方(ip数)×2的16次方(port数)==,也就是server端单个ip单个端口最大tcp连接数约为2的48次方.
但是==在实际环境中,受到服务器配置等物理条件的制约,其最大并发tcp连接数远不能达到理论值,不过通过增加内存、修改最大文件描述符个数等参数,单机最大并发TCP连接数是可以达到10万,甚至上百万的.==
参考：
https://my.oschina.net/yang1992/blog/522533
https://colobu.com/2015/05/22/implement-C1000K-servers-by-spray-netty-undertow-and-node-js/
https://www.dozer.cc/2014/12/netty-long-connection.html
https://blog.csdn.net/Wing_93/article/details/81676314
http://www.blogjava.net/yongboy/archive/2013/04/09/397559.html