## 软件开发规范
### JAVA 开发规范
我们一致按照业内标准[https://google.github.io/styleguide/javaguide.html]配置自己的ide，比如
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
