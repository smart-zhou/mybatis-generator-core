#!/usr/bin/env bash
PATH=$PATH
if [ "$JAVA_HOME" != "" ]; then
export PATH=${JAVA_HOME}/bin:$PATH
else
echo
#     exit 1
fi

rm -rf export/*

RUN_HOME=/
CLASSPATH=$CLASSPATH:$RUN_HOME/lib/classes12.jar
CLASSPATH=$CLASSPATH:$RUN_HOME/lib/ojdbc6-11g.jar
CLASSPATH=$CLASSPATH:$RUN_HOME/lib/sqljdbc-4.jar
CLASSPATH=$CLASSPATH:$RUN_HOME/lib/mysql-connector-java-5.1.18-bin.jar
CLASSPATH=$CLASSPATH:$RUN_HOME/lib/mybatis-generator-core-1.3.6-SNAPSHOT.jar
  
export CLASSPATH=$CLASSPATH  
  
java -jar ../lib/mybatis-generator-core-1.0.0-SNAPSHOT.jar -configfile generatorConfig_mysql.xml -overwrite

echo
