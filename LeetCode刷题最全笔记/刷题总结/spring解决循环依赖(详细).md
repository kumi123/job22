## 写在前面

> 最近，有关于Spring的面试中，Spring的循环依赖问题是必问的知识点，你回答的不好，就会被直接pass掉。即使你没被pass掉，你也会比那些知道如何解决Spring循环依赖问题的同事薪资低，这就是现实。所以，我们今天就一起来探讨下如何解决Spring循环依赖的问题。



## 什么是循环依赖？

循环依赖其实就是循环引用，也就是==两个或者两个以上的bean互相持有对方，最终形成闭环。比如A依赖于B，B依赖于C，C又依赖于A。==如下图：

![img](https://oscimg.oschina.net/oscnet/924c8055-69cc-4ee5-a8d2-3d6a991f96fd.jpg)在这里插入图片描述

注意，这里不是函数的循环调用，是对象的相互依赖关系。循环调用其实就是一个死循环，除非有终结条件。

==Spring中循环依赖场景有：==

==（1）构造器的循环依赖==

==（2）field属性的循环依赖==

==其中，构造器的循环依赖问题无法解决，只能拋出BeanCurrentlyInCreationException异常，在解决属性循环依赖时，spring采用的是提前暴露对象的方法。==



## 怎么检测是否存在循环依赖？

检测循环依赖相对比较容易，Bean在创建的时候可以给该Bean打标，如果递归调用回来发现正在创建中的话，即说明了循环依赖了。



## 三种循环依赖



### 1.构造器的循环依赖。【这个Spring解决不了】

==Spring容器会将每一个正在创建的Bean  标识符放在一个“当前创建Bean池”中，Bean标识符在创建过程中将一直保持在这个池中，因此如果在创建Bean过程中发现自己已经在“当前创建Bean池”里时将抛出BeanCurrentlyInCreationException异常表示循环依赖；而对于创建完毕的Bean将从“当前创建Bean池”中清除掉。==

==Spring容器先创建单例A，A依赖B，然后将A放在“当前创建Bean池”中，此时创建B,B依赖C ,然后将B放在“当前创建Bean池”中,此时创建C，C又依赖A， 但是，此时A已经在池中，所以会报错，因为在池中的Bean都是未初始化完的，所以会依赖错误 ，（初始化完的Bean会从池中移除），也就是说不能依赖没有初始化好的类==

```java
public class StudentA {
    private StudentB studentB ;
 
    public void setStudentB(StudentB studentB) {
        this.studentB = studentB;
    }

    public StudentA() {
    }
    
    public StudentA(StudentB studentB) {
        this.studentB = studentB;
    }
}
public class StudentB {
 
    private StudentC studentC ;
 
    public void setStudentC(StudentC studentC) {
        this.studentC = studentC;
    }
    
    public StudentB() {
    }
 
    public StudentB(StudentC studentC) {
        this.studentC = studentC;
    }
}
public class StudentC {
 
    private StudentA studentA ;
 
    public void setStudentA(StudentA studentA) {
        this.studentA = studentA;
    }
 
    public StudentC() {
    }
 
    public StudentC(StudentA studentA) {
        this.studentA = studentA;
    }
}
```

上面是很基本的3个类，，StudentA有参构造是StudentB。StudentB的有参构造是StudentC，StudentC的有参构造是StudentA ，这样就产生了一个循环依赖的情况，

==我们都把这三个Bean交给Spring管理，并用有参构造实例化==

```java
<bean id="a" class="com.liuqing.student.StudentA">  
    <constructor-arg index="0" ref="b"></constructor-arg>  
</bean>  
<bean id="b" class="com.liuqing.student.StudentB">  
    <constructor-arg index="0" ref="c"></constructor-arg>  
</bean>  
<bean id="c" class="com.liuqing.student.StudentC">  
    <constructor-arg index="0" ref="a"></constructor-arg>  
</bean>
```

下面是测试类：

```java
public class Test {  
    public static void main(String[] args) {  
        ApplicationContext context = new ClassPathXmlApplicationContext("com/liuqing/student/applicationContext.xml");  
        //System.out.println(context.getBean("a", StudentA.class));  
    }  
}
```

执行结果报错信息为：

```java
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException:   
    Error creating bean with name 'a': Requested bean is currently in creation: Is there an unresolvable circular reference?
```



### 2.setter方式单例，默认方式

Spring中Bean实例化的图![img](https://oscimg.oschina.net/oscnet/4f07f200-d488-46db-92dc-bea1f388abd3.jpg)

如图中前两步骤得知：==Spring是先将Bean对象实例化【依赖无参构造函数】--->再设置对象属性的==

==修改配置文件为set方式注入：== 就是可以提前暴露一个还米有完全完成的类

```java
<!--scope="singleton"(默认就是单例方式)  -->
<bean id="a" class="com.liuqing.student.StudentA" scope="singleton">
    <property name="studentB" ref="b"></property>
</bean>
<bean id="b" class="com.liuqing.student.StudentB" scope="singleton">
    <property name="studentC" ref="c"></property>
</bean>
<bean id="c" class="com.liuqing.student.StudentC" scope="singleton">
    <property name="studentA" ref="a"></property>
</bean>
```

下面是测试类：

```java
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/liuqing/student/applicationContext.xml");
        System.out.println(context.getBean("a", StudentA.class));
    }
}
```

打印结果为：

```
com.liuqing.student.StudentA@1fbfd6
```

我们结合上面那张图看，==Spring先是用构造实例化Bean对象 ，此时Spring会将这个实例化结束的对象放到一个Map中，并且Spring提供了获取这个未设置属性的实例化对象引用的方法==，可以不一定要完全实例完就可以引用。 结合我们的实例来看，当Spring实例化了StudentA、StudentB、StudentC后，紧接着会去设置对象的属性，此时StudentA依赖StudentB，就会去Map中取出存在里面的单例StudentB对象，以此类推，不会出来循环的问题



### 3.setter方式原型，prototype

修改配置文件为：

```java
<bean id="a" class="com.liuqing.student.StudentA" scope="prototype">
    <property name="studentB" ref="b"></property>
</bean>
<bean id="b" class="com.liuqing.student.StudentB" scope="prototype">
    <property name="studentC" ref="c"></property>
</bean>
<bean id="c" class="com.liuqing.student.StudentC" scope="prototype">
    <property name="studentA" ref="a"></property>
</bean>
```

scope="prototype" 意思是 每次请求都会创建一个实例对象。两者的区别是：有状态的bean都使用Prototype作用域，无状态的一般都使用singleton单例作用域。

测试用例：

```java
public class Test {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/liuqing/student/applicationContext.xml");
        //此时必须要获取Spring管理的实例，因为现在scope="prototype" 只有请求获取的时候才会实例化对象
        System.out.println(context.getBean("a", StudentA.class));
    }
}
```

打印结果：

```
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: 
    Error creating bean with name 'a': Requested bean is currently in creation: Is there an unresolvable circular reference?
```

==对于“prototype”作用域Bean，Spring容器无法完成依赖注入，因为“prototype”作用域的Bean，Spring容器不进行缓存，因此无法提前暴露一个创建中的Bean。==



## Spring怎么解决循环依赖

Spring的循环依赖的理论依据==基于Java的引用传递，当获得对象的引用时，对象的属性是可以延后设置的。==（但是构造器必须是在获取引用之前）

Spring的单例对象的初始化主要分为三步：

![img](https://oscimg.oschina.net/oscnet/77b44ead-bb7e-4b4b-b560-157e012c3279.jpg)在这里插入图片描述

==（1）createBeanInstance：实例化，其实也就是调用对象的构造方法实例化对象==

==（2）populateBean：填充属性，这一步主要是多bean的依赖属性进行填充==

==（3）initializeBean：调用spring xml中的init 方法。==

从上面单例bean的初始化可以知道：循环依赖主要发生在第一、二步，也就是构造器循环依赖和field循环依赖。那么我们要解决循环引用也应该从初始化过程着手，对于单例来说，在Spring容器整个生命周期内，有且只有一个对象，所以很容易想到这个对象应该存在Cache中，==Spring为了解决单例的循环依赖问题，使用了三级缓存。==

==这三级缓存分别指：==

- ==singletonFactories ：单例对象工厂的cache==
- ==earlySingletonObjects ：提前暴光的单例对象的Cache==
- ==singletonObjects：单例对象的cache==



下边是一个单例对象的这个bean的提取过程

==在创建bean的时候，首先想到的是从cache中获取这个单例的bean，这个缓存就是singletonObjects。如果获取不到，并且对象正在创建中，就再从二级缓存earlySingletonObjects中获取。如果还是获取不到且允许singletonFactories通过getObject()获取，就从三级缓存singletonFactory.getObject()(三级缓存)获取，如果获取到了则：从singletonFactories中移除，并放入earlySingletonObjects中。其实也就是从三级缓存移动到了二级缓存。==

从上面三级缓存的分析，我们可以知道，==Spring解决循环依赖的诀窍就在于singletonFactories这个三级cache。这个cache的类型是ObjectFactory。这里就是解决循环依赖的关键，发生在createBeanInstance之后，也就是说单例对象此时已经被创建出来(调用了构造器)。这个对象已经被生产出来了，虽然还不完美（还没有进行初始化的第二步和第三步），但是已经能被人认出来了（根据对象引用能定位到堆中的对象），所以Spring此时将这个对象提前曝光出来让大家认识，让大家使用。==

这样做有什么好处呢？让我们来分析一下“A的某个field或者setter依赖了B的实例对象，同时B的某个field或者setter依赖了A的实例对象”这种循环依赖的情况。



这个解释基本完美

==A首先完成了初始化的第一步，并且将自己提前曝光到singletonFactories中，此时进行初始化的第二步，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是尝试get(A)，尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，由于A通过ObjectFactory将自己提前曝光了，所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀)，B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中。此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，最终A也完成了初始化，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象完成了初始化。==

知道了这个原理时候，肯定就知道为啥Spring不能解决“A的构造方法中依赖了B的实例对象，同时B的构造方法中依赖了A的实例对象”这类问题了！因为加入singletonFactories三级缓存的前提是执行了构造器，所以构造器的循环依赖没法解决。



## 总结

不要使用基于构造函数的依赖注入，可以通过以下方式解决：

- 在字段上使用@Autowired注解，让Spring决定在合适的时机注入
- 用基于setter方法的依赖注入。