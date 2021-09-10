package org.apache.spark.deploy.ZkMockup

import event_lang.dsl._
import org.apache.spark.deploy.intern.logging.Logger
import org.apache.spark.deploy.types.SesCMPaperTypes.MESSAGES.Main.{FailMtoM, FailMtoW}
import org.apache.spark.deploy.types.SesCMPaperTypes.ZK._
case class StateZK(runApps: Set[Int] = Set()) extends TState

class SessionZK(val block: (AbstractChannelType, StateZK, AbstractChannelImp) => Boolean = (d, x, s) => false,
                val customCode: (AbstractChannelType, StateZK, AbstractChannelImp) => Unit = (a, b, c) => {}) extends EPType_ZK[StateZK] with AbstractEndPointTesting[__EPType_ZK, StateZK] with Logger {

  override def onStartUp: StateZK = StateZK()

  override val receive: Seq[HDL[StateZK]] = ELoop(
    λ_state(Main_zk_ZK.Failed_m_M, Main_zk_ZK.SndFailMtoM, Main_zk_ZK.SndFailMtoW) {
      case (s, c) =>
        debug(s"[Zk] we should restart (not implemented): ${s.runApps.mkString(",")}")
        (s, c.failed_m_M() ! FailMtoM() ! FailMtoW())
    },

    λ_state(Main_zk_ZK.RcvNewDriver) {
      case (s, c) => c ? {
        case (m, c) =>
          (s.copy(runApps = s.runApps + m.appID), c)
      }
    },
    λ_state(Main_zk_ZK.RcvDriverDone) {
      case (s, c) => c ? {
        case (m, c) =>
          (s.copy(runApps = s.runApps - m.appID), c)
      }
    },
    λ_state(Main_zk_ZK.RcvEndCM) {
      case (s, c) => c ? {
        case (m, c) =>
          debug(s"[zk] received $m - start termination")
          if (s.runApps.nonEmpty) {
            debug(s"[zk] received $m however the following apps are still running ${s.runApps.mkString(",")}")
          }
          (s, c)
      }
    }

  )
}


