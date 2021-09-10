package org.apache.spark.deploy.worker

import com.softwaremill.quicklens._
import com.typesafe.config.ConfigFactory
import event_lang.dsl.{AbstractChannelImp, AbstractEndPointTesting, HDL, TState}
import event_lang.semantic.CommonTypes.SessionID
import org.apache.spark.deploy.DeployMessages.{DriverStateChanged, ExecutorStateChanged}
import org.apache.spark.deploy.ExecutorState.ExecutorState
import org.apache.spark.deploy.Master.Messages._
import org.apache.spark.deploy.intern.logging.Logger
import org.apache.spark.deploy.master.DriverState
import org.apache.spark.deploy.master.DriverState.DriverState
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PDriver
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PDriver.AckNStatus
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PExecutor._
import org.apache.spark.deploy.types.SesCMPaperTypes.W._
import org.apache.spark.deploy.{DriverDescription, ExecutorState}
import org.apache.spark.rpc._
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}
import pprint.pprintln

import java.io.{File, IOException}
import scala.concurrent.ExecutionContext

case class PDriverState(appId: AppId, driver: DriverDescription, driverCalc: Thread)

case class WGHdlExStartState(parId: SessionID)

case class WGExWorkerState(appId: AppId, exId: ExId, exCalc: Thread)

case class WGExWorker_wState(driver: AbstractDriver)

case class StateW(driverState: Map[SessionID, PDriverState] = Map(),
                  //                 hdlStartState : Map[SessionID,WGHdlExStartState] = Map(),
                  exState: Map[SessionID, WGExWorkerState] = Map()
                  //                  exState_w: Map[SessionID, WGExWorker_wState] = Map(),
                 ) extends TState {
}


class WorkerRCPEndPoint(val rpcEnv: RpcEnv) extends ThreadSafeRpcEndpoint with Logger{


  val endpointName = "w-rpc-ep"

  val epRef: RpcEndpointRef = rpcEnv.setupEndpoint("wEP", this)
  val wRpcURL = RpcEndpointAddress(rpcEnv.address, endpointName).toString

  var exStatus: Map[ExId, ExecutorState] = Map()
  var driverStatus: Map[AppId, DriverState] = Map()

  /**
   * Process messages from `RpcEndpointRef.send` or `RpcCallContext.reply`. If receiving a
   * unmatched message, `SparkException` will be thrown and sent to `onError`.
   */
  override def receive: PartialFunction[Any, Unit] = {
    case m =>
      debug(s"[WorkerRPC] received $m")
      m match {
        case m: ExecutorStateChanged => //running exited
          exStatus = exStatus + (m.execId -> m.state)
          debug(s"[WorkerRPC] exStatus: $exStatus")
        case m: DriverStateChanged => //finished
          driverStatus = driverStatus + (m.driverId.toInt -> m.state) //.substring(2)
          debug(s"[WorkerRPC] driverStatus: $driverStatus")
        case _ =>
      }
  }

  /**
   * Process messages from `RpcEndpointRef.ask`. If receiving a unmatched message,
   * `SparkException` will be thrown and sent to `onError`.
   */
  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case m =>
      debug(s"[WorkerRPC] received $m")
  }
}


class SessionWorker(val wRPCendPoit: WorkerRCPEndPoint,
                    val realRun: Boolean = true, //false replaces spark calls with dummy calls
                   //realRun is not tested!!
                    val block: (__EPType_W, StateW, AbstractChannelImp) => Boolean = (d, x, s) => false,
                    val customCode: (__EPType_W, StateW, AbstractChannelImp) => Unit = (a, b, c) => {},
                    val outAppPath : String = "resource-managers/session-cm/output/") extends EPType_W[StateW] with AbstractEndPointTesting[__EPType_W, StateW] with Logger {

  val pConf = ConfigFactory.load()
  val SPARK_HOME = pConf.getString("sessioncm.spark_root")
  val OUT_DIR = SPARK_HOME + outAppPath

  private val host = wRPCendPoit.rpcEnv.address.host
  private val port = wRPCendPoit.rpcEnv.address.port

  trait ASparkRunner {
    def createStartDriverThread(m: MESSAGES.PDriver.LaunchDriver): Thread

    def createStartExThread(m: MESSAGES.PExecutor.StartEx): Thread
  }

  case class MokupRunner(wrpcEP: RpcEndpointRef = wRPCendPoit.epRef) extends ASparkRunner {
    //    def startRpcEnvAndEndpoint(name: String,
    //                               host: String,
    //                               port: Int,
    //                               conf: SparkConf): RpcEnv = {
    //      val securityMgr = new SecurityManager(conf)
    //      val rpcEnv = RpcEnv.create(name, host, port, conf, securityMgr)
    //      rpcEnv
    //    }

    val conf = new SparkConf
    conf.set("SPARK_SCALA_VERSION", "2.12")
    var dNum = 0
    var eXnum = 0

    //    val rpcEnv: RpcEnv= startRpcEnvAndEndpoint("driver"+dNum, Utils.localHostName(), 8500+dNum, conf)
    //    val rpcRefWorker : RpcEndpointRef = rpcEnv.setupEndpoint("wEP",this)
    case class AppData()


    var appInfos = Map[Int, AppData]()

    var done = false

    override def createStartDriverThread(m: PDriver.LaunchDriver): Thread = {

      new Thread(new Runnable {
        override def run(): Unit = {
          val myID = dNum
          val d = AppData()
          appInfos += m.appID -> d
//          conf.wait(5000)
          while(!done){
            Thread.sleep(100)
          }
//          Thread.sleep(3000)
          wrpcEP.send(DriverStateChanged(m.appID.toString, DriverState.FINISHED, None))
          dNum += 1
        }
      })

    }

    override def createStartExThread(m: StartEx): Thread = {
      new Thread(new Runnable {
        override def run(): Unit = {
          val exID = eXnum
          eXnum += 1
          Thread.sleep(50)
          wrpcEP.send(ExecutorStateChanged(m.appId.toString, eXnum,ExecutorState.EXITED,None,None))
          done = true
//          conf.notifyAll()
        }
      })
    }
  }

  case class SparkRunner() extends ASparkRunner {
    override def createStartDriverThread(m: MESSAGES.PDriver.LaunchDriver): Thread = {
      val t = new Thread(new Runnable {
        override def run(): Unit = {
          val conf = new SparkConf

          def updateDrivDescripionToIncludeAppId(d: DriverDescription): DriverDescription = {
            d.modify(_.command.arguments).using(_.+:(m.appID.toString))
          }
          val securityMgr = new SecurityManager(conf)
          val driver = new DriverRunner(
            conf,
            m.appID.toString,
            new File(OUT_DIR + "/Drv_" + m.appID + "_" + System.currentTimeMillis()),
            new File(SPARK_HOME),
            updateDrivDescripionToIncludeAppId(m.driver.driverDesc),
            wRPCendPoit.epRef,
            wRPCendPoit.wRpcURL,
            securityMgr)
          driver.start()
        }
      })
      t
    }

    override def createStartExThread(m: MESSAGES.PExecutor.StartEx): Thread = {
      new Thread(new Runnable {
        override def run(): Unit = {
          val conf = new SparkConf
          val appId = m.appId
          val exId = m.exId
          val wId = "wId1"
          debug("starting executor now")
          debug(s"Evn ins tart Executor: ${m.launchEx.appDesc.command.environment.mkString(",")}")
          println(s"[$wId] Start executor (Ex_${exId}_Drv_${appId}_${System.currentTimeMillis()})")
          // Create the executor's working directory
          val executorDir = new File(OUT_DIR, "Ex_" + exId + "_Drv_" + appId + "_" + System.currentTimeMillis())
          if (!executorDir.mkdirs()) {
            //        throw new IOException("Failed to create directory " + executorDir)
          }

          // Create local dirs for the executor. These are passed to the executor via the
          // SPARK_EXECUTOR_DIRS environment variable, and deleted by the Worker when the
          // application finishes.
          val appLocalDirs = {
            val localRootDirs = Utils.getOrCreateLocalRootDirs(conf)
            val dirs = localRootDirs.flatMap { dir =>
              try {
                val appDir = Utils.createDirectory(dir, namePrefix = "executor")
                Utils.chmod700(appDir)
                Some(appDir.getAbsolutePath())
              } catch {
                case e: IOException =>
                  println(s"${e.getMessage}. Ignoring this directory.")
                  None
              }
            }.toSeq
            if (dirs.isEmpty) {
              throw new IOException("No subfolder can be created in " +
                s"${localRootDirs.mkString(",")}.")
            }
            dirs
          }
          val manager = new ExecutorRunner(
            appId.toString,
            m.exId,
            m.launchEx.appDesc,
            m.launchEx.cores,
            m.launchEx.memory,
            wRPCendPoit.epRef,
            wId,
            "webUi.scheme",
            host,
            0,
            host,
            new File(SPARK_HOME),
            executorDir,
            wRPCendPoit.wRpcURL,
            conf,
            appLocalDirs,
            ExecutorState.LAUNCHING)
          manager.start()
        }
      })
    }

  }

  val runner: ASparkRunner = if (realRun) SparkRunner() else MokupRunner()

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.fromExecutor(new java.util.concurrent.ForkJoinPool(6))

  import org.apache.spark.deploy.Master.DSLUtil._

  override def onStartUp: StateW = StateW()

  override val receive: Seq[HDL[StateW]] = ELoop(
    /*
    Main
     */
    λ(Main_W.RcvFailMtoW) {
      case c => c ? {
        case (m, c) =>
          c
      }
    },
    λ(Main_W.RcvPrepSpawn) {
      case c => c.channelCon
    },

    λ(Main_W.RcvBMsg) {
      case c => c.channelCon
    }
    ,
    λ(Main_W.RcvTerminate) {
      case c =>
        debug(s"[W] received L3 - start termination")
        c.channelCon
    },

    /*
       PDriver
        */
    λ_state(PDriver_w_W.RcvLaunchDriver, PDriver_w_W.SndAckNStatus) {
      case (s, c) =>
        c ? {
          case (m, c) =>
            println(s"Start Driver -- ${m.appID}")
            debug(s"LauncheDriver:")
            trace(pprint.apply(m).plainText)


            val t = runner.createStartDriverThread(m)

            val ms = s.modify(_.driverState).using(_ + ((c.session.sesId, PDriverState(m.appID, m.driver.driverDesc, t))))

            t.start()
            (ms, c ! AckNStatus(m.appID))
        }
    },
    λ_state(PDriver_w_W.SndDriverStateChange) {
      case (s, c) if wRPCendPoit.driverStatus.contains(s.driverState(c).appId) =>
        (s, c ! MESSAGES.PDriver.DriverStateChange(wRPCendPoit.driverStatus(s.driverState(c).appId)))
    },
    λ_state(PDriver_W.RcvFailDriverSpawn) {
      case (s, c) =>
        (s, c.channelCon)
    },
    λ_state(PDriver_W.RcvFailDriverEnd) {
      case (s, c) =>
        (s, c.channelCon)
    },
    λ_static_state(PDriver_w_W.SpawnPExSchedule) {
      case (s, c) =>
        //        (s.modify(_.hdlStartState).using(x =>
        //          x + ((c.session.newSubId.get,WGHdlExStartState(c.session.sesId)))
        //        ),c )
        (s, c)
    },
    /*
    GHdlExSpawn
     */
    λ(PExSchedule_tw_W.RcvEnd) {
      case c =>
//        println(s"[tW_W;${c.session.myId};${c.session.sesId}] RcvEnd")
        c.channelCon
    },
    λ(PExSchedule_W.RcvEnd) {
      case c => c.channelCon
    },
    λ(PExSchedule_w_W.RcvEnd) {
      case c => c.channelCon
    },
    λ(PExSchedule_w_W.RcvStartExCase) {
      case c => c.channelCon
    },
    λ(PExSchedule_W.RcvStartExCase) {
      case c => c.channelCon
    },
    λ(PExSchedule_tw_W.RcvStartExCase) {
      case c => c.channelCon
    },
    λ(PExSchedule_w_W.RcvFailExScheduleEnd) {
      case c => c.channelCon
    },
    λ(PExSchedule_W.RcvFailExScheduleEnd) {
      case c => c.channelCon
    },
    λ(PExSchedule_w_W.RcvFailExScheduleSpawn) {
      case c => c.channelCon
    },
    λ(PExSchedule_W.RcvFailExScheduleSpawn) {
      case c => c.channelCon
    },
    λ_static_state(PExSchedule_w_W.SpawnPExecutor) {
      case (s, c) =>
        //        s.selState(c.session.sesId).driver
        //        (s.modify(_.exState_w).using(_ + (c.session.newSubId.get -> WGExWorker_wState(null))), c)
        (s, c)
    }
    ,
    /*
     GStartEx
    */
    λ_state(PExecutor_wEx_W.RcvStartEx, PExecutor_wEx_W.SndExStarted) {
      case (s, c) => c ? {
        case (m, c) =>
          println(s"[Worker${c.session.myId}] Start Executor")
          trace(pprint.apply(m).plainText)

          val t = runner.createStartExThread(m)
          t.start()

          val ns = s.modify(_.exState).using(_ + (c.session.sesId -> WGExWorkerState(m.appId, m.exId, t)))
          debug(s"[w:${c.session.myId}] start executor for exId: ${m.exId}")
          println(s"[w:${c.session.myId}] start executor for exId: ${m.exId}")

          (ns, c ! ExStarted(m.appId, m.exId))
      }
    },
    λ_state(PExecutor_w_W.RcvExRunning) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[w:${c.session.myId}] For app id: ${m.appId} start executor with id: ${m.exId}")
          (s, c)
      }
    },
    λ_state(PExecutor_w_W.RcvExFinishStatus) {
      case (s, c) => c ? {
        case (m, c) =>
          //          s.exState_w(c.session.sesId).driver.exFinished(m.exId)
          (s, c)
      }
    },
    λ_state(PExecutor_w_W.RcvExFailSpawn) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[Worker;pId:${c.session.myId};sId:${c.session.sesId}] Executor failed (exId:${m.exId}, appID: ${m.appId}) we will try to restart")
          (s, c)
      }
    },
    λ_state(PExecutor_w_W.RcvExFailEnd) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[Worker;pId:${c.session.myId};sId:${c.session.sesId}] Executor failed (exId:${m.exId}, appID : ${m.appId}) we will NOT try to restart")

          (s, c)
      }
    },
    λ_state(PExecutor_W.RcvExFailSpawn) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[Worker;pId:${c.session.myId}(W);sId:${c.session.sesId}] Executor failed (exId:${m.exId}, appID: ${m.appId}) we will try to restart")
          (s, c)
      }
    },
    λ_state(PExecutor_W.RcvExFailEnd) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[Worker;pId:${c.session.myId}(W);sId:${c.session.sesId}] Executor failed (exId:${m.exId}, appID: ${m.appId}) we will NOT try to restart")
          (s, c)
      }
    },
    λ_state(PExecutor_wEx_W.SndExDone) {
      case (s, c) if wRPCendPoit.exStatus.contains(s.exState(c).exId) && ExecutorState.isFinished(wRPCendPoit.exStatus(s.exState(c).exId)) =>
        //!s.exState(c.session.sesId).exCalc.isAlive =>
        debug(s"[Worker:${c.session.myId}] send out ExDone")
        val a = s.exState(c.session.sesId)
        (s, c ! ExDone(a.appId, a.exId))
    }


  )
}