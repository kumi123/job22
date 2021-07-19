之前面试被问过，一直想抽空整理下，网上的资源很多，但是其实经常使用和被问到的就是这么一些。今天抽空整理了一些。欢迎大家指正。

目录

常用指令

ps查看进程

kill 杀掉进程

启动服务

查看日志

查看端口

find查找文件

查看文件

常用指令
1.说出10个linux常用的指令

ls 查看目录中的文件

cd /home 进入 ‘/ home’ 目录；cd .. 返回上一级目录；cd ../.. 返回上两级目录

mkdir dir1 创建一个叫做 ‘dir1’ 的目录

rmdir dir1 删除一个叫做 ‘dir1’ 的目录 （只能删除空目录）

==rm -f file1== 删除一个叫做 ‘file1’ 的文件’，-f 参数，忽略不存在的文件，从不给出提示。

==rm -rf /mulu==  目录下面文件以及子目录下文件

cp /test1/file1 /test3/file2   如将/test1目录下的file1复制到/test3目录，并将文件名改为file2

mv /test1/file1 /test3/file2  如将/test1目录下的file1移动到/test3 目录，并将文件名改为file2

==mv * ../== Linux当前目录所有文件移动到上一级目录

==ps -ef|grep xxx== 显示进程pid

==kill==  使用kill命令来终结进程。先使用ps命令找到进程id，使用kill -9命令，终止进程。

tar –xvf file.tar  解压 tar包

unzip file.zip 解压zip

unrar e file.rar 解压rar

==free -m==  查看服务器内存使用情况

ps查看进程
2.如何查看所有java进程

grep是搜索关键字

ps -ef | grep java

-aux 显示所有状态

ps -aux | grep java

kill 杀掉进程
3.如何杀掉某个服务的进程

kill 命令用于终止进程

-9 强迫进程立即停止

kill -9 [PID]

这里pid需要用 ps -ef | grep 查询pid



启动服务
4.如何启动服务

以启动Tomcat为例,先cd到启动的.sh文件目录

> cd /java/tomcat/bin
> ./startup.sh
> 停止Tomcat服务命令

./shutdown.sh

查看日志
5.如何查看测试项目的日志

一般测试的项目里面，有个logs的目录文件，会存放日志文件，有个xxx.out的文件，可以用tail -f 动态实时查看后端日志

先cd 到logs目录(里面有xx.out文件)

==tail -f xx.out==

这时屏幕上会动态实时显示当前的日志，ctr+c停止

6.如何查看最近1000行日志

==tail -1000 xx.out==

查看端口
7.LINUX中如何查看==某个端口是否被占用==

==netstat  -anp  | grep==   端口号



图中主要看==监控状态为LISTEN表示已经被占用==，最后一列显示被服务mysqld占用，查看具体端口号，只要有如图这一行就表示被占用了

查看82端口的使用情况，如图

==netstat  -anp  |grep  82==

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190705111530350.png)



可以看出并没有LISTEN那一行，所以就表示没有被占用。此处注意，图中显示的LISTENING并不表示端口被占用，不要和LISTEN混淆哦，查看具体端口时候，必须要看到tcp，端口号，LISTEN那一行，才表示端口被占用了

==查看当前所有已经使用的端口情况==，如图：

==netstat   -nultp==（此处不用加端口号）



find查找文件
8.如何查找一个文件大小超过5M的文件

==find . -type f -size +5M==

9.如果知道一个文件名称，怎么查这个文件在linux下的哪个目录，如：要查找tnsnames.ora文件

find / -name tnsnames.ora

查到：
/opt/app/oracle/product/10.2/network/admin/tnsnames.ora
/opt/app/oracle/product/10.2/network/admin/samples/tnsnames.ora

还可以用locate 来查找

locate tnsnames.ora

结果是：
/opt/app/oracle/product/10.2/hs/admin/tnsnames.ora.sample
/opt/app/oracle/product/10.2/network/admin/tnsnames.ora
/opt/app/oracle/product/10.2/network/admin/samples/tnsnames.ora

10.find查找文件

find / -name httpd.conf　　#在根目录下查找文件httpd.conf，表示在整个硬盘查找
find /etc -name httpd.conf　　#在/etc目录下文件httpd.conf
find /etc -name ‘srm‘　　#使用通配符(0或者任意多个)。表示在/etc目录下查找文件名中含有字符串‘srm’的文件
find . -name ‘srm‘ 　　#表示当前目录下查找文件名开头是字符串‘srm’的文件

按照文件特征查找 　　　　
find / -amin -10 　　# 查找在系统中最后10分钟访问的文件(==access time==)
find / -atime -2　　 # 查找在系统中最后48小时访问的文件
find / -empty 　　# 查找在系统中为空的文件或者文件夹
find / -group cat 　　# 查找在系统中属于 group为cat的文件
find / -mmin -5 　　# 查找在系统中最后5分钟里修改过的文件(==modify time==)
find / -mtime -1 　　#查找在系统中最后24小时里修改过的文件
find / -user fred 　　#查找在系统中属于fred这个用户的文件
find / -size +10000c　　#查找出大于10000000字节的文件(c:字节，w:双字，k:KB，M:MB，G:GB)
find / -size -1000k 　　#查找出小于1000KB的文件

 

查看文件
查看文件内容的命令：

==cat==     由第一行开始显示内容，并将所有内容输出   （之前公司用的比较多）

==tac==     从最后一行倒序显示内容，并将所有内容输出

more    根据窗口大小，一页一页的现实文件内容

less    和more类似，但其优点可以往前翻页，而且进行可以搜索字符

head    只显示头几行

tail    只显示最后几行                             （之前公司用的比较多）

nl      类似于cat -n，显示时输出行号

tailf   类似于tail -f     

查看命令下一步其实就是编辑，但是编辑命令涉及比较多，参考文章：Linux文件编辑命令vi详细说明

 

1.cat 与 tac
cat的功能是将文件从第一行开始连续的将内容输出在屏幕上。但是cat并不常用，原因是当文件大，行数比较多时，屏幕无法全部容下时，只能看到一部分内容。

==cat语法：cat [-n]  文件名 （-n ： 显示时，连行号一起输出）==

tac的功能是将文件从最后一行开始倒过来将内容数据输出到屏幕上。我们可以发现，tac实际上是cat反过来写。这个命令也不常用。

tac语法：tac 文件名。

 

2.more和less（常用）
==more的功能是将文件从第一行开始，根据输出窗口的大小，适当的输出文件内容==。当一页无法全部输出时，可以用“回车键”向下翻行，用“空格键”向下翻页。退出查看页面，请按“q”键。另外，more还可以配合管道符“|”（pipe）使用，例如:ls -al | more

more的语法：more 文件名

Enter 向下n行，需要定义，默认为1行； 

==Ctrl f 向下滚动一屏；== 

空格键 向下滚动一屏； 

==Ctrl b 返回上一屏；== 

= 输出当前行的行号； 

:f 输出文件名和当前行的行号； 

v 调用vi编辑器； 

! 命令 调用Shell，并执行命令； 

q 退出more

 

less的功能和more相似，但是使用more无法向前翻页，只能向后翻。

less可以使用【pageup】和【pagedown】键进行前翻页和后翻页，这样看起来更方便。

less的语法：less 文件名

less还有一个功能，可以在文件中进行搜索你想找的内容，假设你想在passwd文件中查找有没有weblogic字符串，那么你可以这样来做：

[root@redhat etc]# ==less passwd==

然后输入：

==/weblogic==

回车

此时如果有weblogic字符串，linux会把该字符已高亮方式显示。

退出查看页面，请按“q”键。

 

3.head和tail
head和tail通常使用在只需要读取文件的前几行或者后几行的情况下使用。head的功能是显示文件的前几行内容

head的语法：head [-n number] 文件名 (number 显示行数)

eg: head -n 5  tesr.java

 

tail的功能恰好和head相反，只显示最后几行内容

tail的语法:tail [-n number] 文件名

 

4.nl
nl的功能和cat -n一样，同样是从第一行输出全部内容，并且把行号显示出来

nl的语法：nl 文件名

 

5.tailf
　tailf命令几乎等同于==tail -f==，严格说来应该与tail --follow=name更相似些。当文件改名之后它也能继续跟踪，特别适合于日志文件的跟踪（follow the growth of a log file）。与tail -f不同的是，如果文件不增长，它不会去访问磁盘文件（It is similar to tail -f but does not access the file when it is not growing.  This has the side effect of not updating the access  time for the file, so a filesystem flush does not occur periodically when no log activity is happening.）。tailf特别适合那些便携机上跟踪日志文件，因为它能省电，因为减少了磁盘访问嘛（tailf  is extremely useful for monitoring log files on a laptop when logging is infrequent and the user desires that the hard disk spin down to conserve battery life.）。tailf命令不是个脚本，而是一个用C代码编译后的二进制执行文件，某些Linux安装之后没有这个命令，本文提供了怎么编译安装tailf命令的方法。


（后面空了再操作配图。）