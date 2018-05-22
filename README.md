## 软件开发规范
### JAVA 开发规范
我们一致按照业内标准[https://github.com/google/styleguide]配置自己的ide，比如
1. import中不能有通配符。
2. 重载方法应该放在一起连续出现。
3. 即使代码块中没有内容，或者只有一行代码，也必须使用大括号。
    * 2空格缩进。
    * 列宽是80或100个字符。
    * 不能使用类似C语言的数组变量声明。
    * switch语句中，必须包含default语句。
    * 修饰符的顺序应按照Java语言规范推荐的顺序。
    * 常量命名应使用CONSTANT_CASE格式（译注：所有字母大写，单词使用下划线分隔）注意，所有常量都必须是static final成员，但并不是所有的static final成员都是常量。
    * 类名和变量名用camel case，类名首字母大写,变量名首字母小写
...

### 项目开发约定
1. 资源名称用复数显示在url中：  /api/userinfo/users ....
2. 在url 对接controlller里传递的id 用string, 便于扩展，和数据库表
    资源中的id松耦合
3. Exception 分2种: recoverable 和 non-recoverable 
4. Exception handler尽量集中定义在在公共包里面，避免代码功能散布抬高维护成本.
5. controller 只处理输入审核和返回正常结果和异常处理，具体业务需求放在service层实现
6. module下面区分dto,dao:将和前端交互的数据对象设置在dto下， 将和database交互
的对象设置在dao下
7. swagger能否放在common modle中？
8. MVCConfiguration设置各种静态资源，但是各个模块尽量要一致
9. SpringSecurity用来控制controller层的访问，对应的资源和用户权限控制考虑用对应sso或者对等模块实现
10. 我们尽量用Spring JPA框架或者能够和DB松耦合的框架来实施和DB的交互,以备将来Mysql<->postgres<->mongodb
的切换
11. 我们采用的restful思想进行url设计,任何资源默认支持crud操作,url 的资源从大到小排序从主到次进行排列： 
比如：api/userinfo/user/{userUuid}/bankcards/{bankcardNum}
12. redis key定义规范:{主模块名字}_{模块名字}_{具体变量名字} 比如 aaas_userinfo_userid
13. git 分支开发原则：
 - 平时开发在dev上进行，如果有大规模改动用，姓名_tapd编号_yyyyMMdd,比如：cw_1000246_20180330
 - 提交测试请求，同意后代码merge到test分支（这步在人员少的情况下可以简化为dev测试通过后merge到test分支）
 - 测试通过后，merge到prod分支
 - test 和 prod分支不能直接提交改动，改动必须在dev或者紧急修复分支上完成然后merge到test, test分支上的提交
 测试通过后可以merge 到prod分支

14. git merge 原则
 - test 和 prod分支属于受到保护的分支，不允许直接push
 - 如果test 和 prod 上需要提交merge, 请直接登录 192.168.1.10 gitlab 网页提交merge 请求: http://192.168.1.10/java/aaas/merge_requests/new
 - 选择branch src , 选择branch target, 提交merge comment，提交merge
 - 如果有冲突: 假设本地是dev, 要往test分支merge 那么
   1. git pull origin dev
   2. git checkout test
   3. git pull origin test
   4. git merge dev
   5. git checkout dev
   6. git merge test
   7. git push origin dev
   然后到网页去提交merge
 - 为了更稳定开发的质量，dev也成为保护的分支，不允许直接push
 - 开发在各个项目的分支上开发完成后，申请使用项目测试环境， 测试通过后merge到dev上
 - 任何merge到dev上的请求，在请求前需要做另外一个成员的code review, 有条件的情况下用sonar做代码扫描，在merge完成后上开发系统的自动测试环境测试
   在测试通过后才能申请和test分支做merge
 - 在线上的产品出现需要紧急修复的bug时， 对应的开发基于当前线上产品prod分支建立hotfix分支，开发修复问题后自测过后，提交紧急测试请求给测试人员
   测试人员用hotfix分支做测试，测试完成后请郭锐(运维)做代码合并到prod分支，然后发版
   如果紧急修复牵扯到数据库更新或者回滚的，对应的开发人员设计并且在开发环境测试自己做的数据库变更脚本，验证后请测试人员在测试环境验证，如果没有问题，
   提交郭锐(运维)做线上数据的备份后然后执行这次发布需要做的数据库变动。
   以上数据属于静态数据的话可以简化流程。
15. git flow 流程
 - Git安装：地址：https://git-for-windows.github.io/
 - git flow安装：
   下载util-linux package，地址：http://gnuwin32.sourceforge.net/packages/util-linux-ng.htm，然后把getopt.exe 放到C:\Program Files\Git\bin，
   下载libintl和libiconv，地址：http://gnuwin32.sourceforge.net/packages/libintl.htm，http://gnuwin32.sourceforge.net/packages/libiconv.htm，
   然后把libint13.dll、libiconv2.dll到Git目录的bin下面。
   然后$ git clone --recursive git://github.com/nvie/gitflow.git
 - 以管理员权限执行cmd （打开Windows的cmd窗口执行），进入刚才clone出来的gitflow下的contrib目录，执行msysgit-install.cmd，如果出错，后面加上git的安装目录作为参数：
   msysgit-install.cmd "C:\Program Files (x86)\Git"
 - 安装完成后，可以在git bash运行 git flow help查看是否安装成功
 - 初始化：执行 git flow init，基本都一直回车就行
 - git flow分支模型：http://nvie.com/posts/a-successful-git-branching-model/
 - master：master分支只有一个。 
   master分支上的代码总是稳定的，随时可以发布出去。 
   平时一般不在master分支上操作，当release分支和hotfix分支合并代码到master分支上时，master上代码才更新。 
   当仓库创建时，master分支会自己创建
 - develop：develop分支只有一个。 
   新特性的开发是基于develop分支的，但不直接在develop分支上开发，特性的开发是在feature分支上进行。 
   当develop分支上的特性足够多以至于可以进行新版本的发布时，可以创建release分支的。
 - feature：可以同时存在多个feature分支，新特性的开发正是在此分支上面。 
   可以对每个新特性创建一个新的feature分支，当该特性开发完毕，将此feature分支合并到develop分支。 
   创建一个新的feature分支，可以使用以下命令：git flow feature start test，执行以下命令后，feature/test分支会被创建。 
   当特性开发完毕，需要将此分支合并到develop分支，可以使用以下命令实现：git flow feature finish test，
   上面的命令会将feature/test分支的内容merge到develop分支，并将feature/test分支删除。
   feature分支只是存在于本地仓库，如果需要多个人共同开发此特性，也可以将feature分支推送到过程仓库，命令：git flow feature publish test
 - release：当完成了特性的开发，并且将feature分支上的内容merge到develop分支上，这时可以开始着手准备新版本的发布，release分支正是作为发布而开设的分支。 
   release分支基于develop分支，在同一时间只有一个release分支，其生命周期较短，只是为了发布而使用，在其上测试。这意味着，在release分支上，只是进行较少代码修改，
   比如bug的修复，原有功能的完善等。不允许在release分支增加大的功能，因为这样会导致release分支的不稳定，不利于发布的进行。 
   当release分支（例如，v.1.0）被创建出来后，develop分支可能正准备另一版本（例如，v.2.0），因此，当release分支merge回develop分支时，
   可能会出现冲突，需要手工解决冲突才能继续merge。通过以下命令来创建release分支：git flow release start v.1.0，
   执行过完上面的命令，release分支release/v.1.0会被创建出来 ，并且切换到该分支。
   当完成release分支功能的完善或者bug的修复后，执行以下命令来完成release分支：git flow release finish v.1.0，
   这个命令会执行以下的操作：分支release/v.1.0 merge回master分支，使用release/v.1.0分支名称打tag，分支release/v.1.0 merge回develop分支，删除release/v.1.0分支
 - hotfix：当发现master分支出现一个需要紧急修复的bug，可以使用hotfix分支。hotfix分支基于master分支，用来修复bug，当完成bug的修复工作后，会将其merge回master分支和develop分支。 
   同一时间只有一个hotfix分支，其生命周期较短。可以使用以下命令来创建hotfix分支：git flow hotfix start v.1.0，使用以下命令来结束hotfix分支的生命周期：
   git flow hotfix finish v.1.0，这句命令会将hotfix分支merge到master分支和develop分支，并删除该hotfix分支。 
 - git flow流程控制：由各个小组的leader去start和finish各个相关分支，developer只能在相应的分支上进行push和pull，不可以执行start和finish命令。developer在自己的future
   上开发，自测通过后，由leader执行finish命令merge回develop分支，然后进行集成测试，测试通过后，由leader start一个release分支，给测试人员进行测试，如果有bug，开发人员在
   release分支上进行修复，验证完毕后，product ower说可以发版后，由leader finish release分支，然后线上发此release的tag。线上是否需要hotfix，也由product owner决定，
   然后相应的leader start一个hotfix分支，developer在hotfix分支上修复bug后，经过测试验证和product owner同意后，由leader 执行finish操作。
 - 注意事项：git flow 在自动merge的时候都是现在本地进行，要自己push到remote 分支上。同一时间只能有一个release和hotfix分支，当hotfix时当前已经存在一个release分支时，需要
   手动将hotfix merge回此release分支。
 - 分支命名规范：前缀+tapd号+年月日时分，如hotfix-1000335-201805221628，另外dev分支需要改成默认的develop分支。


### 项目端口规范约定
* aaas-api-asset-allocation 
  - 自身 10020
  - GRPC 10021
* aaas-api-data-manager
  - 自身 10030
  - GRPC 10031
* aaas-api-gateway
  - 自身 10040
  - GRPC 10041
* aaas-api-risk-assessment
  - 自身 10050
  - GRPC 10051
* aaas-api-user-info
  - 自身 10060
  - GRPC 10061
* aaas-api-user-login
  - 自身 10070
  - GRPC 10071
* aaas-app-datacollection-client
  - 自身 10080
  - GRPC 10081
* aaas-funds-datacollection-server
  - 自身 10090
  - GRPC 10091
* aaas-finance-trade-order
  - 自身 10100
  - GRPC 10101
* aaas-finance-trade-pay
  - 自身 10110
  - GRPC 10111 
* aaas-api-finance
  - 自身 10120
  - GRPC 10121 
* mid-api
  - 自身 10130
  - GRPC 10131
* mid-apii -- 外包
  - 自身 10140
  - GRPC 10141
* aaas-app-checkfunds 自动更新10数据库的基金和组合基本信息
  - 自身 10150
  - GRPC 10151
* aaas-app-oeminfo 多银行的基础信息提供模块
  - 自身 10160
  - GRPC 10161   
* aaas-app-zhongzhengapi 中证接口模块
  - 自身 10170
  - GRPC 10171    
* rabbitmq: 5672
* reddit: 6379

### 项目profile约定
 - 所有项目开发的profile 用application-dev.yml
 - 所有项目测试的profile 用application-test.yml
 - 所有项目上线的profile 用application-prod.yml
 
 
### 项目资源
* 1 发布平台

** jenkins

http://192.168.1.15:7015/ (主)
http://192.168.1.10:7015/（备用）
(账户名/密码请查收邮件，务必更改密码。)
阿里云内测数据库账户密码：
1、ID: sa   AD:47.96.179.8 端口:3306 密码：N4eTVr89lX9BaPcx   
拥有权限1：select,insert,update,create,index,References 。
对应数据库包括： ssflogin  ssftrdorder   ssftrdpay   ssfuser  
拥有权限2： select  对应数据库  choice 
2、ID: ssf08    AD:47.96.179.8  密码： t5MqqozQDNramNHh   
拥有权限为：select,insert,update,create,index,References    
对应数据库 ： choice
(阿里云填写数据库地址统一为：localhost)
闪尖联调数据库账户密码：
1、ID: sa   AD:47.96.164.161 端口:3306 密码：N4eTVr89lX9BaPcx   
拥有权限1：select,insert,update,create,index,References 。
对应数据库包括： ssffinance     ssflogin  ssftrdorder   ssftrdpay   ssfuser  
拥有权限2： select  对应数据库  choice 
2、ID: ssf08    AD:47.96.164.161  密码： t5MqqozQDNramNHh   
拥有权限为：select,insert,update,create,index,References    
对应数据库 ： choice
(阿里云填写数据库地址统一为：localhost)
内网开发数据库
1、ID: sa   AD:192.168.1.10 端口:3306 密码：N4eTVr89lX9BaPcx
拥有权限1：select,insert,update,create,index,References 。
对应数据库包括： ssffinance     ssflogin  ssftrdorder   ssftrdpay   ssfuser  
拥有权限2： select  对应数据库  choice 
2、ID: ssf08    AD:192.168.1.10  密码： t5MqqozQDNramNHh   
 拥有权限为：select,insert,update,create,index,References    
对应数据库 ： choice

打包对于名称如下：   
- aaas-api-asset-allocation-1.0.0.jar
- aaas-api-data-manager.jar
- aaas-funds-datacollection-server-1.0.0.jar
- aaas-api-finance-1.0.0.jar
- aaas-finance-trade-order-1.0.0.jar
- aaas-finance-trade-pay-1.0.0.jar
- aaas-api-gateway-0.0.1-SNAPSHOT.jar
- mid-api-0.0.1-SNAPSHOT.jar
- aaas-api-risk-assessment-0.0.1-SNAPSHOT.jar
- aaas-api-user-info-1.0.0.jar
- aaas-api-user-login.jar


## mvn 私服配置文件

http://192.168.1.249:4999/index.php?s=/2

## 贝贝鱼环境信息

http://192.168.1.249:4999/index.php?s=/1&page_id=34

## 数据库设计军规
  先定义一些通用缩写
 1. user info - uinfo
 2. trade - trd
 3. order - ord
 4. finance - fin
关系型数据库的表设计：
- 库名缩写_模块名缩写_表名 或者 模块名缩写_表名
- 其他请参考： http://192.168.1.249:4999/index.php?s=/10&page_id=43