# 【万字长文】Spring AOP 层层递进轻松入门 ！



![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b0d0b0c89e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



# 初识 AOP（传统程序）

Tips：如果想要快速查阅的朋友，可以直接跳转到 初识AOP（Spring 程序）这一大节

前面的内容以联系过去 Java、JavaWeb 的知识逐步引入到 AOP 为主 真正的Spring AOP内容包括下面几个板块 可以跳转一下哈

(一) AOP 术语

(二) AOP 入门案例：XML 、注解方式

(三) 完全基于 Spring 的事务控制：XML、注解方式、纯注解方式

## (一) AOP的简单分析介绍

> 在软件业，**AOP**为Aspect Oriented Programming的缩写，意为：**面向切面编程**，通过预编译方式和运行期间**动态代理**实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。==利用AOP可以对**业务逻辑的各个部分进行隔离**，从而使得业务逻辑各部分之间的**耦合度降低**，提高程序的可重用性，同时提高了开发的效率==。
>
> —— 百度百科

开篇就直接来看 Spring AOP 中的百科说明，我个人认为是非常晦涩的，当回过来头再看这段引言的时候，才恍然大悟，这段话的意思呢，说白了，==就是说我们把程序中一些**重复的代码**拿出来，在需要它执行的时候，可以通过**预编译**或者**运行期的动态代理**实现**不动源码**而动态的给程序进行**增强或者添加功能**的技术== ，==一个很重要的应用就是事务==

拿出一些重复的代码？ 拿出的究竟是什么代码呢？举个例子！

在下面的方法中，我们模拟的是程序中对事务的管理，下面代码中的 A B都可以看做 “==开启事务”、“提交事务” 的一些事务场景，这些代码就可以看做是上面所说的重复的代码的一种==



![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b0b26157d7?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



而还有一些重复代码大多是关于==**权限管理**或者说**日志登录**==等一些虽然影响了我们 代码业务逻辑的 “干净”（因为重复了)，但是却不得不存在，如果有什么办法==能够抽取出这些方法，使得我们的业务代码更加简洁==，自然我们可以更专注与我们的业务，利于开发，这也就是我们今天想要说重点

最后不得不提的是，AOP 作为 Spring 这个框架的核心内容之一，很显然应用了大量的**设计模式**，设计模式，归根结底，就是==为了**解耦**，以及提高**灵活性**，**可扩展性**==，而我们所学的一些框架，直接把这些东西封装好，让你直接用，说的白一点，就是为了让你偷懒，让你既保持了良好的代码结构，又不需要和你去自己编写这些复杂的数据结构，**提高了开发效率**

一上来就直接谈 AOP术语阿，面向切面等等，很显然不是很合适，光听名字总是能能让人 “望文生怯” ， 任何技术的名字只不过是一个名词罢了，实际上对于入门来说，我们更需要搞懂的是，通过传统的程序与使用 Spring AOP 相关技术的程序进行比较，使用 AOP 可以帮助我们解决哪些问题或者需求，通过知其然，然后应用其所以然，这样相比较于，直接学习其基本使用方式，会有灵魂的多！

## (二) 演示案例（传统方式）

说明：下面的第一部分的例子是在上一篇文章的程序加以改进，为了照顾到所有的朋友，我把从依赖到类的编写都会提到，方便大家有需要来练习，看一下程序的整体结构，对后面的说明也有一定的帮助

### (1) 添加必要的依赖

- spring-context
- mysql-connector-java
- c3p0（数据库连接池）
- commons-dbutils（简化JDBC的工具）—后面会简单介绍一下
- junit （单元自测）
- spring-test

说明：由于我这里创建的是一个Maven项目，所以在这里修改 pom.xml 添加一些必要的依赖坐标就可以

如果创建时没有使用依赖的朋友，去下载我们所需要的 jar 包导入就可以了

```
<packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>

        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>

        <dependency>
            <groupId>commons-dbutils</groupId>
            <artifactId>commons-dbutils</artifactId>
            <version>1.4</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
    </dependencies>
复制代码
```

简单看一下，spring核心的一些依赖，以及数据库相关的依赖，还有单元测试等依赖就都导入进来了

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b11a7bb96a?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### (2) 创建账户表以及实体

下面所要使用的第一个案例，涉及到两个账户之间的模拟转账交易，所以我们创建出含有名称以及余额这样几个字段的表

#### A：创建 Account 表

```
-- ----------------------------
-- Table structure for account
-- ----------------------------
CREATE TABLE `account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32),
  `balance` float,
  PRIMARY KEY (`id`)
)
复制代码
```

#### B：创建 Account 类

没什么好说的，对应着我们的表创出实体

```
public class Account implements Serializable {
    private  Integer id;
    private String name;
    private Float balance;
    ......补充 get set toString 方法
复制代码
```

### (3) 创建Service以及Dao

下面我们演示事务问题，最主要还是使用 transfer 这个转账方法，当然还有一些增删改查的方法，我只留了一个查询所有的方法，到时候就可以看出传统方法中一些代码的重复以及复杂的工作度

#### A：AccountService 接口

```
public interface AccountService {
    /**
     * 查询所有
     * @return
     */
    List<Account> findAll();

    /**
     * 转账方法
     * @param sourceName    转出账户
     * @param targetName    转入账户
     * @param money
     */
    void transfer(String sourceName,String targetName,Float money);
}
复制代码
```

#### B：AccountServiceImpl 实现类

```
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    public List<Account> findAll() {
        return accountDao.findAllAccount();
    }
    
    public void transfer(String sourceName, String targetName, Float money) {
        //根据名称分别查询到转入转出的账户
        Account source = accountDao.findAccountByName(sourceName);
        Account target = accountDao.findAccountByName(targetName);

        //转入转出账户加减
        source.setBalance(source.getBalance() - money);
        target.setBalance(target.getBalance() + money);
        //更新转入转出账户
        accountDao.updateAccount(source);
        accountDao.updateAccount(target);
    }
}
复制代码
```

#### C：AccountDao 接口

```
public interface AccountDao {

    /**
     * 更细账户信息（修改）
     * @param account
     */
    void updateAccount(Account account);

    /**
     * 查询所有账户
     * @return
     */
    List<Account> findAllAccount();

    /**
     * 通过名称查询
     * @param accountName
     * @return
     */
    Account findAccountByName(String accountName);
}
复制代码
```

#### ==D：AccountDaoImpl 实现类(具体的来实现这个代码，可以使用注解也可以使用代码QueryRunner)==

我们引入了 DBUtils 这样一个操作数据库的工具，它的作用就是封装代码，达到简化 JDBC 操作的目的，由于以后整合 SSM 框架的时候，持久层的事情就可以交给 MyBatis 来做，而今天我们重点还是讲解 Spring 中的知识，所以这部分会用就可以了

用到的内容基本讲解：

==QueryRunner 提供对 sql 语句进行操作的 API （insert delete update）==

==ResultSetHander 接口，定义了查询后，如何封装结果集==（仅提供了我们用到的）

- BeanHander：将结果集中第第一条记录封装到指定的 JavaBean 中
- BeanListHandler：将结果集中的所有记录封装到指定的 JavaBean 中，并且将每一个 JavaBean封装到 List 中去

```
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private QueryRunner runner;

    public void updateAccount(Account account) {
        try {
            runner.update("update account set name=?,balance=? where id=?", account.getName(), account.getBalance(), account.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> findAllAccount() {
        try {
            return runner.query("select * from account", new BeanListHandler<Account>(Account.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account findAccountByName(String accountName) {
        try {
            List<Account> accounts = runner.query("select * from account where name = ?", new BeanListHandler<Account>(Account.class), accountName);

            if (accounts == null || accounts.size() == 0) {
                return null;
            }
            if (accounts.size() > 1) {
                throw new RuntimeException("结果集不唯一，数据存在问题");
            }
            return accounts.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
复制代码
```

### (4) 配置文件

#### A:bean.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
    <!--开启扫描-->
    <context:component-scan base-package="cn.ideal"></context:component-scan>

    <!--配置 QueryRunner-->
    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner">
        <!--注入数据源-->
        <constructor-arg name="ds" ref="dataSource"></constructor-arg>
    </bean>

    <!--配置数据源-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/ideal_spring"></property>
        <property name="user" value="root"></property>
        <property name="password" value="root99"></property>
    </bean>
</beans>
复制代码
```

#### B: jdbcConfig.properties

```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ideal_spring
jdbc.username=root
jdbc.password=root99
复制代码
```

### (5) 测试代码

#### A：AccountServiceTest

在这里，我们使用 Spring以及Junit 测试

> 说明：使用 @RunWith 注解替换原有运行器 然后使用 @ContextConfiguration 指定 spring 配置文件的位置，然后使用 @Autowired 给变量注入数据

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {

    @Autowired
    private AccountService as;

    @Test
    public void testFindAll() {
        List<Account> list = as.findAll();
        for (Account account : list) {
            System.out.println(account);
        }
    }

    @Test
    public void testTransfer() {
        as.transfer("李四", "张三", 500f);
    }

}
复制代码
```

### (6) 执行效果

先执行查询所有：

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b0ece14688?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

再执行模拟转账方法：

方法中也就是李四向张三转账500，看到下面的结果，是没有任何问题的

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b0b6e793d3?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

## (三) 初步分析以及解决

### (1) 分析事务问题

首先分析一下，我们并没有显式的进行事务的管理，但是不用否定，事务一定存在的，如果没有提交事务，很显然，查询功能是不能够测试成功的，我们的代码事务隐式的被自动控制了，使用了 connection 对象的 setAutoCommit(true)，即自动提交了

接着看一下配置文件中，我们只注入了了数据源，这样做代表什么呢？

```
<!--配置 QueryRunner-->
<bean id="runner" class="org.apache.commons.dbutils.QueryRunner">
	<!--注入数据源-->
	<constructor-arg name="ds" ref="dataSource"></constructor-arg>
</bean>
复制代码
```

也就是说，每一条语句独立事务：

说白了，就是各管各的，彼此没任何沟通，例如在Service的转账方法中，下面==标着 1 2 3 4 5 的位置处的语句，每一个调用时，都会创建一个新的 QueryRunner对象，并且从数据源中获取一个连接，相当于是这个不同的事物==，但是，当在==某一个步骤中突然出现问题，前面的语句仍然会执行，但是后面的语句就因为异常而终止了==，这也就是我们开头说的，彼此之间是独立的

```
public void transfer(String sourceName, String targetName, Float money) {
        //根据名称分别查询到转入转出的账户
        Account source = accountDao.findAccountByName(sourceName); // 1
        Account target = accountDao.findAccountByName(targetName); // 2

        //转入转出账户加减
        source.setBalance(source.getBalance() - money); // 3
        target.setBalance(target.getBalance() + money);
        //更新转入转出账户
        accountDao.updateAccount(source); // 4
        //模拟转账异常
        int num = 100/0; // 异常
        accountDao.updateAccount(target); //5
}
复制代码
```

很显然这是非常不合适的，甚至是致命的，像我们代码中所写，转出账户的账户信息已经扣款更新了，但是转入方的账户信息却由于前面异常的发生，导致并没有成功执行，李四从2500 变成了 2000，但是张三却没有成功收到转账，一致性就破坏了

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b0a865f1c3?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### (2) 初步解决事务问题

上面出现的问题，归根结底是由于我们持久层中的方法独立事务，所以无法实现整体的事务控制（与事务的一致性相悖）那么我们解决问题的思路是什么呢？

首先我们需要做的，就是使用 ThreadLocal 对象把 Connection 和当前线程绑定，从而使得一个线程中只有一个控制事务的对象，

简单提一下Threadlocal：

Threadlocal 是一个线程内部的存储类，可以在指定线程内存储数据，也就相当于，这些数据就被绑定在这个线程上了，只能通过这个指定的线程，才可以获取到想要的数据

这是官方的说明：

> This class provides thread-local variables. These variables differ from their normal counterparts in that each thread that accesses one (via its get or set method) has its own, independently initialized copLy of the variable. ThreadLocal instances are typically private static fields in classes that wish to associate state with a thread (e.g., a user ID or Transaction ID).

就是说，==ThreadLoacl 提供了线程内存储局部变量的方式，这些变量比较特殊的就是，每一个线程获取到的变量都是独立的，获取数据值的方法就是 get 以及 set==

#### ==A：ConnectionUtils 工具类==

创建 utils 包 ，然后创建一个 ConnectionUtils 工具类，其中最主要的部分，其实也就是写了一个简单的判断，==如果这个线程中已经存在连接，就直接返回，如果不存在连接，就获取数据源中的一个链接，然后存入，再返回==

```
@Component
public class ConnectionUtils {
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    @Autowired
    private DataSource dataSource;

    public Connection getThreadConnection() {

        try {
            // 从 ThreadLocal获取
            Connection connection = threadLocal.get();
            //先判断是否为空
            if (connection == null) {
                //从数据源中获取一个连接，且存入 ThreadLocal
                connection = dataSource.getConnection();
                threadLocal.set(connection);
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeConnection(){
        threadLocal.remove();
    }
}
复制代码
```

#### B：TransactionManager 工具类

接着==可以创建一个管理事务的工具类，其中包括，开启、提交、回滚事务，以及释放连接==

```
@Component
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    /**
     * 开启事务
     */
    public void beginTransaction() {
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            System.out.println("回滚事务" + connectionUtils.getThreadConnection());
            connectionUtils.getThreadConnection().rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放连接
     */
    public void release() {
        try {
            connectionUtils.getThreadConnection().close();//还回连接池中
            connectionUtils.removeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
复制代码
```

#### C：业务层增加事务代码

==在方法中添加事务管理的代码，正常情况下执行开启事务，执行操作（你的业务代码），提交事务，捕获到异常后执行回滚事务操作，最终执行释放连接==

在这种情况下，即使在某个步骤中出现了异常情况，也不会对数据造成实际的更改，这样上面的问题就初步解决了

```
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionManager transactionManager;

    public List<Account> findAll() {
        try {
            //开启事务
            transactionManager.beginTransaction();
            //执行操作
            List<Account> accounts = accountDao.findAllAccount();
            //提交事务
            transactionManager.commit();
            //返回结果
            return accounts;
        } catch (Exception e) {
            //回滚操作
            transactionManager.rollback();
            throw new RuntimeException(e);
        } finally {
            //释放连接
            transactionManager.release();
        }
    }

    public void transfer(String sourceName, String targetName, Float money) {

        try {
            //开启事务
            transactionManager.beginTransaction();
            //执行操作

            //根据名称分别查询到转入转出的账户
            Account source = accountDao.findAccountByName(sourceName);
            Account target = accountDao.findAccountByName(targetName);

            //转入转出账户加减
            source.setBalance(source.getBalance() - money);
            target.setBalance(target.getBalance() + money);

            //更新转出转入账户
            accountDao.updateAccount(source);
            //模拟转账异常
            int num = 100 / 0;
            accountDao.updateAccount(target);

            //提交事务
            transactionManager.commit();

        } catch (Exception e) {
            //回滚操作
            transactionManager.rollback();
            e.printStackTrace();
        } finally {
            //释放连接
            transactionManager.release();
        }
    }
}
复制代码
```

## (四) 思考再改进方式

虽然上面，我们已经实现了在业务层进行对事务的控制，但是很显然可以看见，==我们在每一个方法中都存在着太多重复的代码了，并且以业务层与事务管理的方法出现了耦合，打个比方，事务管理类中的随便一个方法名进行更改，就会直接导致业务层中找不到对应的方法，全部需要修改，如果在业务层方法较多时，很显然这是很麻烦的==

这种情况下，我们可以通过使用静态代理这一种方式，来进行对上面程序的改进，改进之前为了照顾到所有的朋友，回顾一下动态代理的一个介绍以及基本使用方式

## (五) 回顾动态代理

### (1) 什么是动态代理

==动态代理，也就是给某个对象提供一个代理对象，用来控制对这个对象的访问==

简单的举个例子就是：买火车、飞机票等，我们可以直接从车站售票窗口进行购买，这就是用户直接在官方购买，但是我们很多地方的店铺或者一些路边的亭台中都可以进行火车票的代售，用户直接可以在代售点购票，这些地方就是代理对象

### (2) 使用代理对象有什么好处呢？

- **功能提供的这个类**（火车站售票处），可以更加专注于主要功能的实现，比如安排车次以及生产火车票等等
- **代理类**（代售点）可以在功能提供类提供方法的基础上进行增加实现更多的一些功能

这个动态代理的优势，带给我们很多方便，它可以帮助我们==实现**无侵入式的代码扩展**，也就是在不**用修改源码**的基础上，同时**增强方法**==

动态代理分为两种：==① 基于接口的动态代理 ② 基于子类的动态代理==

### (3) 动态代理的两种方式

#### A：基于接口的动态代理方式

A：创建官方售票处（类和接口）

**RailwayTicketProducer 接口**

```
/**
 * 生产厂家的接口
 */
public interface RailwayTicketProducer {

    public void saleTicket(float price);

    public void ticketService(float price);

}
复制代码
```

**RailwayTicketProducerImpl 类**

实现类中，我们后面只对销售车票方法进行了增强，售后服务并没有涉及到

```
/**
 * 生产厂家具体实现
 */
public class RailwayTicketProducerImpl implements RailwayTicketProducer{

    public void saleTicket(float price) {
        System.out.println("销售火车票，收到车票钱：" + price);
    }

    public void ticketService(float price) {
        System.out.println("售后服务（改签），收到手续费：" + price);
    }
}
复制代码
```

**Client 类**

这个类，就是客户类，在其中，通过代理对象，实现购票的需求

首先先来说一下如何创建一个代理对象：==答案是 **Proxy类中的 newProxyInstance 方法**==

注意：既然叫做基于接口的动态代理，这就是==说被代理的类，也就是文中官方销售车票的类最少必须实现一个接口，这是必要的！==

```
public class Client {

    public static void main(String[] args) {
        RailwayTicketProducer producer = new RailwayTicketProducerImpl();

        //动态代理
        RailwayTicketProducer proxyProduce = (RailwayTicketProducer) Proxy.newProxyInstance(producer.getClass().getClassLoader(),
                producer.getClass().getInterfaces(),new MyInvocationHandler(producer));

        //客户通过代理买票
        proxyProduce.saleTicket(1000f);
    }
}
复制代码
```

**newProxyInstance** 共有**三个参数** 来解释一下：

- **ClassLoader：类加载器**
  - 用于加载代理对象字节码，和==被代理对象使用相同的类加载器==
- Class[]：字节码数组
  - 为了使被代理对象和的代理对象具有相同的方法，实现相同的接口，==可看做固定写法==
- InvocationHandler：==如何代理，也就是想要增强的方式==
  - 也就是说，我们只需要 new 出 ==InvocationHandler，然后书写其实现类，具体的实现方式==，比如说具体代理哪一个方法，在哪里进行代理，是否写成匿名内部类可以自己选择
  - 如上述代码中 new MyInvocationHandler(producer) 实例化的是我自己编写的一个 MyInvocationHandler类，实际上可以在那里直接 new 出 InvocationHandler，然后重写其方法，其本质也是通过实现 ==InvocationHandler 的 invoke 方法实现增强==

**MyInvocationHandler 类**

==这个 invoke 方法具有拦截的功能，被代理对象的任何方法被执行，都会经过 invoke==

```
public class MyInvocationHandler implements InvocationHandler {

    private  Object implObject ;

    public MyInvocationHandler (Object implObject){
        this.implObject=implObject;
    }

    /**
     * 作用：执行被代理对象的任何接口方法都会经过该方法
     * 方法参数的含义
     * @param proxy   代理对象的引用
     * @param method  当前执行的方法
     * @param args    当前执行方法所需的参数
     * @return        和被代理对象方法有相同的返回值
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = null;
        //获取方法执行的参数
        Float price = (Float)args[0];
        //判断是不是指定方法（以售票为例）
        if ("saleTicket".equals(method.getName())){
            returnValue = method.invoke(implObject,price*0.8f);
        }
        return returnValue;
    }
}
复制代码
```

在此处，我们获取到客户购票的金额，由于我们使用了代理方进行购票，所以代理方会收取一定的手续费，所以用户提交了 1000 元，实际上官方收到的只有800元，这也就是这种代理的实现方式，结果如下

> 销售火车票，收到车票钱：800.0

#### B：基于子类的动态代理方式

上面方法简单的实现起来也不是很难，但是唯一的标准就是，被代理对象必须提供一个接口，而现在所讲解的这一种就是一种可以直接代理普通 Java 类的方式，同时在演示的时候，我会将代理方法直接以内部类的形式写出，就不单独创建类了，方便大家与上面对照

**增加 cglib 依赖坐标**

```
<dependencies>
	<dependency>
		<groupId>cglib</groupId>
		<artifactId>cglib</artifactId>
        <version>3.2.4</version>
    </dependency>
</dependencies>
复制代码
```

**TicketProducer 类**

```
/**
 * 生产厂家
 */
public class TicketProducer {

    public void saleTicket(float price) {
        System.out.println("销售火车票，收到车票钱：" + price);
    }

    public void ticketService(float price) {
        System.out.println("售后服务（改签），收到手续费：" + price);
    }
}
复制代码
```

Enhancer 类中的 create 方法就是用来创建代理对象的

而 create 方法又有两个参数

- Class ：字节码
  - 指定被代理对象的字节码
- Callback：提供增强的方法
  - 与前面 invoke 作用是基本一致的
  - 一般写的都是该接口的子接口实现类：MethodInterceptor

```
public class Client {

    public static void main(String[] args) {
        // 由于下方匿名内部类，需要在此处用final修饰
        final TicketProducer ticketProducer = new TicketProducer();

        TicketProducer cglibProducer =(TicketProducer) Enhancer.create(ticketProducer.getClass(), new MethodInterceptor() {

            /**
             * 前三个三个参数和基于接口的动态代理中invoke方法的参数是一样的
             * @param o
             * @param method
             * @param objects
             * @param methodProxy   当前执行方法的代理对象
             * @return
             * @throws Throwable
             */
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object returnValue = null;
                //获取方法执行的参数
                Float price = (Float)objects[0];
                //判断是不是指定方法（以售票为例）
                if ("saleTicket".equals(method.getName())){
                    returnValue = method.invoke(ticketProducer,price*0.8f);
                }
                return returnValue;
            }
        });
        cglibProducer.saleTicket(900f);
    }
复制代码
```

## (六) 动态代理程序再改进

在这里我们写一个用于创建业务层对象的工厂

在这段代码中，我们使用了前面所回顾的基于接口的**动态代理方式**，在执行方法的前后，分别写入了开启事务，提交事务，回滚事务等事务管理方法，这时候，业务层就可以删除掉前面所写的关于业务的重复代码

```
@Component
public class BeanFactory {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionManager transactionManager;

    @Bean("proxyAccountService")
    public AccountService getAccountService() {
        return (AccountService) Proxy.newProxyInstance(accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        Object returnValue = null;
                        try {
                            //开启事务
                            transactionManager.beginTransaction();
                            //执行操作
                            returnValue = method.invoke(accountService, args);
                            //提交事务
                            transactionManager.commit();
                            //返回结果
                            return returnValue;
                        } catch (Exception e) {
                            //回滚事务
                            transactionManager.rollback();
                            throw new RuntimeException();
                        } finally {
                            //释放连接
                            transactionManager.release();
                        }
                    }
                });
    }
}
复制代码
```

**AccountServiceTest 类**

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean.xml")

public class AccountServiceTest {
    @Autowired
    @Qualifier("proxyAccountService")
    private AccountService as;

    @Test
    public void testFindAll() {
        List<Account> list = as.findAll();
        for (Account account : list) {
            System.out.println(account);
        }
    }

    @Test
    public void testTransfer() {
        as.transfer("李四", "张三", 500f);
    }

}
复制代码
```

到现在，一个相对完善的案例就改造完成了，由于我们上面大体使用的是注解的方式，并没有全部使用 XML 进行配置，如果使用 XML 进行配置，配置也是相对繁琐的，那么我们铺垫这么多的内容，实际上就是为了引出 Spring 中 AOP 的概念，从根源上，一步一步，根据问题引出要学习的技术

让我们一起来看一看！

# 初识 AOP（Spring 程序）

在前面，大篇幅的讲解我们在传统的程序中，是如何一步一步，改进以及处理例如事务这样的问题的，而 Spring 中 AOP 这个技术，就可以帮助我们来在不修源码的基础上对已经存在的方法进行增强，同样维护也是很方便，大大的提高了开发的效率，现在我们开始正式介绍 AOP 的知识，有了一定的知识铺垫后，就可以使用 AOP 的方式继续对前面的程序进行改进！

## (一) AOP 术语

任何一门技术，都会有其特定的术语，实际上就是一些特定的名称而已，事实上，我以前在学习的时候，感觉 AOP 的一些术语都是相对抽象的，并没有很直观的体现出它的意义，但是这些术语已经广泛的被开发者熟知，成为了在这个相关技术中，默认已知的一些概念，虽然更重要的是理解 AOP 的思想与使用方式，但是，我们还是需要讲这样一种 “共识” 介绍一下

《Spring 实战》中有这样一句话，摘出来：

> 在我们进入某个领域之前，必须学会在这个领域该如何说话

**通知（Advice）**

- 将安全，事务，或日志定义好，在某个方法前后执行一些通知、增强的处理
- 也就是说：==通知就是指，拦截到**连接点（Joinpoint）**后需要做的事情==
- 通知分为五种类型：
  - ==前置通知（Before）：在目标方法被执行前调用==
  - ==后置通知（After）：在目标方法完成后使用，输出的结果与它无关==
  - ==返回通知（After-returning）：在目标方法成功执行之后调用==
  - ==异常通知（After-throwing）：在目标方法抛出异常后调用==
  - ==环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为（在注解中体现明显，后面可以注意下）==

**连接点（Joinpoint）**

- 是在应用执行过程中能够插入切面的一个点。这个点可以是调用方法时、抛出异常时、甚至修改一个字段时。切面代码可以利用这些点插入到应用的正常流程之中，并添加新的行为
- 例如我们前面对 Service 中的方法增加了事务的管理，事务层中的方法都会被动态代理所拦截到，这些方法就可以看做是这个连接点，在这些方法的前后，我们就可以增加一些通知
- 一句话：==方法的前后都可以看做是连接点==

**切入点（Pointcut）**

- 有的时候，类中方法有很多，但是我们并不想将所有的方法前后都增加通知，==我们只想对指定的方法进行通过，这就是切入点的概念==
- 一句话：==切入点就是对连接点进行筛选，选出最终要用的==

**切面（Aspect）**

- 切入点，==告诉程序要在哪个位置进行增强或处理，通知告诉程序在这个点要做什么事情，以及什么时候去做，所以 切入点 + 通知 ≈ 切面==
- 切面事实上，就是将我们在业务模块中重复的部分切分放大，大家可以对比前面我们直接在业务层中的每个方法上进行添加重复的事务代码，理解一下
- 一句话：切面就是**切入点**和**通知**的结合

**引入（Introduction）**

- 它是一种特殊的通知，在不修改源代码的前提下，可以在运行期为类动态的添加一些方法或者属性

**织入（Weaving）**

- 把切面（增强）应用到目标对象并且创建新的代理对象的过程
- 实际上就是类似前面，在通过动态代理对某个方法进行增强，且添加事务方法的过程

## (二) AOP 入门案例

首先，通过一个非常简单的案例，来演示一下，如何在某几个方法执行前，均执行一个日志的打印方法，简单模拟为输出一句话，前面的步骤我们都很熟悉，需要注意的就是 bean.xml 中配置的方法，我会代码下面进行详的讲解

### (1) 基于 XML 的方式

#### A：依赖坐标

==aspectjweaver==，这个依赖用来支持切入点表达式等，后面配置中会提到这个知识

```
<packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
    </dependencies>
复制代码
```

#### B：业务层

**AccountService 接口**

```
public interface AccountService {
    /**
     * 保存账户
     */
    void addAccount();
    /**
     * 删除账户
     * @return
     */
    int  deleteAccount();
    /**
     * 更新账户
     * @param i
     */
    void updateAccount(int i);
}
复制代码
```

**AccountServiceImpl 实现类**

```
public class AccountServiceImpl implements AccountService {
    public void addAccount() {
        System.out.println("这是增加方法");
    }

    public int deleteAccount() {
        System.out.println("这是删除方法");
        return 0;
    }

    public void updateAccount(int i) {
        System.out.println("这是更新方法");
    }
}
复制代码
```

#### C：日志类

```
public class Logger {
    /**
     * 用于打印日志：计划让其在切入点方法执行之前执行（切入点方法就是业务层方法）
     */
    public void printLog(){
        System.out.println("Logger类中的printLog方法执行了");
    }
}
复制代码
```

#### D：配置文件

**bean.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
    
    <!--配置Spring的IOC，配置service进来-->
    <bean id="accountService" class="cn.ideal.service.impl.AccountServiceImpl"></bean>

    <!--配置 Logger 进来-->
    <bean id="logger" class="cn.ideal.utils.Logger"></bean>

    <!--配置 AOP-->
    <aop:config>
        <!--配置切面-->
        <aop:aspect id="logAdvice" ref="logger">
            <!--通知的类型，以及建立通知方法和切入点方法的关联-->
            <aop:before method="printLog" pointcut="execution(* cn.ideal.service.impl.*.*(..))"></aop:before>
        </aop:aspect>
    </aop:config>
</beans>
复制代码
```



### (2) XML配置分析

#### A：基本配置

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">
</beans>
复制代码
```

首先需要引入的就是这个XML的头部文件，一些约束，可以直接复制这里的，也可以像以前一样，去官网找对应的约束等

==接着，将 Service 和 Logger 通过 bean 标签配置进来==

#### B：AOP基本配置

**==aop:config**：表明开始 aop 配置，配置的代码全部写在这个标签内==

==**aop:aspect**：表明开始配置切面==

- ==id属性：给切面提供一个唯一的标识==
- ==ref属性：用来引用已经配置好的通知类 bean，填入通知类的id即可，用来作为增强的代码在这个类当中==

aop:aspect 标签内部，通过对应的标签，配置通知的类型

```
<aop:config>
	<!--配置切面-->
	<aop:aspect id="logAdvice" ref="logger">
    	<!--通知的类型，以及建立通知方法和切入点方法的关联-->
	</aop:aspect>
</aop:config>
复制代码
```

#### C：AOP四种常见通知配置

题目中我们是以在方法执行前执行通知，所以是使用了前置通知

**==aop:before**：用于配置前置通知，指定增强的方法在切入点方法之前执行==

==**aop:after-returning**：用于配置后置通知，与异常通知只能执行其中一个==

==**aop:after-throwing**：用于配置异常通知，异常通知只能执行其中一个==

==**aop:after**：用于配置最终通知，无论切入点方法执行时是否有异常，它都会在其后面执行==

**参数：**

- method：==用于指定通知类中的增强方法名称，也就是我们上面的 Logger类中的 printLog 方法==
- poinitcut：==用于指定切入点表达式（文中使用的是这个）指的是对业务层中哪些方法进行增强==
- ponitcut-ref：用于指定切入点的表达式的引用（调用次数过多时，更多的使用这个，减少了重复的代码）

**切入点表达式的写法：**

- 首先，在poinitcut属性的引号内 加入execution() 关键字，括号内书写表达式
- 基本格式：==访问修饰符 返回值 包名.包名.包名...类名.方法名(方法参数)==
  - 说明：包名有几个是根据自己的类所有在的包结构决定
  - 全匹配写法
    - `public void cn.ideal.service.impl.AccountServiceImpl.addAccount()`
  - 访问修饰符，如 public 可以省略，返回值可以使用通配符，表示任意返回值
    - `void cn.ideal.service.impl.AccountServiceImpl.addAccount()`
  - 包名可以使用通配符，表示任意包，有几级包，就需要写几个*.
    - `* *.*.*.*.AccountServiceImpl.addAccount()`
  - 包名可以使用..表示当前包及其子包
    - `cn..*.addAccount()`
  - 类名和方法名都可以使用*来实现通配，下面表示全通配
    - `* *..*.*(..)`
- 方法参数
  - 可以直接写数据类型：例如 int
  - 引用类型写包名.类名的方式 java.lang.String
  - 可以使用通配符表示任意类型，但是必须有参数
  - 可以使用..表示有无参数均可，有参数可以是任意类型

在实际使用中，更加推荐的写法也就是上面代码中的那种，将包结构给出（一般都是对业务层增强），其他的使用通配符

```
pointcut="execution(* cn.ideal.service.impl.*.*(..))"
```

在给出4中通知类型后，就需要多次书写这个切入表达式，所以我们可以使用 pointcut-ref 参数解决重复代码的问题，其实就相当于抽象出来了，方便以后调用

ponitcut-ref：用于指定切入点的表达式的引用（调用次数过多时，更多的使用这个，减少了重复的代码）

位置放在 config里，aspect 外就可以了

```
<aop:pointcut id="pt1" 
expression="execution(* cn.ideal.service.impl.*.*(..))"></aop:pointcut>
复制代码
```

调用时：

```
<aop:before method="PrintLog" pointcut-ref="pt1"></aop:before>
复制代码
```

#### D：环绕通知

接着，spring框架为我们提供的一种可以手动在代码中控制增强代码什么时候执行的方式，也就是环绕通知

配置中需要这样一句话，pt1和前面是一样的

```
<aop:around method="aroundPrintLog" pointcut-ref="pt1"></aop:around>
复制代码
```

Logger类中这样配置

```
public Object aroundPrintLog(ProceedingJoinPoint proceedingJoinPoint) {
    Object returValue = null;
    try {
        Object[] args = proceedingJoinPoint.getArgs();
        System.out.println("这是Logger类中的aroundPrintLog前置方法");

        returValue = proceedingJoinPoint.proceed(args);

        System.out.println("这是Logger类中的aroundPrintLog后置方法");

        return returValue;
    } catch (Throwable throwable) {
        System.out.println("这是Logger类中的aroundPrintLog异常方法");
        throw new RuntimeException();
    } finally {
        System.out.println("这是Logger类中的aroundPrintLog最终方法");
    }
}
复制代码
```

来解释一下：

Spring 中提供了一个接口：ProceedingJoinPoint，其中有一个方法叫做 proceed(args)，这个方法就相当于明确调用切入点方法，proceed() 方法就好像以前动态代理中的 invoke，同时这个接口可以作为环绕通知的方法参数，这样看起来，和前面的动态代理的那种感觉还是很相似的

### (3) 基于注解的方式

依赖，以及业务层方法，我们都是用和 XML 一致的吗，不过为了演示方便，这里就只留下 一个 add 方法

#### A：配置文件

配置文件中一个是需要引入新的约束，再有就是**开启扫描**以及**开启注解 AOP 的支持**

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 配置spring创建容器时要扫描的包-->
    <context:component-scan base-package="cn.ideal"></context:component-scan>

    <!-- 配置spring开启注解AOP的支持 -->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

</beans>
复制代码
```

#### B：添加注解

首先是业务层中把 Service 注进来

```
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    public void addAccount() {
        System.out.println("这是增加方法");
    }
}
复制代码
```

接着就是最终要的位置Logger类中，首先将这个类通过 @Component("logger") 整体注入

然后使用 @Aspect 表明这是一个切面类

下面我分别使用了四种通知类型，以及环绕通知类型，在注解中这里是需要注意的

第一次我首先测试的是四种通知类型：将环绕通知先注释掉，把前面四个放开注释

```
@Component("logger")
@Aspect//表示当前类是一个切面类
public class Logger {

    @Pointcut("execution(* cn.ideal.service.impl.*.*(..))")
    private void pt1(){}


//    @Before("pt1()")
    public void printLog1(){
        System.out.println("Logger类中的printLog方法执行了-前置");
    }

//    @AfterReturning("pt1()")
    public void printLog2(){
        System.out.println("Logger类中的printLog方法执行了-后置");
    }

//    @AfterThrowing("pt1()")
    public void printLog3(){
        System.out.println("Logger类中的printLog方法执行了-异常");
    }

//    @After("pt1()")
    public void printLog4(){
        System.out.println("Logger类中的printLog方法执行了-最终");
    }


    @Around("pt1()")
    public Object aroundPrintLog(ProceedingJoinPoint proceedingJoinPoint) {
        Object returValue = null;
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            System.out.println("这是Logger类中的aroundPrintLog前置方法");

            returValue = proceedingJoinPoint.proceed(args);

            System.out.println("这是Logger类中的aroundPrintLog后置方法");

            return returValue;
        } catch (Throwable throwable) {
            System.out.println("这是Logger类中的aroundPrintLog异常方法");
            throw new RuntimeException();
        } finally {
            System.out.println("这是Logger类中的aroundPrintLog最终方法");
        }
    }

}

复制代码
```

四种通知类型测试结果：

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b1380ec545?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

可以看到，一个特别诡异的事情出现了，后置通知和最终通知的位置出现了问题，同样异常情况下也会出现这样的问题，确实这是这里的一个问题，所以我们注解中一般使用 环绕通知的方式

环绕通知测试结果：

![img](https://user-gold-cdn.xitu.io/2020/3/6/170af0b150562788?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

### (4) 纯注解方式

纯注解还是比较简单的 加好 @EnableAspectJAutoProxy 就可以了

```
@Configuration
@ComponentScan(basePackages="cn.ideal")
@EnableAspectJAutoProxy//主要是这个注解
public class SpringConfiguration {
}
复制代码
```

到这里，两种XML以及注解两种方式的基本使用就都说完了，下面我们会讲一讲如何完全基于 Spring 实现事务的控制

## (三) 完全基于 Spring 的事务控制

上面Spring中 AOP 知识的入门，但是实际上，Spring 作为一个强大的框架，为我们业务层中事务处理，已经进行了考虑，它为我们提供了==一组关于事务控制的接口，基于 AOP 的基础之上，就可以高效的完成事务的控制==，下面我们就通过一个案例，来对这部分内容进行介绍，这一部分，我们选用的的例如 持久层 单元测试等中的内容均使用 Spring，特别注意：持久层我们使用的是 Spring 的 JdbcTemplate ，不熟悉的朋友可以去简单了解一下，在这个案例中，重点还是学习事务的控制，这里不会造成太大的影响的

### (1) 准备代码

注：准备完代码第一个要演示的是基于 XML 的形式，所以我们准备的时候都没有使用注解，后面介绍注解方式的时候，会进行修改

#### A：导入依赖坐标

```
<packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
复制代码
```

#### B：创建账户表以及实体

**创建 Account 表**

```
-- ----------------------------
-- Table structure for account
-- ----------------------------
CREATE TABLE `account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32),
  `balance` float,
  PRIMARY KEY (`id`)
)
复制代码
```

**创建 Account 类**

没什么好说的，对应着我们的表创出实体

```
public class Account implements Serializable {
    private  Integer id;
    private String name;
    private Float balance;
    ......补充 get set toString 方法
复制代码
```

#### C：创建 Service 和 Dao

为了减少篇幅，就给了实现类，接口就不贴了，这很简单

业务层

```
package cn.ideal.service.impl;

import cn.ideal.dao.AccountDao;
import cn.ideal.domain.Account;
import cn.ideal.service.AccountService;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account findAccountById(Integer accountId) {
        return accountDao.findAccountById(accountId);

    }

    public void transfer(String sourceName, String targetName, Float money) {
        System.out.println("转账方法执行");
        //根据名称分别查询到转入转出的账户
        Account source = accountDao.findAccountByName(sourceName);
        Account target = accountDao.findAccountByName(targetName);

        //转入转出账户加减
        source.setBalance(source.getBalance() - money);
        target.setBalance(target.getBalance() + money);
        //更新转入转出账户
        accountDao.updateAccount(source);

        int num = 100/0;

        accountDao.updateAccount(target);
    }
}
复制代码
```

持久层

```
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

    public Account findAccountById(Integer accountId) {
        List<Account> accounts = super.getJdbcTemplate().query("select * from account where id = ?",new BeanPropertyRowMapper<Account>(Account.class),accountId);
        return accounts.isEmpty()?null:accounts.get(0);
    }


    public Account findAccountByName(String accountName) {
        List<Account> accounts = super.getJdbcTemplate().query("select * from account where name = ?",new BeanPropertyRowMapper<Account>(Account.class),accountName);
        if(accounts.isEmpty()){
            return null;
        }
        if(accounts.size()>1){
            throw new RuntimeException("结果集不唯一");
        }
        return accounts.get(0);
    }


    public void updateAccount(Account account) {
        super.getJdbcTemplate().update("update account set name=?,balance=? where id=?",account.getName(),account.getBalance(),account.getId());
    }
}
复制代码
```

#### D：创建 bean.xml 配置文件

提一句：如果没有用过 JdbcTemplate，可能会好奇下面的 DriverManagerDataSource 是什么，这个是 Spring 内置的数据源

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 配置业务层-->
    <bean id="accountService" class="cn.ideal.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!-- 配置账户的持久层-->
    <bean id="accountDao" class="cn.ideal.dao.impl.AccountDaoImpl">
        <property name="dataSource" ref="dataSource"></property>
    </bean>


    <!-- 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/ideal_spring"></property>
        <property name="username" value="root"></property>
        <property name="password" value="root99"></property>
    </bean>
</beans>
复制代码
```

#### E：测试

```
/**
 * 使用Junit单元测试：测试我们的配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {

    @Autowired
    private AccountService as;

    @Test
    public void testTransfer() {
        as.transfer("张三", "李四", 500f);
    }
复制代码
```

### (2) 基于 XML 的方式

首先要做的就是修改配置文件，这里需要引入的就是 aop 和 tx 这两个名称空间

配置 业务层 持久层 以及数据源 没什么好说的，直接复制过来，下面就是我们真正的重要配置

#### ==A：配置事务管理器==

==真正管理事务的对象 Spring 已经提供给我们了==

使用Spring JDBC或iBatis 进行持久化数据时可以使用 

org.springframework.jdbc.datasource.DataSourceTransactionManager

使用 Hibernate 进行持久化数据时可以使用org.springframework.orm.hibernate5.HibernateTransactionManager

在其中将数据源引入

```
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	<property name="dataSource" ref="dataSource"></property>
</bean>
复制代码
```

#### ==B：配置事务通知==

进行事务通知以及属性配置时就需要引入事务的约束，tx 以及 aop 的名称空间和约束

在这里，就可以将事务管理器引入

```
<!-- 配置事务的通知-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">

</tx:advice>
复制代码
```

#### C：配置事务属性

在 `<tx:advice></tx:advice>` 中就可以配置事务的属性了，这里有一些属性需要熟悉一下，关于事务的隔离级别可以暂时看一看就可以了，只针对这个例程的话，我们并没有太多的涉及，事务是一个大问题，需要深入的了解，我们在这里更重点讲的是如何配置使用它

- **name**：指定你需要增加某种事务的方法名，可以使用通配符，例如 * 代表所有 find* 代表名称开头为 find 的方法，第二种优先级要更高一些
- **isolation**：用于指定事务的隔离级别，表示使用数据库的默认隔离级别，默认值是DEFAULT
  - 未提交读取（Read Uncommitted）
    - Spring标识：ISOLATION_READ_UNCOMMITTED
    - 代表允许脏读取，但不允许更新丢失。也就是说，如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据
  - 已提交读取（Read Committed）
    - Spring标识：ISOLATION_READ_COMMITTED
    - 只能读取已经提交的数据，解决了脏读的问题。读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行
  - 可重复读取（Repeatable Read）
    - Spring标识：ISOLATION_REPEATABLE_READ
    - 是否读取其他事务提交修改后的数据，解决了不可重复读以及脏读问题，但是有时可能出现幻读数据。读取数据的事务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务
  - 序列化（Serializable）
    - Spring标识：ISOLATION_SERIALIZABLE。
    - 提供严格的事务隔离。它要求事务序列化执行，解决幻影读问题，事务只能一个接着一个地执行，不能并发执行。
- **propagation**：用于指定事务的传播属性，默认值是 REQUIRED，代表一定会有事务，一般被用于增删改，查询方法可以选择使用 SUPPORTS
- **read-only**：用于指定事务是否只读。默认值是false示读写，一般查询方法才设置为true
- **timeout**：用于指定事务的超时时间，默认值是-1，表示永不超时，如果指定了数值，以秒为单位，一般不会用这个属性
- **rollback-for**：用于指定一个异常，当产生该异常时，事务回滚，产生其他异常时，事务不回滚。没有默认值。表示任何异常都回滚
- **no-rollback-for**：用于指定一个异常，当产生该异常时，事务不回滚，产生其他异常时事务回滚。没有默认值。表示任何异常都回滚

```
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <!-- 配置事务的属性 -->
    <tx:attributes>
        <tx:method name="*" propagation="REQUIRED" read-only="false"/>
        <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
    </tx:attributes>
</tx:advice>
复制代码
```

#### D：配置 AOP 切入点表达式

```
<!-- 配置aop-->
<aop:config>
     !-- 配置切入点表达式-->
    <aop:pointcut id="pt1" expression="execution(* cn.ideal.service.impl.*.*(..))"></aop:pointcut>
</aop:config>
复制代码
```

#### E：建立切入点表达式和事务通知的对应关系

在 `<aop:config></aop:config>` 中进行此步骤

```
<!-- 配置aop-->
<aop:config>
     !-- 配置切入点表达式-->
    <aop:pointcut id="pt1" expression="execution(* cn.ideal.service.impl.*.*(..))"></aop:pointcut>
    <!--建立切入点表达式和事务通知的对应关系 -->
    <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
</aop:config>
复制代码
```

#### F：全部配置代码

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 配置业务层-->
    <bean id="accountService" class="cn.ideal.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>

    <!-- 配置账户的持久层-->
    <bean id="accountDao" class="cn.ideal.dao.impl.AccountDaoImpl">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/ideal_spring"></property>
        <property name="username" value="root"></property>
        <property name="password" value="root99"></property>
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    
	<!-- 配置事务的通知-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <!-- 配置事务的属性 -->
        <tx:attributes>
            <tx:method name="*" propagation="REQUIRED" read-only="false"/>
            <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
        </tx:attributes>
    </tx:advice>

    <!-- 配置aop-->
    <aop:config>
        <!-- 配置切入点表达式-->
        <aop:pointcut id="pt1" expression="execution(* cn.ideal.service.impl.*.*(..))"></aop:pointcut>
        <!--建立切入点表达式和事务通知的对应关系 -->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"></aop:advisor>
    </aop:config>
    
</beans>
复制代码
```

### (3) 基于注解的方式

还是基本的代码，但是需要对持久层进行一个小小的修改，前面为了配置中简单一些，我们直接使用了继承 JdbcDaoSupport 的方式，但是它只能用于 XML 的方式， 注解是不可以这样用的，所以，我们还是需要用传统的一种方式，也就是在 Dao 中定义 JdcbTemplate

#### A：修改 bean.xml 配置文件

注解的常规操作，开启注解，我们这里把数据源和JdbcTemplate也配置好

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 配置spring创建容器时要扫描的包-->
    <context:component-scan base-package="cn.ideal"></context:component-scan>

    <!-- 配置JdbcTemplate-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://localhost:3306/ideal_spring"></property>
        <property name="username" value="root"></property>
        <property name="password" value="root99"></property>
    </bean>

</beans>
复制代码
```

#### B：业务层和持久层添加基本注解

```
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;
    
    //下面是一样的
}
复制代码
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    //下面基本是一样的
    //只需要将原来的 super.getJdbcTemplate().xxx 改为直接用  jdbcTemplate 执行
}
复制代码
```

#### C：在bean.xml中配置事务管理器

```
<!-- 配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"></property>
</bean>
复制代码
```

#### D：在bean.xml中开启对注解事务的支持

```
<!-- 开启spring对注解事务的支持-->
<tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
复制代码
```

#### E：==业务层添加 @Transactional 注解==

这个注解可以出现在接口上，类上和方法上

- ==出现接口上，表示该接口的所有实现类都有事务支持==
- ==出现在类上，表示类中所有方法有事务支持==
- ==出现在方法上，表示方法有事务支持==

例如下例中，我们类中指定了事务的为只读型，但是下面的转账还涉及到了写操作，所以又在方法上增加了一个 readOnly 值为 false 的注解

```
@Service("accountService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class AccountServiceImpl implements AccountService {
	.... 省略
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
    public void transfer(String sourceName, String targetName, Float money) {
    	...... 省略
    }
}
复制代码
```

#### F：测试代码

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bean.xml")
public class AccountServiceTest {

    @Autowired
    private AccountService as;

    @Test
    public void testTransfer() {
        as.transfer("张三", "李四", 500f);
    }
}

复制代码
```

### (4) 基于纯注解方式

下面使用的就是纯注解的方式，bean.xml 就可以删除掉了，这种方式不是很难

#### A： 配置类注解

##### @Configuration

- 指定当前类是 spring 的一个配置类，相当于 XML中的 bean.xml 文件

获取容器时需要使用下列形式

```
private ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
复制代码
```

如果使用了 spring 的单元测试

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= SpringConfiguration.class)
public class AccountServiceTest {
	......
}
复制代码
```

#### B： 指定扫描包注解

##### @ComponentScan

@Configuration 相当于已经帮我们把 bean.xml 文件创立好了，按照我们往常的步骤，应该指定扫描的包了，这也就是我们这个注解的作用

- 指定 spring 在初始化容器时要扫描的包，在 XML 中相当于：

- ```
  <!--开启扫描-->
  <context:component-scan base-package="cn.ideal"></context:component-scan>
  复制代码
  ```

- 其中 basePackages 用于指定扫描的包，和这个注解中value属性的作用是一致的

#### C： 配置 properties 文件

##### @PropertySource

以前在创建数据源的时候，都是直接把配置信息写死了，如果想要使用 properties 进行内容的配置，在这时候就需要，使用 @PropertySource 这个注解

- 用于加载 .properties 文件中的配置
- value [] 指定 properties 文件位置，在类路径下，就需要加上 classpath

**SpringConfiguration 类（相当于 bean.xml）**

```
/**
 * Spring 配置类
 */
@Configuration
@ComponentScan("cn.ideal")
@Import({JdbcConfig.class,TransactionConfig.class})
@PropertySource("jdbcConfig.properties")
@EnableTransactionManagement
public class SpringConfiguration {

}
复制代码
```

#### D： 创建对象

##### @Bean

写好了配置类，以及指定了扫描的包，下面该做的就是配置 jdbcTemplate 以及数据源，再有就是创建事务管理器对象，在 XML 中我们会通过书写 bean 标签来配置，而 Spring 为我们提供了 @Bean 这个注解来替代原来的标签

- 将注解写在方法上（只能是方法），也就是代表用这个方法创建一个对象，然后放到 Spring 的容器中去
- 通过 name 属性 给这个方法指定名称，也就是我们 XML 中 bean 的 id
- 这种方式就将配置文件中的数据读取进来了

**JdbcConfig （JDBC配置类）**

```
/**
 * 和连接数据库相关的配置类
 */
public class JdbcConfig {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    /**
     * 创建JdbcTemplate
     * @param dataSource
     * @return
     */
    @Bean(name="jdbcTemplate")
    public JdbcTemplate createJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    /**
     * 创建数据源对象
     * @return
     */
    @Bean(name="dataSource")
    public DataSource createDataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }
}
复制代码
```

**jdbcConfig.properties**

将配置文件单独配置出来

```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ideal_spring
jdbc.username=root
jdbc.password=root99
复制代码
```

**TransactionConfig**

```
/**
 * 和事务相关的配置类
 */
public class TransactionConfig {
    /**
     * 用于创建事务管理器对象
     * @param dataSource
     * @return
     */
    @Bean(name="transactionManager")
    public PlatformTransactionManager createTransactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
复制代码
```

# 总结：

① 这篇文章就写到这里了，学习任何一门技术，只有知其然，才能明白其所有然，很多人在某个技术领域已经沉浸多年，自然有了特殊的思考与理解，凭借着强大的经验，自然也能快速上手，但如果处于门外状态，或者对这一方面接触的不多，就更需要了解一门技术的前因后果，不过什么源码分析，各种设计模式，这也都是后话，我们的第一要义就是要用它做事，要让他跑起来，自认为我不是什么过于聪明的人，直接去学习一堆配置，一堆注解，一堆专有名词，太空洞了，很难理解。

② 我们往往都陷入了一种，**为学而学的状态**，可能大家都会SSM我也学，大家都说 SpringBoot 简单舒服，我也去学，当然很多时候因为一些工作或者学习的需要，没有办法，但是仍觉得，私下再次看一门技术的时候，可以借助一些文章或者资料，亦或者找点视频资源，去看看这一门究竟带来了什么，其过人之处，必然是解决了我们以前遇到的，或者没考虑到的问题，这样一种循序渐进的学习方式，可以帮助我们对一些技术有一个整体的概念，以及了解其之间的联系。

③ 这一篇文章，我参考了 《Spring 实战》、某马的视频、以及百度谷歌上的一些参考内容，从一个非常简单的 增删改查的案例出发，通过分析其事务问题，一步一步从动态代理，到 AOP进行了多次的改进，其中涉及到一些例如 动态代理或者JdcbTemplate的知识，或许有的朋友不熟悉，我也用了一些篇幅说明，写这样一篇长文章，确实很费功夫，如果想要了解 Spring AOP 相关知识的朋友，可以看一看，也可以当做一个简单的参考，用来手生的时候作为工具书参考

非常希望能给大家带来帮助，再次感谢大家的支持，谢谢！

Tips：同时有需要的朋友可以去看我的前一篇文章