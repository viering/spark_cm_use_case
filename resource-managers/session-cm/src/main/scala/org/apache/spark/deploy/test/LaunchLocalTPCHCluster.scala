package org.apache.spark.deploy.test

import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.Master.Messages
import org.apache.spark.deploy.Master.Messages.WorkerData
import org.apache.spark.deploy.launcher.{ConnectionBootstrap, LaunchEndPoint, SubmitBenchmark}
import org.apache.spark.deploy.test.SimplePi._
import org.apache.spark.deploy.types.SesCMPaperTypes.RS
import org.apache.spark.deploy.types.SesCMPaperTypes.RS.{W, ZK}
import org.apache.spark.rpc.RpcEndpointRef
import org.apache.spark.util.Utils

object LaunchLocalTPCHCluster {
  val outDir = "output/test/tpch"
  val roles = Map((0, zkrole), (1, mrole))
  val roleSets = Map((0, ZK), (1, RS.M), (2, W), (3, W), (4, W), (5, W))
  val apps = Seq(getPiApp())
  val wData = Map(("w1", WorkerData(1, 1024)), ("w2", WorkerData(1, 1024)), ("w3", WorkerData(1, 1024)), ("w4", WorkerData(1, 1024)))
  val B_PORT = getBootPort()


  def main(args: Array[String]): Unit = {
    var startQ = 1
    var endQ = 1
    var numIter = 1
    args.toList match {
      case i :: s :: e :: Nil =>
        numIter = i.toInt
        startQ = s.toInt
        endQ = e.toInt
        assert(startQ <= endQ && startQ >= 0 && endQ <= 21)
      case _ =>
        println(s"Please provide the num Iter, start query index, and the end query index (valid indexes [1,21])")
    }
    val pConf = ConfigFactory.load()
    val remoteIP = pConf.getString("sessioncm.master_ip")
    val exPath = pConf.getString("sessioncm.app_path")
    val remotePort = pConf.getString("sessioncm.master_port")
    import java.nio.file.Paths
    println(Paths.get("../../tpch-spark/dbgen").toAbsolutePath.toString)
    val boot = ConnectionBootstrap.bootstrap(B_IP, B_PORT, roles, roleSets)
    boot.start()
    println("bootstrab started")
    val eps = Seq(
      LaunchEndPoint.startMaster(B_IP, B_PORT, pConf.getString("sessioncm.master_ip"), pConf.getInt("sessioncm.master_port"), getWebPort(), "m", wData, appsToRun = Seq(), waitForNewApps = true),
      LaunchEndPoint.startZk(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "zk", Utils.localHostName()),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w1", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir)),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w2", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir)),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w3", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir)),
      LaunchEndPoint.startWorker(B_IP, B_PORT, MY_IP, getEpPort(), getWebPort(), "w4", Utils.localHostName(), Some("resource-managers/session-cm/" + outDir))
    )
    println("eps created")
    val t = new Thread(new Runnable {
      override def run(): Unit = {


        println("STart Bench")
          SubmitBenchmark.submitBenchmark(
          myPort = 7788,
          myName = "SubBench",
          remoteIP = remoteIP,
          remotePort = remotePort,
          numIter = numIter,
          appName = "main.scala.TpchQuery",
          appArgs = Seq(Paths.get("../../tpch-spark/dbgen").toAbsolutePath.toString),
          exPath = exPath,
          startQ = startQ,
          endQ = endQ,
          cores = 1,
          menPerEx = 512,
          corePerEx = Some(1),
          driverMem = 512,
          driverCores = 1
        )
      }
    })
    t.start()

    t.join()


    sys.exit(0)


  }
}
