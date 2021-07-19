# [【git】第1篇：git面试以及常用命令操作Git面试问题总结

## git fetch和git pull的区别

- git pull = fetch + merge
- 使用==git fetch是取回所有的最新的远程分支更新==，不会对本地执行merge操作，==本地内容不会有变==动；
- ==git pull会更新你本地代码变成服务器上对应分支的最新版本代码；==

## git merge和git rebase的区别

参考答案：[git merge和git rebase的区别](https://blog.csdn.net/rflyee/article/details/79362381)

## git reset、git revert 和 git checkout 有什么区别

这个问题同样也需要先了解 git 仓库的三个组成部分：==工作区（Working Directory）、暂存区（Stage）和历史记录区（History）。==

### 共同点

用来撤销代码仓库中的某些更改。

### 不同点

#### 从 commit 层面来说：

git reset 可以将一个分支的末端指向之前的一个 commit 点。然后再下次 git 执行垃圾回收的时候，会把这个 commit 之后的 commit 都扔掉。git reset 还支持三种标记，用来标记 reset 指令影响的范围：

--mixed：会影响到暂存区和历史记录区。也是默认选项；

--soft：只影响历史记录区；

--hard：影响工作区、暂存区和历史记录区。

注意：因为 git reset 是直接删除 commit 记录，从而会影响到其他开发人员的分支，所以不要在公共分支（比如 develop）做这个操作。

git revert 和 git reset 的目的是一样的，但是做法不同，它会以创建新的 commit 的方式来撤销 commit，这样能保留之前的 commit 历史，比较安全。另外，同样因为可能会覆盖本地的修改，所以执行这个指令之前，你需要 stash 或者 commit 暂存区和工作区的更改。

git checkout 可以将 HEAD 移到一个新的分支，并更新工作目录。因为可能会覆盖本地的修改，所以执行这个指令之前，你需要 stash 或者 commit 暂存区和工作区的更改。

#### 从文件层面来说

==git reset 只是把文件从历史记录区拿到暂存区，不影响工作区的内容==，而且不支持 --mixed、--soft 和 --hard。

==git checkout 则是把文件从历史记录拿到工作区，不影响暂存区的内容。==

git revert 不支持文件层面的操作。

回答关键点：

- 对于 commit 层面和文件层面，这三个指令本身功能差别很大。
- git revert 不支持文件层面的操作。
- 不要在公共分支做 git reset 操作。

# Git命令

## Git配置相关

```
/*设置用户的姓名，用于每次的commit*/
git config - - global user.name "John Simth"
/*设置用户的邮箱，用于每次的commit*/
git config - - global user.email john@example.com  
```

## Git仓库的创建

```
/*将现有的项目转变为Git仓库或者新建一个空的仓库*/
git init
/*克隆仓库*/
git clone '远程仓库地址'      
```

## Git的基本操作

### 提交

```
/*将单个文件添加到暂存区中*/
git add 'filename'
/*将当前所有文件添加到暂存区中*/
git add .
/*提交文件*/
git commit -m "comment"
/*添加并提交文件*/
git commit -a -m "comment"
```

### Git分支操作

#### 创建、删除分支

```
git branch                    //查看所有分支
git branch <name>             //创建分支
git checkout <name>           //切换分支
git checkout -b <name>        //创建并切换分支

git branch -d <name>          //删除分支，无法删除未被合并的分支
git branch -D <name>          //强制删除分支，可以删除未被合并的分支
```

#### 分支合并

```
git merge <branch>             //将branch分支合并到当前分支，当前分支拥有branch分支的记录，branch分支不变
git merge <branch1> <branch2>  //将分支branch1合并到branch2
/*git默认使用fast-farward快合并模式，会直接将要被合并的分支指向当前分支；
 *但是--no-ff不会，它会创建合并点；
 **/
git merge --no-ff <branch>
/*将两个分支合并成一个线性的提交*/
git rebase <branch>
```

[git merge和git rebase的区别](https://www.cnblogs.com/MuYunyun/p/6876413.html?utm_source=itdadao&utm_medium=referral)

### 查看状态或记录

```
/**
 *显示文件的状态: staged,unstaged和untracked三种状态
 *untracked:表示版本库中有新创建的文件，但是并为纳入版本库的管理中
 *unstaged:将untracked状态的文件执行 git add 命令后文件状态就是unstaged，此时 
 *意味着git发现这个文件被改动了，但是改动的部分并没有提交到仓库中
 *staged:表示文件已经被提交到仓库中了
 **/
git status
/*显示当前分支的commit记录*/
git log
/*以图的形式显示当前分支的commit记录*/
git log --graph
```

### 比较

```
/*工作区与暂存区之间的差别，即还没有添加到暂存区的修改，这里比较的是修改内容*/
git diff
/*暂存区与上一次提交的差别*/
git diff --cached
/*比较两次commit之间的差别*/
git diff <commit id1> <commit id2>
/*比较两个分支之间的差别*/
git diff <branch1> <branch2>
```

### 回退

```
/*将HEAD移动到commit id对应的提交点*/
git reset <commit id>
/*工作区、暂存区和历史记录区都会被重置commit id提交点*/
git reset --hard <commit id>
```

### 撤销

### 保存修改

```
git stash                     //保存当前工作区和暂存区的状态
git stash list                //查看所有的stash信息
git stash apply <stash>       //回复指定stash，但不删除该stash记录
git stash drop <stash>        //删除指定stash
git stash clear               //删除所有stash记录
```

### 标签

```
git tag <name>                 //给最新提交打标签
git tag <name> <commit_hash>   //给commit_hash提交打标签
git tag                        //查看所有标签
git show <name>                //查看标签信息
git checkout <tagname>         //切换到标签
git tag -d <tagname>           //可以删除一个本地标签
 
git push --tags                //把本地tag push到远端
git fetch origin tag <tagname> //获取远程tag
git push origin --delete tag <tagname>  //删除远程tag
```

## Git远程仓库操作

## 关联远程仓库

```
git remote add origin git@server-name:path/repo-name.git
git push -u origin master
git push origin master
```