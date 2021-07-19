**递归修改一个目录中所有文件的权限方法** ：进入目录输入命令 sudo  chmod 777 -R *(其中 -R 表示递归处理，*代表所有文件)或 chmod 777 -R /home/abc/dirctoryname,此命令不用进入目录，/home/abc/dirctoryname是目录路径。

 



```
sudo chmod 777 -R /usr/local/bin  #具体目录
sudo chmod 777 -R * 也可以是进入这个目录下 然后执行这个命令
```

 

常用方法如下：

sudo chmod 600 ××× （只有所有者有读和写的权限）
sudo chmod 644 ××× （所有者有读和写的权限，组用户只有读的权限）
sudo chmod 700 ××× （只有所有者有读和写以及执行的权限）
sudo chmod 666 ××× （每个人都有读和写的权限）
sudo chmod 777 ××× （每个人都有读和写以及执行的权限）