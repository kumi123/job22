### 如何保证消息可靠传输
​        消息丢失可能会出现以下三种数据丢失问题： ==（1）生产者弄丢了数据（2）MQ弄丢了数据（3）消费者弄丢了数据==


   （1）生产者弄丢了数据

- 第一种解决方案是==使用RabbitMQ的事务模式==，不过这种==方案是同步的，非常耗性能，一般不使用==。

- 第二种解决方案是使用==confirm模式==。==生产者开启confirm模式后，每次的消息都会被分配一个id，然后写入到RabbitMQ中==。==RabbitMQ会响应一个ack代表消息已经成功写入MQ中。如果是nack，那么代表没有发送到MQ中失败，会触发回调接口。可以在这个回调接口里面重新发送消息。==

- ==confirm模式是异步的，在发完一个消息的后可以再发送另外的消息。RabbitMQ 接收了之后会异步回调你的一个接口通知你这个消息接收到了。==

   （2）RabbitMQ 弄丢了数据

- 如何防止MQ把消息弄丢了，那么就要==支持持久化==。包括==持久化路由器、队列、消息，这样在重启的时候能够恢复这些数据==。

- 发送消息的时候将消息的 deliveryMode 设置为 2，就是将消息设置为持久化的。（默认就是2） 创建队列持久化  创建路由器持久化    

 （3）消费者弄丢了数据（未确认消息）
    
- 消费端刚接收到消息，然后系统就不可用了。如果是自动ack，那么MQ就认为消费端已经成功消费这条消息，但是实际上还没有来得及消费。所以==必须关闭 RabbitMQ 的自动 ack。如果消费消息失败，那么会把消息重新退回MQ中，由MQ将此消息分配给其它消费者。==

