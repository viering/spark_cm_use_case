package org.apache.spark.deploy.test

import event_lang.dsl.AbstractChannelImp
import org.apache.spark.deploy.Master.Messages.WorkerData
import org.apache.spark.deploy.launcher.{ConnectionBootstrap, LaunchEndPoint}
import org.apache.spark.deploy.types.SesCMPaperTypes.RS
import org.apache.spark.deploy.types.SesCMPaperTypes.RS.{W, ZK}
import org.apache.spark.deploy.types.SesCMPaperTypes.W.{PExSchedule_tw_W, __EPType_W}
import org.apache.spark.deploy.worker.StateW
import org.apache.spark.util.Utils

import java.util.concurrent.atomic.AtomicBoolean

object PiFailureTmpWorkerInExSchedule extends TestAppUtil {
  val outDir = "output/test/failureTmpWorkerExSchedule"
  val roles = Map((0, zkrole), (1, mrole))
  val roleSets = Map((0, ZK), (1, RS.M), (2, W), (3, W), (4, W), (5, W))
  val apps = Seq(getPiApp())
  val wData = Map(("w1", WorkerData(1, 1024)), ("w2", WorkerData(1, 1024)), ("w3", WorkerData(1, 1024)), ("w4", WorkerData(1, 1024)))
  val B_PORT = getBootPort()


  def main(args: Array[String]): Unit = {

    val boot = ConnectionBootstrap.bootstrap(B_IP, B_PORT, roles, roleSets)
    boot.start()
    println("bootstrab started")

    val failed = new AtomicBoolean(false)

   val wcustomCode: (__EPType_W, StateW, AbstractChannelImp) => Unit = {
     case (PExSchedule_tw_W.RcvStartExCase,_,_) if !failed.get() =>
       failed.set(true)
       assert(false)
     case _ =>
   }


    val eps = Seq(
      LaunchEndPoint.startMaster(B_IP, B_PORT, pConf.getString("sessioncm.master_ip"), pConf.getInt("sessioncm.master_port"), getWebPort(), "m", wData, appsToRun = apps, waitForNewApps = false,customCode = {
        case _ =>
      }),
      LaunchEndPoint.startZk(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "zk", Utils.localHostName()),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w1", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir),customCode = wcustomCode),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w2", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir),customCode = wcustomCode),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w3", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir),customCode = wcustomCode),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w4", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir),customCode = wcustomCode)
    )
    println("eps created")

    eps.foreach(_._2.join())

    println("\n\n\nDONE\n\n\n")
    sys.exit(0)
  }
}
