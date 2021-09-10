package org.apache.spark.deploy.test

import org.apache.spark.deploy.Master.Messages.WorkerData
import org.apache.spark.deploy.launcher.{ConnectionBootstrap, LaunchEndPoint}
import org.apache.spark.deploy.types.SesCMPaperTypes.RS
import org.apache.spark.deploy.types.SesCMPaperTypes.RS.{W, ZK}
import org.apache.spark.util.Utils

object TwoSimplePiOverProv extends TestAppUtil {
  val outDir = "output/test/twosimplepioverprov"
  val roles = Map((0, zkrole), (1, mrole))
  val roleSets = Map((0, ZK), (1, RS.M), (2, W), (3, W), (4, W))
  val apps = Seq(getPiApp(), getPiApp())
  val wData = Map(("w1", WorkerData(1, 1024)), ("w2", WorkerData(1, 1024)), ("w3", WorkerData(1, 1024)))
  val B_PORT = getBootPort()

  def main(args: Array[String]): Unit = {


    val boot = ConnectionBootstrap.bootstrap(B_IP, B_PORT, roles, roleSets)
    boot.start()
    println("bootstrab started")
    val eps = Seq(
      LaunchEndPoint.startMaster(B_IP, B_PORT, pConf.getString("sessioncm.master_ip"), pConf.getInt("sessioncm.master_port"), getWebPort(), "m", wData, appsToRun = apps, waitForNewApps = false),
      LaunchEndPoint.startZk(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "zk", Utils.localHostName()),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w1", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir)),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w2", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir)),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w3", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir))
    )
    println("eps created")

    eps.foreach(_._2.join())

    println("\n\n\nDONE\n\n\n")
//
//    testPI(new File(outDir), 2)

    sys.exit(0)
  }
}
