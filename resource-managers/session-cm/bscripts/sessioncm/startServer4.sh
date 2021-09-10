#!/bin/bash
killall -9 java

sleep 5s

cd ~/git/spark_cm_use_case/resource-managers/session-cm

mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.LaunchEndPoint" -Dexec.args="10.167.6.103 10.167.6.100 22688 w4 8010 8805 w w4" > /dev/null 2>&1 &
