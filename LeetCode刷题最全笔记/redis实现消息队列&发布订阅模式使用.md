# [redis实现消息队列&发布/订阅模式使用](https://www.cnblogs.com/qlqwjy/p/9763754.html)

　　在项目中用到了redis作为缓存，再学习了ActiveMq之后想着用redis实现简单的消息队列，下面做记录。

 　**Redis的==列表类型键可以用来实现队列，并且支持阻塞式读取==，可以很容易的实现一个高性能的优先队列。同时在更高层面上，Redis还支持"发布/订阅"的消息模式，可以基于此构建一个聊天系统。**

## 一、==redis的列表类型天生支持用作消息队列==。(类似于MQ的队列模型--任何时候都可以消费，一条消息只能消费一次)

==直接使用rpop和lpop这种==

　　list操作参考:https://www.cnblogs.com/qlqwjy/p/7789125.html

　　  在Redis中，==List类型是按照插入顺序排序的字符串链表==。和数据结构中的普通链表一样，我们可以在其头部(left)和尾部(right)添加新的元素。在插入时，如果该键并不存在，Redis将为该键创建一个新的链表。与此相反，如果链表中所有的元素均被移除，那么该键也将会被从数据库中删除。List中可以包含的最大元素数量是4294967295。
   从元素插入和删除的效率视角来看，如果我们是==在链表的两头插入或删除元素，这将会是非常高效的操作，即使链表中已经存储了百万条记录，该操作也可以在常量时间内完成==。然而需要说明的是，如果元素插入或删除操作是作用于链表中间，那将会是非常低效的。相信对于有良好数据结构基础的开发者而言，这一点并不难理解。(类似于java的ArrayList)

 

redis对list的操作命令中。L表示从左边(头部)开始插与弹出，R表示从右边(尾部)开始插与弹出。

 

### 1.redis中简单的操作list，简单的在命令行操作实现队列

(1)从左向右插入，从右向左弹出:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> lpush mylist a b c d
(integer) 4
127.0.0.1:6379> lrange mylist 0 -1
1) "d"
2) "c"
3) "b"
4) "a"
127.0.0.1:6379> rpop mylist
"a"
127.0.0.1:6379> rpop mylist
"b"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

执行完  lpush mylist a b c d 之后数据结构如下:(满足先进先出的队列模式)

![img](https://img2018.cnblogs.com/blog/1196212/201810/1196212-20181009211840923-924506046.png)

 

 执行完第一次:rpop mylist之后数据结构如下:

![img](https://img2018.cnblogs.com/blog/1196212/201810/1196212-20181009212001692-1435228520.png)

 

 (2)从右向左插入，从左向右弹出:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> rpush mylist2 a b c d
(integer) 4
127.0.0.1:6379> lrange mylist2 0 -1
1) "a"
2) "b"
3) "c"
4) "d"
127.0.0.1:6379> lpop mylist2
"a"
127.0.0.1:6379> lpop mylist2
"b"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 执行完:rpush mylist2 a b c d之后的数据结构如下

![img](https://img2018.cnblogs.com/blog/1196212/201810/1196212-20181009212450216-1049851073.png)

 

第一次执行完  lpop mylist2 之后数据结构如下:(满足先进先出的队列模式)

![img](https://img2018.cnblogs.com/blog/1196212/201810/1196212-20181009212603369-1822799103.png)

 

###  2.JAVA程序实现消息队列

redis.properties

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
redis.url=localhost
redis.port=6379
redis.maxIdle=30//最大闲置个数
redis.minIdle=10//最小闲置个数
redis.maxTotal=100//最大连接个数
redis.maxWait=10000//最大等待时间1s
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

获取连接的工具类:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: qlq
 * @Description
 * @Date: 21:32 2018/10/9
 */
public class JedisPoolUtils {

    private static JedisPool pool = null;

    static {

        //加载配置文件
        InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("redis.properties");
        Properties pro = new Properties();
        try {
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获得池子对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.parseInt(pro.get("redis.maxIdle").toString()));//最大闲置个数
        poolConfig.setMaxWaitMillis(Integer.parseInt(pro.get("redis.maxWait").toString()));//最大闲置个数
        poolConfig.setMinIdle(Integer.parseInt(pro.get("redis.minIdle").toString()));//最小闲置个数
        poolConfig.setMaxTotal(Integer.parseInt(pro.get("redis.maxTotal").toString()));//最大连接数
        pool = new JedisPool(poolConfig, pro.getProperty("redis.url"), Integer.parseInt(pro.get("redis.port").toString()));
    }

    //获得jedis资源的方法
    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        System.out.println(jedis);
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 **(1)消息生产者:(开启5个线程生产消息,不断向当中添加消息，线程顺序不一定，但是总数一致)**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;

/**
 * @Author: qlq
 * @Description
 * @Date: 21:29 2018/10/9
 */
public class MessageProducer extends Thread {
    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void putMessage(String message) {
        Jedis jedis = JedisPoolUtils.getJedis();
        Long size = jedis.lpush(MESSAGE_KEY, message);
        System.out.println(Thread.currentThread().getName() + " put message,size=" + size + ",count=" + count);
        count++;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 5; i++) {
            putMessage("message" + count);
        }
    }

    public static void main(String[] args) {
        MessageProducer messageProducer = new MessageProducer();
        Thread t1 = new Thread(messageProducer, "thread1");
        Thread t2 = new Thread(messageProducer, "thread2");
        Thread t3 = new Thread(messageProducer, "thread3");
        Thread t4 = new Thread(messageProducer, "thread4");
        Thread t5 = new Thread(messageProducer, "thread5");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 结果：(证明了redis是单线程操作，只能一个一个操作)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
thread1 put message,size=1,count=0
thread1 put message,size=2,count=1
thread1 put message,size=3,count=2
thread1 put message,size=4,count=3
thread1 put message,size=5,count=4
thread3 put message,size=6,count=5
thread3 put message,size=7,count=6
thread3 put message,size=8,count=7
thread3 put message,size=9,count=8
thread3 put message,size=10,count=9
thread4 put message,size=11,count=10
thread4 put message,size=12,count=11
thread4 put message,size=13,count=12
thread4 put message,size=14,count=13
thread4 put message,size=15,count=14
thread5 put message,size=16,count=15
thread5 put message,size=17,count=16
thread5 put message,size=18,count=17
thread5 put message,size=19,count=18
thread5 put message,size=20,count=19
thread2 put message,size=21,count=20
thread2 put message,size=22,count=21
thread2 put message,size=23,count=22
thread2 put message,size=24,count=23
thread2 put message,size=25,count=24
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 redis后台查看:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> lrange message:queue 0 -1
 1) "message24"
 2) "message23"
 3) "message22"
 4) "message21"
 5) "message20"
 6) "message19"
 7) "message18"
 8) "message17"
 9) "message16"
10) "message15"
11) "message14"
12) "message13"
13) "message12"
14) "message11"
15) "message10"
16) "message9"
17) "message8"
18) "message7"
19) "message6"
20) "message5"
21) "message4"
22) "message3"
23) "message2"
24) "message1"
25) "message0"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 **(2)消息消费者:(开启两个线程消费消息，不断地进行消费，用的是while（true）所以是可能会消费完成的**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;

/**
 * @Author: qlq
 * @Description
 * @Date: 22:34 2018/10/9
 */
public class MessageConsumer implements Runnable {
    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;

    public void consumerMessage() {
        Jedis jedis = JedisPoolUtils.getJedis();
        String message = jedis.rpop(MESSAGE_KEY);
        System.out.println(Thread.currentThread().getName() + " consumer message,message=" + message + ",count=" + count);
        count++;
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread6");
        Thread t2 = new Thread(messageConsumer, "thread7");
        t1.start();
        t2.start();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

结果:(满足先进先出的规则)--虽然消息已经消费完了，但是仍然在不停的rpop，所以造成浪费

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
thread6 consumer message,message=message0,count=0
thread6 consumer message,message=message1,count=1
thread6 consumer message,message=message2,count=2
thread6 consumer message,message=message3,count=3
thread7 consumer message,message=message4,count=4
thread6 consumer message,message=message5,count=5
thread7 consumer message,message=message6,count=6
thread6 consumer message,message=message7,count=7
thread7 consumer message,message=message8,count=8
thread6 consumer message,message=message9,count=9
thread7 consumer message,message=message10,count=10
thread6 consumer message,message=message11,count=11
thread7 consumer message,message=message12,count=12
thread6 consumer message,message=message13,count=13
thread7 consumer message,message=message14,count=14
thread6 consumer message,message=message15,count=15
thread7 consumer message,message=message16,count=16
thread6 consumer message,message=message17,count=16
thread7 consumer message,message=message18,count=18
thread6 consumer message,message=message19,count=19
thread7 consumer message,message=message20,count=20
thread6 consumer message,message=message21,count=20
thread7 consumer message,message=message22,count=22
thread6 consumer message,message=message23,count=22
thread7 consumer message,message=message24,count=24
thread6 consumer message,message=null,count=25
thread7 consumer message,message=null,count=26
thread6 consumer message,message=null,count=27
thread7 consumer message,message=null,count=28
thread6 consumer message,message=null,count=28
thread7 consumer message,message=null,count=30
thread6 consumer message,message=null,count=31...
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

　　 但上述例子中消息消费者有一个问题存在，即==需要不停的调用rpop方法查看List中是否有待处理消息==。==每调用一次都会发起一次连接，这会造成不必要的浪费==。也许你会使用Thread.sleep()等方法让消费者线程隔一段时间再消费，但这样做有两个问题：

  1）、消费队列长度增加：==如果生产者速度大于消费者消费速度，消息队列长度会一直增大，时间久了会占用大量内存空间。==

  2）、时效性变差：==如果睡眠时间过长，这样不能处理一些时效性的消息，睡眠时间过短，也会在连接上造成比较大的开销。==

 

**补充:==brpop和blpop实现阻塞读取(重要)==**

　　也就是上面的操作需要一直调用rpop命令或者lpop命令才可以实现不停的监听且消费消息。为了解决这一问题，==redis提供了阻塞命令 brpop和blpop==。下面以brpop命名为例进行试验:

　　brpop命令可以接收多个键，其完整的命令格式为 BRPOP key [key ...] timeout,如:brpop key1 0（固定的格式）。意义是同时检测多个key，如果所有key都没有元素则阻塞，如果其中一个有元素则从该key中弹出该元素(会按照插入元素的先后的顺序进行读取，可以实现具有优先级的队列，比如说这个brpop)。例如下面试验:

开启两个客户端，第一个客户端中采用brpop阻塞读取两个键:

```
127.0.0.1:6379> brpop mylist1 mylist2 0
```

第二个客户端增加mylist1 :

```
127.0.0.1:6379> lpush mylist1 1 2
(integer) 2
```

 

因为是先弹出右边的数据，但是mylist1是优先的，所以先弹出mylist1的元素



则在第一个客户端显示:

```
127.0.0.1:6379> brpop mylist1 mylist2 0
1) "mylist1"
2) "1"
(56.31s)
```

==== 

==**也就是brpop会阻塞队列，并且每次也是弹出一个消息，如果没有消息会阻塞。==**

 

如果多个键都有元素则按照从左到右读取第一个队列中的一个元素，例如我们现在queue1和queue2各自添加一个元素：

```
127.0.0.1:6379> lpush queue1 1 2
(integer) 2
127.0.0.1:6379> lpush queue2 3 4
(integer) 2
```

 

然后执行brpop命令:(会返回读取的key和value，第一个是返回的key，第二个是value)

```
127.0.0.1:6379> brpop queue1 queue2 2
1) "queue1"
2) "1"
```

 

　　借此特性可以实现区分优先级的任务队列。也就是brpop会按照key的顺序依次读取一个数据。

 

  

改造上面代码实现阻塞读取:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Author: qlq
 * @Description
 * @Date: 22:34 2018/10/9
 */
public class MessageConsumer implements Runnable {
    public static final String MESSAGE_KEY = "message:queue";
    private volatile int count;
    private Jedis jedis = JedisPoolUtils.getJedis();

    public void consumerMessage() {
        List<String> brpop = jedis.brpop(0, MESSAGE_KEY);//0是timeout,返回的是一个集合，第一个是消息的key，第二个是消息的内容
        System.out.println(brpop);
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread6");
        Thread t2 = new Thread(messageConsumer, "thread7");
        t1.start();
        t2.start();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

　　然后可以运行Customer，清空控制台，可以看到程序没有任何输出，阻塞在了brpop这儿,队列当中没有数据就堵塞，反过来就是消费就可以啦。然后在打开Redis的客户端，输入指令client list，可以查看当前的连接个数。

　　**当启动生产者生产消息之后，消费者会自动消费消息，而且消费者会阻塞直到有消息。**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
[message:queue, message0]
[message:queue, message1]
[message:queue, message2]
[message:queue, message3]
[message:queue, message4]
[message:queue, message5]
[message:queue, message6]
[message:queue, message7]
[message:queue, message8]
[message:queue, message9]
[message:queue, message10]
[message:queue, message11]
[message:queue, message12]
[message:queue, message13]
[message:queue, message14]
[message:queue, message15]
[message:queue, message16]
[message:queue, message17]
[message:queue, message18]
[message:queue, message19]
[message:queue, message20]
[message:queue, message21]
[message:queue, message22]
[message:queue, message23]
[message:queue, message24]
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

## 二、发布/订阅模式(类似于MQ的主题模式-只能消费订阅之后发布的消息，一个消息可以被多个订阅者消费)

### 1.客户端发布/订阅

#### 1.1  普通的发布/订阅

 　除了实现任务队列外，redis还提供了一组命令可以让开发者实现"发布/订阅"(publish/subscribe)模式。"发布/订阅"模式同样可以实现进程间的消息传递，其原理如下:

　　=="发布/订阅"模式包含两种角色，分别是发布者和订阅者。订阅者可以订阅一个或者多个频道(channel),而发布者可以向指定的频道(channel)发送消息，所有订阅此频道的订阅者都会收到此消息。==

(1)发布消息

　　发布者发布消息的命令是 publish,用法是 publish channel message，如向 channel1.1说一声hi

```
127.0.0.1:6379> publish channel:1 hi
(integer) 0
```

　　这样消息就发出去了。返回值表示接收这条消息的订阅者数量。发出去的消息不会被持久化，也就是有客户端订阅channel:1后只能接收到后续发布到该频道的消息，之前的就接收不到了。

 

(2)订阅频道

　　==订阅频道的命令是 subscribe，可以同时订阅多个频道，用法是 subscribe channel1 [channel2 ...],==例如新开一个客户端订阅上面频道:(不会收到消息，因为不会收到订阅之前就发布到该频道的消息)

```
127.0.0.1:6379> subscribe channel:1
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel:1"
3) (integer) 1
```

　　执行上面命令客户端会进入订阅状态，==处于此状态下客户端不能使用除subscribe、unsubscribe、psubscribe和punsubscribe这四个属于"发布/订阅"之外的命令，否则会报错==。

　　进入订阅状态后客户端可能收到3种类型的回复。每种类型的回复都包含3个值，第一个值是消息的类型，根据消类型的不同，第二个和第三个参数的含义可能不同。

消息类型的取值可能是以下3个:

　　==(1)subscribe。表示订阅成功的反馈信息。第二个值是订阅成功的频道名称，第三个是当前客户端订阅的频道数量。==

　　==(2)message。表示接收到的消息，第二个值表示产生消息的频道名称，第三个值是消息的内容。==

　　==(3)unsubscribe。表示成功取消订阅某个频道。第二个值是对应的频道名称，第三个值是当前客户端订阅的频道数量，当此值为0时客户端会退出订阅状态，之后就可以执行其他非"发布/订阅"模式的命令了。==

 

(3)第一个客户端重新向channel:1发送一条消息

```
127.0.0.1:6379> publish channel:1 hi
(integer) 1
```

 

返回值表示订阅此频道的数量

c

上面订阅的客户端:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> subscribe channel:1
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel:1"
3) (integer) 1
1) "message"
2) "channel:1"
3) "hi"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

　　红字部分表示成功的收到消息(依次是消息类型，频道，消息内容)

####  

#### 1.2  按照规则发布/订阅

　　除了可以使用subscribe命令订阅指定的频道外，还可以==使用psubscribe命令订阅指定的规则。规则支持通配符格式。命令格式为   psubscribe pattern [pattern ...]订阅多个模式的频道==。

　　通配符中?表示1个占位符， * 表示任意个占位符(包括0)，?*表示1个以上占位符。

例如:

(1)订阅者订阅三个通配符频道

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> psubscribe c? b* d?*
Reading messages... (press Ctrl-C to quit)
1) "psubscribe"
2) "c?"
3) (integer) 1
1) "psubscribe"
2) "b*"
3) (integer) 2
1) "psubscribe"
2) "d?*"
3) (integer) 3
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 

 

(2)新开一个客户端发送到指定频道

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
C:\Users\liqiang>redis-cli
127.0.0.1:6379> publish c m1
(integer) 0
127.0.0.1:6379> publish c1 m1
(integer) 1
127.0.0.1:6379> publish c11 m1
(integer) 0
127.0.0.1:6379> publish b m1
(integer) 1
127.0.0.1:6379> publish b1 m1
(integer) 1
127.0.0.1:6379> publish b11 m1
(integer) 1
127.0.0.1:6379> publish d m1
(integer) 0
127.0.0.1:6379> publish d1 m1
(integer) 1
127.0.0.1:6379> publish d11 m1
(integer) 1
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

上面返回值为1表示被订阅者所接受，可以匹配上面的通配符。

 

订阅者客户端:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
127.0.0.1:6379> psubscribe c? b* d?*
Reading messages... (press Ctrl-C to quit)
1) "psubscribe"
2) "c?"
3) (integer) 1
1) "psubscribe"
2) "b*"
3) (integer) 2
1) "psubscribe"
2) "d?*"
3) (integer) 3
1) "pmessage"
2) "c?"
3) "c1"
4) "m1"
1) "pmessage"
2) "b*"
3) "b"
4) "m1"
1) "pmessage"
2) "b*"
3) "b1"
4) "m1"
1) "pmessage"
2) "b*"
3) "b11"
4) "m1"
1) "pmessage"
2) "d?*"
3) "d1"
4) "m1"
1) "pmessage"
2) "d?*"
3) "d11"
4) "m1"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

　

### 注意:

**(1)==使用psubscribe命令可以重复订阅同一个频道==，如客户端执行了psubscribe c? c?\*。这时向c1发布消息客户端会接受到两条消息，而同时publish命令的返回值是2而不是1。.**

**同样的，如果有另一个客户端==执行了subscribe c1 和psubscribe c?\*的话，向c1发送一条消息该客户顿也会受到两条消息==(但是是两种类型:message和pmessage)，同时publish命令也返回2.**

**(2==)punsubscribe命令可以退订指定的规则，用法是: punsubscribe [pattern [pattern ...]],如果没有参数则会退订所有规则。==**

**(3)==使用punsubscribe只能退订通过psubscribe命令订阅的规则，不会影响直接通过subscribe命令订阅的频道；==**

**同样==unsubscribe命令也不会影响通过psubscribe命令订阅的规则==。另外需要注意==punsubscribe命令退订某个规则时不会将其中的通配符展开，而是进行严格的字符串匹配==，所以punsubscribe \* 无法退订c\*规则，而是必须使用punsubscribe c\*才可以退订。**

 

### 2.Java程序实现发布者订阅者模式

#### 1.生产者

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;

/**
 * @Author: qlq
 * @Description
 * @Date: 21:29 2018/10/9
 */
public class MessageProducer extends Thread {
    public static final String CHANNEL_KEY = "channel:1";
    private volatile int count;

    public void putMessage(String message) {
        Jedis jedis = JedisPoolUtils.getJedis();
        Long publish = jedis.publish(CHANNEL_KEY, message);//返回订阅者数量
        System.out.println(Thread.currentThread().getName() + " put message,count=" + count+",subscriberNum="+publish);
        count++;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < 5; i++) {
            putMessage("message" + count);
        }
    }

    public static void main(String[] args) {
        MessageProducer messageProducer = new MessageProducer();
        Thread t1 = new Thread(messageProducer, "thread1");
        Thread t2 = new Thread(messageProducer, "thread2");
        Thread t3 = new Thread(messageProducer, "thread3");
        Thread t4 = new Thread(messageProducer, "thread4");
        Thread t5 = new Thread(messageProducer, "thread5");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

结果:

thread1 put message,count=0,subscriberNum=0
thread1 put message,count=1,subscriberNum=0
thread1 put message,count=2,subscriberNum=0
thread1 put message,count=3,subscriberNum=0
thread1 put message,count=4,subscriberNum=0
thread4 put message,count=5,subscriberNum=0
thread4 put message,count=6,subscriberNum=0
thread4 put message,count=7,subscriberNum=0
thread4 put message,count=8,subscriberNum=0
thread4 put message,count=9,subscriberNum=0
thread5 put message,count=10,subscriberNum=0
thread5 put message,count=11,subscriberNum=0
thread5 put message,count=12,subscriberNum=0
thread5 put message,count=13,subscriberNum=0
thread5 put message,count=14,subscriberNum=0
thread2 put message,count=15,subscriberNum=0
thread2 put message,count=16,subscriberNum=0
thread2 put message,count=17,subscriberNum=0
thread2 put message,count=18,subscriberNum=0
thread2 put message,count=19,subscriberNum=0
thread3 put message,count=20,subscriberNum=0
thread3 put message,count=21,subscriberNum=0
thread3 put message,count=22,subscriberNum=0
thread3 put message,count=23,subscriberNum=0
thread3 put message,count=24,subscriberNum=0

 

**2.消费者**

**(1)subscribe实现订阅消费消息(开启两个线程订阅消息)**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author: qlq
 * @Description
 * @Date: 22:34 2018/10/9
 */
public class MessageConsumer implements Runnable {
    public static final String CHANNEL_KEY = "channel:1";//频道

    public static final String EXIT_COMMAND = "exit";//结束程序的消息

    private MyJedisPubSub myJedisPubSub = new MyJedisPubSub();//处理接收消息

    public void consumerMessage() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.subscribe(myJedisPubSub, CHANNEL_KEY);//第一个参数是处理接收消息，第二个参数是订阅的消息频道
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread5");
        Thread t2 = new Thread(messageConsumer, "thread6");
        t1.start();
        t2.start();
    }
}

/**
 * 继承JedisPubSub，重写接收消息的方法
 */
class MyJedisPubSub extends JedisPubSub {
    @Override
    /** JedisPubSub类是一个没有抽象方法的抽象类,里面方法都是一些空实现
     * 所以可以选择需要的方法覆盖,这儿使用的是SUBSCRIBE指令，所以覆盖了onMessage
     * 如果使用PSUBSCRIBE指令，则覆盖onPMessage方法
     * 当然也可以选择BinaryJedisPubSub,同样是抽象类，但方法参数为byte[]
     **/
    public void onMessage(String channel, String message) {
        System.out.println(Thread.currentThread().getName()+"-接收到消息:channel=" + channel + ",message=" + message);
        //接收到exit消息后退出
        if (MessageConsumer.EXIT_COMMAND.equals(message)) {
            System.exit(0);
        }
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

我们再次启动生产者生产消息,生产者控制台:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
thread5 put message,count=0,subscriberNum=2
thread5 put message,count=1,subscriberNum=2
thread5 put message,count=2,subscriberNum=2
thread5 put message,count=3,subscriberNum=2
thread5 put message,count=4,subscriberNum=2
thread3 put message,count=5,subscriberNum=2
thread3 put message,count=6,subscriberNum=2
thread3 put message,count=7,subscriberNum=2
thread3 put message,count=8,subscriberNum=2
thread3 put message,count=9,subscriberNum=2
thread2 put message,count=10,subscriberNum=2
thread2 put message,count=11,subscriberNum=2
thread2 put message,count=12,subscriberNum=2
thread2 put message,count=13,subscriberNum=2
thread2 put message,count=14,subscriberNum=2
thread4 put message,count=15,subscriberNum=2
thread4 put message,count=16,subscriberNum=2
thread4 put message,count=17,subscriberNum=2
thread4 put message,count=18,subscriberNum=2
thread4 put message,count=19,subscriberNum=2
thread1 put message,count=20,subscriberNum=2
thread1 put message,count=21,subscriberNum=2
thread1 put message,count=22,subscriberNum=2
thread1 put message,count=23,subscriberNum=2
thread1 put message,count=24,subscriberNum=2

Process finished with exit code 0
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

消费者控制台:（每一次生产者进行生产之后呢，就会两个线程订阅都可以收到)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
thread6-接收到消息:channel=channel:1,message=message0
thread5-接收到消息:channel=channel:1,message=message0
thread5-接收到消息:channel=channel:1,message=message1
thread6-接收到消息:channel=channel:1,message=message1
thread5-接收到消息:channel=channel:1,message=message2
thread6-接收到消息:channel=channel:1,message=message2
thread5-接收到消息:channel=channel:1,message=message3
thread6-接收到消息:channel=channel:1,message=message3
thread5-接收到消息:channel=channel:1,message=message4
thread6-接收到消息:channel=channel:1,message=message4
thread5-接收到消息:channel=channel:1,message=message5
thread6-接收到消息:channel=channel:1,message=message5
thread5-接收到消息:channel=channel:1,message=message6
thread6-接收到消息:channel=channel:1,message=message6
thread5-接收到消息:channel=channel:1,message=message7
thread6-接收到消息:channel=channel:1,message=message7
thread5-接收到消息:channel=channel:1,message=message8
thread6-接收到消息:channel=channel:1,message=message8
thread5-接收到消息:channel=channel:1,message=message9
thread6-接收到消息:channel=channel:1,message=message9
thread5-接收到消息:channel=channel:1,message=message10
thread6-接收到消息:channel=channel:1,message=message10
thread5-接收到消息:channel=channel:1,message=message11
thread6-接收到消息:channel=channel:1,message=message11
thread5-接收到消息:channel=channel:1,message=message12
thread6-接收到消息:channel=channel:1,message=message12
thread5-接收到消息:channel=channel:1,message=message13
thread6-接收到消息:channel=channel:1,message=message13
thread5-接收到消息:channel=channel:1,message=message14
thread6-接收到消息:channel=channel:1,message=message14
thread5-接收到消息:channel=channel:1,message=message15
thread6-接收到消息:channel=channel:1,message=message15
thread5-接收到消息:channel=channel:1,message=message16
thread6-接收到消息:channel=channel:1,message=message16
thread5-接收到消息:channel=channel:1,message=message17
thread6-接收到消息:channel=channel:1,message=message17
thread5-接收到消息:channel=channel:1,message=message18
thread6-接收到消息:channel=channel:1,message=message18
thread5-接收到消息:channel=channel:1,message=message19
thread6-接收到消息:channel=channel:1,message=message19
thread5-接收到消息:channel=channel:1,message=message20
thread6-接收到消息:channel=channel:1,message=message20
thread5-接收到消息:channel=channel:1,message=message21
thread6-接收到消息:channel=channel:1,message=message21
thread5-接收到消息:channel=channel:1,message=message22
thread6-接收到消息:channel=channel:1,message=message22
thread5-接收到消息:channel=channel:1,message=message23
thread6-接收到消息:channel=channel:1,message=message23
thread5-接收到消息:channel=channel:1,message=message24
thread6-接收到消息:channel=channel:1,message=message24
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

**(2)psubscribe实现订阅消费消息(开启两个线程订阅消息)**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author: qlq
 * @Description
 * @Date: 22:34 2018/10/9
 */
public class MessageConsumer implements Runnable {
    public static final String CHANNEL_KEY = "channel*";//频道

    public static final String EXIT_COMMAND = "exit";//结束程序的消息

    private MyJedisPubSub myJedisPubSub = new MyJedisPubSub();//处理接收消息

    public void consumerMessage() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.psubscribe(myJedisPubSub, CHANNEL_KEY);//第一个参数是处理接收消息，第二个参数是订阅的消息频道
    }

    @Override
    public void run() {
        while (true) {
            consumerMessage();
        }
    }

    public static void main(String[] args) {
        MessageConsumer messageConsumer = new MessageConsumer();
        Thread t1 = new Thread(messageConsumer, "thread5");
        Thread t2 = new Thread(messageConsumer, "thread6");
        t1.start();
        t2.start();
    }
}

/**
 * 继承JedisPubSub，重写接收消息的方法
 */
class MyJedisPubSub extends JedisPubSub {
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(Thread.currentThread().getName()+"-接收到消息:pattern="+pattern+",channel=" + channel + ",message=" + message);
        //接收到exit消息后退出
        if (MessageConsumer.EXIT_COMMAND.equals(message)) {
            System.exit(0);
        }
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

重写JedisPubSub 的onPMessage方法即可

 

启动生产者生产消息之后查看消费者控制台:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message0
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message0
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message1
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message1
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message2
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message2
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message3
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message3
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message4
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message4
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message5
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message5
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message6
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message6
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message7
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message7
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message8
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message8
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message9
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message9
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message10
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message10
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message11
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message11
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message12
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message12
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message13
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message13
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message14
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message14
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message15
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message15
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message16
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message16
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message17
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message17
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message18
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message18
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message19
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message19
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message20
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message20
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message21
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message21
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message22
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message22
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message23
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message23
thread5-接收到消息:pattern=channel*,channel=channel:1,message=message24
thread6-接收到消息:pattern=channel*,channel=channel:1,message=message24
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

补充:订阅的时候subscribe()和psubscribe()的第二个参数支持可变参数，也就是可以实现订阅多个频道。

### 至此实现了两种方式的消息队列:

- redis自带的list类型(lpush和rpop或者brpop，rpush和lpop或者blpop)---blpop和brpop是阻塞读取。

- "发布/订阅"模式(publish channel message 和 subscribe channel [channel ...] 或者 psubscribe pattern [pattern ...] 通配符订阅多个频道)

 

 

 

### **补充:**

### **1.==订阅执行之后该线程处于阻塞状态，线程不会终止，如果终止线程需要退订，需要调用JedisPubSub的unsubscribe()方法==**

**例如:**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
package plainTest;

import cn.xm.redisChat.util.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author: qlq
 * @Description
 * @Date: 23:36 2018/10/13
 */
public class Test111 {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        System.out.println("订阅前");
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
            }
        }, "c1");
        System.out.println("订阅后");
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

**结果只会打印订阅前，而且线程不会终止。**

 

**为了使线程可以停止，必须退订，而且退订只能调用** JedisPubSub.unsubscribe()方法，例如:收到quit消息之后会退订，线程会回到主线程打印订阅后。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
package plainTest;

import cn.xm.redisChat.util.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author: qlq
 * @Description
 * @Date: 23:36 2018/10/13
 */
public class Test111 {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        System.out.println("订阅前");
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if("quit".equals(message)){
                    unsubscribe("c1");
                }
                System.out.println(message);
            }

            @Override
            public void unsubscribe(String... channels) {
                super.unsubscribe(channels);
            }
        }, "c1");
        System.out.println("订阅后");
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

### **2.==BRPOP:当给定列表内没有任何元素可供弹出的时候，连接将被BRPOP命令阻塞，直到等待超时或发现可弹出元素为止。==(每次只弹出一个元素，当没有元素的时候处于阻塞，当弹出一个元素之后就会解除阻塞)**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
package plainTest;

import cn.xm.redisChat.util.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Author: qlq
 * @Description
 * @Date: 23:36 2018/10/13
 */
public class Test111 {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        System.out.println("brpop之前");
        List<String> messages = jedis.brpop(0,"list1");
        System.out.println(messages);
        System.out.println("brpop之后");
    }
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

没有元素的时候只会打印brpop之前。