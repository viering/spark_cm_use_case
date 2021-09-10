#!/bin/bash
killall -9 java

sleep 5s

cd ~/git/spark_cm_use_case/resource-managers/session-cm

mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.ConnectionBootstrap" -Dexec.args="10.167.6.100 4" > /dev/null 2>&1  &

sleep 5s

mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.LaunchEndPoint" -Dexec.args="10.167.6.100 10.167.6.100 22688 zk 8006 8801 zk" > /dev/null 2>&1 &

mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.LaunchEndPoint" -Dexec.args="10.167.6.100 10.167.6.100 22688 master 8007 8802 master {[w1,0,0],[w2,6,10240],[w3,6,10240],[w4,6,12240]}" > /dev/null 2>&1 &

mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.LaunchEndPoint" -Dexec.args="10.167.6.100 10.167.6.100 22688 w1 8008 8803 w w1" > /dev/null 2>&1 &
