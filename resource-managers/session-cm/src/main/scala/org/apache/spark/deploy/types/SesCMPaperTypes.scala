package org.apache.spark.deploy.types

import event_lang._
import event_lang.dsl.{AbstractChannel, AbstractChannelImp, AbstractChannelType, AbstractEndPoint, EndPointType, Session, TState}
import event_lang.types.{MSG, Role, RoleSet, RRole}

/* ##########################################
 * ###### GENERATED CODE - DO NOT EDIT ######
 * ##########################################
*/


object SesCMPaperTypes {
  object RS {
    val W: RoleSet = RoleSet("W")
    val M: RoleSet = RoleSet("M")
    val ZK: RoleSet = RoleSet("ZK")
  }

  object MESSAGES {
    object PExecutor {
      case class ExRunning(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExRunning"
      }

      case class ExFinishStatus(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExFinishStatus"
      }

      case class ExStarted(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExStarted"
      }

      case class ExFailSpawn(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExFailSpawn"
      }

      case class ExDone(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExDone"
      }

      case class ExFailEnd(appId: Int, exId: Int) extends MSG {
        override def l: String = "ExFailEnd"
      }

      case class StartEx(appId: Int, exId: Int, launchEx: org.apache.spark.deploy.DeployMessages.LaunchExecutor) extends MSG {
        override def l: String = "StartEx"
      }

    }

    object PExSchedule {
      case class StartExCase(appID: Int) extends MSG {
        override def l: String = "StartExCase"
      }

      case class End() extends MSG {
        override def l: String = "End"
      }

      case class FailExScheduleSpawn(appId: Int) extends MSG {
        override def l: String = "FailExScheduleSpawn"
      }

      case class FailExScheduleEnd(appId: Int) extends MSG {
        override def l: String = "FailExScheduleEnd"
      }

    }

    object Main {
      case class EndCM() extends MSG {
        override def l: String = "EndCM"
      }

      case class BMsg() extends MSG {
        override def l: String = "BMsg"
      }

      case class FailMtoM() extends MSG {
        override def l: String = "FailMtoM"
      }

      case class NewDriver(appID: Int) extends MSG {
        override def l: String = "NewDriver"
      }

      case class PrepSpawn() extends MSG {
        override def l: String = "PrepSpawn"
      }

      case class DriverDone(appID: Int) extends MSG {
        override def l: String = "DriverDone"
      }

      case class FailMtoW() extends MSG {
        override def l: String = "FailMtoW"
      }

      case class Terminate() extends MSG {
        override def l: String = "Terminate"
      }

    }

    object PDriver {
      case class FailDriverEnd(appId: Int) extends MSG {
        override def l: String = "FailDriverEnd"
      }

      case class AckNStatus(appID: Int) extends MSG {
        override def l: String = "AckNStatus"
      }

      case class FailDriverSpawn(appId: Int, newAppID: Int) extends MSG {
        override def l: String = "FailDriverSpawn"
      }

      case class DriverStateChange(status: org.apache.spark.deploy.master.DriverState.DriverState) extends MSG {
        override def l: String = "DriverStateChange"
      }

      case class LaunchDriver(appID: Int, driver: org.apache.spark.deploy.DeployMessages.LaunchDriver) extends MSG {
        override def l: String = "LaunchDriver"
      }

    }

  }

  object PROTOCOLS {
    object PExecutor {
      val wEx_W = Role("wEx", RoleSet("W"))
      val W = RoleSet("W")
      val m_M = Role("m", RoleSet("M"))
      val w_W = Role("w", RoleSet("W"))
    }

    object PDriver {
      val w_W = Role("w", RoleSet("W"))
      val W = RoleSet("W")
      val m_M = Role("m", RoleSet("M"))
    }

    object PExSchedule {
      val tw_W = Role("tw", RoleSet("W"))
      val W = RoleSet("W")
      val m_M = Role("m", RoleSet("M"))
      val w_W = Role("w", RoleSet("W"))
    }

    object Main {
      val m_M = Role("m", RoleSet("M"))
      val M = RoleSet("M")
      val W = RoleSet("W")
      val zk_ZK = Role("zk", RoleSet("ZK"))
    }

  }

  object W {
    val subs: Seq[dsl.ChannelTypeSubS] = List(PExecutor_w_W.EPPExecutor_w_W, PExSchedule_w_W.EPPExSchedule_w_W, PExSchedule_W.EPPExSchedule_W, PExecutor_W.EPPExecutor_W, PDriver_W.EPPDriver_W, PExecutor_wEx_W.EPPExecutor_wEx_W, Main_W.EPMain_W, PDriver_w_W.EPPDriver_w_W, PExSchedule_tw_W.EPPExSchedule_tw_W)

    trait __EPType_W extends AbstractChannelType {

    }

    trait EPType_W[T <: TState] extends AbstractEndPoint[__EPType_W, T] {
      override val roleSet: RoleSet = RoleSet("W")
      override val subs: Seq[dsl.ChannelTypeSubS] = List(PExecutor_w_W.EPPExecutor_w_W, PExSchedule_w_W.EPPExSchedule_w_W, PExSchedule_W.EPPExSchedule_W, PExecutor_W.EPPExecutor_W, PDriver_W.EPPDriver_W, PExecutor_wEx_W.EPPExecutor_wEx_W, Main_W.EPMain_W, PDriver_w_W.EPPDriver_w_W, PExSchedule_tw_W.EPPExSchedule_tw_W)

    }

    object Main_W {
      trait EPMain_W extends __EPType_W

      object EPMain_W extends EPMain_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPMain_W] = List(Hdl)

        override type implT = __EPMain_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPMain_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("zk", RoleSet("ZK")))

        override def argsP: Role = Role("m", RoleSet("M"))

        override def argsRs: List[RoleSet] = List(RoleSet("M"), RoleSet("W"))

        override def prjTo: RRole = RoleSet("W")

        override def rootRole: Role = Role("zk", RoleSet("ZK"))

        override def name: String = "Main"
      }

      protected case class __EPMain_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPMain_W
        }

      }


      protected trait Hdl extends EPMain_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPMain_W] = List(RecT, RcvFailMtoW)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPMain_W with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPMain_W] = List(Merge_PrepSpawn_BMsg_Terminate)

        override type implT = __RecTImp
        override type implNextT = __Merge_PrepSpawn_BMsg_TerminateImp

        override def toString(): String = {
          "EPMain_W.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait Merge_PrepSpawn_BMsg_Terminate extends EPMain_W with event_lang.dsl.ChannelTypeMerge

      protected object Merge_PrepSpawn_BMsg_Terminate extends Merge_PrepSpawn_BMsg_Terminate {
        override protected def __children: List[EPMain_W] = List(RcvPrepSpawn, RcvBMsg, RcvTerminate)

        override type implT = __Merge_PrepSpawn_BMsg_TerminateImp
        override type implNextT = __RcvPrepSpawnImp

        override def toString(): String = {
          "EPMain_W.Merge_PrepSpawn_BMsg_Terminate"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __Merge_PrepSpawn_BMsg_TerminateImp(c, session)
      }

      protected case class __Merge_PrepSpawn_BMsg_TerminateImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Merge_PrepSpawn_BMsg_Terminate
        }

      }


      trait RcvPrepSpawn extends EPMain_W with event_lang.dsl.ChannelTypeRcv

      object RcvPrepSpawn extends RcvPrepSpawn {
        override protected def __children: List[EPMain_W] = List(SpawnPDriver)

        override type implT = __RcvPrepSpawnImp
        override type implNextT = __SpawnPDriverImp

        override def toString(): String = {
          "EPMain_W.RcvPrepSpawn"
        }

        override type msgT = MESSAGES.Main.PrepSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "PrepSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvPrepSpawnImp(c, session)
      }

      protected case class __RcvPrepSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvPrepSpawn
        }

        def rcvFrmm_M: (MESSAGES.Main.PrepSpawn, __SpawnPDriverImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.PrepSpawn], __SpawnPDriverImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.PrepSpawn, __SpawnPDriverImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.PrepSpawn], __SpawnPDriverImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.PrepSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.PrepSpawn]
        }

        def ? : MESSAGES.Main.PrepSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.PrepSpawn]
        }

        def channelCon: __SpawnPDriverImp = {
          __SpawnPDriverImp(c, session)
        }

      }


      trait SpawnPDriver extends EPMain_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPDriver extends SpawnPDriver {
        override protected def __children: List[EPMain_W] = List(T)

        override type implT = __SpawnPDriverImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_W.SpawnPDriver"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PDriver"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPDriverImp(c, session)
      }

      protected case class __SpawnPDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPDriver
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPMain_W with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPMain_W] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_W.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait RcvBMsg extends EPMain_W with event_lang.dsl.ChannelTypeRcv

      object RcvBMsg extends RcvBMsg {
        override protected def __children: List[EPMain_W] = List(T)

        override type implT = __RcvBMsgImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_W.RcvBMsg"
        }

        override type msgT = MESSAGES.Main.BMsg

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "BMsg"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvBMsgImp(c, session)
      }

      protected case class __RcvBMsgImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvBMsg
        }

        def rcvFrmm_M: (MESSAGES.Main.BMsg, __TImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.BMsg], __TImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.BMsg, __TImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.BMsg], __TImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.BMsg = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.BMsg]
        }

        def ? : MESSAGES.Main.BMsg = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.BMsg]
        }

        def channelCon: __TImp = {
          __TImp(c, session)
        }

      }

      //there was an occurens of t already

      trait RcvTerminate extends EPMain_W with event_lang.dsl.ChannelTypeRcv

      object RcvTerminate extends RcvTerminate {
        override protected def __children: List[EPMain_W] = List(End_W_MainTerminate)

        override type implT = __RcvTerminateImp
        override type implNextT = __End_W_MainTerminateImp

        override def toString(): String = {
          "EPMain_W.RcvTerminate"
        }

        override type msgT = MESSAGES.Main.Terminate

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "Terminate"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvTerminateImp(c, session)
      }

      protected case class __RcvTerminateImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvTerminate
        }

        def rcvFrmm_M: (MESSAGES.Main.Terminate, __End_W_MainTerminateImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.Terminate], __End_W_MainTerminateImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.Terminate, __End_W_MainTerminateImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.Terminate], __End_W_MainTerminateImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.Terminate = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.Terminate]
        }

        def ? : MESSAGES.Main.Terminate = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.Terminate]
        }

        def channelCon: __End_W_MainTerminateImp = {
          __End_W_MainTerminateImp(c, session)
        }

      }


      protected trait End_W_MainTerminate extends EPMain_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_MainTerminate extends End_W_MainTerminate {
        override protected def __children: List[EPMain_W] = List()

        override type implT = __End_W_MainTerminateImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_W.End_W_MainTerminate"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_MainTerminateImp(c, session)
      }

      protected case class __End_W_MainTerminateImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_MainTerminate
        }

      }


      trait RcvFailMtoW extends EPMain_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailMtoW extends RcvFailMtoW {
        override protected def __children: List[EPMain_W] = List(End_W_MainFHandling)

        override type implT = __RcvFailMtoWImp
        override type implNextT = __End_W_MainFHandlingImp

        override def toString(): String = {
          "EPMain_W.RcvFailMtoW"
        }

        override type msgT = MESSAGES.Main.FailMtoW

        override def frm: Role = Role("zk", RoleSet("ZK"))

        override def l: String = "FailMtoW"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailMtoWImp(c, session)
      }

      protected case class __RcvFailMtoWImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailMtoW
        }

        def rcvFrmzk_ZK: (MESSAGES.Main.FailMtoW, __End_W_MainFHandlingImp) = {
          (c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoW], __End_W_MainFHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.FailMtoW, __End_W_MainFHandlingImp), T]): T = {
          f((c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoW], __End_W_MainFHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.FailMtoW = {
          c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoW]
        }

        def ? : MESSAGES.Main.FailMtoW = {
          c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoW]
        }

        def channelCon: __End_W_MainFHandlingImp = {
          __End_W_MainFHandlingImp(c, session)
        }

      }


      protected trait End_W_MainFHandling extends EPMain_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_MainFHandling extends End_W_MainFHandling {
        override protected def __children: List[EPMain_W] = List()

        override type implT = __End_W_MainFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_W.End_W_MainFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_MainFHandlingImp(c, session)
      }

      protected case class __End_W_MainFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_MainFHandling
        }

      }


    }

    object PExecutor_W {
      trait EPPExecutor_W extends __EPType_W

      object EPPExecutor_W extends EPPExecutor_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExecutor_W] = List(Hdl)

        override type implT = __EPPExecutor_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExecutor_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("wEx", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = RoleSet("W")

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExecutor"
      }

      protected case class __EPPExecutor_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExecutor_W
        }

      }


      protected trait Hdl extends EPPExecutor_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExecutor_W] = List(End_W_PExecutor, SelExFailSpawnExFailEnd)

        override type implT = __HdlImp
        override type implNextT = __End_W_PExecutorImp

        override def toString(): String = {
          "EPPExecutor_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait End_W_PExecutor extends EPPExecutor_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExecutor extends End_W_PExecutor {
        override protected def __children: List[EPPExecutor_W] = List()

        override type implT = __End_W_PExecutorImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_W.End_W_PExecutor"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExecutorImp(c, session)
      }

      protected case class __End_W_PExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExecutor
        }

      }


      protected trait SelExFailSpawnExFailEnd extends EPPExecutor_W with event_lang.dsl.ChannelTypeBrn

      protected object SelExFailSpawnExFailEnd extends SelExFailSpawnExFailEnd {
        override protected def __children: List[EPPExecutor_W] = List(RcvExFailSpawn, RcvExFailEnd)

        override type implT = __SelExFailSpawnExFailEndImp
        override type implNextT = __RcvExFailSpawnImp

        override def toString(): String = {
          "EPPExecutor_W.SelExFailSpawnExFailEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelExFailSpawnExFailEndImp(c, session)
      }

      protected case class __SelExFailSpawnExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelExFailSpawnExFailEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvExFailSpawn extends EPPExecutor_W with event_lang.dsl.ChannelTypeRcv

      object RcvExFailSpawn extends RcvExFailSpawn {
        override protected def __children: List[EPPExecutor_W] = List(SpawnPExecutor)

        override type implT = __RcvExFailSpawnImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExecutor_W.RcvExFailSpawn"
        }

        override type msgT = MESSAGES.PExecutor.ExFailSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExFailSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExFailSpawnImp(c, session)
      }

      protected case class __RcvExFailSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExFailSpawn
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExFailSpawn, __SpawnPExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn], __SpawnPExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExFailSpawn, __SpawnPExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn], __SpawnPExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExFailSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn]
        }

        def ? : MESSAGES.PExecutor.ExFailSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn]
        }

        def channelCon: __SpawnPExecutorImp = {
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExecutor_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExecutor_W] = List(End_W_PExecutorExFailSpawn_FHandling)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __End_W_PExecutorExFailSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_W.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_W_PExecutorExFailSpawn_FHandling extends EPPExecutor_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExecutorExFailSpawn_FHandling extends End_W_PExecutorExFailSpawn_FHandling {
        override protected def __children: List[EPPExecutor_W] = List()

        override type implT = __End_W_PExecutorExFailSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_W.End_W_PExecutorExFailSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExecutorExFailSpawn_FHandlingImp(c, session)
      }

      protected case class __End_W_PExecutorExFailSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExecutorExFailSpawn_FHandling
        }

      }


      trait RcvExFailEnd extends EPPExecutor_W with event_lang.dsl.ChannelTypeRcv

      object RcvExFailEnd extends RcvExFailEnd {
        override protected def __children: List[EPPExecutor_W] = List(End_W_PExecutorExFailEnd_FHandling)

        override type implT = __RcvExFailEndImp
        override type implNextT = __End_W_PExecutorExFailEnd_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_W.RcvExFailEnd"
        }

        override type msgT = MESSAGES.PExecutor.ExFailEnd

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExFailEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExFailEndImp(c, session)
      }

      protected case class __RcvExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExFailEnd
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExFailEnd, __End_W_PExecutorExFailEnd_FHandlingImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd], __End_W_PExecutorExFailEnd_FHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExFailEnd, __End_W_PExecutorExFailEnd_FHandlingImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd], __End_W_PExecutorExFailEnd_FHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExFailEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd]
        }

        def ? : MESSAGES.PExecutor.ExFailEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd]
        }

        def channelCon: __End_W_PExecutorExFailEnd_FHandlingImp = {
          __End_W_PExecutorExFailEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_W_PExecutorExFailEnd_FHandling extends EPPExecutor_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExecutorExFailEnd_FHandling extends End_W_PExecutorExFailEnd_FHandling {
        override protected def __children: List[EPPExecutor_W] = List()

        override type implT = __End_W_PExecutorExFailEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_W.End_W_PExecutorExFailEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExecutorExFailEnd_FHandlingImp(c, session)
      }

      protected case class __End_W_PExecutorExFailEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExecutorExFailEnd_FHandling
        }

      }


    }

    object PExSchedule_w_W {
      trait EPPExSchedule_w_W extends __EPType_W

      object EPPExSchedule_w_W extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExSchedule_w_W] = List(Hdl)

        override type implT = __EPPExSchedule_w_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExSchedule_w_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("tw", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("w", RoleSet("W"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExSchedule"
      }

      protected case class __EPPExSchedule_w_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExSchedule_w_W
        }

      }


      protected trait Hdl extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExSchedule_w_W] = List(RecT, SelFailExScheduleSpawnFailExScheduleEnd)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_w_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPPExSchedule_w_W] = List(SelStartExCaseEnd)

        override type implT = __RecTImp
        override type implNextT = __SelStartExCaseEndImp

        override def toString(): String = {
          "EPPExSchedule_w_W.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelStartExCaseEnd extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeBrn

      protected object SelStartExCaseEnd extends SelStartExCaseEnd {
        override protected def __children: List[EPPExSchedule_w_W] = List(RcvStartExCase, RcvEnd)

        override type implT = __SelStartExCaseEndImp
        override type implNextT = __RcvStartExCaseImp

        override def toString(): String = {
          "EPPExSchedule_w_W.SelStartExCaseEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelStartExCaseEndImp(c, session)
      }

      protected case class __SelStartExCaseEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelStartExCaseEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvStartExCase extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvStartExCase extends RcvStartExCase {
        override protected def __children: List[EPPExSchedule_w_W] = List(SpawnPExecutor)

        override type implT = __RcvStartExCaseImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExSchedule_w_W.RcvStartExCase"
        }

        override type msgT = MESSAGES.PExSchedule.StartExCase

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "StartExCase"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvStartExCaseImp(c, session)
      }

      protected case class __RcvStartExCaseImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvStartExCase
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def ? : MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def channelCon: __SpawnPExecutorImp = {
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExSchedule_w_W] = List(T)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPPExSchedule_w_W.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPPExSchedule_w_W] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_w_W.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait RcvEnd extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvEnd extends RcvEnd {
        override protected def __children: List[EPPExSchedule_w_W] = List(End_w_W_PExScheduleEnd)

        override type implT = __RcvEndImp
        override type implNextT = __End_w_W_PExScheduleEndImp

        override def toString(): String = {
          "EPPExSchedule_w_W.RcvEnd"
        }

        override type msgT = MESSAGES.PExSchedule.End

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "End"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvEndImp(c, session)
      }

      protected case class __RcvEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvEnd
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.End, __End_w_W_PExScheduleEndImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_w_W_PExScheduleEndImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.End, __End_w_W_PExScheduleEndImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_w_W_PExScheduleEndImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def ? : MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def channelCon: __End_w_W_PExScheduleEndImp = {
          __End_w_W_PExScheduleEndImp(c, session)
        }

      }


      protected trait End_w_W_PExScheduleEnd extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExScheduleEnd extends End_w_W_PExScheduleEnd {
        override protected def __children: List[EPPExSchedule_w_W] = List()

        override type implT = __End_w_W_PExScheduleEndImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_w_W.End_w_W_PExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExScheduleEndImp(c, session)
      }

      protected case class __End_w_W_PExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExScheduleEnd
        }

      }


      protected trait SelFailExScheduleSpawnFailExScheduleEnd extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeBrn

      protected object SelFailExScheduleSpawnFailExScheduleEnd extends SelFailExScheduleSpawnFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_w_W] = List(RcvFailExScheduleSpawn, RcvFailExScheduleEnd)

        override type implT = __SelFailExScheduleSpawnFailExScheduleEndImp
        override type implNextT = __RcvFailExScheduleSpawnImp

        override def toString(): String = {
          "EPPExSchedule_w_W.SelFailExScheduleSpawnFailExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelFailExScheduleSpawnFailExScheduleEndImp(c, session)
      }

      protected case class __SelFailExScheduleSpawnFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelFailExScheduleSpawnFailExScheduleEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvFailExScheduleSpawn extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailExScheduleSpawn extends RcvFailExScheduleSpawn {
        override protected def __children: List[EPPExSchedule_w_W] = List(SpawnPExSchedule)

        override type implT = __RcvFailExScheduleSpawnImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPExSchedule_w_W.RcvFailExScheduleSpawn"
        }

        override type msgT = MESSAGES.PExSchedule.FailExScheduleSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailExScheduleSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailExScheduleSpawnImp(c, session)
      }

      protected case class __RcvFailExScheduleSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailExScheduleSpawn
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.FailExScheduleSpawn, __SpawnPExScheduleImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn], __SpawnPExScheduleImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.FailExScheduleSpawn, __SpawnPExScheduleImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn], __SpawnPExScheduleImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.FailExScheduleSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn]
        }

        def ? : MESSAGES.PExSchedule.FailExScheduleSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn]
        }

        def channelCon: __SpawnPExScheduleImp = {
          __SpawnPExScheduleImp(c, session)
        }

      }


      trait SpawnPExSchedule extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPExSchedule_w_W] = List(End_w_W_PExScheduleFailExScheduleSpawn_FHandling)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __End_w_W_PExScheduleFailExScheduleSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_w_W.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_w_W_PExScheduleFailExScheduleSpawn_FHandling extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExScheduleFailExScheduleSpawn_FHandling extends End_w_W_PExScheduleFailExScheduleSpawn_FHandling {
        override protected def __children: List[EPPExSchedule_w_W] = List()

        override type implT = __End_w_W_PExScheduleFailExScheduleSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_w_W.End_w_W_PExScheduleFailExScheduleSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExScheduleFailExScheduleSpawn_FHandlingImp(c, session)
      }

      protected case class __End_w_W_PExScheduleFailExScheduleSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExScheduleFailExScheduleSpawn_FHandling
        }

      }


      trait RcvFailExScheduleEnd extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailExScheduleEnd extends RcvFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_w_W] = List(End_w_W_PExScheduleFailExScheduleEnd_FHandling)

        override type implT = __RcvFailExScheduleEndImp
        override type implNextT = __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_w_W.RcvFailExScheduleEnd"
        }

        override type msgT = MESSAGES.PExSchedule.FailExScheduleEnd

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailExScheduleEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailExScheduleEndImp(c, session)
      }

      protected case class __RcvFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailExScheduleEnd
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.FailExScheduleEnd, __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd], __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.FailExScheduleEnd, __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd], __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.FailExScheduleEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd]
        }

        def ? : MESSAGES.PExSchedule.FailExScheduleEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd]
        }

        def channelCon: __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp = {
          __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_w_W_PExScheduleFailExScheduleEnd_FHandling extends EPPExSchedule_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExScheduleFailExScheduleEnd_FHandling extends End_w_W_PExScheduleFailExScheduleEnd_FHandling {
        override protected def __children: List[EPPExSchedule_w_W] = List()

        override type implT = __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_w_W.End_w_W_PExScheduleFailExScheduleEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
      }

      protected case class __End_w_W_PExScheduleFailExScheduleEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExScheduleFailExScheduleEnd_FHandling
        }

      }


    }

    object PExecutor_wEx_W {
      trait EPPExecutor_wEx_W extends __EPType_W

      object EPPExecutor_wEx_W extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExecutor_wEx_W] = List(Hdl)

        override type implT = __EPPExecutor_wEx_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExecutor_wEx_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("wEx", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("wEx", RoleSet("W"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExecutor"
      }

      protected case class __EPPExecutor_wEx_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExecutor_wEx_W
        }

      }


      protected trait Hdl extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExecutor_wEx_W] = List(RcvStartEx, End_wEx_W_PExecutorFHandling)

        override type implT = __HdlImp
        override type implNextT = __RcvStartExImp

        override def toString(): String = {
          "EPPExecutor_wEx_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait RcvStartEx extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeRcv

      object RcvStartEx extends RcvStartEx {
        override protected def __children: List[EPPExecutor_wEx_W] = List(SndExStarted)

        override type implT = __RcvStartExImp
        override type implNextT = __SndExStartedImp

        override def toString(): String = {
          "EPPExecutor_wEx_W.RcvStartEx"
        }

        override type msgT = MESSAGES.PExecutor.StartEx

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "StartEx"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvStartExImp(c, session)
      }

      protected case class __RcvStartExImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvStartEx
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.StartEx, __SndExStartedImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.StartEx], __SndExStartedImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.StartEx, __SndExStartedImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.StartEx], __SndExStartedImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.StartEx = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.StartEx]
        }

        def ? : MESSAGES.PExecutor.StartEx = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.StartEx]
        }

        def channelCon: __SndExStartedImp = {
          __SndExStartedImp(c, session)
        }

      }


      trait SndExStarted extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeSnd

      object SndExStarted extends SndExStarted {
        override protected def __children: List[EPPExecutor_wEx_W] = List(SndExDone)

        override type implT = __SndExStartedImp
        override type implNextT = __SndExDoneImp

        override def toString(): String = {
          "EPPExecutor_wEx_W.SndExStarted"
        }

        override def to: RRole = Role("m", RoleSet("M"))

        override def l: String = "ExStarted"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExStartedImp(c, session)
      }

      protected case class __SndExStartedImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExStarted
        }

        private var notUsed = true

        def sndTom_M(m: MESSAGES.PExecutor.ExStarted): __SndExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SndExDoneImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExStarted): __SndExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SndExDoneImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExStarted): __SndExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SndExDoneImp(c, session)
        }

      }


      trait SndExDone extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeSnd

      object SndExDone extends SndExDone {
        override protected def __children: List[EPPExecutor_wEx_W] = List(End_wEx_W_PExecutor)

        override type implT = __SndExDoneImp
        override type implNextT = __End_wEx_W_PExecutorImp

        override def toString(): String = {
          "EPPExecutor_wEx_W.SndExDone"
        }

        override def to: RRole = Role("m", RoleSet("M"))

        override def l: String = "ExDone"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExDoneImp(c, session)
      }

      protected case class __SndExDoneImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExDone
        }

        private var notUsed = true

        def sndTom_M(m: MESSAGES.PExecutor.ExDone): __End_wEx_W_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_wEx_W_PExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExDone): __End_wEx_W_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_wEx_W_PExecutorImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExDone): __End_wEx_W_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_wEx_W_PExecutorImp(c, session)
        }

      }


      protected trait End_wEx_W_PExecutor extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeEnd

      protected object End_wEx_W_PExecutor extends End_wEx_W_PExecutor {
        override protected def __children: List[EPPExecutor_wEx_W] = List()

        override type implT = __End_wEx_W_PExecutorImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_wEx_W.End_wEx_W_PExecutor"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_wEx_W_PExecutorImp(c, session)
      }

      protected case class __End_wEx_W_PExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_wEx_W_PExecutor
        }

      }


      protected trait End_wEx_W_PExecutorFHandling extends EPPExecutor_wEx_W with event_lang.dsl.ChannelTypeEnd

      protected object End_wEx_W_PExecutorFHandling extends End_wEx_W_PExecutorFHandling {
        override protected def __children: List[EPPExecutor_wEx_W] = List()

        override type implT = __End_wEx_W_PExecutorFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_wEx_W.End_wEx_W_PExecutorFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_wEx_W_PExecutorFHandlingImp(c, session)
      }

      protected case class __End_wEx_W_PExecutorFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_wEx_W_PExecutorFHandling
        }

      }


    }

    object PExSchedule_W {
      trait EPPExSchedule_W extends __EPType_W

      object EPPExSchedule_W extends EPPExSchedule_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExSchedule_W] = List(Hdl)

        override type implT = __EPPExSchedule_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExSchedule_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("tw", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = RoleSet("W")

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExSchedule"
      }

      protected case class __EPPExSchedule_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExSchedule_W
        }

      }


      protected trait Hdl extends EPPExSchedule_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExSchedule_W] = List(RecT, SelFailExScheduleSpawnFailExScheduleEnd)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPPExSchedule_W with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPPExSchedule_W] = List(SelStartExCaseEnd)

        override type implT = __RecTImp
        override type implNextT = __SelStartExCaseEndImp

        override def toString(): String = {
          "EPPExSchedule_W.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelStartExCaseEnd extends EPPExSchedule_W with event_lang.dsl.ChannelTypeBrn

      protected object SelStartExCaseEnd extends SelStartExCaseEnd {
        override protected def __children: List[EPPExSchedule_W] = List(RcvStartExCase, RcvEnd)

        override type implT = __SelStartExCaseEndImp
        override type implNextT = __RcvStartExCaseImp

        override def toString(): String = {
          "EPPExSchedule_W.SelStartExCaseEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelStartExCaseEndImp(c, session)
      }

      protected case class __SelStartExCaseEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelStartExCaseEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvStartExCase extends EPPExSchedule_W with event_lang.dsl.ChannelTypeRcv

      object RcvStartExCase extends RcvStartExCase {
        override protected def __children: List[EPPExSchedule_W] = List(SpawnPExecutor)

        override type implT = __RcvStartExCaseImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExSchedule_W.RcvStartExCase"
        }

        override type msgT = MESSAGES.PExSchedule.StartExCase

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "StartExCase"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvStartExCaseImp(c, session)
      }

      protected case class __RcvStartExCaseImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvStartExCase
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def ? : MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def channelCon: __SpawnPExecutorImp = {
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExSchedule_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExSchedule_W] = List(T)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPPExSchedule_W.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPPExSchedule_W with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPPExSchedule_W] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_W.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait RcvEnd extends EPPExSchedule_W with event_lang.dsl.ChannelTypeRcv

      object RcvEnd extends RcvEnd {
        override protected def __children: List[EPPExSchedule_W] = List(End_W_PExScheduleEnd)

        override type implT = __RcvEndImp
        override type implNextT = __End_W_PExScheduleEndImp

        override def toString(): String = {
          "EPPExSchedule_W.RcvEnd"
        }

        override type msgT = MESSAGES.PExSchedule.End

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "End"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvEndImp(c, session)
      }

      protected case class __RcvEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvEnd
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.End, __End_W_PExScheduleEndImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_W_PExScheduleEndImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.End, __End_W_PExScheduleEndImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_W_PExScheduleEndImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def ? : MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def channelCon: __End_W_PExScheduleEndImp = {
          __End_W_PExScheduleEndImp(c, session)
        }

      }


      protected trait End_W_PExScheduleEnd extends EPPExSchedule_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExScheduleEnd extends End_W_PExScheduleEnd {
        override protected def __children: List[EPPExSchedule_W] = List()

        override type implT = __End_W_PExScheduleEndImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_W.End_W_PExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExScheduleEndImp(c, session)
      }

      protected case class __End_W_PExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExScheduleEnd
        }

      }


      protected trait SelFailExScheduleSpawnFailExScheduleEnd extends EPPExSchedule_W with event_lang.dsl.ChannelTypeBrn

      protected object SelFailExScheduleSpawnFailExScheduleEnd extends SelFailExScheduleSpawnFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_W] = List(RcvFailExScheduleSpawn, RcvFailExScheduleEnd)

        override type implT = __SelFailExScheduleSpawnFailExScheduleEndImp
        override type implNextT = __RcvFailExScheduleSpawnImp

        override def toString(): String = {
          "EPPExSchedule_W.SelFailExScheduleSpawnFailExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelFailExScheduleSpawnFailExScheduleEndImp(c, session)
      }

      protected case class __SelFailExScheduleSpawnFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelFailExScheduleSpawnFailExScheduleEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvFailExScheduleSpawn extends EPPExSchedule_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailExScheduleSpawn extends RcvFailExScheduleSpawn {
        override protected def __children: List[EPPExSchedule_W] = List(SpawnPExSchedule)

        override type implT = __RcvFailExScheduleSpawnImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPExSchedule_W.RcvFailExScheduleSpawn"
        }

        override type msgT = MESSAGES.PExSchedule.FailExScheduleSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailExScheduleSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailExScheduleSpawnImp(c, session)
      }

      protected case class __RcvFailExScheduleSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailExScheduleSpawn
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.FailExScheduleSpawn, __SpawnPExScheduleImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn], __SpawnPExScheduleImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.FailExScheduleSpawn, __SpawnPExScheduleImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn], __SpawnPExScheduleImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.FailExScheduleSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn]
        }

        def ? : MESSAGES.PExSchedule.FailExScheduleSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleSpawn]
        }

        def channelCon: __SpawnPExScheduleImp = {
          __SpawnPExScheduleImp(c, session)
        }

      }


      trait SpawnPExSchedule extends EPPExSchedule_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPExSchedule_W] = List(End_W_PExScheduleFailExScheduleSpawn_FHandling)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __End_W_PExScheduleFailExScheduleSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_W.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_W_PExScheduleFailExScheduleSpawn_FHandling extends EPPExSchedule_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExScheduleFailExScheduleSpawn_FHandling extends End_W_PExScheduleFailExScheduleSpawn_FHandling {
        override protected def __children: List[EPPExSchedule_W] = List()

        override type implT = __End_W_PExScheduleFailExScheduleSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_W.End_W_PExScheduleFailExScheduleSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExScheduleFailExScheduleSpawn_FHandlingImp(c, session)
      }

      protected case class __End_W_PExScheduleFailExScheduleSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExScheduleFailExScheduleSpawn_FHandling
        }

      }


      trait RcvFailExScheduleEnd extends EPPExSchedule_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailExScheduleEnd extends RcvFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_W] = List(End_W_PExScheduleFailExScheduleEnd_FHandling)

        override type implT = __RcvFailExScheduleEndImp
        override type implNextT = __End_W_PExScheduleFailExScheduleEnd_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_W.RcvFailExScheduleEnd"
        }

        override type msgT = MESSAGES.PExSchedule.FailExScheduleEnd

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailExScheduleEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailExScheduleEndImp(c, session)
      }

      protected case class __RcvFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailExScheduleEnd
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.FailExScheduleEnd, __End_W_PExScheduleFailExScheduleEnd_FHandlingImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd], __End_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.FailExScheduleEnd, __End_W_PExScheduleFailExScheduleEnd_FHandlingImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd], __End_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.FailExScheduleEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd]
        }

        def ? : MESSAGES.PExSchedule.FailExScheduleEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.FailExScheduleEnd]
        }

        def channelCon: __End_W_PExScheduleFailExScheduleEnd_FHandlingImp = {
          __End_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_W_PExScheduleFailExScheduleEnd_FHandling extends EPPExSchedule_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PExScheduleFailExScheduleEnd_FHandling extends End_W_PExScheduleFailExScheduleEnd_FHandling {
        override protected def __children: List[EPPExSchedule_W] = List()

        override type implT = __End_W_PExScheduleFailExScheduleEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_W.End_W_PExScheduleFailExScheduleEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
      }

      protected case class __End_W_PExScheduleFailExScheduleEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PExScheduleFailExScheduleEnd_FHandling
        }

      }


    }

    object PExecutor_w_W {
      trait EPPExecutor_w_W extends __EPType_W

      object EPPExecutor_w_W extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExecutor_w_W] = List(Hdl)

        override type implT = __EPPExecutor_w_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExecutor_w_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("wEx", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("w", RoleSet("W"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExecutor"
      }

      protected case class __EPPExecutor_w_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExecutor_w_W
        }

      }


      protected trait Hdl extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExecutor_w_W] = List(RcvExRunning, SelExFailSpawnExFailEnd)

        override type implT = __HdlImp
        override type implNextT = __RcvExRunningImp

        override def toString(): String = {
          "EPPExecutor_w_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait RcvExRunning extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvExRunning extends RcvExRunning {
        override protected def __children: List[EPPExecutor_w_W] = List(RcvExFinishStatus)

        override type implT = __RcvExRunningImp
        override type implNextT = __RcvExFinishStatusImp

        override def toString(): String = {
          "EPPExecutor_w_W.RcvExRunning"
        }

        override type msgT = MESSAGES.PExecutor.ExRunning

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExRunning"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExRunningImp(c, session)
      }

      protected case class __RcvExRunningImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExRunning
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExRunning, __RcvExFinishStatusImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExRunning], __RcvExFinishStatusImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExRunning, __RcvExFinishStatusImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExRunning], __RcvExFinishStatusImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExRunning = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExRunning]
        }

        def ? : MESSAGES.PExecutor.ExRunning = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExRunning]
        }

        def channelCon: __RcvExFinishStatusImp = {
          __RcvExFinishStatusImp(c, session)
        }

      }


      trait RcvExFinishStatus extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvExFinishStatus extends RcvExFinishStatus {
        override protected def __children: List[EPPExecutor_w_W] = List(End_w_W_PExecutor)

        override type implT = __RcvExFinishStatusImp
        override type implNextT = __End_w_W_PExecutorImp

        override def toString(): String = {
          "EPPExecutor_w_W.RcvExFinishStatus"
        }

        override type msgT = MESSAGES.PExecutor.ExFinishStatus

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExFinishStatus"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExFinishStatusImp(c, session)
      }

      protected case class __RcvExFinishStatusImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExFinishStatus
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExFinishStatus, __End_w_W_PExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFinishStatus], __End_w_W_PExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExFinishStatus, __End_w_W_PExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFinishStatus], __End_w_W_PExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExFinishStatus = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFinishStatus]
        }

        def ? : MESSAGES.PExecutor.ExFinishStatus = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFinishStatus]
        }

        def channelCon: __End_w_W_PExecutorImp = {
          __End_w_W_PExecutorImp(c, session)
        }

      }


      protected trait End_w_W_PExecutor extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExecutor extends End_w_W_PExecutor {
        override protected def __children: List[EPPExecutor_w_W] = List()

        override type implT = __End_w_W_PExecutorImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_w_W.End_w_W_PExecutor"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExecutorImp(c, session)
      }

      protected case class __End_w_W_PExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExecutor
        }

      }


      protected trait SelExFailSpawnExFailEnd extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeBrn

      protected object SelExFailSpawnExFailEnd extends SelExFailSpawnExFailEnd {
        override protected def __children: List[EPPExecutor_w_W] = List(RcvExFailSpawn, RcvExFailEnd)

        override type implT = __SelExFailSpawnExFailEndImp
        override type implNextT = __RcvExFailSpawnImp

        override def toString(): String = {
          "EPPExecutor_w_W.SelExFailSpawnExFailEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelExFailSpawnExFailEndImp(c, session)
      }

      protected case class __SelExFailSpawnExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelExFailSpawnExFailEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvExFailSpawn extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvExFailSpawn extends RcvExFailSpawn {
        override protected def __children: List[EPPExecutor_w_W] = List(SpawnPExecutor)

        override type implT = __RcvExFailSpawnImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExecutor_w_W.RcvExFailSpawn"
        }

        override type msgT = MESSAGES.PExecutor.ExFailSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExFailSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExFailSpawnImp(c, session)
      }

      protected case class __RcvExFailSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExFailSpawn
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExFailSpawn, __SpawnPExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn], __SpawnPExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExFailSpawn, __SpawnPExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn], __SpawnPExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExFailSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn]
        }

        def ? : MESSAGES.PExecutor.ExFailSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailSpawn]
        }

        def channelCon: __SpawnPExecutorImp = {
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExecutor_w_W] = List(End_w_W_PExecutorExFailSpawn_FHandling)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __End_w_W_PExecutorExFailSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_w_W.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_w_W_PExecutorExFailSpawn_FHandling extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExecutorExFailSpawn_FHandling extends End_w_W_PExecutorExFailSpawn_FHandling {
        override protected def __children: List[EPPExecutor_w_W] = List()

        override type implT = __End_w_W_PExecutorExFailSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_w_W.End_w_W_PExecutorExFailSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExecutorExFailSpawn_FHandlingImp(c, session)
      }

      protected case class __End_w_W_PExecutorExFailSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExecutorExFailSpawn_FHandling
        }

      }


      trait RcvExFailEnd extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvExFailEnd extends RcvExFailEnd {
        override protected def __children: List[EPPExecutor_w_W] = List(End_w_W_PExecutorExFailEnd_FHandling)

        override type implT = __RcvExFailEndImp
        override type implNextT = __End_w_W_PExecutorExFailEnd_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_w_W.RcvExFailEnd"
        }

        override type msgT = MESSAGES.PExecutor.ExFailEnd

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "ExFailEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExFailEndImp(c, session)
      }

      protected case class __RcvExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExFailEnd
        }

        def rcvFrmm_M: (MESSAGES.PExecutor.ExFailEnd, __End_w_W_PExecutorExFailEnd_FHandlingImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd], __End_w_W_PExecutorExFailEnd_FHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExFailEnd, __End_w_W_PExecutorExFailEnd_FHandlingImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd], __End_w_W_PExecutorExFailEnd_FHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExFailEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd]
        }

        def ? : MESSAGES.PExecutor.ExFailEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExecutor.ExFailEnd]
        }

        def channelCon: __End_w_W_PExecutorExFailEnd_FHandlingImp = {
          __End_w_W_PExecutorExFailEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_w_W_PExecutorExFailEnd_FHandling extends EPPExecutor_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PExecutorExFailEnd_FHandling extends End_w_W_PExecutorExFailEnd_FHandling {
        override protected def __children: List[EPPExecutor_w_W] = List()

        override type implT = __End_w_W_PExecutorExFailEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_w_W.End_w_W_PExecutorExFailEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PExecutorExFailEnd_FHandlingImp(c, session)
      }

      protected case class __End_w_W_PExecutorExFailEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PExecutorExFailEnd_FHandling
        }

      }


    }

    object PDriver_W {
      trait EPPDriver_W extends __EPType_W

      object EPPDriver_W extends EPPDriver_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPDriver_W] = List(Hdl)

        override type implT = __EPPDriver_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPDriver_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")))

        override def argsP: Role = Role("w", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = RoleSet("W")

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PDriver"
      }

      protected case class __EPPDriver_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPDriver_W
        }

      }


      protected trait Hdl extends EPPDriver_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPDriver_W] = List(SpawnPExSchedule, SelFailDriverSpawnFailDriverEnd)

        override type implT = __HdlImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPDriver_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait SpawnPExSchedule extends EPPDriver_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPDriver_W] = List(End_W_PDriver)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __End_W_PDriverImp

        override def toString(): String = {
          "EPPDriver_W.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_W_PDriver extends EPPDriver_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PDriver extends End_W_PDriver {
        override protected def __children: List[EPPDriver_W] = List()

        override type implT = __End_W_PDriverImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_W.End_W_PDriver"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PDriverImp(c, session)
      }

      protected case class __End_W_PDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PDriver
        }

      }


      protected trait SelFailDriverSpawnFailDriverEnd extends EPPDriver_W with event_lang.dsl.ChannelTypeBrn

      protected object SelFailDriverSpawnFailDriverEnd extends SelFailDriverSpawnFailDriverEnd {
        override protected def __children: List[EPPDriver_W] = List(RcvFailDriverSpawn, RcvFailDriverEnd)

        override type implT = __SelFailDriverSpawnFailDriverEndImp
        override type implNextT = __RcvFailDriverSpawnImp

        override def toString(): String = {
          "EPPDriver_W.SelFailDriverSpawnFailDriverEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelFailDriverSpawnFailDriverEndImp(c, session)
      }

      protected case class __SelFailDriverSpawnFailDriverEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelFailDriverSpawnFailDriverEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvFailDriverSpawn extends EPPDriver_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailDriverSpawn extends RcvFailDriverSpawn {
        override protected def __children: List[EPPDriver_W] = List(SpawnPDriver)

        override type implT = __RcvFailDriverSpawnImp
        override type implNextT = __SpawnPDriverImp

        override def toString(): String = {
          "EPPDriver_W.RcvFailDriverSpawn"
        }

        override type msgT = MESSAGES.PDriver.FailDriverSpawn

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailDriverSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailDriverSpawnImp(c, session)
      }

      protected case class __RcvFailDriverSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailDriverSpawn
        }

        def rcvFrmm_M: (MESSAGES.PDriver.FailDriverSpawn, __SpawnPDriverImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverSpawn], __SpawnPDriverImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PDriver.FailDriverSpawn, __SpawnPDriverImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverSpawn], __SpawnPDriverImp(c, session)))
        }

        def rcvMSG: MESSAGES.PDriver.FailDriverSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverSpawn]
        }

        def ? : MESSAGES.PDriver.FailDriverSpawn = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverSpawn]
        }

        def channelCon: __SpawnPDriverImp = {
          __SpawnPDriverImp(c, session)
        }

      }


      trait SpawnPDriver extends EPPDriver_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPDriver extends SpawnPDriver {
        override protected def __children: List[EPPDriver_W] = List(End_W_PDriverFailDriverSpawn_FHandling)

        override type implT = __SpawnPDriverImp
        override type implNextT = __End_W_PDriverFailDriverSpawn_FHandlingImp

        override def toString(): String = {
          "EPPDriver_W.SpawnPDriver"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PDriver"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPDriverImp(c, session)
      }

      protected case class __SpawnPDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPDriver
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_W_PDriverFailDriverSpawn_FHandling extends EPPDriver_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PDriverFailDriverSpawn_FHandling extends End_W_PDriverFailDriverSpawn_FHandling {
        override protected def __children: List[EPPDriver_W] = List()

        override type implT = __End_W_PDriverFailDriverSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_W.End_W_PDriverFailDriverSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PDriverFailDriverSpawn_FHandlingImp(c, session)
      }

      protected case class __End_W_PDriverFailDriverSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PDriverFailDriverSpawn_FHandling
        }

      }


      trait RcvFailDriverEnd extends EPPDriver_W with event_lang.dsl.ChannelTypeRcv

      object RcvFailDriverEnd extends RcvFailDriverEnd {
        override protected def __children: List[EPPDriver_W] = List(End_W_PDriverFailDriverEnd_FHandling)

        override type implT = __RcvFailDriverEndImp
        override type implNextT = __End_W_PDriverFailDriverEnd_FHandlingImp

        override def toString(): String = {
          "EPPDriver_W.RcvFailDriverEnd"
        }

        override type msgT = MESSAGES.PDriver.FailDriverEnd

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "FailDriverEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailDriverEndImp(c, session)
      }

      protected case class __RcvFailDriverEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailDriverEnd
        }

        def rcvFrmm_M: (MESSAGES.PDriver.FailDriverEnd, __End_W_PDriverFailDriverEnd_FHandlingImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverEnd], __End_W_PDriverFailDriverEnd_FHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PDriver.FailDriverEnd, __End_W_PDriverFailDriverEnd_FHandlingImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverEnd], __End_W_PDriverFailDriverEnd_FHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.PDriver.FailDriverEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverEnd]
        }

        def ? : MESSAGES.PDriver.FailDriverEnd = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.FailDriverEnd]
        }

        def channelCon: __End_W_PDriverFailDriverEnd_FHandlingImp = {
          __End_W_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_W_PDriverFailDriverEnd_FHandling extends EPPDriver_W with event_lang.dsl.ChannelTypeEnd

      protected object End_W_PDriverFailDriverEnd_FHandling extends End_W_PDriverFailDriverEnd_FHandling {
        override protected def __children: List[EPPDriver_W] = List()

        override type implT = __End_W_PDriverFailDriverEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_W.End_W_PDriverFailDriverEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_W_PDriverFailDriverEnd_FHandlingImp(c, session)
      }

      protected case class __End_W_PDriverFailDriverEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_W_PDriverFailDriverEnd_FHandling
        }

      }


    }

    object PExSchedule_tw_W {
      trait EPPExSchedule_tw_W extends __EPType_W

      object EPPExSchedule_tw_W extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExSchedule_tw_W] = List(Hdl)

        override type implT = __EPPExSchedule_tw_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExSchedule_tw_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("tw", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("tw", RoleSet("W"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExSchedule"
      }

      protected case class __EPPExSchedule_tw_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExSchedule_tw_W
        }

      }


      protected trait Hdl extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExSchedule_tw_W] = List(RecT, End_tw_W_PExScheduleFHandling)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPPExSchedule_tw_W] = List(SelStartExCaseEnd)

        override type implT = __RecTImp
        override type implNextT = __SelStartExCaseEndImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelStartExCaseEnd extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeBrn

      protected object SelStartExCaseEnd extends SelStartExCaseEnd {
        override protected def __children: List[EPPExSchedule_tw_W] = List(RcvStartExCase, RcvEnd)

        override type implT = __SelStartExCaseEndImp
        override type implNextT = __RcvStartExCaseImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.SelStartExCaseEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelStartExCaseEndImp(c, session)
      }

      protected case class __SelStartExCaseEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelStartExCaseEnd
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvStartExCase extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeRcv

      object RcvStartExCase extends RcvStartExCase {
        override protected def __children: List[EPPExSchedule_tw_W] = List(SpawnPExecutor)

        override type implT = __RcvStartExCaseImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.RcvStartExCase"
        }

        override type msgT = MESSAGES.PExSchedule.StartExCase

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "StartExCase"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvStartExCaseImp(c, session)
      }

      protected case class __RcvStartExCaseImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvStartExCase
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.StartExCase, __SpawnPExecutorImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase], __SpawnPExecutorImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def ? : MESSAGES.PExSchedule.StartExCase = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.StartExCase]
        }

        def channelCon: __SpawnPExecutorImp = {
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExSchedule_tw_W] = List(T)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPPExSchedule_tw_W] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait RcvEnd extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeRcv

      object RcvEnd extends RcvEnd {
        override protected def __children: List[EPPExSchedule_tw_W] = List(End_tw_W_PExScheduleEnd)

        override type implT = __RcvEndImp
        override type implNextT = __End_tw_W_PExScheduleEndImp

        override def toString(): String = {
          "EPPExSchedule_tw_W.RcvEnd"
        }

        override type msgT = MESSAGES.PExSchedule.End

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "End"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvEndImp(c, session)
      }

      protected case class __RcvEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvEnd
        }

        def rcvFrmm_M: (MESSAGES.PExSchedule.End, __End_tw_W_PExScheduleEndImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_tw_W_PExScheduleEndImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExSchedule.End, __End_tw_W_PExScheduleEndImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End], __End_tw_W_PExScheduleEndImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def ? : MESSAGES.PExSchedule.End = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PExSchedule.End]
        }

        def channelCon: __End_tw_W_PExScheduleEndImp = {
          __End_tw_W_PExScheduleEndImp(c, session)
        }

      }


      protected trait End_tw_W_PExScheduleEnd extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeEnd

      protected object End_tw_W_PExScheduleEnd extends End_tw_W_PExScheduleEnd {
        override protected def __children: List[EPPExSchedule_tw_W] = List()

        override type implT = __End_tw_W_PExScheduleEndImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_tw_W.End_tw_W_PExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_tw_W_PExScheduleEndImp(c, session)
      }

      protected case class __End_tw_W_PExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_tw_W_PExScheduleEnd
        }

      }


      protected trait End_tw_W_PExScheduleFHandling extends EPPExSchedule_tw_W with event_lang.dsl.ChannelTypeEnd

      protected object End_tw_W_PExScheduleFHandling extends End_tw_W_PExScheduleFHandling {
        override protected def __children: List[EPPExSchedule_tw_W] = List()

        override type implT = __End_tw_W_PExScheduleFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_tw_W.End_tw_W_PExScheduleFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_tw_W_PExScheduleFHandlingImp(c, session)
      }

      protected case class __End_tw_W_PExScheduleFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_tw_W_PExScheduleFHandling
        }

      }


    }

    object PDriver_w_W {
      trait EPPDriver_w_W extends __EPType_W

      object EPPDriver_w_W extends EPPDriver_w_W with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPDriver_w_W] = List(Hdl)

        override type implT = __EPPDriver_w_WImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPDriver_w_WImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")))

        override def argsP: Role = Role("w", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("w", RoleSet("W"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PDriver"
      }

      protected case class __EPPDriver_w_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPDriver_w_W
        }

      }


      protected trait Hdl extends EPPDriver_w_W with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPDriver_w_W] = List(RcvLaunchDriver, End_w_W_PDriverFHandling)

        override type implT = __HdlImp
        override type implNextT = __RcvLaunchDriverImp

        override def toString(): String = {
          "EPPDriver_w_W.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait RcvLaunchDriver extends EPPDriver_w_W with event_lang.dsl.ChannelTypeRcv

      object RcvLaunchDriver extends RcvLaunchDriver {
        override protected def __children: List[EPPDriver_w_W] = List(SndAckNStatus)

        override type implT = __RcvLaunchDriverImp
        override type implNextT = __SndAckNStatusImp

        override def toString(): String = {
          "EPPDriver_w_W.RcvLaunchDriver"
        }

        override type msgT = MESSAGES.PDriver.LaunchDriver

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "LaunchDriver"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvLaunchDriverImp(c, session)
      }

      protected case class __RcvLaunchDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvLaunchDriver
        }

        def rcvFrmm_M: (MESSAGES.PDriver.LaunchDriver, __SndAckNStatusImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.LaunchDriver], __SndAckNStatusImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PDriver.LaunchDriver, __SndAckNStatusImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.LaunchDriver], __SndAckNStatusImp(c, session)))
        }

        def rcvMSG: MESSAGES.PDriver.LaunchDriver = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.LaunchDriver]
        }

        def ? : MESSAGES.PDriver.LaunchDriver = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.PDriver.LaunchDriver]
        }

        def channelCon: __SndAckNStatusImp = {
          __SndAckNStatusImp(c, session)
        }

      }


      trait SndAckNStatus extends EPPDriver_w_W with event_lang.dsl.ChannelTypeSnd

      object SndAckNStatus extends SndAckNStatus {
        override protected def __children: List[EPPDriver_w_W] = List(SpawnPExSchedule)

        override type implT = __SndAckNStatusImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPDriver_w_W.SndAckNStatus"
        }

        override def to: RRole = Role("m", RoleSet("M"))

        override def l: String = "AckNStatus"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndAckNStatusImp(c, session)
      }

      protected case class __SndAckNStatusImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndAckNStatus
        }

        private var notUsed = true

        def sndTom_M(m: MESSAGES.PDriver.AckNStatus): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SpawnPExScheduleImp(c, session)
        }

        def !(m: MESSAGES.PDriver.AckNStatus): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SpawnPExScheduleImp(c, session)
        }

        def snd(m: MESSAGES.PDriver.AckNStatus): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __SpawnPExScheduleImp(c, session)
        }

      }


      trait SpawnPExSchedule extends EPPDriver_w_W with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPDriver_w_W] = List(SndDriverStateChange)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __SndDriverStateChangeImp

        override def toString(): String = {
          "EPPDriver_w_W.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      trait SndDriverStateChange extends EPPDriver_w_W with event_lang.dsl.ChannelTypeSnd

      object SndDriverStateChange extends SndDriverStateChange {
        override protected def __children: List[EPPDriver_w_W] = List(End_w_W_PDriver)

        override type implT = __SndDriverStateChangeImp
        override type implNextT = __End_w_W_PDriverImp

        override def toString(): String = {
          "EPPDriver_w_W.SndDriverStateChange"
        }

        override def to: RRole = Role("m", RoleSet("M"))

        override def l: String = "DriverStateChange"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndDriverStateChangeImp(c, session)
      }

      protected case class __SndDriverStateChangeImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndDriverStateChange
        }

        private var notUsed = true

        def sndTom_M(m: MESSAGES.PDriver.DriverStateChange): __End_w_W_PDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_w_W_PDriverImp(c, session)
        }

        def !(m: MESSAGES.PDriver.DriverStateChange): __End_w_W_PDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_w_W_PDriverImp(c, session)
        }

        def snd(m: MESSAGES.PDriver.DriverStateChange): __End_w_W_PDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("m", RoleSet("M")), m)
          __End_w_W_PDriverImp(c, session)
        }

      }


      protected trait End_w_W_PDriver extends EPPDriver_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PDriver extends End_w_W_PDriver {
        override protected def __children: List[EPPDriver_w_W] = List()

        override type implT = __End_w_W_PDriverImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_w_W.End_w_W_PDriver"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PDriverImp(c, session)
      }

      protected case class __End_w_W_PDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PDriver
        }

      }


      protected trait End_w_W_PDriverFHandling extends EPPDriver_w_W with event_lang.dsl.ChannelTypeEnd

      protected object End_w_W_PDriverFHandling extends End_w_W_PDriverFHandling {
        override protected def __children: List[EPPDriver_w_W] = List()

        override type implT = __End_w_W_PDriverFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_w_W.End_w_W_PDriverFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_w_W_PDriverFHandlingImp(c, session)
      }

      protected case class __End_w_W_PDriverFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_w_W_PDriverFHandling
        }

      }


    }

  }

  object M {
    val subs: Seq[dsl.ChannelTypeSubS] = List(PExecutor_m_M.EPPExecutor_m_M, PExSchedule_m_M.EPPExSchedule_m_M, PDriver_m_M.EPPDriver_m_M, Main_m_M.EPMain_m_M, Main_M.EPMain_M)

    trait __EPType_M extends AbstractChannelType {

    }

    trait EPType_M[T <: TState] extends AbstractEndPoint[__EPType_M, T] {
      override val roleSet: RoleSet = RoleSet("M")
      override val subs: Seq[dsl.ChannelTypeSubS] = List(PExecutor_m_M.EPPExecutor_m_M, PExSchedule_m_M.EPPExSchedule_m_M, PDriver_m_M.EPPDriver_m_M, Main_m_M.EPMain_m_M, Main_M.EPMain_M)

    }

    object PExecutor_m_M {
      trait EPPExecutor_m_M extends __EPType_M

      object EPPExecutor_m_M extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExecutor_m_M] = List(Hdl)

        override type implT = __EPPExecutor_m_MImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExecutor_m_MImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("wEx", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("m", RoleSet("M"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExecutor"
      }

      protected case class __EPPExecutor_m_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExecutor_m_M
        }

      }


      protected trait Hdl extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExecutor_m_M] = List(SndStartEx, Failed_wEx_W)

        override type implT = __HdlImp
        override type implNextT = __SndStartExImp

        override def toString(): String = {
          "EPPExecutor_m_M.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait SndStartEx extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSnd

      object SndStartEx extends SndStartEx {
        override protected def __children: List[EPPExecutor_m_M] = List(RcvExStarted)

        override type implT = __SndStartExImp
        override type implNextT = __RcvExStartedImp

        override def toString(): String = {
          "EPPExecutor_m_M.SndStartEx"
        }

        override def to: RRole = Role("wEx", RoleSet("W"))

        override def l: String = "StartEx"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndStartExImp(c, session)
      }

      protected case class __SndStartExImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndStartEx
        }

        private var notUsed = true

        def sndTowEx_W(m: MESSAGES.PExecutor.StartEx): __RcvExStartedImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("wEx", RoleSet("W")), m)
          __RcvExStartedImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.StartEx): __RcvExStartedImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("wEx", RoleSet("W")), m)
          __RcvExStartedImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.StartEx): __RcvExStartedImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("wEx", RoleSet("W")), m)
          __RcvExStartedImp(c, session)
        }

      }


      trait RcvExStarted extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeRcv

      object RcvExStarted extends RcvExStarted {
        override protected def __children: List[EPPExecutor_m_M] = List(SndExRunning)

        override type implT = __RcvExStartedImp
        override type implNextT = __SndExRunningImp

        override def toString(): String = {
          "EPPExecutor_m_M.RcvExStarted"
        }

        override type msgT = MESSAGES.PExecutor.ExStarted

        override def frm: Role = Role("wEx", RoleSet("W"))

        override def l: String = "ExStarted"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExStartedImp(c, session)
      }

      protected case class __RcvExStartedImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExStarted
        }

        def rcvFrmwEx_W: (MESSAGES.PExecutor.ExStarted, __SndExRunningImp) = {
          (c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExStarted], __SndExRunningImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExStarted, __SndExRunningImp), T]): T = {
          f((c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExStarted], __SndExRunningImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExStarted = {
          c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExStarted]
        }

        def ? : MESSAGES.PExecutor.ExStarted = {
          c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExStarted]
        }

        def channelCon: __SndExRunningImp = {
          __SndExRunningImp(c, session)
        }

      }


      trait SndExRunning extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSnd

      object SndExRunning extends SndExRunning {
        override protected def __children: List[EPPExecutor_m_M] = List(RcvExDone)

        override type implT = __SndExRunningImp
        override type implNextT = __RcvExDoneImp

        override def toString(): String = {
          "EPPExecutor_m_M.SndExRunning"
        }

        override def to: RRole = Role("w", RoleSet("W"))

        override def l: String = "ExRunning"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExRunningImp(c, session)
      }

      protected case class __SndExRunningImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExRunning
        }

        private var notUsed = true

        def sndTow_W(m: MESSAGES.PExecutor.ExRunning): __RcvExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvExDoneImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExRunning): __RcvExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvExDoneImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExRunning): __RcvExDoneImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvExDoneImp(c, session)
        }

      }


      trait RcvExDone extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeRcv

      object RcvExDone extends RcvExDone {
        override protected def __children: List[EPPExecutor_m_M] = List(SndExFinishStatus)

        override type implT = __RcvExDoneImp
        override type implNextT = __SndExFinishStatusImp

        override def toString(): String = {
          "EPPExecutor_m_M.RcvExDone"
        }

        override type msgT = MESSAGES.PExecutor.ExDone

        override def frm: Role = Role("wEx", RoleSet("W"))

        override def l: String = "ExDone"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvExDoneImp(c, session)
      }

      protected case class __RcvExDoneImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvExDone
        }

        def rcvFrmwEx_W: (MESSAGES.PExecutor.ExDone, __SndExFinishStatusImp) = {
          (c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExDone], __SndExFinishStatusImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PExecutor.ExDone, __SndExFinishStatusImp), T]): T = {
          f((c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExDone], __SndExFinishStatusImp(c, session)))
        }

        def rcvMSG: MESSAGES.PExecutor.ExDone = {
          c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExDone]
        }

        def ? : MESSAGES.PExecutor.ExDone = {
          c.rcv(Role("wEx", RoleSet("W"))).asInstanceOf[MESSAGES.PExecutor.ExDone]
        }

        def channelCon: __SndExFinishStatusImp = {
          __SndExFinishStatusImp(c, session)
        }

      }


      trait SndExFinishStatus extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSnd

      object SndExFinishStatus extends SndExFinishStatus {
        override protected def __children: List[EPPExecutor_m_M] = List(End_m_M_PExecutor)

        override type implT = __SndExFinishStatusImp
        override type implNextT = __End_m_M_PExecutorImp

        override def toString(): String = {
          "EPPExecutor_m_M.SndExFinishStatus"
        }

        override def to: RRole = Role("w", RoleSet("W"))

        override def l: String = "ExFinishStatus"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExFinishStatusImp(c, session)
      }

      protected case class __SndExFinishStatusImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExFinishStatus
        }

        private var notUsed = true

        def sndTow_W(m: MESSAGES.PExecutor.ExFinishStatus): __End_m_M_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __End_m_M_PExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExFinishStatus): __End_m_M_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __End_m_M_PExecutorImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExFinishStatus): __End_m_M_PExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __End_m_M_PExecutorImp(c, session)
        }

      }


      protected trait End_m_M_PExecutor extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExecutor extends End_m_M_PExecutor {
        override protected def __children: List[EPPExecutor_m_M] = List()

        override type implT = __End_m_M_PExecutorImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_m_M.End_m_M_PExecutor"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExecutorImp(c, session)
      }

      protected case class __End_m_M_PExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExecutor
        }

      }


      trait Failed_wEx_W extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeFDtct

      object Failed_wEx_W extends Failed_wEx_W {
        override protected def __children: List[EPPExecutor_m_M] = List(SelExFailSpawnExFailEnd)

        override type implT = __Failed_wEx_WImp
        override type implNextT = __SelExFailSpawnExFailEndImp

        override def toString(): String = {
          "EPPExecutor_m_M.Failed_wEx_W"
        }

        override def suspect: Role = Role("wEx", RoleSet("W"))

        override protected def __create(c: AbstractChannel, session: Session): implT = __Failed_wEx_WImp(c, session)
      }

      protected case class __Failed_wEx_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Failed_wEx_W
        }

        def failed_wEx_W(): __SelExFailSpawnExFailEndImp = { //FIXME: not doing anything for now
          __SelExFailSpawnExFailEndImp(c, session)
        }

      }


      protected trait SelExFailSpawnExFailEnd extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSel

      protected object SelExFailSpawnExFailEnd extends SelExFailSpawnExFailEnd {
        override protected def __children: List[EPPExecutor_m_M] = List(SndExFailSpawn, SndExFailEnd)

        override type implT = __SelExFailSpawnExFailEndImp
        override type implNextT = __SndExFailSpawnImp

        override def toString(): String = {
          "EPPExecutor_m_M.SelExFailSpawnExFailEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelExFailSpawnExFailEndImp(c, session)
      }

      protected case class __SelExFailSpawnExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelExFailSpawnExFailEnd
        }

        private var notUsed = true

        def !(m: MESSAGES.PExecutor.ExFailSpawn): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def sndToW(m: MESSAGES.PExecutor.ExFailSpawn): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExFailEnd): __End_m_M_PExecutorExFailEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
        }

        def sndToW(m: MESSAGES.PExecutor.ExFailEnd): __End_m_M_PExecutorExFailEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
        }

      }


      trait SndExFailSpawn extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSnd

      object SndExFailSpawn extends SndExFailSpawn {
        override protected def __children: List[EPPExecutor_m_M] = List(SpawnPExecutor)

        override type implT = __SndExFailSpawnImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExecutor_m_M.SndExFailSpawn"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "ExFailSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExFailSpawnImp(c, session)
      }

      protected case class __SndExFailSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExFailSpawn
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExecutor.ExFailSpawn): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExFailSpawn): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExFailSpawn): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExecutor_m_M] = List(End_m_M_PExecutorExFailSpawn_FHandling)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __End_m_M_PExecutorExFailSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_m_M.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_m_M_PExecutorExFailSpawn_FHandling extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExecutorExFailSpawn_FHandling extends End_m_M_PExecutorExFailSpawn_FHandling {
        override protected def __children: List[EPPExecutor_m_M] = List()

        override type implT = __End_m_M_PExecutorExFailSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_m_M.End_m_M_PExecutorExFailSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExecutorExFailSpawn_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PExecutorExFailSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExecutorExFailSpawn_FHandling
        }

      }


      trait SndExFailEnd extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeSnd

      object SndExFailEnd extends SndExFailEnd {
        override protected def __children: List[EPPExecutor_m_M] = List(End_m_M_PExecutorExFailEnd_FHandling)

        override type implT = __SndExFailEndImp
        override type implNextT = __End_m_M_PExecutorExFailEnd_FHandlingImp

        override def toString(): String = {
          "EPPExecutor_m_M.SndExFailEnd"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "ExFailEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndExFailEndImp(c, session)
      }

      protected case class __SndExFailEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndExFailEnd
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExecutor.ExFailEnd): __End_m_M_PExecutorExFailEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
        }

        def !(m: MESSAGES.PExecutor.ExFailEnd): __End_m_M_PExecutorExFailEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
        }

        def snd(m: MESSAGES.PExecutor.ExFailEnd): __End_m_M_PExecutorExFailEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_m_M_PExecutorExFailEnd_FHandling extends EPPExecutor_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExecutorExFailEnd_FHandling extends End_m_M_PExecutorExFailEnd_FHandling {
        override protected def __children: List[EPPExecutor_m_M] = List()

        override type implT = __End_m_M_PExecutorExFailEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExecutor_m_M.End_m_M_PExecutorExFailEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExecutorExFailEnd_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PExecutorExFailEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExecutorExFailEnd_FHandling
        }

      }


    }

    object Main_m_M {
      trait EPMain_m_M extends __EPType_M

      object EPMain_m_M extends EPMain_m_M with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPMain_m_M] = List(Hdl)

        override type implT = __EPMain_m_MImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPMain_m_MImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("zk", RoleSet("ZK")))

        override def argsP: Role = Role("m", RoleSet("M"))

        override def argsRs: List[RoleSet] = List(RoleSet("M"), RoleSet("W"))

        override def prjTo: RRole = Role("m", RoleSet("M"))

        override def rootRole: Role = Role("zk", RoleSet("ZK"))

        override def name: String = "Main"
      }

      protected case class __EPMain_m_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPMain_m_M
        }

      }


      protected trait Hdl extends EPMain_m_M with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPMain_m_M] = List(RecT, End_m_M_MainFHandling)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_m_M.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPMain_m_M with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPMain_m_M] = List(SelNewDriverDriverDoneEndCM)

        override type implT = __RecTImp
        override type implNextT = __SelNewDriverDriverDoneEndCMImp

        override def toString(): String = {
          "EPMain_m_M.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelNewDriverDriverDoneEndCM extends EPMain_m_M with event_lang.dsl.ChannelTypeSel

      protected object SelNewDriverDriverDoneEndCM extends SelNewDriverDriverDoneEndCM {
        override protected def __children: List[EPMain_m_M] = List(SndNewDriver, SndDriverDone, SndEndCM)

        override type implT = __SelNewDriverDriverDoneEndCMImp
        override type implNextT = __SndNewDriverImp

        override def toString(): String = {
          "EPMain_m_M.SelNewDriverDriverDoneEndCM"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelNewDriverDriverDoneEndCMImp(c, session)
      }

      protected case class __SelNewDriverDriverDoneEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelNewDriverDriverDoneEndCM
        }

        private var notUsed = true

        def !(m: MESSAGES.Main.NewDriver): __SndPrepSpawnImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndPrepSpawnImp(c, session)
        }

        def sndTozk_ZK(m: MESSAGES.Main.NewDriver): __SndPrepSpawnImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndPrepSpawnImp(c, session)
        }

        def !(m: MESSAGES.Main.DriverDone): __SndBMsgImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndBMsgImp(c, session)
        }

        def sndTozk_ZK(m: MESSAGES.Main.DriverDone): __SndBMsgImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndBMsgImp(c, session)
        }

        def !(m: MESSAGES.Main.EndCM): __SndTerminateImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndTerminateImp(c, session)
        }

        def sndTozk_ZK(m: MESSAGES.Main.EndCM): __SndTerminateImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndTerminateImp(c, session)
        }

      }


      trait SndNewDriver extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndNewDriver extends SndNewDriver {
        override protected def __children: List[EPMain_m_M] = List(SndPrepSpawn)

        override type implT = __SndNewDriverImp
        override type implNextT = __SndPrepSpawnImp

        override def toString(): String = {
          "EPMain_m_M.SndNewDriver"
        }

        override def to: RRole = Role("zk", RoleSet("ZK"))

        override def l: String = "NewDriver"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndNewDriverImp(c, session)
      }

      protected case class __SndNewDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndNewDriver
        }

        private var notUsed = true

        def sndTozk_ZK(m: MESSAGES.Main.NewDriver): __SndPrepSpawnImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndPrepSpawnImp(c, session)
        }

        def !(m: MESSAGES.Main.NewDriver): __SndPrepSpawnImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndPrepSpawnImp(c, session)
        }

        def snd(m: MESSAGES.Main.NewDriver): __SndPrepSpawnImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndPrepSpawnImp(c, session)
        }

      }


      trait SndPrepSpawn extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndPrepSpawn extends SndPrepSpawn {
        override protected def __children: List[EPMain_m_M] = List(SpawnPDriver)

        override type implT = __SndPrepSpawnImp
        override type implNextT = __SpawnPDriverImp

        override def toString(): String = {
          "EPMain_m_M.SndPrepSpawn"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "PrepSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndPrepSpawnImp(c, session)
      }

      protected case class __SndPrepSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndPrepSpawn
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.Main.PrepSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def !(m: MESSAGES.Main.PrepSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def snd(m: MESSAGES.Main.PrepSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

      }


      trait SpawnPDriver extends EPMain_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPDriver extends SpawnPDriver {
        override protected def __children: List[EPMain_m_M] = List(T)

        override type implT = __SpawnPDriverImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_m_M.SpawnPDriver"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PDriver"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPDriverImp(c, session)
      }

      protected case class __SpawnPDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPDriver
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPMain_m_M with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPMain_m_M] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_m_M.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait SndDriverDone extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndDriverDone extends SndDriverDone {
        override protected def __children: List[EPMain_m_M] = List(SndBMsg)

        override type implT = __SndDriverDoneImp
        override type implNextT = __SndBMsgImp

        override def toString(): String = {
          "EPMain_m_M.SndDriverDone"
        }

        override def to: RRole = Role("zk", RoleSet("ZK"))

        override def l: String = "DriverDone"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndDriverDoneImp(c, session)
      }

      protected case class __SndDriverDoneImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndDriverDone
        }

        private var notUsed = true

        def sndTozk_ZK(m: MESSAGES.Main.DriverDone): __SndBMsgImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndBMsgImp(c, session)
        }

        def !(m: MESSAGES.Main.DriverDone): __SndBMsgImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndBMsgImp(c, session)
        }

        def snd(m: MESSAGES.Main.DriverDone): __SndBMsgImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndBMsgImp(c, session)
        }

      }


      trait SndBMsg extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndBMsg extends SndBMsg {
        override protected def __children: List[EPMain_m_M] = List(T)

        override type implT = __SndBMsgImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_m_M.SndBMsg"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "BMsg"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndBMsgImp(c, session)
      }

      protected case class __SndBMsgImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndBMsg
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.Main.BMsg): __TImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __TImp(c, session)
        }

        def !(m: MESSAGES.Main.BMsg): __TImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __TImp(c, session)
        }

        def snd(m: MESSAGES.Main.BMsg): __TImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __TImp(c, session)
        }

      }

      //there was an occurens of t already

      trait SndEndCM extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndEndCM extends SndEndCM {
        override protected def __children: List[EPMain_m_M] = List(SndTerminate)

        override type implT = __SndEndCMImp
        override type implNextT = __SndTerminateImp

        override def toString(): String = {
          "EPMain_m_M.SndEndCM"
        }

        override def to: RRole = Role("zk", RoleSet("ZK"))

        override def l: String = "EndCM"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndEndCMImp(c, session)
      }

      protected case class __SndEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndEndCM
        }

        private var notUsed = true

        def sndTozk_ZK(m: MESSAGES.Main.EndCM): __SndTerminateImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndTerminateImp(c, session)
        }

        def !(m: MESSAGES.Main.EndCM): __SndTerminateImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndTerminateImp(c, session)
        }

        def snd(m: MESSAGES.Main.EndCM): __SndTerminateImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("zk", RoleSet("ZK")), m)
          __SndTerminateImp(c, session)
        }

      }


      trait SndTerminate extends EPMain_m_M with event_lang.dsl.ChannelTypeSnd

      object SndTerminate extends SndTerminate {
        override protected def __children: List[EPMain_m_M] = List(End_m_M_MainEndCM)

        override type implT = __SndTerminateImp
        override type implNextT = __End_m_M_MainEndCMImp

        override def toString(): String = {
          "EPMain_m_M.SndTerminate"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "Terminate"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndTerminateImp(c, session)
      }

      protected case class __SndTerminateImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndTerminate
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.Main.Terminate): __End_m_M_MainEndCMImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_MainEndCMImp(c, session)
        }

        def !(m: MESSAGES.Main.Terminate): __End_m_M_MainEndCMImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_MainEndCMImp(c, session)
        }

        def snd(m: MESSAGES.Main.Terminate): __End_m_M_MainEndCMImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_MainEndCMImp(c, session)
        }

      }


      protected trait End_m_M_MainEndCM extends EPMain_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_MainEndCM extends End_m_M_MainEndCM {
        override protected def __children: List[EPMain_m_M] = List()

        override type implT = __End_m_M_MainEndCMImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_m_M.End_m_M_MainEndCM"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_MainEndCMImp(c, session)
      }

      protected case class __End_m_M_MainEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_MainEndCM
        }

      }


      protected trait End_m_M_MainFHandling extends EPMain_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_MainFHandling extends End_m_M_MainFHandling {
        override protected def __children: List[EPMain_m_M] = List()

        override type implT = __End_m_M_MainFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_m_M.End_m_M_MainFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_MainFHandlingImp(c, session)
      }

      protected case class __End_m_M_MainFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_MainFHandling
        }

      }


    }

    object PExSchedule_m_M {
      trait EPPExSchedule_m_M extends __EPType_M

      object EPPExSchedule_m_M extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPExSchedule_m_M] = List(Hdl)

        override type implT = __EPPExSchedule_m_MImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPExSchedule_m_MImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def argsP: Role = Role("tw", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("m", RoleSet("M"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PExSchedule"
      }

      protected case class __EPPExSchedule_m_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPExSchedule_m_M
        }

      }


      protected trait Hdl extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPExSchedule_m_M] = List(RecT, Failed_tw_W)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_m_M.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPPExSchedule_m_M] = List(SelStartExCaseEnd)

        override type implT = __RecTImp
        override type implNextT = __SelStartExCaseEndImp

        override def toString(): String = {
          "EPPExSchedule_m_M.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelStartExCaseEnd extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSel

      protected object SelStartExCaseEnd extends SelStartExCaseEnd {
        override protected def __children: List[EPPExSchedule_m_M] = List(SndStartExCase, SndEnd)

        override type implT = __SelStartExCaseEndImp
        override type implNextT = __SndStartExCaseImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SelStartExCaseEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelStartExCaseEndImp(c, session)
      }

      protected case class __SelStartExCaseEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelStartExCaseEnd
        }

        private var notUsed = true

        def !(m: MESSAGES.PExSchedule.StartExCase): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def sndToW(m: MESSAGES.PExSchedule.StartExCase): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.End): __End_m_M_PExScheduleEndImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleEndImp(c, session)
        }

        def sndToW(m: MESSAGES.PExSchedule.End): __End_m_M_PExScheduleEndImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleEndImp(c, session)
        }

      }


      trait SndStartExCase extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSnd

      object SndStartExCase extends SndStartExCase {
        override protected def __children: List[EPPExSchedule_m_M] = List(SpawnPExecutor)

        override type implT = __SndStartExCaseImp
        override type implNextT = __SpawnPExecutorImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SndStartExCase"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "StartExCase"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndStartExCaseImp(c, session)
      }

      protected case class __SndStartExCaseImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndStartExCase
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExSchedule.StartExCase): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.StartExCase): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

        def snd(m: MESSAGES.PExSchedule.StartExCase): __SpawnPExecutorImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExecutorImp(c, session)
        }

      }


      trait SpawnPExecutor extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExecutor extends SpawnPExecutor {
        override protected def __children: List[EPPExSchedule_m_M] = List(T)

        override type implT = __SpawnPExecutorImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SpawnPExecutor"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExecutor"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExecutorImp(c, session)
      }

      protected case class __SpawnPExecutorImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExecutor
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait T extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPPExSchedule_m_M] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPPExSchedule_m_M.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait SndEnd extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSnd

      object SndEnd extends SndEnd {
        override protected def __children: List[EPPExSchedule_m_M] = List(End_m_M_PExScheduleEnd)

        override type implT = __SndEndImp
        override type implNextT = __End_m_M_PExScheduleEndImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SndEnd"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "End"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndEndImp(c, session)
      }

      protected case class __SndEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndEnd
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExSchedule.End): __End_m_M_PExScheduleEndImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleEndImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.End): __End_m_M_PExScheduleEndImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleEndImp(c, session)
        }

        def snd(m: MESSAGES.PExSchedule.End): __End_m_M_PExScheduleEndImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleEndImp(c, session)
        }

      }


      protected trait End_m_M_PExScheduleEnd extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExScheduleEnd extends End_m_M_PExScheduleEnd {
        override protected def __children: List[EPPExSchedule_m_M] = List()

        override type implT = __End_m_M_PExScheduleEndImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_m_M.End_m_M_PExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExScheduleEndImp(c, session)
      }

      protected case class __End_m_M_PExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExScheduleEnd
        }

      }


      trait Failed_tw_W extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeFDtct

      object Failed_tw_W extends Failed_tw_W {
        override protected def __children: List[EPPExSchedule_m_M] = List(SelFailExScheduleSpawnFailExScheduleEnd)

        override type implT = __Failed_tw_WImp
        override type implNextT = __SelFailExScheduleSpawnFailExScheduleEndImp

        override def toString(): String = {
          "EPPExSchedule_m_M.Failed_tw_W"
        }

        override def suspect: Role = Role("tw", RoleSet("W"))

        override protected def __create(c: AbstractChannel, session: Session): implT = __Failed_tw_WImp(c, session)
      }

      protected case class __Failed_tw_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Failed_tw_W
        }

        def failed_tw_W(): __SelFailExScheduleSpawnFailExScheduleEndImp = { //FIXME: not doing anything for now
          __SelFailExScheduleSpawnFailExScheduleEndImp(c, session)
        }

      }


      protected trait SelFailExScheduleSpawnFailExScheduleEnd extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSel

      protected object SelFailExScheduleSpawnFailExScheduleEnd extends SelFailExScheduleSpawnFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_m_M] = List(SndFailExScheduleSpawn, SndFailExScheduleEnd)

        override type implT = __SelFailExScheduleSpawnFailExScheduleEndImp
        override type implNextT = __SndFailExScheduleSpawnImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SelFailExScheduleSpawnFailExScheduleEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelFailExScheduleSpawnFailExScheduleEndImp(c, session)
      }

      protected case class __SelFailExScheduleSpawnFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelFailExScheduleSpawnFailExScheduleEnd
        }

        private var notUsed = true

        def !(m: MESSAGES.PExSchedule.FailExScheduleSpawn): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExScheduleImp(c, session)
        }

        def sndToW(m: MESSAGES.PExSchedule.FailExScheduleSpawn): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExScheduleImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.FailExScheduleEnd): __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

        def sndToW(m: MESSAGES.PExSchedule.FailExScheduleEnd): __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

      }


      trait SndFailExScheduleSpawn extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSnd

      object SndFailExScheduleSpawn extends SndFailExScheduleSpawn {
        override protected def __children: List[EPPExSchedule_m_M] = List(SpawnPExSchedule)

        override type implT = __SndFailExScheduleSpawnImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SndFailExScheduleSpawn"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "FailExScheduleSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailExScheduleSpawnImp(c, session)
      }

      protected case class __SndFailExScheduleSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailExScheduleSpawn
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExSchedule.FailExScheduleSpawn): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExScheduleImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.FailExScheduleSpawn): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExScheduleImp(c, session)
        }

        def snd(m: MESSAGES.PExSchedule.FailExScheduleSpawn): __SpawnPExScheduleImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPExScheduleImp(c, session)
        }

      }


      trait SpawnPExSchedule extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPExSchedule_m_M] = List(End_m_M_PExScheduleFailExScheduleSpawn_FHandling)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __End_m_M_PExScheduleFailExScheduleSpawn_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_m_M_PExScheduleFailExScheduleSpawn_FHandling extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExScheduleFailExScheduleSpawn_FHandling extends End_m_M_PExScheduleFailExScheduleSpawn_FHandling {
        override protected def __children: List[EPPExSchedule_m_M] = List()

        override type implT = __End_m_M_PExScheduleFailExScheduleSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_m_M.End_m_M_PExScheduleFailExScheduleSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExScheduleFailExScheduleSpawn_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PExScheduleFailExScheduleSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExScheduleFailExScheduleSpawn_FHandling
        }

      }


      trait SndFailExScheduleEnd extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeSnd

      object SndFailExScheduleEnd extends SndFailExScheduleEnd {
        override protected def __children: List[EPPExSchedule_m_M] = List(End_m_M_PExScheduleFailExScheduleEnd_FHandling)

        override type implT = __SndFailExScheduleEndImp
        override type implNextT = __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp

        override def toString(): String = {
          "EPPExSchedule_m_M.SndFailExScheduleEnd"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "FailExScheduleEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailExScheduleEndImp(c, session)
      }

      protected case class __SndFailExScheduleEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailExScheduleEnd
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PExSchedule.FailExScheduleEnd): __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

        def !(m: MESSAGES.PExSchedule.FailExScheduleEnd): __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

        def snd(m: MESSAGES.PExSchedule.FailExScheduleEnd): __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_m_M_PExScheduleFailExScheduleEnd_FHandling extends EPPExSchedule_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PExScheduleFailExScheduleEnd_FHandling extends End_m_M_PExScheduleFailExScheduleEnd_FHandling {
        override protected def __children: List[EPPExSchedule_m_M] = List()

        override type implT = __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPExSchedule_m_M.End_m_M_PExScheduleFailExScheduleEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PExScheduleFailExScheduleEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PExScheduleFailExScheduleEnd_FHandling
        }

      }


    }

    object PDriver_m_M {
      trait EPPDriver_m_M extends __EPType_M

      object EPPDriver_m_M extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPPDriver_m_M] = List(Hdl)

        override type implT = __EPPDriver_m_MImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPPDriver_m_MImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("m", RoleSet("M")))

        override def argsP: Role = Role("w", RoleSet("W"))

        override def argsRs: List[RoleSet] = List(RoleSet("W"))

        override def prjTo: RRole = Role("m", RoleSet("M"))

        override def rootRole: Role = Role("m", RoleSet("M"))

        override def name: String = "PDriver"
      }

      protected case class __EPPDriver_m_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPPDriver_m_M
        }

      }


      protected trait Hdl extends EPPDriver_m_M with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPPDriver_m_M] = List(SndLaunchDriver, Failed_w_W)

        override type implT = __HdlImp
        override type implNextT = __SndLaunchDriverImp

        override def toString(): String = {
          "EPPDriver_m_M.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      trait SndLaunchDriver extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSnd

      object SndLaunchDriver extends SndLaunchDriver {
        override protected def __children: List[EPPDriver_m_M] = List(RcvAckNStatus)

        override type implT = __SndLaunchDriverImp
        override type implNextT = __RcvAckNStatusImp

        override def toString(): String = {
          "EPPDriver_m_M.SndLaunchDriver"
        }

        override def to: RRole = Role("w", RoleSet("W"))

        override def l: String = "LaunchDriver"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndLaunchDriverImp(c, session)
      }

      protected case class __SndLaunchDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndLaunchDriver
        }

        private var notUsed = true

        def sndTow_W(m: MESSAGES.PDriver.LaunchDriver): __RcvAckNStatusImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvAckNStatusImp(c, session)
        }

        def !(m: MESSAGES.PDriver.LaunchDriver): __RcvAckNStatusImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvAckNStatusImp(c, session)
        }

        def snd(m: MESSAGES.PDriver.LaunchDriver): __RcvAckNStatusImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(Role("w", RoleSet("W")), m)
          __RcvAckNStatusImp(c, session)
        }

      }


      trait RcvAckNStatus extends EPPDriver_m_M with event_lang.dsl.ChannelTypeRcv

      object RcvAckNStatus extends RcvAckNStatus {
        override protected def __children: List[EPPDriver_m_M] = List(SpawnPExSchedule)

        override type implT = __RcvAckNStatusImp
        override type implNextT = __SpawnPExScheduleImp

        override def toString(): String = {
          "EPPDriver_m_M.RcvAckNStatus"
        }

        override type msgT = MESSAGES.PDriver.AckNStatus

        override def frm: Role = Role("w", RoleSet("W"))

        override def l: String = "AckNStatus"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvAckNStatusImp(c, session)
      }

      protected case class __RcvAckNStatusImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvAckNStatus
        }

        def rcvFrmw_W: (MESSAGES.PDriver.AckNStatus, __SpawnPExScheduleImp) = {
          (c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.AckNStatus], __SpawnPExScheduleImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PDriver.AckNStatus, __SpawnPExScheduleImp), T]): T = {
          f((c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.AckNStatus], __SpawnPExScheduleImp(c, session)))
        }

        def rcvMSG: MESSAGES.PDriver.AckNStatus = {
          c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.AckNStatus]
        }

        def ? : MESSAGES.PDriver.AckNStatus = {
          c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.AckNStatus]
        }

        def channelCon: __SpawnPExScheduleImp = {
          __SpawnPExScheduleImp(c, session)
        }

      }


      trait SpawnPExSchedule extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPExSchedule extends SpawnPExSchedule {
        override protected def __children: List[EPPDriver_m_M] = List(RcvDriverStateChange)

        override type implT = __SpawnPExScheduleImp
        override type implNextT = __RcvDriverStateChangeImp

        override def toString(): String = {
          "EPPDriver_m_M.SpawnPExSchedule"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")), Role("w", RoleSet("W")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PExSchedule"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPExScheduleImp(c, session)
      }

      protected case class __SpawnPExScheduleImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPExSchedule
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      trait RcvDriverStateChange extends EPPDriver_m_M with event_lang.dsl.ChannelTypeRcv

      object RcvDriverStateChange extends RcvDriverStateChange {
        override protected def __children: List[EPPDriver_m_M] = List(End_m_M_PDriver)

        override type implT = __RcvDriverStateChangeImp
        override type implNextT = __End_m_M_PDriverImp

        override def toString(): String = {
          "EPPDriver_m_M.RcvDriverStateChange"
        }

        override type msgT = MESSAGES.PDriver.DriverStateChange

        override def frm: Role = Role("w", RoleSet("W"))

        override def l: String = "DriverStateChange"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvDriverStateChangeImp(c, session)
      }

      protected case class __RcvDriverStateChangeImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvDriverStateChange
        }

        def rcvFrmw_W: (MESSAGES.PDriver.DriverStateChange, __End_m_M_PDriverImp) = {
          (c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.DriverStateChange], __End_m_M_PDriverImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.PDriver.DriverStateChange, __End_m_M_PDriverImp), T]): T = {
          f((c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.DriverStateChange], __End_m_M_PDriverImp(c, session)))
        }

        def rcvMSG: MESSAGES.PDriver.DriverStateChange = {
          c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.DriverStateChange]
        }

        def ? : MESSAGES.PDriver.DriverStateChange = {
          c.rcv(Role("w", RoleSet("W"))).asInstanceOf[MESSAGES.PDriver.DriverStateChange]
        }

        def channelCon: __End_m_M_PDriverImp = {
          __End_m_M_PDriverImp(c, session)
        }

      }


      protected trait End_m_M_PDriver extends EPPDriver_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PDriver extends End_m_M_PDriver {
        override protected def __children: List[EPPDriver_m_M] = List()

        override type implT = __End_m_M_PDriverImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_m_M.End_m_M_PDriver"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PDriverImp(c, session)
      }

      protected case class __End_m_M_PDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PDriver
        }

      }


      trait Failed_w_W extends EPPDriver_m_M with event_lang.dsl.ChannelTypeFDtct

      object Failed_w_W extends Failed_w_W {
        override protected def __children: List[EPPDriver_m_M] = List(SelFailDriverSpawnFailDriverEnd)

        override type implT = __Failed_w_WImp
        override type implNextT = __SelFailDriverSpawnFailDriverEndImp

        override def toString(): String = {
          "EPPDriver_m_M.Failed_w_W"
        }

        override def suspect: Role = Role("w", RoleSet("W"))

        override protected def __create(c: AbstractChannel, session: Session): implT = __Failed_w_WImp(c, session)
      }

      protected case class __Failed_w_WImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Failed_w_W
        }

        def failed_w_W(): __SelFailDriverSpawnFailDriverEndImp = { //FIXME: not doing anything for now
          __SelFailDriverSpawnFailDriverEndImp(c, session)
        }

      }


      protected trait SelFailDriverSpawnFailDriverEnd extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSel

      protected object SelFailDriverSpawnFailDriverEnd extends SelFailDriverSpawnFailDriverEnd {
        override protected def __children: List[EPPDriver_m_M] = List(SndFailDriverSpawn, SndFailDriverEnd)

        override type implT = __SelFailDriverSpawnFailDriverEndImp
        override type implNextT = __SndFailDriverSpawnImp

        override def toString(): String = {
          "EPPDriver_m_M.SelFailDriverSpawnFailDriverEnd"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelFailDriverSpawnFailDriverEndImp(c, session)
      }

      protected case class __SelFailDriverSpawnFailDriverEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelFailDriverSpawnFailDriverEnd
        }

        private var notUsed = true

        def !(m: MESSAGES.PDriver.FailDriverSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def sndToW(m: MESSAGES.PDriver.FailDriverSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def !(m: MESSAGES.PDriver.FailDriverEnd): __End_m_M_PDriverFailDriverEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

        def sndToW(m: MESSAGES.PDriver.FailDriverEnd): __End_m_M_PDriverFailDriverEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

      }


      trait SndFailDriverSpawn extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSnd

      object SndFailDriverSpawn extends SndFailDriverSpawn {
        override protected def __children: List[EPPDriver_m_M] = List(SpawnPDriver)

        override type implT = __SndFailDriverSpawnImp
        override type implNextT = __SpawnPDriverImp

        override def toString(): String = {
          "EPPDriver_m_M.SndFailDriverSpawn"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "FailDriverSpawn"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailDriverSpawnImp(c, session)
      }

      protected case class __SndFailDriverSpawnImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailDriverSpawn
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PDriver.FailDriverSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def !(m: MESSAGES.PDriver.FailDriverSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

        def snd(m: MESSAGES.PDriver.FailDriverSpawn): __SpawnPDriverImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __SpawnPDriverImp(c, session)
        }

      }


      trait SpawnPDriver extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSpawn

      object SpawnPDriver extends SpawnPDriver {
        override protected def __children: List[EPPDriver_m_M] = List(End_m_M_PDriverFailDriverSpawn_FHandling)

        override type implT = __SpawnPDriverImp
        override type implNextT = __End_m_M_PDriverFailDriverSpawn_FHandlingImp

        override def toString(): String = {
          "EPPDriver_m_M.SpawnPDriver"
        }

        override def y: List[Role] = List(Role("m", RoleSet("M")))

        override def pickR: RoleSet = RoleSet("W")

        override def rs: List[RoleSet] = List(RoleSet("W"))

        override def name: String = "PDriver"

        override def subC(r: RRole): event_lang.dsl.ChannelTypeSubS = {
          null
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SpawnPDriverImp(c, session)
      }

      protected case class __SpawnPDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SpawnPDriver
        }
        // SPAWN is handled internally -- i.e. no use code here
      }


      protected trait End_m_M_PDriverFailDriverSpawn_FHandling extends EPPDriver_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PDriverFailDriverSpawn_FHandling extends End_m_M_PDriverFailDriverSpawn_FHandling {
        override protected def __children: List[EPPDriver_m_M] = List()

        override type implT = __End_m_M_PDriverFailDriverSpawn_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_m_M.End_m_M_PDriverFailDriverSpawn_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PDriverFailDriverSpawn_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PDriverFailDriverSpawn_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PDriverFailDriverSpawn_FHandling
        }

      }


      trait SndFailDriverEnd extends EPPDriver_m_M with event_lang.dsl.ChannelTypeSnd

      object SndFailDriverEnd extends SndFailDriverEnd {
        override protected def __children: List[EPPDriver_m_M] = List(End_m_M_PDriverFailDriverEnd_FHandling)

        override type implT = __SndFailDriverEndImp
        override type implNextT = __End_m_M_PDriverFailDriverEnd_FHandlingImp

        override def toString(): String = {
          "EPPDriver_m_M.SndFailDriverEnd"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "FailDriverEnd"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailDriverEndImp(c, session)
      }

      protected case class __SndFailDriverEndImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailDriverEnd
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.PDriver.FailDriverEnd): __End_m_M_PDriverFailDriverEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

        def !(m: MESSAGES.PDriver.FailDriverEnd): __End_m_M_PDriverFailDriverEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

        def snd(m: MESSAGES.PDriver.FailDriverEnd): __End_m_M_PDriverFailDriverEnd_FHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
        }

      }


      protected trait End_m_M_PDriverFailDriverEnd_FHandling extends EPPDriver_m_M with event_lang.dsl.ChannelTypeEnd

      protected object End_m_M_PDriverFailDriverEnd_FHandling extends End_m_M_PDriverFailDriverEnd_FHandling {
        override protected def __children: List[EPPDriver_m_M] = List()

        override type implT = __End_m_M_PDriverFailDriverEnd_FHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPPDriver_m_M.End_m_M_PDriverFailDriverEnd_FHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_m_M_PDriverFailDriverEnd_FHandlingImp(c, session)
      }

      protected case class __End_m_M_PDriverFailDriverEnd_FHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_m_M_PDriverFailDriverEnd_FHandling
        }

      }


    }

    object Main_M {
      trait EPMain_M extends __EPType_M

      object EPMain_M extends EPMain_M with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPMain_M] = List(Hdl)

        override type implT = __EPMain_MImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPMain_MImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("zk", RoleSet("ZK")))

        override def argsP: Role = Role("m", RoleSet("M"))

        override def argsRs: List[RoleSet] = List(RoleSet("M"), RoleSet("W"))

        override def prjTo: RRole = RoleSet("M")

        override def rootRole: Role = Role("zk", RoleSet("ZK"))

        override def name: String = "Main"
      }

      protected case class __EPMain_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPMain_M
        }

      }


      protected trait Hdl extends EPMain_M with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPMain_M] = List(End_M_Main, RcvFailMtoM)

        override type implT = __HdlImp
        override type implNextT = __End_M_MainImp

        override def toString(): String = {
          "EPMain_M.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait End_M_Main extends EPMain_M with event_lang.dsl.ChannelTypeEnd

      protected object End_M_Main extends End_M_Main {
        override protected def __children: List[EPMain_M] = List()

        override type implT = __End_M_MainImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_M.End_M_Main"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_M_MainImp(c, session)
      }

      protected case class __End_M_MainImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_M_Main
        }

      }


      trait RcvFailMtoM extends EPMain_M with event_lang.dsl.ChannelTypeRcv

      object RcvFailMtoM extends RcvFailMtoM {
        override protected def __children: List[EPMain_M] = List(End_M_MainFHandling)

        override type implT = __RcvFailMtoMImp
        override type implNextT = __End_M_MainFHandlingImp

        override def toString(): String = {
          "EPMain_M.RcvFailMtoM"
        }

        override type msgT = MESSAGES.Main.FailMtoM

        override def frm: Role = Role("zk", RoleSet("ZK"))

        override def l: String = "FailMtoM"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvFailMtoMImp(c, session)
      }

      protected case class __RcvFailMtoMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvFailMtoM
        }

        def rcvFrmzk_ZK: (MESSAGES.Main.FailMtoM, __End_M_MainFHandlingImp) = {
          (c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoM], __End_M_MainFHandlingImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.FailMtoM, __End_M_MainFHandlingImp), T]): T = {
          f((c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoM], __End_M_MainFHandlingImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.FailMtoM = {
          c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoM]
        }

        def ? : MESSAGES.Main.FailMtoM = {
          c.rcv(Role("zk", RoleSet("ZK"))).asInstanceOf[MESSAGES.Main.FailMtoM]
        }

        def channelCon: __End_M_MainFHandlingImp = {
          __End_M_MainFHandlingImp(c, session)
        }

      }


      protected trait End_M_MainFHandling extends EPMain_M with event_lang.dsl.ChannelTypeEnd

      protected object End_M_MainFHandling extends End_M_MainFHandling {
        override protected def __children: List[EPMain_M] = List()

        override type implT = __End_M_MainFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_M.End_M_MainFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_M_MainFHandlingImp(c, session)
      }

      protected case class __End_M_MainFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_M_MainFHandling
        }

      }


    }

  }

  object ZK {
    val subs: Seq[dsl.ChannelTypeSubS] = List(Main_zk_ZK.EPMain_zk_ZK)

    trait __EPType_ZK extends AbstractChannelType {

    }

    trait EPType_ZK[T <: TState] extends AbstractEndPoint[__EPType_ZK, T] {
      override val roleSet: RoleSet = RoleSet("ZK")
      override val subs: Seq[dsl.ChannelTypeSubS] = List(Main_zk_ZK.EPMain_zk_ZK)

    }

    object Main_zk_ZK {
      trait EPMain_zk_ZK extends __EPType_ZK

      object EPMain_zk_ZK extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeSubS {
        override protected def __children: List[EPMain_zk_ZK] = List(Hdl)

        override type implT = __EPMain_zk_ZKImp

        override def __create(c: AbstractChannel, session: Session): implT = __EPMain_zk_ZKImp(c, session)

        override def body: AbstractChannelType = children.head

        override def argsC: List[Role] = List(Role("zk", RoleSet("ZK")))

        override def argsP: Role = Role("m", RoleSet("M"))

        override def argsRs: List[RoleSet] = List(RoleSet("M"), RoleSet("W"))

        override def prjTo: RRole = Role("zk", RoleSet("ZK"))

        override def rootRole: Role = Role("zk", RoleSet("ZK"))

        override def name: String = "Main"
      }

      protected case class __EPMain_zk_ZKImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          EPMain_zk_ZK
        }

      }


      protected trait Hdl extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeHdl

      protected object Hdl extends Hdl {
        override protected def __children: List[EPMain_zk_ZK] = List(RecT, Failed_m_M)

        override type implT = __HdlImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_zk_ZK.Hdl"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __HdlImp(c, session)
      }

      protected case class __HdlImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Hdl
        }

      }


      protected trait RecT extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeRec

      protected object RecT extends RecT {
        override protected def __children: List[EPMain_zk_ZK] = List(SelNewDriverDriverDoneEndCM)

        override type implT = __RecTImp
        override type implNextT = __SelNewDriverDriverDoneEndCMImp

        override def toString(): String = {
          "EPMain_zk_ZK.RecT"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __RecTImp(c, session)
      }

      protected case class __RecTImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RecT
        }

      }


      protected trait SelNewDriverDriverDoneEndCM extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeBrn

      protected object SelNewDriverDriverDoneEndCM extends SelNewDriverDriverDoneEndCM {
        override protected def __children: List[EPMain_zk_ZK] = List(RcvNewDriver, RcvDriverDone, RcvEndCM)

        override type implT = __SelNewDriverDriverDoneEndCMImp
        override type implNextT = __RcvNewDriverImp

        override def toString(): String = {
          "EPMain_zk_ZK.SelNewDriverDriverDoneEndCM"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __SelNewDriverDriverDoneEndCMImp(c, session)
      }

      protected case class __SelNewDriverDriverDoneEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SelNewDriverDriverDoneEndCM
        }
        // Branching is only a valid return type not a valid input type
      }


      trait RcvNewDriver extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeRcv

      object RcvNewDriver extends RcvNewDriver {
        override protected def __children: List[EPMain_zk_ZK] = List(T)

        override type implT = __RcvNewDriverImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_zk_ZK.RcvNewDriver"
        }

        override type msgT = MESSAGES.Main.NewDriver

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "NewDriver"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvNewDriverImp(c, session)
      }

      protected case class __RcvNewDriverImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvNewDriver
        }

        def rcvFrmm_M: (MESSAGES.Main.NewDriver, __TImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.NewDriver], __TImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.NewDriver, __TImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.NewDriver], __TImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.NewDriver = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.NewDriver]
        }

        def ? : MESSAGES.Main.NewDriver = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.NewDriver]
        }

        def channelCon: __TImp = {
          __TImp(c, session)
        }

      }


      protected trait T extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeT

      protected object T extends T {
        override protected def __children: List[EPMain_zk_ZK] = List(RecT)

        override type implT = __TImp
        override type implNextT = __RecTImp

        override def toString(): String = {
          "EPMain_zk_ZK.T"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __TImp(c, session)
      }

      protected case class __TImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          T
        }

      }


      trait RcvDriverDone extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeRcv

      object RcvDriverDone extends RcvDriverDone {
        override protected def __children: List[EPMain_zk_ZK] = List(T)

        override type implT = __RcvDriverDoneImp
        override type implNextT = __TImp

        override def toString(): String = {
          "EPMain_zk_ZK.RcvDriverDone"
        }

        override type msgT = MESSAGES.Main.DriverDone

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "DriverDone"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvDriverDoneImp(c, session)
      }

      protected case class __RcvDriverDoneImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvDriverDone
        }

        def rcvFrmm_M: (MESSAGES.Main.DriverDone, __TImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.DriverDone], __TImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.DriverDone, __TImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.DriverDone], __TImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.DriverDone = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.DriverDone]
        }

        def ? : MESSAGES.Main.DriverDone = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.DriverDone]
        }

        def channelCon: __TImp = {
          __TImp(c, session)
        }

      }

      //there was an occurens of t already

      trait RcvEndCM extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeRcv

      object RcvEndCM extends RcvEndCM {
        override protected def __children: List[EPMain_zk_ZK] = List(End_zk_ZK_MainEndCM)

        override type implT = __RcvEndCMImp
        override type implNextT = __End_zk_ZK_MainEndCMImp

        override def toString(): String = {
          "EPMain_zk_ZK.RcvEndCM"
        }

        override type msgT = MESSAGES.Main.EndCM

        override def frm: Role = Role("m", RoleSet("M"))

        override def l: String = "EndCM"

        override protected def __create(c: AbstractChannel, session: Session): implT = __RcvEndCMImp(c, session)
      }

      protected case class __RcvEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          RcvEndCM
        }

        def rcvFrmm_M: (MESSAGES.Main.EndCM, __End_zk_ZK_MainEndCMImp) = {
          (c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.EndCM], __End_zk_ZK_MainEndCMImp(c, session))
        }

        def ?[T](f: PartialFunction[(MESSAGES.Main.EndCM, __End_zk_ZK_MainEndCMImp), T]): T = {
          f((c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.EndCM], __End_zk_ZK_MainEndCMImp(c, session)))
        }

        def rcvMSG: MESSAGES.Main.EndCM = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.EndCM]
        }

        def ? : MESSAGES.Main.EndCM = {
          c.rcv(Role("m", RoleSet("M"))).asInstanceOf[MESSAGES.Main.EndCM]
        }

        def channelCon: __End_zk_ZK_MainEndCMImp = {
          __End_zk_ZK_MainEndCMImp(c, session)
        }

      }


      protected trait End_zk_ZK_MainEndCM extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeEnd

      protected object End_zk_ZK_MainEndCM extends End_zk_ZK_MainEndCM {
        override protected def __children: List[EPMain_zk_ZK] = List()

        override type implT = __End_zk_ZK_MainEndCMImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_zk_ZK.End_zk_ZK_MainEndCM"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_zk_ZK_MainEndCMImp(c, session)
      }

      protected case class __End_zk_ZK_MainEndCMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_zk_ZK_MainEndCM
        }

      }


      trait Failed_m_M extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeFDtct

      object Failed_m_M extends Failed_m_M {
        override protected def __children: List[EPMain_zk_ZK] = List(SndFailMtoM)

        override type implT = __Failed_m_MImp
        override type implNextT = __SndFailMtoMImp

        override def toString(): String = {
          "EPMain_zk_ZK.Failed_m_M"
        }

        override def suspect: Role = Role("m", RoleSet("M"))

        override protected def __create(c: AbstractChannel, session: Session): implT = __Failed_m_MImp(c, session)
      }

      protected case class __Failed_m_MImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          Failed_m_M
        }

        def failed_m_M(): __SndFailMtoMImp = { //FIXME: not doing anything for now
          __SndFailMtoMImp(c, session)
        }

      }


      trait SndFailMtoM extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeSnd

      object SndFailMtoM extends SndFailMtoM {
        override protected def __children: List[EPMain_zk_ZK] = List(SndFailMtoW)

        override type implT = __SndFailMtoMImp
        override type implNextT = __SndFailMtoWImp

        override def toString(): String = {
          "EPMain_zk_ZK.SndFailMtoM"
        }

        override def to: RRole = RoleSet("M")

        override def l: String = "FailMtoM"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailMtoMImp(c, session)
      }

      protected case class __SndFailMtoMImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailMtoM
        }

        private var notUsed = true

        def sndToM(m: MESSAGES.Main.FailMtoM): __SndFailMtoWImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("M"), m)
          __SndFailMtoWImp(c, session)
        }

        def !(m: MESSAGES.Main.FailMtoM): __SndFailMtoWImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("M"), m)
          __SndFailMtoWImp(c, session)
        }

        def snd(m: MESSAGES.Main.FailMtoM): __SndFailMtoWImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("M"), m)
          __SndFailMtoWImp(c, session)
        }

      }


      trait SndFailMtoW extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeSnd

      object SndFailMtoW extends SndFailMtoW {
        override protected def __children: List[EPMain_zk_ZK] = List(End_zk_ZK_MainFHandling)

        override type implT = __SndFailMtoWImp
        override type implNextT = __End_zk_ZK_MainFHandlingImp

        override def toString(): String = {
          "EPMain_zk_ZK.SndFailMtoW"
        }

        override def to: RRole = RoleSet("W")

        override def l: String = "FailMtoW"

        override protected def __create(c: AbstractChannel, session: Session): implT = __SndFailMtoWImp(c, session)
      }

      protected case class __SndFailMtoWImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          SndFailMtoW
        }

        private var notUsed = true

        def sndToW(m: MESSAGES.Main.FailMtoW): __End_zk_ZK_MainFHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_zk_ZK_MainFHandlingImp(c, session)
        }

        def !(m: MESSAGES.Main.FailMtoW): __End_zk_ZK_MainFHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_zk_ZK_MainFHandlingImp(c, session)
        }

        def snd(m: MESSAGES.Main.FailMtoW): __End_zk_ZK_MainFHandlingImp = {
          assert(notUsed, s"The channel send musted be used linear")
          notUsed = false
          c.snd(RoleSet("W"), m)
          __End_zk_ZK_MainFHandlingImp(c, session)
        }

      }


      protected trait End_zk_ZK_MainFHandling extends EPMain_zk_ZK with event_lang.dsl.ChannelTypeEnd

      protected object End_zk_ZK_MainFHandling extends End_zk_ZK_MainFHandling {
        override protected def __children: List[EPMain_zk_ZK] = List()

        override type implT = __End_zk_ZK_MainFHandlingImp
        override type implNextT = Nothing

        override def toString(): String = {
          "EPMain_zk_ZK.End_zk_ZK_MainFHandling"
        }

        override protected def __create(c: AbstractChannel, session: Session): implT = __End_zk_ZK_MainFHandlingImp(c, session)
      }

      protected case class __End_zk_ZK_MainFHandlingImp(private val c: AbstractChannel, session: Session) extends AbstractChannelImp {
        override def from: AbstractChannelType = {
          End_zk_ZK_MainFHandling
        }

      }


    }

  }

}
