#!/usr/bin/zsh

./startAll.sh
# 
 sleep 2
# 

echo 'cd ~/git/spark_cm_use_case/resource-managers/session-cm;nohup mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.SubmitBenchmark" -Dexec.args="10.167.6.100 8900 submit 10.167.6.100 8007 -appPath /home/dspadmin/git/spark_cm_use_case/examples/target/original-spark-examples_2.12-3.0.1-session.jar -cores 6 -memEx 10000 -coresEx 6 -dMem 12000 -dCores 6 -iter 1 -startQ 18 -endQ 18  main.scala.TpchQuery /home/dspadmin/git/spark_cm_use_case/tpch-spark/dbgen" > eval.txt 2>1 &' | ssh sgx-server1
# cat nohup_bench_start.sh | ssh sgx-server4 &
# 
sleep 20s

server2=`cat lib/lastOutFileChange.sh | ssh sgx-server2`
server2=`(echo $server2 | tail -1)`
echo "Time on server 2: $server2"

server3=`cat lib/lastOutFileChange.sh | ssh sgx-server3`
server3=`(echo $server3 | tail -1)`
echo "Time on server 3: $server3"

 if [ $server2 -le $server3 ]; then
 	echo "Kill server 2"
 	echo "killall -9 java" | ssh sgx-server2
## 	echo "~/git/spark_cm_use_case/sbin/start-slave.sh .167.6.100:7077 -c 1 -m 12000m" | ssh sgx-server2
 else
 	echo "Kill server 3"
 	echo "killall -9 java" | ssh sgx-server3
## 	echo "~/git/spark_cm_use_case/sbin/start-slave.sh .167.6.100:7077 -c 1 -m 12000m" | ssh sgx-server3
 fi


echo 'cd ~/git/spark_cm_use_case/resource-managers/session-cm;nohup mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.launcher.SignalFailure" -Dexec.args="10.167.6.100 8900 submit 10.167.6.100 8007 -1" > crash.txt 2>1 &' | ssh sgx-server1