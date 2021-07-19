###### step1：在电脑上安装git软件

git的下载网址：[https://git-for-windows.github.io/](https://link.jianshu.com?t=https://git-for-windows.github.io/)
 安装过程不多说，除安装位置不要放到C盘之外，其他设置都默认就好。安装完成后会自动打开一个如下窗口，视为安装成功。或者点击开始菜单-git-git Bash也能打开该窗口。

![img](https:////upload-images.jianshu.io/upload_images/4923361-aaf54bccd5371ccd.png?imageMogr2/auto-orient/strip|imageView2/2/w/595/format/webp)



###### step2：设置git的用户名和密码

点击开始菜单》git-bash打开命令窗口，输入下列命令，将双引号中内容替换成自己的用户名和密码。
 $ git config --global user.name "Your Name"
 $ git config --global [user.email](https://link.jianshu.com?t=http://user.email) "[xxxxxxx@qq.com](https://link.jianshu.com?t=mailto:xxxxxxx@qq.com)"

![img](https:////upload-images.jianshu.io/upload_images/4923361-d2eb491eaeda8c24.png?imageMogr2/auto-orient/strip|imageView2/2/w/595/format/webp)



###### step3：创建本机的ssh Key

在git Bash窗口中输入下列命令，创建本电脑的ssh Key
 $ ssh-keygen -t rsa -C "[xxxxxxx@qq.com](https://link.jianshu.com?t=mailto:xxxxxxx@qq.com)"
 输入后一路回车，直到显示如下图则表示生成成功。

![img](https:////upload-images.jianshu.io/upload_images/4923361-a50a89f25f385f98.png?imageMogr2/auto-orient/strip|imageView2/2/w/595/format/webp)


 注意这段代码里有一个文件地址：/c/Users/Administrator/.ssh/，这个就是SSH Key文件夹了，打开文件夹找到id_rsa.pub，用记事本打开它，复制下来。



###### step4：在github账号中填写公钥

按照下图步骤，依次点击Setting》SSH and GPG keys进入SSH Key设置页面



![img](https:////upload-images.jianshu.io/upload_images/4923361-98e3c47646a5fd3d.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)



点击New SSH key新增SSH keys，title可以随意填写，Key填写刚刚复制的内容，然后保存。



![img](https:////upload-images.jianshu.io/upload_images/4923361-163e6cbe4599fb44.png?imageMogr2/auto-orient/strip|imageView2/2/w/991/format/webp)


 这台电脑就可以向github提交代码了。





#  从这里开始



每次建立新的仓库，提交的时总会出现这样的错误，真是头疼，……

直接开始正题，git 提交的步骤：

1. git init //初始化仓库

1. git add .(文件name) //添加文件到本地仓库
2. git commit -m “first commit” //添加文件描述信息
3. git remote add origin + 远程仓库地址 //链接远程仓库，创建主分支
4. git push -u origin master //把本地仓库的文件推送到远程仓库

提交之后就会出现以下错误
![这里写图片描述](https://img-blog.csdn.net/20180330091437163?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ29uZ3FpbmdsaW4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
要想解决以上错误，只需要在4，5之间使用git pull origin master即可

正确步骤：

1. git init //初始化仓库

1. git add .(文件name) //添加文件到本地仓库
2. git commit -m “first commit” //添加文件描述信息
3. git remote add origin + 远程仓库地址 //链接远程仓库，创建主分支
4. git pull origin master // 把本地仓库的变化连接到远程仓库主分支
5. git push -u origin master //把本地仓库的文件推送到远程仓库

OK
搞定！！



















###### step5：在github中创建仓库

点击New repository进入仓库创建页面，然后填写仓库名称，其他内容可不填，直接保存。



![img](https:////upload-images.jianshu.io/upload_images/4923361-4208c608c68d00af.png?imageMogr2/auto-orient/strip|imageView2/2/w/1162/format/webp)



创建成功后得到下图中的git地址



![img](https:////upload-images.jianshu.io/upload_images/4923361-5bf535ade645363d.png?imageMogr2/auto-orient/strip|imageView2/2/w/1182/format/webp)

###### step6：将本地仓库与github仓库关联

====在本地新建一个文件夹，和github仓库名称一致。====然后选中本地仓库文件夹，右键选择git Bash打开git命令窗口。使用git init命令初始化==，在本地工作区中创建一个git隐藏目录。
 然后使用如下命令即可关联，注意将origin后面的地址换成你自己的github地址。
 ==git remote add origin [git@github.com](https://link.jianshu.com?t=mailto:git@github.com):daisy1995/baidu-ife.git即可关联成功。==
 git使用过程中，不会出现操作成功的提示，只要不报错，就视为操作成功。

###### step7：提交代码

为了检验是否能正常提交，可在文件夹中放入一个txt测试文件。
 git有一个工作区和暂存区。工作区就是我们在电脑上看见的文件夹，工作区有一个隐藏的目录.git，这个是版本库。版本库中分为暂存区和master分支。提交代码的时候，我们需要先将工作区的代码提交到暂存区，再从暂存区同步到master分支。
 所以第一步使用git add命令将本地工作区的文件添加待提交的文件。==git add .表示添加文件夹中的所有文件，一般都是用这个命令一次性添加==。
 ==接下来使用git commit -m"描述文字"将添加的文件提交到暂存区。==
 ==最后，使用git push origin master同步到github远程仓库。==

==以后每次提交代码，只需重复以下命令：==
 ==$ git add .
 $ git commit -m"提交描述"==
 ==$ git push origin master==

第一次使用git的push命令时，会出现如下警告：
 The authenticity of host 'github.com (xx.xx.xx.xx)' can't be established.RSA key fingerprint is xx.xx.xx.xx.xx.Are you sure you want to continue connecting (yes/no)?
 这是github的安全验证，直接输入yes即可，下次就不会有提示了。

###### 常见错误问题解决：

错误提示一：
 fatal: Not a git repository (or any of the parent directories): .git
 出现这个问题是因为没有初始化，本地工作区中没有.git隐藏文件。解决方法：使用git init命令。

错误提示二：
 error: src refspec master does not match any.
 error: failed to push some refs to '[git@github.com](https://link.jianshu.com?t=mailto:git@github.com):daisy1995/baidu-ife.git'
 这个错误提示一般会出现在使用push提交命令的时候，出现代表暂存区没有待提交的文件，很有可能是你忘记了将文件提交到暂存区。解决办法是使用git add .命令添加所有文件，然后使用git commit -m""命令提交到暂存区，最后再使用push提交。<p class="number-item">110收藏</p>





新建文件夹 并且在其中git bash打开 执行 ``git init`` 初始化

