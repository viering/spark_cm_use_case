#!/bin/bash
numIter="${1:-3}"
startQ="${2:-1}"
endQ="${3:-2}"
mvn exec:java -Dexec.mainClass="org.apache.spark.deploy.sparkLauncher.SparkBench" \
	-Dexec.args="--class main.scala.TpchQuery \
	--master spark://127.0.0.1:7077 \
	--deploy-mode client --driver-memory 512M \
	--executor-memory 512M \
	--total-executor-cores 1 \
	--start-query ${startQ} \
	--end-query ${endQ} \
	--num-iter ${numIter} \
	--sub-path /code/git/spark_cm_use_case/bin/spark-submit \
	--appjar \
		/code/git/spark_cm_use_case/tpch-spark/target/scala-2.12/spark-tpc-h-queries_2.12-1.0.jar \
		/code/git/spark_cm_use_case/tpch-spark/dbgen"