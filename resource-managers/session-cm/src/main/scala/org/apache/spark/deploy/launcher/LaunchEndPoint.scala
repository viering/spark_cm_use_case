package org.apache.spark.deploy.launcher

import event_lang.EndPoint
import event_lang.dsl.AbstractChannelImp
import event_lang.network.netty.EPAddr
import event_lang.semantic.OperationalSemantic
import fastparse.NoWhitespace._
import fastparse._
import org.apache.spark.deploy.Master.Messages.{AbstractDriver, WorkerData}
import org.apache.spark.deploy.Master.{MasterRPCEndPoint, SessionMaster, StateM}
import org.apache.spark.deploy.ZkMockup.{SessionZK, StateZK}
import org.apache.spark.deploy.types.SesCMPaperTypes.M.__EPType_M
import org.apache.spark.deploy.types.SesCMPaperTypes.W.__EPType_W
import org.apache.spark.deploy.types.SesCMPaperTypes.{M, ZK}
import org.apache.spark.deploy.worker.{SessionWorker, StateW, WorkerRCPEndPoint}
import org.apache.spark.rpc.RpcEnv
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}


object LaunchEndPoint {

  def main(args: Array[String]): Unit = {


    def number[_: P]: P[Int] = P(CharIn("0-9").rep(1).!.map(_.toInt))

    def sID[_: P]: P[String] = P(CharIn("0-9a-zA-Z_")).rep(1).!

    def aWData[_: P]: P[(String, WorkerData)] = P("[" ~ sID ~ "," ~ number ~ "," ~ number ~ "]").map(x => (x._1, WorkerData(x._2, x._3)))

    def workerData[_: P]: P[Map[String, WorkerData]] = P("{" ~ aWData.rep(sep = ","./) ~ "}").map(x => x.toMap)

    //
    //    val t: Parsed[Map[Int, WorkerData]] = parse("{[1,2,2],[2,3,4]}", workerData(_))
    //    println(t.get.value)
    //    sys.exit(-1)


    def parseMArgs(args: List[String]): Map[String, WorkerData] = {
      assert(args.length == 1)
      val t: Parsed[Map[String, WorkerData]] = parse(args.head, workerData(_))
      println(t.get.value)
      t.get.value
    }

    args.toList match {
      case myIP :: bIP :: bPort :: name :: port :: webPort :: kind :: kArgs =>
        val rpcEnv = prepStart(name, Utils.localHostName(), port.toInt, webPort.toInt)

        val os = kind.toLowerCase match {
          case k if k == "master" || k == "m" =>
            startMaster(bIP, bPort.toInt, myIP, port.toInt, webPort.toInt, name,  parseMArgs(kArgs))
          //            val ep = new SessionMaster(new MasterRPCEndPoint(rpcEnv), parseMArgs(kArgs))
          //            val os = EndPoint.createEPusingNetty(ep, EPAddr(bIP, bPort.toInt), myIP, Some("m"))
          //            ep.mRPC.network = os.network
          //            os
          case k if k == "zookeeper" || k == "zk" =>
            startZk(bIP, bPort.toInt, myIP, port.toInt, webPort.toInt, name, Utils.localHostName())
          //            val ep = new SessionZK()
          //            EndPoint.createEPusingNetty(ep, EPAddr(bIP, bPort.toInt), myIP, Some("zk"))
          case k if k == "worker" || k == "w" =>
            startWorker(bIP, bPort.toInt, myIP, port.toInt, webPort.toInt, name, Utils.localHostName())
          //            val cID = kArgs.head
          //            val ep = new SessionWorker(new WorkerRCPEndPoint(rpcEnv))
          //            EndPoint.createEPusingNetty(ep, EPAddr(bIP, bPort.toInt), myIP, Some(cID))
        }
      //        os.debugID = 0
      //        os.start()
    }
  }

  /**
   * partial copy from spark
   * Start the Master and return a three tuple of:
   * (1) The Master RpcEnv
   * (2) The web UI bound port
   * (3) The REST server bound port, if any
   */
  private def startRpcEnvAndEndpoint(name: String,
                                     host: String,
                                     port: Int,
                                     webUiPort: Int,
                                     conf: SparkConf): RpcEnv = {
    val securityMgr = new SecurityManager(conf)
    val rpcEnv = RpcEnv.create(name, host, port, conf, securityMgr)
    rpcEnv
  }

  private def prepStart(name: String,
                        host: String,
                        port: Int,
                        webPort: Int): RpcEnv = {
    val conf = new SparkConf
    conf.set("SPARK_SCALA_VERSION", "2.12")
    val rpcEnv = startRpcEnvAndEndpoint(name, host, port, webPort, conf)
    rpcEnv
  }


  def startMaster(bootstrapIP: String, bootstrapPort: Int, myIP: String, myPort: Int, myWebPort: Int, myName: String, workerInfo: Map[String, WorkerData], appsToRun: Seq[AbstractDriver] = Seq(), waitForNewApps : Boolean = true, customCode: (__EPType_M, StateM, AbstractChannelImp) => Unit = (a, b, c) => {}): (OperationalSemantic[M.__EPType_M, StateM], Thread) = {

    var os: OperationalSemantic[M.__EPType_M, StateM] = null
    val th = new Thread(new Runnable {
      def run(): Unit = {
        val rpcEnv = prepStart("master", myIP, myPort, myWebPort)
        val s = StateM(__workerInfo = workerInfo, appsToRun = appsToRun)
        val ep = new SessionMaster(new MasterRPCEndPoint(rpcEnv,waitForDeployMessages = waitForNewApps), s, customCode = customCode)
        println(s"[StartMaster] Start Netty")
        os = EndPoint.createEPusingNetty(ep, EPAddr(bootstrapIP, bootstrapPort), myIP)
        println(s"[StartMaster] Start Semantic (${os.spawnMain})")
        ep.mRPC.network = os.network
        os.start()
        os.debugID = 0
      }
    })
    th.start()
    (os, th)
  }

  def startWorker(bootstrapIP: String, bootstrapPort: Int, myIP: String, myPort: Int, myWebPort: Int, myName: String, localHostName: String, outAppDir: Option[String] = None,customCode: (__EPType_W, StateW, AbstractChannelImp) => Unit = (a, b, c) => {}): (OperationalSemantic[__EPType_W, StateW], Thread) = {

    var os: OperationalSemantic[__EPType_W, StateW] = null
    val th = new Thread(new Runnable {
      def run(): Unit = {
        val rpcEnv = prepStart(myName, localHostName, myPort, myWebPort)
        val ep = new SessionWorker(new WorkerRCPEndPoint(rpcEnv), outAppPath = outAppDir.getOrElse("resource-managers/session-cm/output/"),customCode=customCode)
        println(s"[StartWorker] Start Netty")
        os = EndPoint.createEPusingNetty(ep, EPAddr(bootstrapIP, bootstrapPort), myIP, Some(myName))
        os.debugID = 0
        println(s"[StartWorker] Start (${os.spawnMain})")
        os.start()
      }
    })
    th.start()
    (os, th)
  }

  def startZk(bootstrapIP: String, bootstrapPort: Int, myIP: String, myPort: Int, myWebPort: Int, myName: String, localHostName: String): (OperationalSemantic[ZK.__EPType_ZK, StateZK], Thread) = {
    var os: OperationalSemantic[ZK.__EPType_ZK, StateZK] = null
    val th = new Thread(new Runnable {
      def run(): Unit = {
        val ep = new SessionZK()
        println(s"[StartZk] Start Netty")
        os = EndPoint.createEPusingNetty(ep, EPAddr(bootstrapIP, bootstrapPort), myIP, Some(myName))
        os.debugID = 0
        println(s"[StartZk] Start (${os.spawnMain})")
        os.start()
      }
    })
    th.start()
    (os, th)
  }
}
