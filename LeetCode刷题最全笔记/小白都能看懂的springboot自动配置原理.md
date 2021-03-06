# [小白都能看懂的springboot自动配置原理](https://segmentfault.com/a/1190000030685746)

​    随着互联网越来越流行，springboot已经成为我们无论是工作，还是面试当中，不得不掌握的技术。说起springboot笔者认为最重要的功能非自动配置莫属了，为什么这么说？如果参与过以前==spring复杂项目的朋友肯定，有过这样的经历，每次需要一个新功能，比如事务、AOP等，需要大量的配置，需要导出找jar包==，时不时会出现jar兼容性问题，可以说苦不堪言。

   springboot的出现得益于“习惯优于配置”的理念，==没有繁琐的配置、难以集成的内容==（大多数流行第三方技术都被集成），这是基于Spring 4.x以上的版本提供的按条件配置Bean的能力。有了springboot的自动配置的功能，我们可以快速的开始一个项目。

## 一 什么是自动配置

不知道朋友们在工作当中有没有这样的经历:

### 1.1 引入redisTemplate

只要我们在pom.xml文件中引入==spring-boot-starter==-data-redis-xxx.jar包，然后只要在==配置文件中配置redis连接==，如：

```
spring.redis.database = 0
spring.redis.timeout = 10000
spring.redis.host = 10.72.16.9
spring.redis.port = 6379
spring.redis.pattern = 1
```

就可以==在service方法中直接注入`StringRedisTemplate`对象的实例，可以直接使用了。朋友们有没有想过这是为什么？==

```
@Autowired
private StringRedisTemplate stringRedisTemplate;
```

### 1.2  引入transactionTemplate

在项目中只要引入==spring-boot-starter-xxx.jar，事务就自动生效了，并且可以直接在service方法中直接注入TransactionTemplate，用它开发编程式事务代码==。是不是很神奇？这又是为什么？

### 1.3 使用@ConfigurationProperties

使用==@ConfigurationProperties可以把指定路径下的属性，直接注入到实体对象中==，看看下面这个例子：

```
@Data
@Component
@ConfigurationProperties("jump.threadpool")
public class ThreadPoolProperties {

 private int corePoolSize;
 private int maxPoolSize;
 private int keepAliveSeconds;
 private int queueCapacity;
}
```

只要application.properties这样配置，就可以自动注入到上面的实体中

```
jump.threadpool.corePoolSize=8
jump.threadpool.maxPoolSize=16
jump.threadpool.keepAliveSeconds=10
jump.threadpool.queueCapacity=100
```

没错，这三个例子都是springboot自动配置在起作用，我们分为两种情况：bean的自动配置 和 属性的自动配置。

二 工作原理

------

### 2.1 bean的自动配置

Spring Boot的==启动类==上有一个==@SpringBootApplication注解==，这个注解是Spring Boot项目必不可少的注解。

我们先看看@SpringBootApplication注解

![img](https://segmentfault.com/img/remote/1460000030685753)

它上面定义了另外一个注解：==@EnableAutoConfiguration==

![img](https://segmentfault.com/img/remote/1460000030685749)

该注解的关键功能==由@Import提供，其导入的AutoConfigurationImportSelector的selectImports()方法通过SpringFactoriesLoader.loadFactoryNames()扫描所有具有META-INF/spring.factories的jar包下面key是EnableAutoConfiguration全名的，所有自动配置类。==

我们看看springboot的spring-boot-autoconfigure-xxx.jar

![img](https://segmentfault.com/img/remote/1460000030685752)

该jar包里面就有META-INF/spring.factories文件。

![img](https://segmentfault.com/img/remote/1460000030685751)

这个==spring.factories文件是一组一组的key=value的形式==，其中一个key是EnableAutoConfiguration类的全类名，而它的==value是一个xxxxAutoConfiguration的类名==的列表，这些类名以逗号分隔。





灵魂

==@EnableAutoConfiguration注解通过@SpringBootApplication被间接的标记在了Spring Boot的启动类上。在SpringApplication.run(...)的内部就会执行selectImports()方法，找到所有JavaConfig自动配置类的全限定名对应的class，然后将所有自动配置类加载到Spring容器中。==

SpringApplication.run(...)方法怎么调到selectImports()方法的

加载过程大概是这样的：

SpringApplication.run(...)方法  》 

AbstractApplicationContext.refresh()方法 》 

invokeBeanFactoryPostProcessors(...)方法 》 

PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(...) 方法 》

ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(..)方法 》

AutoConfigurationImportSelector.selectImports

该方法会找到自动配置的类，并给打了@Bean注解的方法创建对象。

postProcessBeanDefinitionRegistry方法是最核心的方法，它负责解析@Configuration、@Import、@ImportSource、@Component、@ComponentScan、@Bean等，完成bean的自动配置功能。

回到刚刚第二个例子TransactionTemplate为什么可以直接引用？

是因为在spring-boot-autoconfigure-xxx.jar的spring.factories配置文件中，EnableAutoConfiguration全类名下配置了TransactionAutoConfiguration全类名，springboot在启动的时候会加载这个类。

![img](https://segmentfault.com/img/remote/1460000030685754)

而TransactionAutoConfiguration类是一个配置类，它里面创建TransactionTemplate类的实例。

![img](https://segmentfault.com/img/remote/1460000030685750)

### 这样在其他地方就可以直接注入TransactionTemplate类的实例。

### 2.2 属性的自动配置

属性的自动配置是通过ConfigurationPropertiesBindingPostProcessor类的postProcessBeforeInitialization方法完成，

```
public Object postProcessBeforeInitialization(Object bean, String beanName)
 throws BeansException {
 ConfigurationProperties annotation = getAnnotation(bean, beanName,
 ConfigurationProperties.class);
 if (annotation != null) {
 bind(bean, beanName, annotation);
 }
 return bean;
}
```

它会解析@ConfigurationProperties注解上的属性，将配置文件中对应key的值绑定到属性上。

## 三 自动配置的生效条件

### ==每个xxxxAutoConfiguration类上都可以定义一些生效条件，这些条件基本都是从@Conditional派生出来的。==

常用的条件如下：

```
@ConditionalOnBean：当容器里有指定的bean时生效
@ConditionalOnMissingBean：当容器里不存在指定bean时生效
@ConditionalOnClass：当类路径下有指定类时生效
@ConditionalOnMissingClass：当类路径下不存在指定类时生效
@ConditionalOnProperty：指定的属性是否有指定的值，比如@ConditionalOnProperties(prefix=”xxx.xxx”, value=”enable”, matchIfMissing=true)，代表当xxx.xxx为enable时条件的布尔值为true，如果没有设置的情况下也为true。
```

举个比较常用的例子看看TransactionAutoConfiguration，是如何使用条件的

![img](https://segmentfault.com/img/remote/1460000030685755)

我们可以看到，条件用的是：@ConditionalOnClass，表示TransactionAutoConfiguration类只有在PlatformTransactionManager类存在时才会生效。

如何自定义自动配置类？

请阅读《[老司机手把手教你编写自己的springboot starter](http://mp.weixin.qq.com/s?__biz=MzUxODkzNTQ3Nw==&mid=2247484236&idx=1&sn=e7eb1011b39ab1f1bbeb46e1b6228b93&chksm=f9800596cef78c80416c7cdaa9177af8fac8c98959beea95af46c7ac45e8892f73228cb9f879&scene=21#wechat_redirect)》里面有详细步骤。

## 总结

本篇文章从什么是自动配置，工作原理 和 自动配置的生效条件 三个方面介绍了自动配置的相关知识点。自动配置又分为：bean的自动配置 和 属性的自动配置，二者的实现原理不一样。自动配置的生效条件用得非常多，建议朋友们好好研究一下。至于如何自定义自动配置类，本篇没有讲，是因为我在另外一篇文章《[老司机手把手教你编写自己的springboot starter](http://mp.weixin.qq.com/s?__biz=MzUxODkzNTQ3Nw==&mid=2247484236&idx=1&sn=e7eb1011b39ab1f1bbeb46e1b6228b93&chksm=f9800596cef78c80416c7cdaa9177af8fac8c98959beea95af46c7ac45e8892f73228cb9f879&scene=21#wechat_redirect)》中仔细介绍过的，有需要的朋友可以自行查阅。