# SSM框架原理,作用及使用方法



SSM是sping+springMVC+mybatis集成的框架。是标准的MVC模式，将整个系统划分为==表现层，controller层，service层，DAO层==四层

使用==spring MVC负责请求的转发和视图管理==

==spring实现业务对象管理==，==mybatis作为数据对象的持久化引擎==；

MVC即==model view controller==。

model层=entity层。存放我们的实体类，与数据库中的属性值基本保持一致。

service层。存放业务逻辑处理，也是一些关于数据库处理的操作，但不是直接和数据库打交道，他有接口还有接口的实现方法，在接口的实现方法中需要导入mapper层，mapper层是直接跟数据库打交道的，他也是个接口，只有方法名字，具体实现在mapper.xml文件里，service是供我们使用的方法。

mapper层=dao层，现在用mybatis逆向工程生成的mapper层，其实就是dao层。对数据库进行数据持久化操作，他的方法语句是直接针对数据库操作的，而service层是针对我们controller，也就是针对我们使用者。service的impl是把mapper和service进行整合的文件。

（多说一句，数据持久化操作就是指，把数据放到持久化的介质中，同时提供增删改查操作，比如数据通过hibernate插入到数据库中。）

controller层。控制器，导入service层，因为service中的方法是我们使用到的，==controller通过接收前端传过来的参数进行业务操作，在返回一个指定的路径或者数据表。==



==SpringMVC：==

==1.客户端发送请求到DispacherServlet（分发器）==

==2.由DispacherServlet控制器查询HanderMapping，找到处理请求的Controller==

==3.Controller调用业务逻辑处理后，返回ModelAndView==

==4.DispacherServlet查询视图解析器，找到ModelAndView指定的视图==

==5.视图负责将结果显示到客户端==

![img](https://upload-images.jianshu.io/upload_images/16045088-8a655995c36f7f0f.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

Spring：我们平时开发接触最多的估计就是IOC容器，它可以装载bean（也就是我们Java中的类，当然也包括service dao里面的），有了这个机制，我们就不用在每次使用这个类的时候为它初始化，很少看到关键字new。另外spring的aop，事务管理等等都是我们经常用到的。

Mybatis：==mybatis是对jdbc的封装，它让数据库底层操作变的透明==。mybatis的操作都是==围绕一个sqlSessionFactory实例展开的==。==mybatis通过配置文件关联到各实体类的Mapper文件，Mapper文件中配置了每个类对数据库所需进行的sql语句映射==。在每次与数据库交互时，通过sqlSessionFactory拿到一个sqlSession，再执行sql命令。

使用方法：

要完成一个功能：





#### 从DAO 层开始写，然后到Controller层这样慢慢的写

- 先写实体类entity，定义对象的属性，（可以参照数据库中表的字段来设置，数据库的设计应该在所有编码开始之前）。

- 写Mapper.xml（Mybatis），其中定义你的功能，对应要对数据库进行的那些操作，比如 insert、selectAll、selectByKey、delete、update等。

- 写Mapper.java，将Mapper.xml中的操作按照id映射成Java函数。

- 写Service.java，为控制层提供服务，接受控制层的参数，完成相应的功能，并返回给控制层。

- 写Controller.java，==连接页面请求和服务层==，==获取页面请求的参数，映射不同的URL到相应的处理函数，并获取参数，对参数进行处理，之后传给服务层。==

- 写==JSP页面调用，请求哪些参数，需要获取什么数据==。

DataBase ===> Entity ===> Mapper.xml ===> Mapper.Java ===> Service.java ===> Controller.java ===> Jsp. 

====================================================================================================================================

Spring MVC 拥有控制器，作用跟Struts类似，接收外部请求，解析参数传给服务层

Spring 容器属于协调上下文，管理对象间的依赖，提供事务机制

mybatis 属于orm持久层框架，将业务实体 与数据表联合 起来

Spring MVC 控制层，想当与 Struts的作用

Spring 控制反转和依赖注入 创建对象交由容器管理，达到了解耦的作用

mybatis 主要用来操作数据库（数据库的增删改查）

IOC:控制反转，是一种==降低对象之间耦合关系的设计思想==，面试的时候最好能说出来个例子，加深理解。例子：租房子，以前租房子需要一个房子一个房子找，费时费力，然后现在加入一个房屋中介，把你需要的房型告诉中介，就可以直接选到需要的房子，中介就相当于spring容器。

AOP:面向切面编程，是==面向对象开发的一种补充==，它==允许开发人员在不改变原来模型的基础上动态的修改模型以满足新的需求==，如：==动态的增加日志、安全或异常处理==等。==AOP使业务逻辑各部分间的耦合度降低，提高程序可重用性，提高开发效率==。



## 持久层：DAO层（mapper）

DAO层：DAO层主要是==做数据持久层==的工作，负责与数据库进行联络的一些任务都封装在此，

DAO层的设计首先是==设计DAO的接口==，

然后在==Spring的配置文件中定义此接口的实现类==，

然后就可在模块中调用此接口来进行数据业务的处理，而不用关心此接口的具体实现类是哪个类，显得结构非常清晰，

DAO层的==数据源配置，以及有关数据库连接的参数都在Spring的配置文件中进行配置==。

## 业务层：Service层

Service层：Service层主要负责业务模块的逻辑应用设计。

首先设计接口，再设计其实现的类

接着再在Spring的配置文件中配置其实现的关联。这样我们就可以在应用中调用Service接口来进行业务处理。

==Service层的业务实现，具体要调用到已定义的DAO层的接口==，

封装Service层的业务逻辑有利于通用的业务逻辑的独立性和重复利用性，程序显得非常简洁。

==Service层是建立在DAO层之上的，建立了DAO层后才可以建立Service层，而Service层又是在Controller层之下的，因而Service层应该既调用DAO层的接口，又要提供接口给Controller层的类来进行调用，它刚好处于一个中间层的位置。每个模型都有一个Service接口，每个接口分别封装各自的业务处理方法。==

## 表现层：Controller层（Handler层）

Controller层:Controller层负责具体的业务模块流程的控制，

在此层里面要调用Service层的接口来控制业务流程，

控制的配置也同样是在Spring的配置文件里面进行，针对具体的业务流程，会有不同的控制器，我们具体的设计过程中可以将流程进行抽象归纳，设计出可以重复利用的子单元流程模块，这样不仅使程序结构变得清晰，也大大减少了代码量。

View层

View层 此层与控制层结合比较紧密，需要二者结合起来协同工发。View层主要负责前台jsp页面的表示.







====================================================================================================================================

  最近在学习Spring+SpringMVC+MyBatis的整合。以下是参考网上的资料自己实践操作的详细步骤。



1、基本概念



1.1、Spring 

​    Spring是一个开源框架，Spring是于2003 年兴起的一个轻量级的Java 开发框架，由Rod Johnson 在其著作Expert One-On-One J2EE Development and Design中阐述的部分理念和原型衍生而来。它是为了解决企业应用开发的复杂性而创建的。Spring使用基本的JavaBean来完成以前只可能由EJB完成的事情。然而，Spring的用途不仅限于服务器端的开发。从简单性、可测试性和松耦合的角度而言，任何Java应用都可以从Spring中受益。 简单来说，Spring是一个轻量级的控制反转（IoC）和面向切面（AOP）的容器框架。



1.2、SpringMVC   

​    Spring MVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。Spring MVC 分离了控制器、模型对象、分派器以及处理程序对象的角色，这种分离让它们更容易进行定制。



1.3、MyBatis

​    MyBatis 本是apache的一个开源项目iBatis, 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。MyBatis是一个基于Java的持久层框架。iBATIS提供的持久层框架包括SQL Maps和Data Access Objects（DAO）MyBatis 消除了几乎所有的JDBC代码和参数的手工设置以及结果集的检索。MyBatis 使用简单的 XML或注解用于配置和原始映射，将接口和 Java 的POJOs（Plain Old Java Objects，普通的 Java对象）映射成数据库中的记录。