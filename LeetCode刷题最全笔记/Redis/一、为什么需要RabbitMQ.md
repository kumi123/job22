



## 一、为什么需要RabbitMQ

### 1、简介

AMQP，即Advanced Message Queuing [Proto]()col，==高级消息队列协议==，是应用层协议的一个开放标准，为面向消息的中间件设计。消息中间件主要用于组件之间的解耦，消息的发送者无需知道消息使用者的存在，反之亦然。 **AMQP的主要特征是面向消息、队列、路由（包括==点对点和发布/订阅==）、可靠性、安全。** RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写，支持多种[客户端]()，如：Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等，支持AJAX。用==于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。== 

### 2、原因

- 在分布式系统下具备==**异步,削峰,负载均衡**==等一系列高级功能;

- ==拥有**持久化**的机制，进程消息，队列中的信息也可以保存下来。==

- ==实现**消费者和生产者之间的解耦**。==

- ==对于高并发场景下，利用消息队列可以使得**同步访问变为串行访问**达到一定量的限流，利于数据库的操作。==

- ==可以使用消息队列达到**异步**下单的效果，排队中，后台进行逻辑下单。==

  ### 3、使用场景

- ==服务间异步通信==

- ==顺序消费==

- ==定时任务==

- ==请求削峰==

  ## 二、RabbitMQ基础

  ### 1、基础架构

  ![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608500_20200430220017232.png)

- message
  消息，消息是不具名的，它由==**消息头**和**消息体**组成==。消息体是不透明的，而消息头则由一系列的可选属性组成，这些属性包括 routing-key（路由键）、priority（相对于其他消息的优先权）、delivery-mode（指出该消息可能需要持久性存储）等。

- Publisher

- *消息的生产者，也是一个向交换器发布消息的[客户端]()应用程序。**

- Exchange（**将消息路由给队列** ）

- *交换器**，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。

- Binding（**消息队列和交换器之间的关联**）
  绑定，**用于消息队列和交换器之间的关联**。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则，所以可以将交换器理解成一个由绑定构成的路由表。

- Queue

- *消息队列，** 用来保存消息直到发送给消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个队列。消息一直在队列里面，等待消费者连接到这个队列将其取走。
  Connection

- **网络连接**，比如一个 TCP 连接。

- Channel

- *信道，** 多路复用连接中的**一条独立的双向数据流通道**。信道是建立在真实的 TCP 连接内地虚拟连接，AMQP 命令都是通过信道发出去的，不管是发布消息、订阅队列还是接收消息，这些动作都是通过信道完成。因为对于操作系统来说建立和销毁 TCP 都是非常昂贵的开销，所以引入了信道的概念，以复用一条 TCP 连接。

- Consumer

- *消息的消费者**，表示一个从消息队列中取得消息的[客户端]()应用程序。

- Virtual Host
  虚拟主机，表示一批交换器、消息队列和相关对象。**虚拟主机是共享相同的身份认证和加密**

- Broker
  表示**消息队列服务器实体**。

  ### 2、Exchange 类型

  Exchange 分发消息时根据类型的不同分发策略有区别，目前共四种类型：**direct、fanout、topic、headers** 。headers 匹配 AMQP 消息的 header 而不是路由键，此外 headers 交换器和direct 交换器完全一致，但性能差很多，目前几乎用不到了。

- Direct 键（routing key）分布
  Direct：**消息中的路由键（routing key）如果和 Binding 中的 binding key 一致**，交换器就将消息发到对应的队列中。它是完全匹配、单播的模式
  ![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608499_20200430221759681.png)

- Fanout（广播分发）
  Fanout：每个发到 fanout 类型交换器的消息都会分到所有绑定的队列上去。很像**子网广播**，每台子网内的主机都获得了一份复制的消息，fanout 类型转发消息是最快的。
  ![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608619_20200430221947926.png)

- topic 交换器（模式匹配）
  topic 交换器：topic 交换器通过**模式匹配分配消息的路由键属性**，将路由键和某个模式进行匹配，此时队列需要绑定到一个模式上。它将路由键和绑定键的字符串切分成单词，这些单词之间用点隔开。它同样也会识别两个通配符：符号“#”和符号“”。#匹配 0 个或多个单词，匹配不多不少一个单词。
  ![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608632_20200430222254131.png)

- Headers
  Headers Exchange不同于上面三种Exchange，它是**根据Message的一些头部信息来分发过滤Message，忽略routing key的属性，如果Header信息和message消息的头信息相匹配，那么这条消息就匹配上了。**![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608398_20200430222542672.png)

  ![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608459_20200430230151501.png)

  ## 三、搭建一个简单demo

  ### 1、消息发送方配置及使用

- pom.xml配置

[复制代码](#)

```
<dependencies>``    ``<dependency>``      ``<groupId>org.springframework.boot</groupId>``      ``<artifactId>spring-boot-starter-amqp</artifactId>``    ``</dependency>``    ``<dependency>``      ``<groupId>org.springframework.boot</groupId>``      ``<artifactId>spring-boot-starter-web</artifactId>``    ``</dependency>` `    ``<dependency>``      ``<groupId>org.springframework.boot</groupId>``      ``<artifactId>spring-boot-starter-test</artifactId>``      ``<scope>test</scope>``      ``<exclusions>``        ``<exclusion>``          ``<groupId>org.junit.vintage</groupId>``          ``<artifactId>junit-vintage-engine</artifactId>``        ``</exclusion>``      ``</exclusions>``    ``</dependency>``    ``<dependency>``      ``<groupId>org.springframework.amqp</groupId>``      ``<artifactId>spring-rabbit-test</artifactId>``      ``<scope>test</scope>``    ``</dependency>``  ``</dependencies>
```

- 基本连接配置 

[复制代码](#)

```
spring.rabbitmq.host=***``spring.rabbitmq.username=***``spring.rabbitmq.password=***``spring.rabbitmq.port=``5672
```

- rabbitMQ配置 

[复制代码](#)

```
@Configuration``public` `class` `RabbitMQConfig {` `  ``@Bean``  ``Queue queue() {``    ``return` `new` `Queue(``"StringQueue"``);``  ``}` `}
```

- 添加发送逻辑 

[复制代码](#)

```
@Repository``public` `class` `RabbitSender {``  ``@Autowired``  ``private` `AmqpTemplate rabbitTemplete;` `  ``public` `String sendString() {``    ``rabbitTemplete.convertAndSend(``"StringQueue"``,``"string message send"``);``    ``return` `"string send ok!"``;``  ``}``}
```

- Controller层 

[复制代码](#)

```
@RestController``@RequestMapping``(value = ``"/rabbitsender"``)``public` `class` `RabbitMQController {``  ``@Autowired``  ``private` `RabbitSender sender;` `  ``@RequestMapping``(value=``"/sendMessage"``,method= RequestMethod.GET)``  ``public` `Result<String> sendMessage() {``    ``String ret = sender.sendString();``    ``Result<String> result = ``new` `Result<>(ResultCode.OK, ret);``    ``return` `result;``  ``}``}
```

启动发送方服务，调用上述接口：
![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608365_20200430230118345.png)
消息队列有消息显示：
![在这里插入图片描述](https://uploadfiles.nowcoder.com/files/20200501/783233862_1588262608552_20200430230218463.png)

### 2、接收方配置及使用

在接收方需要做的就是把接收逻辑与RabbitMQ中的队列绑定，当队列中有待处理的消息时，接收方从中获取消息并且进行处理

- 队列配置 

[复制代码](#)

```
public` `class` `RabbitmqConfig {``  ``@Bean``  ``public` `Queue StringQueue(){``    ``return` `new` `Queue(``"StringQueue"``);``  ``}``}
```

- 添加队列监听，并且获取消息进行处理 

[复制代码](#)

```
@Component``@RabbitListener``(queues = ``"StringQueue"``)``public` `class` `MessageReceiver {``  ``@RabbitHandler``  ``public` `void` `process(String message){``    ``System.out.println(``"messageReceiver:"``+message);``  ``}``}
```

- 接收方与发送方进行测试

  由于两方在不同的工程中创建，因此它们的启动端口有所不同，另外发送方应该先启动，接收方后启动，接收方的控制台输出如下内容：

  

  ## 四、常见面试题

  ### 1、如何确保消息正确地发送至RabbitMQ？ 如何确保消息接收方消费了消息？

- ==**发送方确认模式**==
  将==信道==设置成**==confirm模式（发送方确认模式）==**，则所有在信道上发布的消息都会==**被指派一个唯一的ID**==。一旦消息被投递==到目的队列后==，或者消息被写入磁盘后（可持久化的消息），==**信道会发送一个确认给生产者（包含消息唯一ID）**==。如果RabbitMQ发生内部错误从而==导致消息丢失==，==**会发送一条nack（not acknowledged，未确认）消息**==。==发送方确认模式是异步的==，生产者应用程序在==等待确认的同时，可以继续发送消息==。当确认消息到达生产者应用程序，生产者应用程序的==回调方法就会被触发来处理确认消息==。 

- 接收方确认机制

  接收方消息确认机制：==消费者接收每一条消息后都必须进行确认==（消息接收和消息确认是两个不同操作）。

  只有消费者确认了消息，RabbitMQ才能安全地把消息从队列中删除。

  这里并没有用到超时机制，RabbitMQ仅通过Consumer的连接中断来确认是否需要重新发送消息。

  也就是说，只要连接不中断，RabbitMQ给了Consumer足够长的时间来处理消息。保证数据的最终一致性

  ；

  下面罗列几种特殊情况：

  如果消费者接收到消息，在确认之前断开了连接或取消订阅，RabbitMQ会认为消息没有被分发，然后重新分发给下一个订阅的消费者。（可能存在消息重复消费的隐患，需要去重）

  如果消费者接收到消息却没有确认消息，连接也未断开，则RabbitMQ认为该消费者繁忙，将不会给该消费者分发更多的消息。

  ### 2.如何避免消息重复投递或重复消费？

   在消息生产时，MQ内部

  针对每条生产者发送的消息生成一个inner-msg-id，作为去重的依据（消息投递失败并重传），避免重复的消息进入队列；

   在消息消费时，要求消息体中必须要有一个bizId（对于同一业务全局唯一，如支付ID、订单ID、帖子ID等）作为去重的依据，避免同一条消息被重复消费。

  ### 3、消息基于什么传输？

   由于TCP连接的创建和销毁开销较大，且并发数受系统资源限制，会造成性能瓶颈。

  RabbitMQ使用信道的方式来传输数据。信道是建立在真实的TCP连接内的虚拟连接，且每条TCP连接上的信道数量没有限制。

  ### 4、消息如何分发？

   若该队列至少有一个消费者订阅，消息将以循环（round-robin）的方式发送给消费者。每条消息只会分发给一个订阅的消费者（前提是消费者能够正常处理消息并进行确认）。通过

  路由可实现多消费

  的功能

  ### 5、消息怎么路由？

  消息提供方->路由->一至多个队列

  ，消息发布到交换器时，

  消息将拥有一个路由键（routing key）

  ，在消息创建时设定。通过

  队列路由键，可以把队列绑定到交换器上。

   消息到达交换器后，RabbitMQ会将消息的路由键与队列的路由键进行匹配（针对不同的交换器有不同的路由规则）；

  常用的交换器主要分为一下三种：

  fanout：如果交换器收到消息，将会广播到所有绑定的队列上

  direct：如果路由键完全匹配，消息就被投递到相应的队列

  topic：可以使来自不同源头的消息能够到达同一个队列。 使用topic交换器时，可以使用通配符

### 6、如何确保消息不丢失？

**消息持久化，当然前提是队列必须持久化**。RabbitMQ确保持久性消息能从服务器重启中恢复的方式是 **，将它们写入磁盘上的一个持久化日志文件，** 当发布一条持久性消息到持久交换器上时，Rabbit会在消息提交到日志文件后才发送响应。**一旦消费者从持久队列中消费了一条持久化消息，RabbitMQ会在持久化日志中把这条消息标记为等待垃圾收集**。如果持久化消息在被消费之前RabbitMQ重启，那么Rabbit会自动重建交换器和队列（以及绑定），并重新发布持久化日志文件中的消息到合适的队列。

### 7、使用RabbitMQ有什么好处？

- ==服务间高度解耦== 
- ==异步通信性能高== 
- ==流量削峰== 

### 8、rabbitmq的集群

- 镜像集群模式
  你创建的queue，无论元数据还是queue里的消息都会存在于多个实例上，然后每次你写消息到queue的时候，都会**自动把消息到多个实例的queue里进行消息同步。** 
- *好处在于，你任何一个机器宕机了，没事儿，别的机器都可以用**。坏处在于，第一，这个**性能开销也太大**了吧，消息同步所有机器，导致网络带宽压力和消耗很重！第二，这么玩儿，就没有扩展性可言了，如果某个queue负载很重，你加机器，新增的机器也包含了这个queue的所有数据，并没有办法线性扩展你的queue 

### 9、rabbitmq的缺点

- **系统可用性降低**
  **系统引入的外部依赖越多，越容易挂掉，**本来你就是A系统调用BCD三个系统的接口就好了，本来ABCD四个系统好好的，没啥问题，你偏加个MQ进来，万一MQ挂了咋整？MQ挂了，整套系统崩溃了，你不就完了么。 
- **系统复杂性提高**
  硬生生加个MQ进来，你怎么保证消息没有重复消费？怎么处理消息丢失的情况？怎么保证消息传递的顺序性？ 
- **一致性问题**
  A系统处理完了直接返回成功了，本来都以为你这个请求就成功了；但是问题是，BD两个系统写库成功了，结果C系统写库失败了，咋整？你这数据就不一致了。