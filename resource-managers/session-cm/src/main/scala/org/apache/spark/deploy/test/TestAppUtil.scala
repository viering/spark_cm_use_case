package org.apache.spark.deploy.test

import com.typesafe.config.ConfigFactory
import event_lang.types.Role
import org.apache.spark.deploy.Master.Messages.SparkData
import org.apache.spark.deploy.launcher.Util.createSimpleApp
import org.apache.spark.deploy.types.SesCMPaperTypes.RS
import org.apache.spark.deploy.types.SesCMPaperTypes.RS.ZK

class TestAppUtil {
  val pConf = ConfigFactory.load()
  val B_IP = "127.0.0.1"

  val zkrole = Role("zk", ZK)
  val mrole = Role("m", RS.M)

  val MY_IP = "127.0.0.1"

  var __webPort = 22800
  var __epPort = 9000
  var __bootPort = 22688

  def getBootPort(): Int = {
    __bootPort += 1
    __bootPort
  }

  def getWebPort(): Int = {
    __webPort += 1
    __webPort
  }


  def getEpPort(): Int = {
    __epPort += 1
    __epPort
  }

  def getPiApp(driverMem: Int = 512, driverCores: Int = 1, maxCores: Int = 1, menPerEx: Int = 555, corePerEx: Option[Int] = Some(1)): SparkData = {
    createSimpleApp(pConf.getString("sessioncm.app_path"), "org.apache.spark.examples.SparkPi", Seq("2"),
      driverMem = driverMem,
      driverCores = driverCores,
      maxCores = maxCores,
      menPerEx = menPerEx,
      corePerEx = corePerEx)
  }

  def getTcApp(driverMem: Int = 512, driverCores: Int = 1, maxCores: Int = 1, menPerEx: Int = 555, corePerEx: Option[Int] = Some(1)): SparkData = {
    createSimpleApp(pConf.getString("sessioncm.app_path"),
      "org.apache.spark.examples.SparkTC",
      Seq("2"),
      driverMem = driverMem,
      driverCores = driverCores,
      maxCores = maxCores,
      menPerEx = menPerEx,
      corePerEx = corePerEx)
  }

}
