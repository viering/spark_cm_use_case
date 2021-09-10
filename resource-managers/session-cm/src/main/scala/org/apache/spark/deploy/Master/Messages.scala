package org.apache.spark.deploy.Master

import org.apache.spark.deploy.{ApplicationDescription, DriverDescription}
import org.apache.spark.rpc.RpcEndpointRef

object Messages {

  case class SubmitApp(app : AbstractDriver)
  case class SignalFailure(p : Int)
  case class SparkAppFinished(app : AbstractDriver,status : org.apache.spark.deploy.master.DriverState.DriverState)
  case class StopCM()
  case class DeployStartEx(rp : RpcEndpointRef,appID: AppId, app : ApplicationDescription)
  case class StartExInternal(driver : RpcEndpointRef, appDS: ApplicationDescription)

  case class StartDriver(ds: DriverDescription)

  case class DriverStarted()


  case class ExNum(i : Int)
  case class ExNumAck(b : Boolean)

  case class WorkerData(maxCore: Int, maxMem: Int, curCoreUsage: Int=0, curMemUsage: Int=0) {
    //-1 is a quick hack for a failed worker
    assert(maxCore == -1 || (maxCore >= curCoreUsage && maxMem >= curMemUsage), s"WorkerData is not sensible $this")
    assert(maxMem >= 0 && curCoreUsage >= 0 && curMemUsage >= 0,s"$maxMem >= 0 && $curCoreUsage >= 0 && $curMemUsage >= 0 (maxCores = $maxCore)")

    def availableCores: Int = maxCore - curCoreUsage

    def availableMem: Int = maxMem - curMemUsage
  }

  case class DriverInfo(driverD: DriverDescription)

  case class AppInfo(appD: ApplicationDescription)


  case class SparkData(driverD: DriverDescription,maxCores: Int, memPerEx : Int, corePerEx : Option[Int], subRPCEpRef : Option[RpcEndpointRef]= None)//, appD: ApplicationDescription
  case class SparkAppDescription(appD : ApplicationDescription)
  type AppId = Int
  type WorkerID = Long
  type ExId = Int
  type AbstractDriver = SparkData
  type AppDescription = SparkAppDescription
  type Task = Object
}
