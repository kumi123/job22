Spring Boot 主要优势之一，就是“开箱即用，远离繁琐的配置”。

Spring Boot 架构没有代码生成，也不需要XML配置，有效避免大量的 Maven 导入和各种版本冲突，为 Spring 开发提供一个更快、更广泛的入门体验。

不得不说，掌握 Spring Boot 常用注解，是学习Spring Boot 架构的关键。

一、核心注解
@SpringBootApplication
通常用在启动类上，申明让spring boot自动给程序进行必要的配置，它也是 Spring Boot 的核心注解，主要组合包含了以下 3 个注解：

1. @SpringBootConfiguration

组合了 @Configuration 注解，实现配置文件的功能。

2. @EnableAutoConfiguration

打开自动配置的功能，也可以关闭某个自动配置的选项。

如关闭数据源自动配置功能： @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })；

3. @ComponentScan

Spring组件扫描功能，让spring Boot扫描到Configuration类并把它加入到程序上下文。

二、常用注解
1. 配置导入功能
1.1 @Configuration

等同于spring的XML配置文件，使用Java代码可以检查类型安全。指出该类是 Bean 配置的信息源，相当于XML中的<beans></beans>，一般加在主类上。

1.2 @Bean

相当于XML中的<bean></bean>，放在方法的上面，而不是类，意思是产生一个bean,并交给spring管理；

1.3 @Import

用来导入其他配置类；

1.4 @ImportResource

用来加载xml配置文件；

1.5 @Autowired

自动导入依赖的bean，自动导入依赖的bean。byType方式。把配置好的Bean拿来用，完成属性、方法的组装，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。当加上（required=false）时，就算找不到bean也不报错；

1.6 @Resource(name="name",type="type")

没有括号内内容的话，默认byName，与@Autowired干类似的事；

1.7 @Inject

等价于默认的@Autowired，只是没有required属性；

2. 业务层功能
2.1 @Component

泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注；

2.2 @Controller

用于定义控制器类，在spring项目中由控制器负责将用户发来的URL请求转发到对应的服务接口（service层），一般这个注解在类中，通常方法需要配合注解@RequestMapping；

2.3 @RestController

用于标注控制层组件(如struts中的action)，是@ResponseBody和@Controller的合集；

2.4 @Service

一般用于修饰service层的组件；

2.5 @Repository

用于标注数据访问组件，即DAO组件；

2.6 @RequestMapping

提供路由信息，负责URL到Controller中的具体函数的映射；

该注解包含以下6个属性：（常用value）

params：指定request中必须包含某些参数值是，才让该方法处理；
headers：指定request中必须包含某些指定的header值，才能让该方法处理请求；
value：指定请求的实际地址，指定的地址可以是URI Template 模式；
method：指定请求的method类型， GET、POST、PUT、DELETE等；
consumes：指定处理请求的提交内容类型（Content-Type），如application/json,text/html；
produces：指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回。
2.7 @ResponseBody

表示该方法的返回结果直接写入HTTP response body中，一般在异步获取数据时使用，用于构建RESTful的api。在使用@RequestMapping后，返回值通常解析为跳转路径，加上@esponsebody后返回结果不会被解析为跳转路径，而是直接写入HTTP response body中。比如异步获取json数据，加上@Responsebody后，会直接返回json数据。该注解一般会配合@RequestMapping一起使用；

2.8 @Value

注入 application.properties 或 application.yml 配置的属性的值；

2.9 @PathVariable

路径变量，参数与大括号里的名字一样要相同；

2.10 @Profiles

Spring Profiles 提供了一种隔离应用程序配置的方式，并让这些配置只能在特定的环境下生效。任何@Component或@Configuration都能被@Profile标记，从而限制加载它的时机；

2.11 @ConfigurationProperties

Spring Boot将尝试校验外部的配置，默认使用JSR-303（如果在classpath路径中）。你可以轻松的为你的@ConfigurationProperties 类添加JSR-303 javax.validation约束注解；

3. 全局异常处理
3.1 @ControllerAdvice

包含@Component，可以被扫描到，统一处理异常；

3.2 @ExceptionHandler（Exception.class）

用在方法上面，表示遇到这个异常就执行以下方法；