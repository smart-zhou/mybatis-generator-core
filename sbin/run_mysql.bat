@echo off 
echo 正在清除历史文件，请稍等...... 
rd /s /q "%~dp0export\com
echo 清除完成！ 
echo.
echo 正在生成mybatis文件,请稍等......
java -jar ../lib/mybatis-generator-core-1.0.0-SNAPSHOT.jar -configfile generatorConfig_mysql.xml -overwrite

echo 生成完成
echo. &pause