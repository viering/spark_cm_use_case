#!/usr/bin/zsh

#cat start_crash_query | ssh sgx-server4 &
echo "~/git/spark_cm_use_case/sbin/stop-slave.sh" | ssh sgx-server2
echo "~/git/spark_cm_use_case/sbin/stop-slave.sh" | ssh sgx-server3
sleep 5s
echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 6 -m 10000m " | ssh sgx-server2
echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 6 -m 10000m " | ssh sgx-server3
 
sleep 20s
echo "Submit Query"
cat nohup_bench_start.sh | ssh sgx-server4 &

sleep 20s

server2=`cat crash_to_blub.sh | ssh sgx-server2`
server2=`(echo $server2 | tail -1)`
echo "Time on server 2: $server2"

server3=`cat crash_to_blub.sh | ssh sgx-server3`
server3=`(echo $server3 | tail -1)`
echo "Time on server 3: $server3"

if [ $server2 -le $server3 ]; then
	echo "Kill server 2"
	cat kill_process_script.sh | ssh sgx-server2
	#sometimes the standalone master seems to hang start slave will send a message to the master which lets him progress
	echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 1 -m 10000m " | ssh sgx-server2
else
	echo "Kill server 3"
	cat kill_process_script.sh | ssh sgx-server3
	echo "~/git/spark_cm_use_case/sbin/start-slave.sh 10.167.6.100:7077 -c 1 -m 10000m " | ssh sgx-server3
fi

# ssh sgx-server1 << EOF
# echo `pwd`
# filename=`ls -Art $workPWD | tail -n 1`
# filename=$workPWD"/"$filename
# echo $(($(date +%s) - $(date +%s -r "$filename"))) seconds
# EOFs
## time=`ssh sgx-server1 | $(($(date +%s) - $(date +%s -r $workPWD'/'$(ls -Art $workPWD | tail -n 1))))`
## # 
## 
## echo $(ssh sgx-server1 | $(($(date +%s) - $(date +%s -r $workPWD'/'$(ls -Art $workPWD | tail -n 1)))))
## 
## 
## # $(($(date +%s) - $(date +%s -r $($(workPWD"\"$(ls -Art $workPWD | tail -n 1) ))))
## # 
## # 
## # echo $workPWD'/'$(ls -Art $workPWD | tail -n 1)
## # 
## # 
## # echo $(($(date +%s) - $(date +%s -r $workPWD'/'$(ls -Art $workPWD | tail -n 1))))
## #  ))))