package org.apache.spark.deploy

import com.typesafe.config.ConfigFactory
import event_lang.EndPoint
import event_lang.network.SpawnMain
import event_lang.network.netty.EPAddr
import event_lang.semantic.OperationalSemantic
import event_lang.types.{Role, RoleSet}
import org.apache.spark.deploy.Master.Messages.{SparkData, WorkerData}
import org.apache.spark.deploy.Master.{MasterRPCEndPoint, SessionMaster, StateM}
import org.apache.spark.deploy.ZkMockup.SessionZK
import org.apache.spark.deploy.launcher.Util.createSimpleApp
import org.apache.spark.deploy.types.SesCMPaperTypes.M.PExSchedule_m_M
import org.apache.spark.deploy.types.SesCMPaperTypes.RS._
import org.apache.spark.deploy.types.SesCMPaperTypes.{M, RS}
import org.apache.spark.deploy.worker.{SessionWorker, WorkerRCPEndPoint}
import org.apache.spark.rpc.RpcEnv
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}


object Bootstrap {
  def main(args: Array[String]): Unit = {
    println(Utils.localHostName())
    val pConf = ConfigFactory.load()
    val mIP = pConf.getString("sessioncm.master_ip")
    val mPort = pConf.getInt("sessioncm.master_port")
    val DEFAULT_BOOTSTRAP_ADDR = EPAddr(mIP, 22688)

    val zkrole = Role("zk", ZK)
    val mrole = Role("m", RS.M)

    val mainSes = SpawnMain(0, 0, Map((0, zkrole), (1, mrole)), Map((0, ZK), (1, RS.M), (2, W), (3, W), (4, W), (5, W)))

    (new Thread(new Runnable {
      override def run(): Unit = {
        EndPoint.createNettyConnectionBootstrapManger(DEFAULT_BOOTSTRAP_ADDR, mainSes)
      }
    })).start()


    /**
     * partial copy from spark
     * Start the Master and return a three tuple of:
     * (1) The Master RpcEnv
     * (2) The web UI bound port
     * (3) The REST server bound port, if any
     */
    def startRpcEnvAndEndpoint(name: String,
                               host: String,
                               port: Int,
                               webUiPort: Int,
                               conf: SparkConf): RpcEnv = {
      val securityMgr = new SecurityManager(conf)
      val rpcEnv = RpcEnv.create(name, host, port, conf, securityMgr)
      rpcEnv
    }

    val simulate_crash = false
    val mName = "master"
    val w1Name = "w1"
    val w2Name = "w2"
    val w3Name = "w3"
    val w4Name = "w4"
    val conf = new SparkConf
    conf.set("SPARK_SCALA_VERSION", "2.12")


    val rpcEnvM = startRpcEnvAndEndpoint(mName, mIP, mPort, 8080, conf)
    val rpcEnvW1 = startRpcEnvAndEndpoint(w1Name, mIP, 8013, 8082, conf)
    val rpcEnvW2 = startRpcEnvAndEndpoint(w2Name, mIP, 8014, 8083, conf)
    val rpcEnvW3 = startRpcEnvAndEndpoint(w3Name, mIP, 8015, 8084, conf)
    val rpcEnvW4 = startRpcEnvAndEndpoint(w4Name, mIP, 8016, 8085, conf)

    val mRPC = new MasterRPCEndPoint(rpcEnvM, false)
    val wRPC1 = new WorkerRCPEndPoint(rpcEnvW1)
    val wRPC2 = new WorkerRCPEndPoint(rpcEnvW2)
    val wRPC3 = new WorkerRCPEndPoint(rpcEnvW3)
    val wRPC4 = new WorkerRCPEndPoint(rpcEnvW4)

    val zk = new Thread(new Runnable {
      override def run(): Unit = {
        val ep = new SessionZK()
        val oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP)
        oSemantic.debugID = 0
        oSemantic.start()
        println(s"\nZK DONE \n")
      }
    })
    val exPath = pConf.getString("sessioncm.app_path")


    var theApp: SparkData = null

    def parseRest(as: List[String]): Unit = {
      as match {
        case cores :: memE :: coresE :: mC :: aArges =>
          assert(cores.toInt >= coresE.toInt)
          theApp = createSimpleApp(exPath,
            mainClass = mC,
            args = aArges,
            driverCores = 1,
            menPerEx = memE.toInt,
            maxCores = cores.toInt,
            corePerEx = Some(coresE.toInt))
        case _ =>
          sys.error(s"could not parse $as")
      }
    }

    parseRest(args.toList)
    assert(theApp != null)
    //    val bQ1 = createSimpleApp(exPath,
    //      "main.scala.TpchQuery", Seq("/home/dspadmin/git/spark_cm_use_case/tpch-spark/dbgen", "1"),
    //      menPerEx = 555,
    //      maxCores = 2,
    //      corePerEx = Some(1))
    //
    //    val piApp1 = createSimpleApp(exPath,
    //      "org.apache.spark.examples.SparkPi", Seq("1"),
    //      menPerEx = 555,
    //      maxCores = 2,
    //      corePerEx = Some(1))
    //    val piApp2 = createSimpleApp(exPath,"org.apache.spark.examples.SparkPi", Seq("2"))
    //    val bCast = createSimpleApp(exPath,"org.apache.spark.examples.MultiBroadcastTest", Seq())
    var numKills = if(simulate_crash) 0 else 1
    val m = new Thread(new Runnable {
      override def run(): Unit = {
        val apps = Seq(theApp)
        var oSemantic: OperationalSemantic[M.__EPType_M, StateM] = null
        val ep = new SessionMaster(mRPC, StateM(
          appsToRun = apps,
          __workerInfo = Map(("w1", WorkerData(1, 1024)), ("w2", WorkerData(1, 1024)), ("w3", WorkerData(1, 1024)), ("w4", WorkerData(1, 1024)))),
          customCode = {
            //            case (PExecutor_m_M.RcvExStarted, s, c) if numKills == 0 =>
            case (PExSchedule_m_M.SndStartExCase, s, c) if numKills == 0 =>
              numKills += 1
              val network = oSemantic.network

              val id = c.session.roleToPId(Role("tw", RoleSet("W")))
              (new Thread(
                new Runnable {
                  override def run(): Unit = {
                    Thread.sleep(100)
                    println(s"##########\n kill $id \n######")
                    network.signalFail(id)
                  }
                }
              )).start()

            case _ =>
          })

        oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP)
        oSemantic.debugID = 0
        ep.mRPC.network = oSemantic.network
        oSemantic.start()

        println(s"\nMASTER DONE \n")
      }
    }

    )

    val w1 = new Thread(new Runnable {
      override def run(): Unit = {
        val ep = new SessionWorker(wRPC1)
        val oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP, Some("w1"))
        oSemantic.debugID = 0
        oSemantic.start()
        println(s"\nW1 DONE \n")
      }
    })

    val w2 = new Thread(new Runnable {
      override def run(): Unit = {
        val ep = new SessionWorker(wRPC2)
        val oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP, Some("w2"))
        oSemantic.debugID = 0
        oSemantic.start()
        println(s"\nW2 DONE \n")
      }
    })

    val w3 = new Thread(new Runnable {
      override def run(): Unit = {
        val ep = new SessionWorker(wRPC3)
        val oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP, Some("w3"))
        oSemantic.debugID = 0
        oSemantic.start()
        println(s"\nW3 ONE \n")
      }
    })
    val w4 = new Thread(new Runnable {
      override def run(): Unit = {
        val ep = new SessionWorker(wRPC4)
        val oSemantic = EndPoint.createEPusingNetty(ep, DEFAULT_BOOTSTRAP_ADDR, mIP, Some("w4"))
        oSemantic.debugID = 0
        oSemantic.start()
        println(s"\nW4 DONE \n")
      }
    })
    zk.start()
    m.start()
    w1.start()
    w2.start()
    w3.start()
    w4.start()
    w1.join()
    w2.join()
    w3.join()
    w4.join()
    zk.join()
    m.join()
    println("\n\n\nDONE\n\n\n")
    System.exit(0)
  }
}
