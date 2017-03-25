# generatorConfig_*.xml 说明

## 元素说明

| 父元素 | 元素名 | 元素说明 | 属性名 | 属性说明 |
| :---- | :-- |:---- | :----- | :--- |
| 无 | generatorConfiguration | 根元素 | | |
| generatorConfiguration | | | | |
| | classPathEntry | 外部jar包 | | |
| | | | location | jar包相对路径 |
| | context | 上下文环境 | | |
| | | | id | 名称 |
| | | | targetRuntime | MYBATIS3或IBATIS2 |
| context | | | | |
| | jdbcConnection | 数据库连接配置 | | |
| | | | driverClass | 数据库连接驱动 |
| | | | connectionURL | 数据库连接串 |
| | | | userId | 数据库用户名 |
| | | | password | 数据库用户密码 |
| | javaModelGenerator | DO对象模型配置 | | |
| | | | targetPackage | 对象包路径 |
| | | | targetProject | 对象文件输出路径 |
| | sqlMapGenerator | DAO对象配置 | | |
| | | | targetPackage | 对象包路径 |
| | | | targetProject | 对象文件输出路径 |
| | javaClientGenerator | xml文件配置 | | |
| | | | type | xml文件类型 |
| | | | targetPackage | DAO包路径 |
| | | | targetProject | 文件输出路径 |
| | table | 数据库表配置 | | |
| | | | tableName | 数据库表名 |
| | | | domainObjectName | 生成的DO对象名 |
| table | | |  | |
| | columnOverride | 数据表列配置 |  | |
| | | | column | 表列名 |
| | | | replaceName | 对应value的替换值 |
| | | |  | |


### 文件示例
```javascript
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <classPathEntry location="../lib/mysql-connector-java-5.1.18-bin.jar"/>
    <context id="dbName" targetRuntime="MyBatis3">

        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://172.30.248.21:3306/trc_reward?characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull"
                        userId="root" password="DYfunds@#1369">
        </jdbcConnection>

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <javaModelGenerator targetPackage="com.trc.reward.model"
                            targetProject="export">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="com.trc.reward.dao"
                         targetProject="export">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.trc.reward.dao" targetProject="export">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!-- 数据库表配置 -->
        <table tableName="reward_grant" domainObjectName="RewardGrant">
            <columnOverride column="createDate" replaceName="sysdate()"/>
        </table>

    </context>
</generatorConfiguration>
```