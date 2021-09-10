#!/bin/sh
/bin/rm -rf $HOME/.m2/repository/org/me/my-project_2.12/*

cd $GIT_FOLDER/MPST_toolchain_for_fault-tolerant_distributed_EDP; git pull ; /bin/rm -rf /home/dspadmin/.m2/repository/org/me/my-project_2.12/*; sbt clean publishM2; cd $GIT_FOLDER/spark_cm_use_case/resource-managers/session-cm ; git pull; mvn clean compile


cd $GIT_FOLDER/spark_cm_use_case/tpch-spark/; git pull; sbt package; cp target/scala-2.12/spark-tpc-h-queries_2.12-1.0.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/


/bin/rm -rfv $GIT_FOLDER/spark_cm_use_case/resource-managers/session-cm/output/*


$GIT_FOLDER/spark_cm_use_case/resource-managers/session-cm/distributedJars.sh