## Java的三大特性详解

## 封装

## 封装的定义

封装（Encapsulation）是面向对象方法的重要原则，就是把==对象的属性和操作（或服务）结合为一个独立的整体，并尽可能隐藏对象的内部实现细节。==

## 封装的好处

1. 良好的封装能够==减少耦合==。
2. 类内部的结构可以==自由修改==。
3. 可以对成员进行更精确的控制。
4. ==隐藏信息，实现细节，更加灵活==。

```java
public class Person{
    private String name;
    private int age;
    private String sex;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    private int getAge(){
        return age;
    }
    private void setAge(int age){
        this.age = age;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
}
```


如果没有setter()，getter()的话，那么Person类应该是这样的：

```text
public class Person {  
    public String name ;  
    public String sex ;  
    public int age ;   
}
```


我们应该这样使用它：

```text
Person person = new Person();  
person.age = 25;  
person.name = "张三";  
person.sex = "男";
```


但是如果需要修改Person，比如将sex修改为int类型，几十或者上百个这样的地方估计要改到崩溃，如果封装了，只需要修改setSex()方法就好了。另外，封装确实可以使我们容易地修改类的内部实现，而无需修改使用了该类的客户代码。还有针对某些成员变量，==不想让其他类知道值为多少，就可以不去做getter()操作，比如Person类中的sex，不想让其他人知道性别，就不做getSex()。还有就是private修饰的成员变量以及成员函数，只能无法在类外调用，做到隐藏的作用。==比如：Person类中的成员变量以及getAge()和setAge()在其他类是无法获取到的。

```text
public class Person{
    private String name;
    private int age;
    private int sex;//1:男；2：女；0：保密

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    private int getAge(){
        return age;
    }
    private void setAge(int age){
        this.age = age;
    }
    public void setSex(String sex){
        if(StringUtils.isNotEmpty(sex)){
            if(sex.equals('男')){
                this.sex = 1;
            }else if(sex.equals('女')){
                this.sex = 2;                   
            }else{
                this.sex = 0;
            }
        }else{
            System.out.println("请输入性别！");    //提示错误信息
        }
    }
}
```



继承

继承的定义

继承是面向对象最显著的一个特性。继承==是从已有的类中派生出新的类，新的类能吸收已有类的数据属性和行为，并能扩展新的能力。==关键字（extends）让类与类之间产生继承关系。

```text
//Man是子类，Person是父类
class Man extends Person{}
```

## 继承的特点

1. 继承关系是传递的。若类C继承类B，类B继承类A（多层继承），则类C既有从类B那里继承下来的属性与方法，也有从类A那里继承下来的属性与方法，还可以有自己新定义的属性和方法。继承来的属性和方法尽管是隐式的，但仍是类C的属性和方法。==继承是在一些比较一般的类的基础上构造、建立和扩充新类的最有效的手段。==
2. 提供多重继承机制。从理论上说，一个类可以是多个一般类的特殊类，它可以从多个一般类中继承属性与方法，这便是多重继承。==Java出于安全性和可靠性的考虑，仅支持单重继承，而通过使用接口机制来实现多重继承。==
3. 提高代码的复用性。若类B继承类A，那么==建立类B时只需要再描述与基类(类A)不同的少量特征(数据成员和成员方法)即可。这种做法能减小代码和数据的冗余度，大大增加程序的重用性。==
4. Java只支持单继承，不支持多继承。也就是一个类只能有一个父类，不可以有多个父类。

## super和this的区别

1. super(参数)是调用基类中的某一个构造函数（构造函数的第一条语句）；this(参数)是调用本类中另一种形成的构造函数（构造函数中的第一条语句）
2. super引用当前对象的直接父类中的成员（用来访问直接父类中被隐藏的父类中成员数据或者函数，基类与派生类中有相同成员定义时如：super.变量名 super.成员函数名(实参)）；this代表当前对象名（在程序中产生二义性之处，应使用this来指明当前对象；如果函数的形参与类中的成员数据同名，这时需要用this来指明成员变量名）
3. ==调用super()必须写在子类构造方法的第一行，否则编译不通过。每个子类构造方法的第一条语句，都是隐含地调用super()，如果父类没有这种形式的构造函数，那么在编译的时候就会报错。==
4. super()和this()类似,区别是，super()从子类中调用父类的构造方法，this()在同一类内调用其它方法。
5. s==uper()和this()均需放在构造方法内第一行。==
6. 尽管可以用this调用一个构造器，但却不能调用两个。
7. ==this和super不能同时出现在一个构造函数里面，因为this必然会调用其它的构造函数，其它的构造函数必然也会有super语句的存在，==所以在同一个构造函数里面有相同的语句，就失去了语句的意义，编译器也不会通过。
8. this()和super()都指的是对象，所以，均不可以在static环境中使用。包括：==static变量,static方法，static语句块。==
9. 从本质上讲，this是一个指向本对象的指针, 然而super是一个Java关键字。

## 内部类

将一个类定义在另一个类里面，里面的那个类就称为内部类。内部类的出现，再次打破了Java单继承的局限性。

## 访问特点：

- 内部类可以直接访问外部类的成员，包括私有成员。
- 外部类要访问内部类的成员，必须要建立内部类的对象。

## 内部类分类及共性：

**1. 共性**

- 内部类仍然是一个独立的类，在编译之后会内部类会被编译成独立的.class文件，但是前面冠以外部类的类名和$符号。
- ==内部类不能用普通的方式访问。内部类是外部类的一个成员，因此内部类可以自由地访问外部类的成员变量，无论是否是private的。==

**2. 成员内部类**

在外部类中有成员变量和成员方法，成员内部类就是把整个一个类作为了外部类的成员；

成员内部类是定义在类中方法外的类；

创建对象的格式为：外部类名.内部类名 对象名 = 外部类对象.内部类对象；

成员内部类之所以可以直接访问外部类的成员，那是因为内部类中都持有一个外部类对象的引用：外部类名.this；

成员内部类可以用的修饰符有final，abstract，public，private，protected，static.

**3. 静态内部类**

静态内部类就是成员内部类加上静态修饰符static，定义在类中方法外。

在外部类中访问静态内部类有两种场景：

- 在外部类中访问静态内部类中非静态成员：*外部类名.内部类名 对象名 = 外部类名.内部对象*，需要通过创建对象访问；
- 在外部类中访问静态内部类中的静态成员：同样可以使用上面的格式进行访问，也可以直接使用外部类名.内部类名.成员。

**4. 局部内部类**

局部内部类是定义在方法中的类。

- 方法内部类只能在定义该内部类的方法内实例化，不可以在此方法外对其实例化。
- 方法内部类对象不能使用该内部类所在方法的非final局部变量。

可以用于方法内部类的修饰符有final，abstract；

静态方法中的方法内部类只能访问外部的静态成员。

**5. 匿名内部类**

匿名内部类是内部类的简化写法，是建立一个带内容的外部类或者接口的子类匿名对象。

前提：

内部类可以继承或实现一个外部类或者接口。

格式：

- new 外部类名或者接口名(){重写方法};
- 通常在方法的形式参数是接口或者抽象类，并且该接口中的方法不超过三个时，可以将匿名内部类作为参数传递。

## 多态

## 多态的定义

==对象在不同时刻表现出来的不同状态。==

## 多态的注意事项

- 一定不能够将父类的对象转换成子类类型。
- 多态自始至终都是子类对象在变化。
- ==父类的引用指向子类对象，该引用可以被提升，也可以被强制转换。==

## 重写与重载

方法的重写Overriding和重载Overloading是Java多态性的不同表现。重写Overriding是父类与子类之间多态性的一种表现，重载Overloading是一个类中多态性的一种表现。

重写遵循以下规则：

- 参数列表必须完全和被重写方法的参数列表一致。

- 返回类型必须完全和被重写方法的返回类型一致。

- 访问修饰符的限制一定要大于被重写方法的访问修饰符

  （public>protected>default>private）。

- 重写的方法一定不能抛出新的检查异常或者比被重写方法声明更加宽泛的检测型异常。

重载的注意事项：

- 在使用重载时只能通过相同的方法名、不同的参数形式实现。不同的参数类型可以是不同的参数类型，不同的参数个数，不同的参数顺序（参数类型必须不一样）。
- 各个重载方法的参数列表必须不同。
- 各个重载方法的返回值类型可以相同也可以不同，但是仅仅返回值类型不同的不是重载。
- 不能通过仅仅通过访问权限、返回类型、抛出的异常的不同而进行重载

## 不同修饰符修饰的内容

```text
类       成员变量   成员方法        构造方法

private                Y          Y            Y
default       Y        Y          Y            Y
protected              Y          Y            Y
public        Y        Y          Y            Y
abstract      Y                   Y  
static                 Y          Y            Y
final         Y        Y          Y
```

注意，常见规则如下：

- 以后，所有的类都用public修饰。并且，在一个java文件中，只写一个类。
- 以后，所有的成员变量用private修饰。
- 以后，所有的成员方法用public修饰。
- 如果是抽象类或者接口：public abstract + …
- 以后，所有的构造方法用public修饰。
- 如果类是工具类或者单例类：构造用private修饰

## 抽象

## 抽象的定义

抽象就是从多个事物中将共性的，本质的内容抽象出来。

## 抽象类

Java中可以定义没有方法体的方法，该方法的具体实现由子类完成，该方法称为抽象方法，包含抽象方法的类就是抽象类。

由来：

多个对象都具备相同的功能，但是功能具体内容有所不同，那么在抽取过程中，只抽取了功能定义，并未抽取功能主体，那么只有功能声明，没有功能主体的方法称为抽象方法。

## 抽象类特点：

- 抽象方法一定在抽象类中；
- 抽象方法和抽象类都必须被abstract关键字修饰；
- 抽象类不可以用new创建对象，因为调用抽象方法没意义；
- 抽象类中的抽象方法要被使用，必须由子类复写其所有的抽象方法后，建立子类对象调用； 如果子类只覆盖了部分的抽象方法，那么该子类还是一个抽象类；
- 抽象类中可以有抽象方法，也可以有非抽象方法，抽象方法用于子类实例化；
- 如果一个类是抽象类，那么，继承它的子类，要么是抽象类，要么重写所有抽象方法。
- 特殊：抽象类中可以不定义抽象方法，这样做仅仅是不让该类建立对象。

## 抽象类的成员特点：

- 成员变量：可以是变量，也可以是常量；
- 构造方法：有构造方法；
- 成员方法：可以是抽象方法，也可以是非抽象方法。

## 抽象类注意事项：

**1. 抽象类不能被实例化，为什么还有构造函数？**

只要是class定义的类里面就肯定有构造函数。抽象类中的函数是给子类实例化的。

**2. 一个类没有抽象方法，为什么定义为抽象类?**

不想被继承，还不想被实例化。

**3. 抽象关键字abstract不可以和哪些关键字共存？**

- final：如果方法被抽象，就需要被覆盖，而final是不可以被覆盖，所以冲突。
- private：如果函数被私有了，子类无法直接访问，怎么覆盖呢？
- static：不需要对象，类名就可以调用抽象方法。而调用抽象方法没有意义。

## 接口（interface）

## 接口的定义

接口是抽象方法和常量值的集合。从本质上讲，接口是一种特殊的抽象类，这种抽象类只包含常量和方法的定义，而没有变量和方法的实现。

格式：interface 接口名{}

接口的出现将”多继承“通过另一种形式体现出来，即”多实现“。

实现（implements）

格式：class 类名 implements 接口名 {}

## 接口特点

- 接口不能被实例化。
- 一个类如果实现了接口，要么是抽象类，要么实现接口中的所有方法。

## 接口的成员特点：

接口中的成员修饰符是固定的！

- 成员常量：public static final，接口里定义的变量是全局常量，而且修饰符只能是这三个关键字，都可以省略，常量名要大写。
- 成员方法：public abstract，接口里定义的方法都是抽象的，两个修饰符关键字可省略。
- 推荐：永远手动给出修饰符。

## 继承与实现的区别：

- 类与类之间称为继承关系：因为该类无论是抽象的还是非抽象的，它的内部都可以定义非抽象方法，这个方法可以直接被子类使用，子类继承即可。只能单继承，可以多层继承。（(class)）
- 类与接口之间是实现关系：因为接口中的方法都是抽象的，必须由子类实现才可以实例化。可以单实现，也可以多实现；还可以在继承一个类的同时实现多个接口。（(class) extends (class) implements (interface1,interface2…)）
- 接口与接口之间是继承关系：一个接口可以继承另一个接口，并添加新的属性和抽象方法，并且接口可以多继承。（(interface) extends (interface1,interface2…)）

## 抽象类和接口的区别：

**成员变量**

- 抽象类能有变量也可以有常量
- 接口只能有常量

**成员方法**

- 抽象类可以有非抽象的方法,也可以有抽象的方法
- 接口只能有抽象的方法

**构造方法**

- 抽象类有构造方法
- 接口没有构造方法

## 类与抽象类和接口的关系

- 类与抽象类的关系是继承 extends
- 类与接口的关系是实现 implements

## 接口的思想特点：

1. 接口是对外暴露的规则；
2. 接口是程序的功能扩展；
3. 接口的出现降低耦合性；(实现了模块化开发,定义好规则,每个人实现自己的模块,大大提高了开发效率)
4. 接口可以用来多实现；
5. 多个无关的类可以实现同一个接口；
6. 一个类可以实现多个相互直接没有关系的接口；
7. 与继承关系类似，接口与实现类之间存在多态性。