[(26条消息) 面试题思考：解释一下什么叫AOP（面向切面编程）_冷囧囧-CSDN博客](https://blog.csdn.net/czh500/article/details/102193124?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_baidulandingword-0&spm=1001.2101.3001.4242)



1) JDK动态代理

- 主要使用到 InvocationHandler 接口和 Proxy.newProxyInstance() 方法。

- JDK动态代理要求被代理实现一个接口，只有接口中的方法才能够被代理 。

- 其方法是将被代理对象注入到一个中间对象，而中间对象实现InvocationHandler接口，

- 在实现该接口时，可以在 被代理对象调用它的方法时，在调用的前后插入一些代码。

- 而 Proxy.newProxyInstance() 能够利用中间对象来生产代理对象。

- 插入的代码就是切面代码。所以使用JDK动态代理可以实现AOP。

我们看个例子：

被代理对象实现的接口，只有接口中的方法才能够被代理：

被代理对象实现的接口，只有接口中的方法才能够被代理：

```java
public interface UserService {

    public void addUser(User user);

    public User getUser(int id);

}

public class UserServiceImpl implements UserService {
    public void addUser(User user) {
        System.out.println("add user into database.");
    }
    public User getUser(int id) {

        User user = new User();
        user.setId(id);
        System.out.println("getUser from database.");
        return user;
    }

}
```

代理中间类：

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class ProxyUtil implements InvocationHandler {
    private Object target;    // 被代理的对象
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("do sth before....");
        Object result =  method.invoke(target, args);
        System.out.println("do sth after....");
        return result;

    }
    ProxyUtil(Object target){
        this.target = target;
    }
    public Object getTarget() {
        return target;
    }
    public void setTarget(Object target) {
        this.target = target;
    }
}
```

测试：

```java
import java.lang.reflect.Proxy;

import net.aazj.pojo.User;

public class ProxyTest {

    public static void main(String[] args){

        Object proxyedObject = new UserServiceImpl();    // 被代理的对象

        ProxyUtil proxyUtils = new ProxyUtil(proxyedObject);
        // 生成代理对象，对被代理对象的这些接口进行代理：UserServiceImpl.class.getInterfaces()
        UserService proxyObject = (UserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
                    UserServiceImpl.class.getInterfaces(), proxyUtils);

        proxyObject.getUser(1);
        proxyObject.addUser(new User());

    }
}
```

执行结果：

```java
do sth before....
getUser from database.



do sth after....



do sth before....



add user into database.



do sth after....
我们看到在 UserService接口中的方法 addUser 和 getUser方法的前面插入了我



们自己的代码。这就是JDK动态代理实现AOP的原理。



 



我们看到该方式有一个要求， 被代理的对象必须实现接口，而且只有接口中的方法才能被代理 。
```