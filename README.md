# mybatis-generator-core

### 背景说明

* 平时工作中用到MyBatis框架访问数据库,就需要自动生成相应的代码及文件.使用的同事发给我的工具(不知出处),里面有mybatis-generator的jar包.
* 找到一处源码编译后,替换上面工具中的对应jar包,并修改bat文件,完全可以适配.就在这个源码上做修改以适应代码的需求.
* 源码出处:https://github.com/mybatis/generator 里面的mybatis-generator-core部分.在此要感谢代码原作者!

### 代码版本

* 初版代码定为1.0.0
  * 增加了bat和sh文件,使其可以在windows和linux下使用.
  * 修改生成的insert语句,增加配置使指定列的value为配置的值.并增加select条件查询返回对应DO的List.

### 使用说明

* 修改sbin目录下的generatorConfig_mysql.xml
  * 修改数据库连接信息
  * 修改数据库表配置信息,需要生成哪些表对应的代码就配置哪些表
* 用Maven打包会生成一个 *-bundle.zip 文件.解压后进入sbin目录
* 选择对应的执行文件,windows下执行run_mysql.bat,linux下执行run_mysql.sh

### 调试说明

* 修改sbin目录下的generatorConfig_mysql.xml
  * 修改数据库连接信息
  * 修改数据库表配置信息,需要生成哪些表对应的代码就配置哪些表
* 增加运行参数 -configfile generatorConfig_mysql.xml -t -overwrite
* 运行 ShellRunner里的main方法,要调试可以直接打断点
