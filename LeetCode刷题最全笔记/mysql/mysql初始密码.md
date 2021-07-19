![image-20201118094806766](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201118094806766.png)

初始密码：4W+T!dfsh.dS



4W+T!dfsh.dS



```java
import java.sql.*;
public class db {

        //MySQL驱动包名
        private static final String driver_Name = "com.mysql.jdbc.Driver";
        //数据库连接地址  booknanage为你需要连接的数据库名
        private static final String URL = "jdbc:mysql://localhost:3306/bookmanage?serverTimezone=GMT";
        //用户名
        private static final String USER_NAME = "root";
        //密码
        private static final String PASSWORD = "123456";

        public static void main(String[] args) {
            Connection con = null;
            try {
                //加载驱动类
                Class.forName(driver_Name);
                System.out.println("加载数据库驱动成功");
                //获取连接
                con = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println("获取连接成功");
                //数据库查询语句
                //注意换自己的表
                String sql = "SELECT author from book";
                Statement prst = con.createStatement();
                ResultSet rs = prst.executeQuery(sql);
                System.out.println("成功");
                while (rs.next()) {
                    System.out.println("作者 " + rs.getString("author"));
                }
                rs.close();
                prst.close();
            } catch (ClassNotFoundException e) {
                System.out.println("连接失败！");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("连接失败！");
                e.printStackTrace();
            }
        }
}


```



### 现在的密码

13863978000liu

### 注意事项

0、需要cd 到G:mysql:bin下才可以

1、要先启动mysql才可以进行登录：

net start mysql 

2、mysql -uroot -p -P3306 -hlocalhost

输入密码然后进行具体的操作就可以



之后是这个教程

http://www.imooc.com/wiki/mysqllesson/contentmysql.html

