package org.apache.spark.deploy.sparkLauncher

import scala.sys.process.Process

object SparkBench {
  def main(args: Array[String]): Unit = {

    //    val aa = """driver-\d+-\d+""".r
    //    val ll =  """"submissionId" : "driver-20160623083722-0026""""
    //    val id = aa.findFirstIn(ll)
    //    println(id)

    println(s"SparkBench app -- args: \n${args.mkString(",")}")
    var clazz = "main.scala.TpchQuery"
    var master = "spark://10.167.6.100:7077"
    var deploymode = "client"
    var dMem = "1024M"
    var eMem = "1024M"
    var tCores = 2
    var appJar = "/home/dspadmin/git/spark_cm_use_case/tpch-spark/target/scala-2.12/spark-tpc-h-queries_2.12-1.0.jar "
    var appArgs = Seq("/home/dspadmin/git/spark_cm_use_case/tpch-spark/dbgen")
    var sQuery = 1
    var eQuery = 1
    var numIter = 2
    var submitPath = "/home/dspadmin/git/spark_cm_use_case/bin/spark-submit "
    var pollForComp = false

    def parseRest(as: List[String]): Unit = {
      //          println(as)
      as match {
        case op :: a :: tail if op == ("--class") =>
          clazz = a
          parseRest(tail)
        case op :: a :: tail if op == ("--master") =>
          master = a
          parseRest(tail)
        case op :: a :: tail if op == ("--deploy-mode") =>
          deploymode = a
          parseRest(tail)
        case op :: a :: tail if op == ("--driver-memory") =>
          dMem = a
          parseRest(tail)
        case op :: a :: tail if op == ("--executor-memory") =>
          eMem = a
          parseRest(tail)
        case op :: a :: tail if op == ("--total-executor-cores") =>
          tCores = a.toInt
          parseRest(tail)
        case op :: a :: tail if op == ("--start-query") =>
          sQuery = a.toInt
          parseRest(tail)
        case op :: a :: tail if op == ("--end-query") =>
          eQuery = a.toInt
          parseRest(tail)
        case op :: a :: tail if op == ("--num-iter") =>
          numIter = a.toInt
          parseRest(tail)
        case op :: a :: tail if op == ("--sub-path") =>
          submitPath = a + " "
          parseRest(tail)
        case op :: a :: tail if op == "--poll-for-completion" =>
          pollForComp = a.toBoolean
          parseRest(tail)
        case op :: aJar :: aArgs if op == "--appjar" =>
          appJar = aJar
          appArgs = aArgs.toSeq
        case _ =>
          println(s"could not parse $as")
      }
    }
    val argsNoSpaces = args.toList.map(_.trim)
//    argsNoSpaces.zipWithIndex.foreach( vi => {
//      println(s"${vi._2} -> ${vi._1}")
//    })
    parseRest(argsNoSpaces.toList)
    val numQs = eQuery - sQuery + 1
    val res = Array.ofDim[Long](numQs, numIter)
    //Main benchmark loop (first iterated over queries then iterate over number of iterations)
    for (qIdx <- 0 until numQs) { //qIdx is uses as sQuery (index of start query) + qIdx
      for (i <- 0 until numIter) {
        //build command to start the spark application (which will connect to the SparkCM)
        val cmdStr = s"$submitPath --class $clazz --master $master --deploy-mode $deploymode --driver-memory $dMem --executor-memory $eMem --total-executor-cores $tCores $appJar ${appArgs.mkString(" ")} ${qIdx + sQuery}"
        println(s"\n\nqIdx: $qIdx, i: $i, cmd: $cmdStr\n")
        //start time
        val sT = System.nanoTime()
        //Start Spark app and wait for completion
        val output = Process(cmdStr).!!
        //Stop time
        val eT = System.nanoTime()
        res(qIdx)(i) = (eT - sT)
      }
      //Print partial result (all timing measured so far)
      println(s"Partial result: ")
      println((sQuery to eQuery).map(x => s"Q$x").mkString(";"))
      res.transpose.foreach(x => println(x.mkString(";")))
      println("\n\n")
    }
    //Finish benchmark and now print final result
    println(s"Times: ")
    println((sQuery to eQuery).map(x => s"Q$x").mkString(";"))
    res.transpose.foreach(x => println(x.mkString(";")))
    println("\n\n")
  }
}
