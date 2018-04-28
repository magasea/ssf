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
