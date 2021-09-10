package org.apache.spark.deploy.sessioncm

import org.apache.spark.SparkConf
import org.apache.spark.deploy.{JavaMainApplication, SparkApplication}
import org.apache.spark.scheduler.ExternalClusterManager
import org.apache.spark.util.Utils

import java.util.ServiceLoader
import scala.collection.JavaConverters.iterableAsScalaIterableConverter

object Main {
  def main(args : Array[String]) : Unit ={

    val jarLocation = "/home/me/gitProjects/spark_cm_use_case/examples/target/original-spark-examples_2.12-3.0.1.jar"

    val mainClassName = "org.apache.spark.examples.SparkPi"

    val useOurs = true

    val conf =  new SparkConf()

    if(useOurs) {
      conf.set("spark.master", "sessioncm://127.0.0.1")
      conf.set("spark.app.name", mainClassName)
      conf.setJars(Seq("/home/me/gitProjects/spark_cm_use_case/resource-managers/session-cm/target/spark-session-cm_2.12-3.0.1.jar"))
      conf.set("spark.cores.max", "1");
    }else{
      conf.set("spark.master", "local[2]")
      conf.set("spark.app.name", mainClassName)
      conf.setJars(Seq("/home/me/gitProjects/spark_cm_use_case/resource-managers/session-cm/target/spark-session-cm_2.12-3.0.1.jar"))
      conf.set("spark.cores.max", "1");
    }
    var mainClass: Class[_] = null

    mainClass = Utils.classForName(mainClassName)

    val loader = Utils.getContextOrSparkClassLoader
    val serviceLoaders =
      ServiceLoader.load(classOf[ExternalClusterManager], loader).asScala

    val d =  Utils.classForName("org.apache.spark.deploy.sessioncm.SessionCM")
    val mainMethod = d.getMethod("canCreate", "".getClass)


    val sadf = ServiceLoader.load(classOf[ExternalClusterManager],Thread.currentThread().getContextClassLoader)

    println("ss")

    val app: SparkApplication =
      new JavaMainApplication(mainClass)


    app.start(Array("10"), conf)

  }
}
