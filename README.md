一、理解git和github的概念

git:是一种分布式版本控制系统，与SVN同概念

github:一个网站，利用git将我们的项目代码托管在上面





二、准备阶段
1、在github上申请账号，并New Repositories（我的名字是April-Taurus），新建的Repository默认master为其主干分支

2、下载-安装-配置git   http://jingyan.baidu.com/article/9f7e7ec0b17cac6f2815548d.html

3、git与github连接

原理：本地Git仓库和GitHub仓库之间的传输是通过SSH加密的，所以要在本地生成一个私钥和一个密钥

步骤：1）打开git bash  键入  $ssh-keygen -t  rsa  -C  "msyangyanfei@outlook.com"，若不设置密码就一路回车

                   这样会生成一个.ssh文件，在C:/用户/yanfei/ssh下会有两个文件：id_rsa（私钥）和id_rsa.pub（公钥）

             2）打开github，点击头像—setting—SSH，New SSH key，笔记本方式打开id_rsa.pub把里面的内容复制进去，自己取个Tittle名





三、Git上传项目代码到github上

1、右键项目文件夹Git Bash Here

2、键入

git init//设置该目录为推送

git add 更新的文件名//加入修改列表

git commit-m "first commit"//递交修改声明

git remote add origin https://github.com/Yapril/April-Taurus.git//为远程Git更名为origin

git push -u origin master //推送此次修改
命令细节解析：

参数“-u”，Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支和远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令





四、一些问题解决

问题1：

$git remote add origin https://github.com/Yapril/April-Taurus.git

fatal:remote origin already exists.
解决：

先输入$ git remote rm origin

再输入$ git remote add origin https://github.com/Yapril/April-Taurus.git


问题2：

解决：

用户名和密码对应的是github上的



问题3：




解决：

一般push前要先pull以下

键入$git pull origin master



问题4：




解决：

git 在pull或者合并分支的时候有时会遇到这个界面。可以不管(直接下面3,4步)，如果要输入解释的话就需要:

1.按键盘字母 i 进入insert模式

2.修改最上面那行黄色合并信息,可以不修改

3.按键盘左上角"Esc"

4.输入":wq",注意是冒号+wq,按回车键即可





五、注意点

考虑到版本还会更新，而master是主分支，可以在其下新建一个develop分支，最后开发完毕后合并到master中(还不是很懂下次碰到问题在解决)

原地址：http://www.jianshu.com/p/01975e421ddb
上传下载项目：http://blog.csdn.net/lsyz0021/article/details/51292311
