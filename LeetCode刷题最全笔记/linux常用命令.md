# [【基础】26个命令玩转linux，菜鸟及面试必备](https://segmentfault.com/a/1190000009532290)



## 1 查看目录与文件：ls

```
#显示当前目录下所有文件的详细信息
ls -la
```

## 2 切换目录：cd

```
#切换当前目录为/opt/test
cd /opt/test
```

## 3 显示当前目录：pwd

```
pwd
```

## 4 创建空文件：touch

```
#在当前目录下创建文件desc.txt
touch desc.txt
```

## 5 创建目录：mkdir

```
#在当前目录下创建test目录
mkdir test
#在/opt/test目录下创建目录img，若无test目录，先创建test目录
mkdir -p /opt/test/img
```

## 6 查看文件内容：cat

```
#查看desc.txt的内容
cat desc.txt
```

## 7 分页查看文件内容：more

```
#分页查看desc.txt的内容
more desc.txt
```

## 8 查看文件尾内容：tail

```
#查看desc.txt的最后100行内容
tail -100 desc.txt
```

## 9 拷贝：cp

```
#拷贝desc.txt到/mnt目录下
cp desc.txt /mnt/
#拷贝test目录到/mnt目录下
cp -r test /mnt/
```

## 10 剪切或改名：mv

```
#剪切文件desc.txt到目录/mnt下
mv desc.txt /mnt/
```

## 11 删除：rm

```
#删除test目录，-r递归删除，-f强制删除。危险操作，务必小心，切记！
rm -rf test
```

## 12 搜索文件：find

```
#在opt目录下查找以.txt结尾的文件
find /opt -name '*.txt'
```

## 13 创建链接文件：ln

```
#创建目录/opt/test的符号链接
ln -s /opt/test ./link2test
```

## 14 显示或配置网络设备：ifconfig

```
#显示网络设备情况
ifconfig
```

## 15 显示网络相关信息：netstat

```
#列出所有端口
netstat -a
```

## 16 显示进程状态：ps

```
#显示当前所有进程
ps -ef
#显示当前所有java相关进程
ps-ef | grep java
```

## 17 查看目录使用情况：du

```
#查看/opt/test目录的磁盘使用情况
du -h /opt/test
```

## 18 查看磁盘空间使用情况：df

```
#查看磁盘空间使用情况
df -h 
```

## 19 显示系统当前进程信息：top

```
#显示系统当前进程信息
top
```

## 20 杀死进程：kill

```
#杀死进程号为27810的进程，强制终止，系统资源无法回收
kill -s 9 27810
```

## 21 压缩和解压：tar

```
#打包test目录为test.tar.gz文件，-z表示用gzip压缩
tar -zcvf test.tar.gz ./test
#解压test.tar.gz文件
tar -zxvf test.tar.gz
```

## 22 改变文件或目录的拥有者和组：chown

```
#变更文件desc.txt的拥有者为nginx，用户组为nginx
chown nginx:nginx desc.txt
#变更test及目录下所有文件的拥有者为nginx，用户组为nginx
chown -R nginx:nginx test
```

## 23 改变文件或目录的访问权限：chmod

```
#权限范围：u(拥有者)g(郡组)o(其它用户)， 权限代号：r(读权限/4)w(写权限/2)x(执行权限/1)
#给文件拥有者增加test.sh的执行权限
chmod u+x test.sh
#给文件拥有者增加test目录及其下所有文件的执行权限
chmod u+x -R test
```

## 24 文本编辑：vim

```
#vim三种模式：命令模式，插入模式，编辑模式。使用ESC或i或：来切换模式。
#命令模式下，:q退出 :q!强制退出 :wq保存退出 :set number显示行号 /java在文档中查找java yy复制 p粘贴 
#编辑desc.txt文件
vim desc.txt
```

## 25 关机或重启：shutdown

```
#立刻关机
shutdown -h now
#60秒后重启
shutdown -r -t 60
```

## 26 帮助命令：man

```
#查看ls命令的帮助文档
man ls
```

本文简要介绍了linux的26个常用命令及其最基本的用法，虽然个数不多，但却能覆盖大多数的使用场景。在实际的使用过程中，要多注意利用man命令，认真阅读liunx的帮助文档，多多加练习，一定会进步很快的。本人菜鸟，如有错误请指正。