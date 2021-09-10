package org.apache.spark.deploy.sessioncm

import org.apache.spark.SparkConf
import org.apache.spark.deploy.{JavaMainApplication, SparkApplication}
import org.apache.spark.util.Utils

class SessionCMModeLauncher extends SparkApplication{

  override def start(args: Array[String], conf: SparkConf): Unit = {

    println("yeah first step")

    println(args.mkString(","))

    println(conf.getAll.mkString(" ; "))

    var mainClass: Class[_] = null

    mainClass = Utils.classForName(args.head)


    println(s"lunch spark app ${args.head}")
    val app: SparkApplication =
      new JavaMainApplication(mainClass)

      app.start(args.tail.toArray, conf)
  }

}


