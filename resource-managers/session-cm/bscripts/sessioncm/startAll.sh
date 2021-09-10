#!/usr/bin/zsh

sfolder="/home/dspadmin/git/spark_cm_use_case/resource-managers/session-cm/bscripts/sessioncm/"

server=$sfolder"startServer1.sh"
echo "nohup $server 2>1 &" | ssh sgx-server1
server=$sfolder"startServer2.sh"
echo "nohup $server 2>1 &"| ssh sgx-server2
server=$sfolder"startServer3.sh"
echo "nohup $server 2>1 &"| ssh sgx-server3
server=$sfolder"startServer4.sh"
echo "nohup $server 2>1 &"| ssh sgx-server4

