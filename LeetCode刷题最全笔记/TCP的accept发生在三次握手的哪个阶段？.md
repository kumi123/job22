# TCP的accept发生在三次握手的哪个阶段？



这些天在网上看到这样一个题目，据听说是腾讯的面试题：

TCP三次握手的过程，accept发生在三次握手的哪一个阶段?

> 答案是：==accept过程发生在三次握手之后，三次握手完成后，客户端和服务器就建立了tcp连接并可以进行数据交互了。这时可以调用accept函数获得此连接。==
>
> 

如果作为一个==服务器==，在调用socket()、bind()之后就会==调用listen()来监听这个socket，如果客户端这时调用connect()发出连接请求，服务器端就会接收到这个请求==。

```text
int listen(int sockfd, int backlog);
int connect(int sockfd, const struct sockaddr *addr, socklen_t addrlen);
```

listen函数的第一个参数即为==要监听的socket描述字==，第二个参数为相应socket==可以排队的最大连接个数==。socket()函数创建的socket默认是一个主动类型的，==listen函数将socket变为被动类型的，等待客户的连接请求==。

connect函数的第一个参数即为==客户端的socket描述字，第二参数为服务器的socket地址，第三个参数为socket地址的长度==。==客户端通过调用connect函数来建立与TCP服务器的连接==。

### accept()函数

==TCP服务器端依次调用socket()、bind()、listen()之后，就会监听指定的socket地址了，先监听。TCP客户端依次调用socket()、connect()之后就向TCP服务器发送了一个连接请求，再连接。TCP服务器监听到这个请求之后，就会调用accept()函数取接收请求，这样连接就建立好了。之后就可以开始网络I/O操作了，即类同于普通文件的读写I/O操作。==

也许这个图描述的更加清晰。

![img](https://upload-images.jianshu.io/upload_images/5822251-477f1361d732b133.JPEG?imageMogr2/auto-orient/strip|imageView2/2/w/495/format/webp)



有的网友评论说这个题目太简单了，也有人说腾讯不会出这么简单的问题，但是就tcp accept而言你又知道多少呢？

我们今天就学习下TCP Accept

POSIX Programmer's Manua对TCP Accept的说明

Accept函数的原型是：

int accept(int socket, struct sockaddr *restrict address,socklen_t *restrict address_len);

功能描述的：

The accept() function shall extract the first connection on the queue of pending connections, create a new socket with the same socket type protocol and address family as the specified socket, and allocate a new file descriptor for that socket.

意思就是：==accept函数会从已经建立连接的队列中取出第一个连接，并创建一个新的socket==，新的socket的类型和地址参数要和原来的那个指定的socket的地址一一样，并且还要为这个新的socket分配文件描述符。

POSIX Programmer's Manual 还说了这么两句话

The accepted socket cannot itself accept more connections. The original socket remains open and can accept more connections.

新建的这个socket自身是无法再接收连接了，但是最开始的那个socket仍然是处于开放状态，而且可以接收更多连接。

If the listen queue is empty of connection requests and O_NONBLOCK is not set on the file descriptor for the socket, accept() shall block until a connection is present. If the listen() queue is empty of connection requests and O_NONBLOCK is set on the file descriptor for the socket, accept() shall fail and set errno to [EAGAIN] or [EWOULDBLOCK].

意思就是：==在连接的监听队列为空并且O_NONBLOCK 没有置位的情况下，accpet是阻塞的==。如果监听队列为空，但是O_NONBLOCK 置位的情况下，accpet会立即返回。

### TCP Accept总结

TCP Accept 是三次握手以后，Accept正确返回以后TCP Server 可以和Client的连接已建立并可以通信了

注意区分==listen socket 和 accept socke==t。



监听套接字: 监听套接字正如accept的参数sockfd，它是监听套接字，==在调用listen函数之后，是服务器开始调用socket()函数生成的，称为监听socket描述字(监听套接字)==，socket()函数创建的socket默认是一个主动类型的，==listen函数将socket变为被动类型的，等待客户的连接请求==。

连接套接字：一个套接字会从主动连接的套接字变身为一个监听套接字；而==accept函数返回的是已连接socket描述字(一个连接套接字)，它代表着一个网络已经存在的点点连接。==



socket分为两种，一种套接字正如accept的参数sockfd，它是listen socket，在调用listen函数之后，一个socket会从主动连接的套接字变为listen 套接字；而accept返回是一个连接套接字，它代表着一个网络已经存在的点对点连接。以后的数据交互就是基于这个连接socket ,而之前的那个listen socket可以继续工作，从而接收更多的连接。

Accept默认会阻塞进程，直到有一个客户连接建立后返回



### read()、write()等函数

万事具备只欠东风，至此服务器与客户已经建立好连接了。可以调用网络I/O进行读写操作了，即实现了网咯中不同进程之间的通信！网络I/O操作有下面几组：

- read()/write()
- recv()/send()
- readv()/writev()
- recvmsg()/sendmsg()
- recvfrom()/sendto()

我推荐使用==recvmsg()/sendmsg()==函数，这两个函数是最通用的I/O函数，实际上可以把上面的其它函数都替换成这两个函数。它们的声明如下：

```text
 #include <unistd.h>
 
ssize_t read(int fd, void *buf, size_t count);
ssize_t write(int fd, const void *buf, size_t count);
 
#include <sys/types.h>
#include <sys/socket.h>
 
ssize_t send(int sockfd, const void *buf, size_t len, int flags);
ssize_t recv(int sockfd, void *buf, size_t len, int flags);
ssize_t sendto(int sockfd, const void *buf, size_t len, int flags,
const struct sockaddr *dest_addr, socklen_t addrlen);
ssize_t recvfrom(int sockfd, void *buf, size_t len, int flags,
struct sockaddr *src_addr, socklen_t *addrlen);
 
ssize_t sendmsg(int sockfd, const struct msghdr *msg, int flags);
ssize_t recvmsg(int sockfd, struct msghdr *msg, int flags);
```

==read函数是负责从fd中读取内容.当读成功时，read返回实际所读的字节数==，如果返回的值是==0表示已经读到文件的结束了，小于0表示出现了错误==。如果错误为EINTR说明读是由中断引起的，如果是ECONNREST表示网络连接出了问题。

==write函数将buf中的nbytes字节内容写入文件描述符fd.成功时返回写的字节数==。失败时返回-1，并设置errno变量。 在网络程序中，当我们向套接字文件描述符写时有俩种可能。==1)write的返回值大于0，表示写了部分或者是全部的数据==。2)返回的值==小于0，此时出现了错误==。我们要根据错误类型来处理。如果错误为==EINTR表示在写的时候出现了中断错误。如果为EPIPE表示网络连接出现了问题==(对方已经关闭了连接)。

其它的我就不一一介绍这几对I/O函数了，具体参见man文档或者baidu、Google，下面的例子中将使用到send/recv。

### 4.6、close()函数

在服务器与客户端建立连接之后，会进行一些读写操作，==完成了读写操作就要关闭相应的socket描述字==，好比操作完打开的文件要调用fclose关闭打开的文件。

```text
#include <unistd.h>
int close(int fd);
```

close一个TCP socket的缺省行为时把该socket标记为以关闭，然后立即返回到调用进程。该描述字不能再由调用进程使用，也就是说不能再作为read或write的第一个参数。

注意：close操作只是使相应socket描述字的引用计数-1，只有当引用计数为0的时候，才会触发TCP客户端向服务器发送终止连接请求。