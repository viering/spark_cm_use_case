#!/usr/bin/zsh

echo "~/git/spark_cm_use_case/sbin/start-master.sh -h 10.167.6.100 -p 7077"| ssh sgx-server1
echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 6 -m 12000m"| ssh sgx-server2
echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 6 -m 12000m"| ssh sgx-server3
