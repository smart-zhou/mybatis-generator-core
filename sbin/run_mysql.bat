@echo off 
echo ���������ʷ�ļ������Ե�...... 
rd /s /q "%~dp0export\com
echo �����ɣ� 
echo.
echo ��������mybatis�ļ�,���Ե�......
java -jar ../lib/mybatis-generator-core-1.0.0-SNAPSHOT.jar -configfile generatorConfig_mysql.xml -overwrite

echo �������
echo. &pause