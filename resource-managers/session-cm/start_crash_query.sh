#!/bin/bash
START=$(date +%s.%N)

/home/dspadmin/git/spark_cm_use_case/bin/spark-submit \
	--class "main.scala.TpchQuery" \
	--master spark://10.167.6.100:7077 \
	--deploy-mode client \
	--driver-memory 12000M \
	--executor-memory 10000M \
	--total-executor-cores 6 \
	--executor-cores 6 \
	/home/dspadmin/git/spark_cm_use_case/tpch-spark/target/scala-2.12/spark-tpc-h-queries_2.12-1.0.jar \
	/home/dspadmin/git/spark_cm_use_case/tpch-spark/dbgen 18 \
	> eval_out_failure.txt 2>&1 

END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo $(date) >> eval_time.txt
echo $DIFF >> eval_time.txt