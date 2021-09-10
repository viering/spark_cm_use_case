#!/bin/sh

# The session cm does not distribute Jars therefore copy all jars to the spark jar folder
cp -f $GIT_FOLDER/spark_cm_use_case/examples/target/original-spark-examples_2.12-3.0.1-session.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/
cp -f $HOME/.m2/repository/com/typesafe/config/1.4.0/config-1.4.0.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/
cp -f $GIT_FOLDER/spark_cm_use_case/tpch-spark/target/scala-2.12/spark-tpc-h-queries_2.12-1.0.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/

cp -f $HOME/.m2/repository/com/lihaoyi/sourcecode_2.12/0.2.1/sourcecode_2.12-0.2.1.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/
cp -f $HOME/.m2/repository/com/lihaoyi/fansi_2.12/0.2.7/fansi_2.12-0.2.7.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/
cp -f $HOME/.m2/repository/com/lihaoyi/geny_2.12/0.6.0/geny_2.12-0.6.0.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/
cp -f $HOME/.m2/repository/com/lihaoyi/pprint_2.12/0.5.6/pprint_2.12-0.5.6.jar $GIT_FOLDER/spark_cm_use_case/assembly/target/scala-2.12/jars/