##### ReentrantLock（可重入锁）与synchronized区别：

相同点：

==**（1）可重入性**==
==二者都是同一个线程进入1次，锁的计数器就自增1，需要等到锁的计数器下降为0时，才能释放锁。==



**性能方面**
synchronized优化之前性能比ReentrantLock差很多，但是自从synchronized引入了偏向锁，轻量级锁也就是自旋锁后，性能就差不多了。

**（4）不同点**

- 底层实现
  ==synchronized是基于JVM实现的，而ReentrantLock是JDK实现的。==

- 便利性

==synchronized使用起来比较方便，并且由编译器保证加锁和释放锁；ReentrantLock需要手工声明加锁和释放锁，最好是在finally代码块中声明释放锁。==

- 锁的灵活度和细粒度

==在这点上ReentrantLock会优于synchronized。==

- 可中断性

  ==提供能够中断等待锁的线程的机制，lock.lockInterruptibly()。==

- 锁的性质

  ==ReentrantLock实现是一种自旋锁，通过循环调用CAS操作来实现加锁，性能上比较好是因为避免了使线程进入内核态的阻塞状态。==而另一个是悲观锁并发度差一些
  
- ==ReentrantLock可指定是公平锁还是非公平锁。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。==

- ==提供了一个Condition类，可以分组唤醒需要唤醒的线程。而synchronized只能随机唤醒一个线程，或者唤醒全部的线程==







