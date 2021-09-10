package org.apache.spark.deploy.Master

import com.softwaremill.quicklens._
import event_lang.dsl._
import event_lang.semantic.CommonTypes.{PartID, SessionID}
import event_lang.types.{Role, RoleSet}
import org.apache.spark.deploy.ApplicationDescription
import org.apache.spark.deploy.Master.ExCompletionStatus.ExCompletionStatus
import org.apache.spark.deploy.Master.Messages._
import org.apache.spark.deploy.intern.logging.Logger
import org.apache.spark.deploy.types.SesCMPaperTypes.M._
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.Main._
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PDriver._
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PExSchedule._
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.PExecutor._
import pprint.pprintln

object ID_Gen {
  private var __id = 0
  private var __curExId: Int = 1

  def appId(): AppId = {
    __id += 1
    __id
  }

  def exId(): ExId = {
    __curExId += 1
    __curExId
  }
}

object ExCompletionStatus extends Enumeration {
  type ExCompletionStatus = Value
  val Undefined, Completed, Failed = Value
}


object ControlFlowUtil {

  implicit class IterToLoop[T](iter: Iterable[T]) {
    def loopUntil[A](until: T => Option[A]): Option[A] = {
      var res: Option[A] = None
      val ita = iter.iterator
      while (res.isEmpty && ita.hasNext) {
        res = until(ita.next())
      }
      res
    }
  }

}

object DSLUtil {
  implicit def sessionToId(ses: Session): SessionID = {
    ses.sesId
  }

  implicit def channelToSesId(c: AbstractChannelImp): SessionID = {
    c.session.sesId
  }
}


case class SparkAppResRequest(cores: Int, memPairEx: Int, coresPairEx: Int)

case class SparkResourceOffer(workerID: PartID, cores: Int, mem: Int)

case class PDriverState(appID: AppId,
                        driver: AbstractDriver,
                        wID: PartID,
                        app: Option[AppDescription],
                        done: Boolean = false,
                        failed: Boolean = false,
                        replacement: Option[PDriverState] = None)

case class PExScheduleState(appID: AppId,
                            driver: AbstractDriver,
                            app: Option[AppDescription],
                            coresGranted: Int,
                            numEx: Int,
                            resourceOffer: Option[SparkResourceOffer],
                            driverSesId: SessionID,
                            failed: Boolean = false
                            //done: Boolean = false
                           ) {

  assert(app.isEmpty || app.get.appD.maxCores.get >= 1)
  assert(app.isEmpty || app.get.appD.memoryPerExecutorMB >= 128)


  def coresPairEx = app.get.appD.coresPerExecutor.getOrElse(1)

  def memPairEx = app.get.appD.memoryPerExecutorMB

  def wantedCores = app.get.appD.maxCores.getOrElse(2)

  def resRequest: Option[SparkAppResRequest] = {
    if (coresGranted < wantedCores) {
      Some(SparkAppResRequest(wantedCores - coresGranted, memPairEx, coresPairEx))
    } else {
      None
    }
  }
}

case class PExecutorState(appID: AppId,
                          exID: ExId,
                          wID: PartID,
                          cores: Int,
                          mem: Int,
                          app: ApplicationDescription,
                          exComStatus: ExCompletionStatus = ExCompletionStatus.Undefined,
                          failed: Boolean = false,
                          replacementOffer: Option[SparkResourceOffer] = None) {

  def resToRestart: Option[SparkAppResRequest] = {
    assert(failed, "Only request new resource for an ex if it has failed")
    Some(SparkAppResRequest(cores, mem, cores))
  }
}

case class AppExDate(maxCores: Int, menPerEx: Int, corePerEx: Option[Int])

case class StateM(
                   appsToRun: Seq[AbstractDriver] = Seq(),
                   appsRunning: Map[AppId, AbstractDriver] = Map(),
                   appsDone: Map[AppId, AbstractDriver] = Map(),
                   //                   appsExDate: Map[AppId, AppExDate] = Map(),
                   // __workerInfo is the inital worker info and not used after
                   __workerInfo: Map[String, WorkerData] = Map(),
                   workerInfo: Map[PartID, WorkerData] = Map(),
                   dToLunch: Option[(AbstractDriver, AppId, PartID)] = None,
                   pDriverState: Map[SessionID, PDriverState] = Map(),
                   pExScheduleState: Map[SessionID, PExScheduleState] = Map(),
                   pExecutorState: Map[SessionID, PExecutorState] = Map()
                 ) extends TState {

  import ControlFlowUtil._

  def setWorkerInfo(s: Session): StateM = {
    var wi = Map[PartID, WorkerData]()
    for (ici <- s.setupInfo.itToCId) {
      this.__workerInfo.get(ici._2).foreach(x =>
        wi += ((ici._1, x))
      )
    }
    assert(wi.size == this.__workerInfo.size)
    copy(workerInfo = wi, __workerInfo = Map())
  }

  def freeWorkerRes(id: PartID, coresFreed: Int, memFreed: Int): StateM = {
    this.modify(_.workerInfo.index(id)).using(wD => {
      wD.copy(curCoreUsage = wD.curCoreUsage - coresFreed, curMemUsage = wD.curMemUsage - memFreed)
    })
  }

  def getFreeMem(): Int = {
    workerInfo.values.foldLeft(0)((acc, wd) => acc + wd.maxMem - wd.curMemUsage)
  }

  def getFreeCPU(): Int = {
    workerInfo.values.foldLeft(0)((acc, wd) => acc + wd.maxCore - wd.curCoreUsage)
  }

  def acquireWorkerRes(id: PartID, acquireCores: Int, acquireMem: Int): StateM = {
    this.modify(_.workerInfo.index(id)).using(wD => {
      wD.copy(curCoreUsage = wD.curCoreUsage + acquireCores, curMemUsage = wD.curMemUsage + acquireMem)
    })
  }

  def findWorkerCanRun(cores: Int, mem: Int, exclude: Set[PartID] = Set(), s: Session): Option[(PartID, WorkerData)] = {
    workerInfo.find(w => {
      val wD = w._2
      !s.failedIDs.contains(w._1) && !exclude.contains(w._1) && wD.availableCores >= cores && wD.availableMem >= mem
    })
  }

  def driverToRun(s: Session): Option[(AbstractDriver, PartID)] = {
    if (workerInfo.isEmpty) {
      setWorkerInfo(s).driverToRun(s)
    } else {
      val res = appsToRun.loopUntil { aD =>
        findWorkerCanRun(aD.driverD.cores, aD.driverD.mem, s = s).map(w => (aD, w._1))
        //        workerInfo.find(w => {
        //          val wD = w._2
        //          wD.availableCores >= aD.driverD.cores && wD.availableMem >= aD.driverD.mem
        //        }).map(w => (aD, w._1))
      }
      res
    }
  }


  def canRunDriver(d: Option[AbstractDriver], s: Session): Option[PartID] = {
    if (workerInfo.isEmpty) {
      setWorkerInfo(s).canRunDriver(d, s)
    } else {
      d.flatMap(aD =>
        findWorkerCanRun(aD.driverD.cores, aD.driverD.mem, s = s).map(w => w._1))
      //        workerInfo.find(w => {
      //          val wD = w._2
      //          wD.availableCores >= aD.driverD.cores && wD.availableMem >= aD.driverD.mem
      //        }).map(w => w._1))
    }
  }

  def exResourceOffer(sparkResReq: Option[SparkAppResRequest], s: Session): Option[SparkResourceOffer] = {
    sparkResReq match {
      case Some(req) =>
        findWorkerCanRun(req.coresPairEx, req.memPairEx, s.roleToPId.values.toSet, s).map(w => SparkResourceOffer(w._1, Math.min(w._2.availableCores, req.cores), req.memPairEx))
      case _ => None
    }
  }

  def reserveExResourceOffer(sessionID: SessionID, sparkResourceOffer: SparkResourceOffer): StateM = {
    this.modify(_.workerInfo.index(sparkResourceOffer.workerID).curCoreUsage).using(_ + sparkResourceOffer.cores).
      modify(_.workerInfo.index(sparkResourceOffer.workerID).curMemUsage).using(_ + sparkResourceOffer.mem).
      modify(_.pExScheduleState.index(sessionID).resourceOffer).setTo(Some(sparkResourceOffer))
  }


}

case class MRPCState()


class SessionMaster(val mRPC: MasterRPCEndPoint,
                    val sState: StateM = StateM(),
                    val block: (__EPType_M, StateM, AbstractChannelImp) => Boolean = (d, x, s) => false,
                    val customCode: (__EPType_M, StateM, AbstractChannelImp) => Unit = (a, b, c) => {})
  extends EPType_M[StateM]
    with AbstractEndPointTesting[__EPType_M, StateM] with Logger {

  import DSLUtil._

  assert(sState.workerInfo.isEmpty)

  override def onStartUp: StateM = sState

  override val pickHandler: Seq[PickHandler[StateM]] = ControlPick(
    //FIXME: it is possible that a worker fails between selecting it for work and performing the spawn.
    // currently we then just let the master role fail -- that is a bit drastic
    /*
      Spawn Driver picks
     */
    controlPick(Main_m_M.SpawnPDriver) {
      case (st, s, ps) =>
        val dL = st.dToLunch.get
        assert(ps.contains(dL._3))
        debug(s"[Master] we picked ${dL._3} for w in SpawnPDriver")
        (st, Some(dL._3))
    },
    //restart driver session
    controlPick(PDriver_m_M.SpawnPDriver) {
      case (st, s, ps) =>
        val nD = st.pDriverState(s).replacement.get
        assert(ps.contains(nD.wID))
        debug(s"[Master] we picked ${nD.wID} for w in SpawnPDriver (restart)")
        (st, Some(nD.wID))
    },
    /*
  Spawn Schedule picks
 */
    controlPick(PDriver_m_M.SpawnPExSchedule) {
      case (st, s, ps) =>
        var pId = ps.head
        var mC = Int.MaxValue
        st.workerInfo.foreach(kv => {
          if (kv._2.maxCore < mC && ps.contains(kv._1)) {
            pId = kv._1
            mC = kv._2.maxCore
          }
        })
        (st, Some(pId))
    },
    //below the restart picking
    controlPick(PExSchedule_m_M.SpawnPExSchedule) {
      case (st, s, ps) =>
        var pId = ps.head
        var mC = Int.MaxValue
        st.workerInfo.foreach(kv => {
          if (kv._2.maxCore < mC && ps.contains(kv._1)) {
            pId = kv._1
            mC = kv._2.maxCore
          }
        })
        (st, Some(pId))
    },
    /*
    Spawn Executor Picks
     */
    controlPick(PExSchedule_m_M.SpawnPExecutor) {
      case (st, s, ps) =>
        val rs = st.pExScheduleState(s).resourceOffer.get
        debug(s"[Master] pick for PExSchedule_m_M.SpawnPExecutor (s: $s, rs: $rs, ps: $ps)")
        assert(ps.contains(rs.workerID))
        mRPC.idOfWorkerStartedLastEx = rs.workerID
        (st, Some(rs.workerID))
    },
    //restart
    controlPick(PExecutor_m_M.SpawnPExecutor) {
      case (st, s, ps) =>
        val rs = st.pExecutorState(s).replacementOffer.get
        debug(s"[Master] pick for GStartEx (rs: $rs, ps: $ps)")
        assert(ps.contains(rs.workerID))
        mRPC.idOfWorkerStartedLastEx = rs.workerID
        (st, Some(rs.workerID))
    }

  )

  override val subFinishHandler: Seq[FinishSpawnHandler[StateM]] = FinishSubSessionHandlers(
    finishSubSession(PDriver_m_M.EPPDriver_m_M) {
      case (st, parentS, childS) =>
        debug(s"[PDriver] FINISHED: we finished a sub session $childS and its parent is $parentS")
        val state = st.pDriverState(childS.sesId)
        val done = state.done //state.driver.isFinished()
        val ns = if (done) {
          st.freeWorkerRes(state.wID, state.driver.driverD.cores, state.driver.driverD.mem).
            modify(_.appsRunning).using(_ - state.appID).
            modify(_.appsDone)(_ + (state.appID -> state.driver)).
            modify(_.pDriverState)(_ - childS.sesId)


        } else {
          st.freeWorkerRes(state.wID, state.driver.driverD.cores, state.driver.driverD.mem).
            modify(_.appsRunning).using(_ - state.appID).
            modify(_.pDriverState)(_ - childS.sesId)
        }
        //          println(s"[Master] Finished PDriver - state: $ns")
        trace(pprint.apply(ns).plainText)
        ns
    },
    finishSubSession(PExSchedule_m_M.EPPExSchedule_m_M) {
      case (st, pS, cS) =>
        debug(s"[PexSchedule] FINISHED: we finished a sub session $cS and its parent is $pS, state: $st")
        trace(pprint.apply(st).plainText)
        val nS = st.modify(_.pExecutorState).using(_ - cS.sesId)
        nS
    },
    finishSubSession(PExecutor_m_M.EPPExecutor_m_M) {
      case (st, pS, cS) =>
        debug(s"[PExecutor] FINISHED: PExecutor done. Id $cS and its parent is $pS")
        val exS = st.pExecutorState(cS)
        val nS = st.freeWorkerRes(exS.wID, exS.cores, exS.mem).
          modify(_.pExecutorState)(_ - cS.sesId)

        debug(s"[Master] Finished PExecutor - new state: $nS")
        nS
    },


  )

  private def setWInfo(s: StateM, ses: Session) = {
    if (s.workerInfo.isEmpty) {
      s.setWorkerInfo(ses)
    } else {
      s
    }
  }

  override val receive: Seq[HDL[StateM]] = ELoop(

    /*
        Main
     */
    λ(Main_M.RcvFailMtoM) {
      case c => c ? {
        case (m, c) =>
          c
      }
    },
    // we run a driver the ep knows about
    λ_state(Main_m_M.SndNewDriver, Main_m_M.SndPrepSpawn) {
      case (s, c) if s.driverToRun(c.session).nonEmpty =>
        val s1 = setWInfo(s, c.session)

        val dRun = s1.driverToRun(c.session).get


        info(s"[Master] We want to run $dRun")
        println(s"[Master] We want to run $dRun")

        assert(s1.dToLunch.isEmpty)
        assert(s1.appsToRun.contains(dRun._1))


        val nAppId = ID_Gen.appId()
        val nS = s1.acquireWorkerRes(dRun._2, dRun._1.driverD.cores, dRun._1.driverD.mem).
          modify(_.dToLunch).setTo(Some((dRun._1, nAppId, dRun._2))).
          modify(_.appsToRun).using(_ diff Seq(dRun._1)).
          modify(_.appsRunning).using(_ + (nAppId -> dRun._1))
        assert(s1.appsRunning.size + 1 == nS.appsRunning.size)
        assert(s1.appsToRun.size == nS.appsToRun.size + 1)
        assert(s1.getFreeMem() == nS.getFreeMem() + dRun._1.driverD.mem)
        assert(s1.getFreeCPU() == nS.getFreeCPU() + dRun._1.driverD.cores)
        trace(s"[Master] New state: $nS (old state $s)")
//        pprint.pprintln(s"[Master] New state: ")
        trace(pprint.apply(nS).plainText)


        (nS, c ! NewDriver(nAppId) ! PrepSpawn())
    },
    //we rune a driver send to us from extern
    λ_state(Main_m_M.SndNewDriver, Main_m_M.SndPrepSpawn) {
      case (s, c) if s.canRunDriver(Option(mRPC.subApps.peek()), c.session).nonEmpty =>
        val s1 = setWInfo(s, c.session)
        val app = mRPC.subApps.poll()
        val pID = s1.canRunDriver(Option(app), c.session).get
        pprintln(s"[Master] We want to run $app")

        val nAppId = ID_Gen.appId()
        val nS = s1.acquireWorkerRes(pID, app.driverD.cores, app.driverD.mem).
          //          modify(_.workerInfo.index(pID).curMemUsage).using(_ + app.driverD.mem).
          //          modify(_.workerInfo.index(pID).curCoreUsage).using(_ + app.driverD.cores).
          modify(_.dToLunch).setTo(Some((app, nAppId, pID))).
          modify(_.appsRunning).using(_ + (nAppId -> app))
        assert(s1.getFreeMem() == nS.getFreeMem() + app.driverD.mem)
        assert(s1.getFreeCPU() == nS.getFreeCPU() + app.driverD.cores)
        assert(s1.appsToRun.size == nS.appsToRun.size)
        trace(s"[Master] New state: $nS (old state $s)")
        trace(s"[Master] New state: \n $nS \n(old state $s)")
        trace(pprint.apply(nS).plainText)
        (nS, c ! NewDriver(nAppId) ! PrepSpawn())
    },
    λ_state(Main_m_M.SndDriverDone, Main_m_M.SndBMsg) {
      case (s, c) if s.appsDone.nonEmpty => //  apps.exists(_.finished) =>
        val dA = s.appsDone.head
        println(s"[Master] Abb done: $dA -- State: $s")
        info(s"[Master] Abb done: $dA -- State: $s")

        val dAppId = dA._1
        val dAppDriver = dA._2
        assert(!s.appsRunning.contains(dAppId), "Running apps should be removed by finishing PDriver")
        val mS = s.modifyAll(_.appsDone, _.appsRunning).using(_ - dAppId)
        (mS, c ! DriverDone(dAppId) ! BMsg())
    },


    λ_state(Main_m_M.SndEndCM, Main_m_M.SndTerminate) {
      //this is a save block.
      case (s, c) if c.session.hasNoSubsession() && mRPC.stopMaster =>
        info(s"[Master] ### Signal completion of Scheduling")
        info(s"[Master] state after sending end: $s")
        info(s"[Master] state after sending end: \n $s")
        trace(pprint.apply(s).plainText)
        (s, c ! EndCM() ! Terminate())
    },
    λ_static_state(Main_m_M.SpawnPDriver) {
      case (s, c) =>
        val nSubId = c.session.newSubId.get
        val toLunch = s.dToLunch.get
        debug(s"[Master] ### Spawn GSel: " +
          s"toLunch: $toLunch ")
        val nS = s.
          modify(_.dToLunch).setTo(None).
          modify(_.pDriverState).using(_ +
          (nSubId -> PDriverState(
            toLunch._2,
            toLunch._1,
            toLunch._3,
            None
          )))
        debug(s"[Master] Added ${nS.pDriverState(nSubId)} (state: $nS)")
        debug(s"[Master] state after sending end: \n $nS")
        trace(pprint.apply(nS).plainText)
        (nS, c)
    },
    /*
      PDriver
   */
    λ_state(PDriver_m_M.SndLaunchDriver) {
      case (s, c) =>
        val st = s.pDriverState(c.session.sesId)
        (s, c ! LaunchDriver(st.appID, org.apache.spark.deploy.DeployMessages.LaunchDriver(st.appID.toString, st.driver.driverD, Map())))
    },
    λ_state(PDriver_m_M.RcvAckNStatus) {
      /*
      The started driver registers itself with the CM Master, this communication is not part of the Session CM protocol.
      We wait here for that registration before we move forward.
      TODO consider to let the driver register with the worker who runs the driver
       */
      case (s, c) if mRPC.regApps.contains(s.pDriverState(c).appID) => c ? {
        case (m, c) =>
          debug(s"[Master] ### Ack Driver Launch $m")
          val aId = s.pDriverState(c).appID
          (s.modify(_.pDriverState.index(c).app).setTo(Some(SparkAppDescription(mRPC.regApps(aId)._1))), c)
      }
    },
    λ_static_state(PDriver_m_M.SpawnPExSchedule) {
      case (s, c) =>
        val gS = s.pDriverState(c)
        val nS = s.modify(_.pExScheduleState).using(_ + (c.session.newSubId.get -> PExScheduleState(
          appID = gS.appID,
          driver = gS.driver,
          app = gS.app,
          coresGranted = 0,
          numEx = 0,
          resourceOffer = None,
          driverSesId = c.session.sesId
        )))
        debug(s"[Master] ### Spawn GHdlExSpawn for ${c.session} (state: \n $nS)")
        (nS, c)
    },
    λ_state(PDriver_m_M.RcvDriverStateChange) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[Master] ### Ack Driver finish. $m")
          val ss = s.pDriverState(c)
          //inform the submitter about the result of the driver
          ss.driver.subRPCEpRef.foreach(_.send(SparkAppFinished(ss.driver, m.status)))

          val aId = s.pDriverState(c).appID
          (s.modify(_.pDriverState.index(c).done).setTo(true), c) //Some(SparkAppDescription(mRPC.regApps(aId)._1))), c)
      }
    }
    ,
    //TODO we only handle a restart if we NOW have resource for that, if we have no resources we will just STOP the driver
    //we restart a failed driver that has not yet finished
    λ_state(PDriver_m_M.Failed_w_W, PDriver_m_M.SndFailDriverSpawn) {
      case (s, c) if !s.pDriverState(c.session).done && s.findWorkerCanRun(s.pDriverState(c.session).driver.driverD.cores, s.pDriverState(c.session).driver.driverD.mem, s = c.session).nonEmpty =>
        val gSelS = s.pDriverState(c)
        info(s"[Master] ### FAILURE in PDriver => RESTART DRIVER " +
          s"Worker ${c.session.roleToPId(Role("w", RoleSet("W")))} for " +
          s"App ${gSelS} [${s.pDriverState}]")
        val d = gSelS.driver
        val w = s.findWorkerCanRun(s.pDriverState(c.session).driver.driverD.cores, s.pDriverState(c.session).driver.driverD.mem, s = c.session).get

        val nAppId = ID_Gen.appId()

        /*
        cleanup
         */
        // we do cleanup of mem and core for works in the endsub handling
        // we the driver worker failure
        // no cores and mem cleanup needed
        val s1 = s.
          modify(_.appsRunning).using(_ - gSelS.appID).
          modify(_.pDriverState.index(c.session.sesId).failed).setTo(true)

        /*
        setup new PDriver
         */
        val nS = s1.acquireWorkerRes(w._1, d.driverD.cores, d.driverD.mem).
          modify(_.pDriverState.index(c.session.sesId).replacement).setTo(Some(PDriverState(nAppId, gSelS.driver, w._1, None))).
          modify(_.appsRunning).using(_ + (nAppId -> gSelS.driver))


        debug(s"[Master] Restart $gSelS, replacement ${nS.pDriverState(c.session.sesId).replacement}")

        assert(s1.appsToRun.size == nS.appsToRun.size)
        debug(s"[Master] New state: $nS (old state $s)")
        debug(s"[Master] New state: ")
        debug(pprint.apply(nS).plainText)


        (nS, c.failed_w_W() ! FailDriverSpawn(s.pDriverState(c).appID, nAppId))
    },
    λ_static_state(PDriver_m_M.SpawnPDriver) {
      case (s, c) =>
        val nSubId = c.session.newSubId.get
        val oldState = s.pDriverState(c)
        val replacement = oldState.replacement.get

        debug(s"[Master] ### Restart SpawnPDriver: " +
          s"old: $oldState replacement: $replacement ")
        val nS = s.
          modify(_.pDriverState).using(_ +
          (nSubId -> replacement))
        debug(s"[Master] Added ${nS.pDriverState(nSubId)} (state: $nS)")
        debug(s"[Master] state after sending end: \n $nS")
        debug(pprint.apply(nS).plainText)
        (nS, c)
    },
    //WE handle a failed but finished driver or a failed driver and we have NOW no resources
    λ_state(PDriver_m_M.Failed_w_W, PDriver_m_M.SndFailDriverEnd) {
      case (s, c) if s.pDriverState(c.session).done || s.findWorkerCanRun(s.pDriverState(c.session).driver.driverD.cores, s.pDriverState(c.session).driver.driverD.mem, s = c.session).isEmpty =>
        val gSelS = s.pDriverState(c)
        info(s"[Master] ### FAILURE in GSel -- " +
          s"Worker ${c.session.roleToPId(Role("w", RoleSet("W")))} for " +
          s"App ${gSelS} [${s.pDriverState}]")

        if (!s.pDriverState(c.session).done) {
          System.err.println(s"#### DRIVER FAILED -- NOT RESTARTED -- NO RESOURCES\n" +
            "Driver details:\n" +
            s"\t$gSelS")
        }

        val appId = gSelS.appID
        (s, c.failed_w_W() ! FailDriverEnd(s.pDriverState(c).appID))
    },
    /*
    PExSchedule
     */
    λ_state(PExSchedule_m_M.SndStartExCase) {
      case (s, c) if s.exResourceOffer(s.pExScheduleState(c.session).resRequest, c.session).nonEmpty =>
        val resOff = s.exResourceOffer(s.pExScheduleState(c.session).resRequest, c.session).get
        val app = s.pExScheduleState(c.session.sesId).appID
        val nS = s.reserveExResourceOffer(c, resOff)

        assert(s.getFreeMem() == nS.getFreeMem() + resOff.mem,
          s"${s.getFreeMem()} == ${nS.getFreeMem()} + ${resOff.mem}")
        assert(s.getFreeCPU() == nS.getFreeCPU() + resOff.cores,
          s"${s.getFreeCPU()} == ${nS.getFreeCPU()} + ${resOff.cores}")

        info(s"[Master] StartEx - Resource offer: $resOff for app: $app -- new state: $nS")
        debug(pprint.apply(resOff).plainText)
        debug(pprint.apply(app).plainText)
        debug(pprint.apply(nS).plainText)

        (nS, c ! StartExCase(app))
    }
    ,
    λ_state(PExSchedule_m_M.SndEnd) {
      // This block is save
      case (s, c) if s.pDriverState(s.pExScheduleState(c).driverSesId).done =>
        val app = s.pExScheduleState(c)
        info(s"[Master] ### Finished app: $app send out End in GSel (apps: ${s.appsRunning}")
        println(s"[Master] ### Finished app: $app send out End in GSel (apps: ${s.appsRunning}")

        (s, c ! End())
    }
    ,
    λ_static_state(PExSchedule_m_M.SpawnPExecutor) {
      case (s, c) =>
        val nSubId = c.session.newSubId.get
        val app = s.pExScheduleState(c)
        val rO = app.resourceOffer.get
        assert(rO.cores >= 1)
        assert(rO.mem >= 128)
        val exId = ID_Gen.exId()
        val newExS = PExecutorState(
          appID = app.appID,
          exID = exId,
          wID = rO.workerID,
          cores = rO.cores,
          mem = rO.mem,
          app = app.app.get.appD
        )

        val nS =
          s.modify(_.pExecutorState).using(_ + (nSubId -> newExS)).
            modify(_.pExScheduleState.index(c).resourceOffer).setTo(None).
            modify(_.pExScheduleState.index(c).coresGranted).using(_ + rO.cores).
            modify(_.pExScheduleState.index(c).numEx).using(_ + 1)
        debug(s"[Master] spawn PExecutor: $newExS (state: $nS)")
        debug(pprint.apply(newExS).plainText)
        debug(pprint.apply(nS).plainText)
        (nS, c)
    }
    ,
    λ_state(PExSchedule_m_M.Failed_tw_W, PExSchedule_m_M.SndFailExScheduleSpawn) {
      case (s, c) =>
        val gSelS = s.pExScheduleState(c.session.sesId)
        val nS = s.modify(_.pExScheduleState.index(c.session.sesId).failed).setTo(true)

        info(s"[Master] ### FAILURE in PExSchedule_m_M -- " +
          s"Worker ${c.session.roleToPId(Role("tw", RoleSet("W")))} for " +
          s"App ${gSelS} [${s.pDriverState}]")
        val appId = gSelS.appID
        (nS, c.failed_tw_W() ! FailExScheduleSpawn(appId))
    },
    λ_static_state(PExSchedule_m_M.SpawnPExSchedule) {
      case (s, c) =>
        val gS = s.pExScheduleState(c)
        val nS = s.modify(_.pExScheduleState).using(_ + (c.session.newSubId.get -> gS.copy(failed = false, resourceOffer = None)))
        debug(s"[Master] ### Spawn PExSchedule_m_M.SpawnPExSchedule for ${c.session} (state: \n $nS)")
        (nS, c)
    },
    λ_state(PExSchedule_m_M.Failed_tw_W, PExSchedule_m_M.SndFailExScheduleEnd) {
      // we always restart PExSchedule, i.e., use the handler λ_state(PExSchedule_m_M.Failed_tw_W, ... above
      // we only have the handler to satisfy the coverage checker. Todo update protocol and remove the branching case we never perform
      case (s, c) if false =>
        ???
        val gSelS = s.pExScheduleState(c.session.sesId)
        warn(s"[Master] ### FAILURE in GSel -- " +
          s"Worker ${c.session.roleToPId(Role("w", RoleSet("W")))} for " +
          s"App ${gSelS} [${s.pDriverState}]")
        val appId = gSelS.appID
        (s, c.failed_tw_W() ! FailExScheduleEnd(gSelS.appID))
    }
    ,
    /*
    PExecutor
     */
    λ_state(PExecutor_m_M.SndStartEx) {
      case (s, c) =>
        val mS = s.pExecutorState(c.session.sesId)
        (s, c ! StartEx(mS.appID, mS.exID,
          org.apache.spark.deploy.DeployMessages.LaunchExecutor(
            "XXX",
            mS.appID.toString,
            mS.exID,
            mS.app,
            mS.app.coresPerExecutor.getOrElse(1),
            mS.app.memoryPerExecutorMB
          )))
    }
    ,
    λ(PExecutor_m_M.RcvExStarted, PExecutor_m_M.SndExRunning) {
      case c => c ? {
        case (m, c) =>
          debug(s"[Master] ### Ack Ex Started: $m")
          c ! ExRunning(m.appId, m.exId)
      }
    }
    ,
    λ_state(PExecutor_m_M.RcvExDone, PExecutor_m_M.SndExFinishStatus) {
      case (s, c) => c ? {
        case (m, c) =>
          (s.modify(_.pExecutorState.index(c.session.sesId).exComStatus).setTo(ExCompletionStatus.Completed), c ! ExFinishStatus(m.appId, m.exId))
      }
    }
    ,
    λ_state(PExecutor_m_M.Failed_wEx_W) {
      case (s, c) =>
        val eX = s.pExecutorState(c.session.sesId)
        warn(s"[Master] ### FAILURE in PExecutor (we will try to restart) -- " +
          s"Worker ${c.session.roleToPId(Role("wEx", RoleSet("W")))} for " +
          s"eX ${eX} [${s.pExecutorState}]")

        val nS = s.modify(_.pExecutorState.index(c).failed).setTo(true)
        (nS, c.failed_wEx_W())
    },
    λ_state(PExecutor_m_M.SndExFailSpawn) {
      case (s, c) if s.exResourceOffer(s.pExecutorState(c.session).resToRestart, c.session).nonEmpty =>
        val resOff = s.exResourceOffer(s.pExecutorState(c.session).resToRestart, c.session).get

        val eS = s.pExecutorState(c)
        val nS = s.modify(_.pExecutorState.index(c).replacementOffer).setTo(Some(resOff))

        debug(s"[Master] Restart Executor: OLD state: $eS -- restart resource offer: $resOff")
        debug(pprint.apply(resOff).plainText)
        debug(pprint.apply(nS).plainText)

        val sId = c.session.sesId
        val appId = eS.appID
        val exId = eS.exID
        (nS, c ! ExFailSpawn(eS.appID, eS.exID))
    },
    λ_static_state(PExecutor_m_M.SpawnPExecutor) {
      case (s, c) =>
        val nSubId = c.session.newSubId.get
        val ex = s.pExecutorState(c)
        val rO = ex.replacementOffer.get
        assert(rO.cores >= 1)
        assert(rO.mem >= 128)
        val exId = ID_Gen.exId()
        val newExS = PExecutorState(
          appID = ex.appID,
          exID = exId,
          wID = rO.workerID,
          cores = rO.cores,
          mem = rO.mem,
          app = ex.app
        )

        val nS = s.reserveExResourceOffer(c, rO).modify(_.pExecutorState).using(_ + (nSubId -> newExS))

        debug(s"[Master] restart pexecutor: $newExS (state: $nS)")
        debug(pprint.apply(newExS).plainText)
        debug(pprint.apply(nS).plainText)

        (nS, c)
    },
    //This handler should only be triggered in rare occations
    λ_state(PExecutor_m_M.SndExFailEnd) {
      case (s, c) if s.appsDone.contains(s.pExecutorState(c).appID) || !s.appsRunning.contains(s.pExecutorState(c).appID) =>
        val eS = s.pExecutorState(c)

        System.err.println(s"#### Executor FAILED and is NOT restarted\n" +
          "Ex details:\n" +
          s"\t$eS")
        (s, c ! ExFailEnd(eS.appID, eS.exID))
    }
  )
}