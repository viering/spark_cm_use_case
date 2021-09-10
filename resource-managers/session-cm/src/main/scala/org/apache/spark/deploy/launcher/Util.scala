package org.apache.spark.deploy.launcher

import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.{Command, DriverDescription}
import org.apache.spark.deploy.Master.Messages.SparkData

object Util {
  val conf = ConfigFactory.load()

  val SESSION_CM_MAX_CORES = "session_cm_max_cores"
  val SESSION_CM_CORES_PAIR_EX = "session_cm_cores_pair_ex"
  val SESSION_CM_MEM_P_EX =  "SPARK_EXECUTOR_MEMORY"
  def createSimpleApp(jarUrl: String, mainClass : String, args : Seq[String], driverMem: Int = 512, driverCores : Int = 1, maxCores : Int = 1, menPerEx : Int = 555, corePerEx : Option[Int] = Some(1)) : SparkData = {
    SparkData(DriverDescription(
      jarUrl = jarUrl,
      mem = driverMem,
      cores = driverCores,
      supervise = false,
      command = Command(
        mainClass = "org.apache.spark.deploy.worker.OurDriverWrapper",
        arguments = Seq("", jarUrl,mainClass) ++ args,
        environment = Map(
          ("spark.master", s"sessioncm://${conf.getString("sessioncm.master_ip")}"), //127.0.0.1"),
          ("deploy-mode", "cluster"),
          ("session-id", "asdf"),
          (SESSION_CM_MEM_P_EX,menPerEx.toString),
          (SESSION_CM_MAX_CORES,maxCores.toString),
          (SESSION_CM_CORES_PAIR_EX, corePerEx.getOrElse(-1).toString)
        ),
        classPathEntries = Seq(conf.getString("sessioncm.classpath ")),//"/home/me/gitProjects/spark_cm_use_case/resource-managers/session-cm/target/scala-2.12/classes/"),
        libraryPathEntries = Seq(),
        javaOpts = Seq()
      )),maxCores,menPerEx,corePerEx)
  }

}
