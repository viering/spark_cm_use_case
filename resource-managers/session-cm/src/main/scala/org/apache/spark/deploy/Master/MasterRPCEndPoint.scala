package org.apache.spark.deploy.Master

import java.util.concurrent.LinkedBlockingQueue

import event_lang.network.SessionChannelEP
import org.apache.spark.deploy.ApplicationDescription
import org.apache.spark.deploy.Master.Messages.{AbstractDriver, AppId, DeployStartEx, SignalFailure, StopCM, SubmitApp}
import org.apache.spark.rpc.{RpcEndpointRef, RpcEnv, ThreadSafeRpcEndpoint}

class MasterRPCEndPoint(override val rpcEnv: RpcEnv, waitForDeployMessages: Boolean = true) extends ThreadSafeRpcEndpoint {
  var rpcState: MRPCState = MRPCState()
  val rpcEP: RpcEndpointRef = rpcEnv.setupEndpoint("m", this)

  println(s"[MasterRPC] ${rpcEP.address} -- ${rpcEP.name}")

  @volatile var regApps: Map[AppId, (ApplicationDescription, RpcEndpointRef)] = Map()
  @volatile var stopMaster = !waitForDeployMessages
  val subApps = new LinkedBlockingQueue[AbstractDriver]()

  var network: SessionChannelEP = null
  var idOfWorkerStartedLastEx = -1

  override def receive: PartialFunction[Any, Unit] = {
    case m => println(s"[master rpc] receive $m")
      m match {
        case m: DeployStartEx =>
          regApps = regApps + (m.appID -> (m.app, m.rp))
          println(s"regApps: ${regApps.mkString(",")}")
        case m: SubmitApp =>
          println(s"submitApp $m")
          subApps.put(m.app)
        case m: StopCM =>
          stopMaster = true
          // We currently use a failure oracle that supplies SignalFailure to trigger failure handling
        case m: SignalFailure =>
          println(s"Signal failure of: ${m.p}")
          if (m.p >= 0) {
            network.signalFail(m.p)
          } else {
            //Todo, this is a work around to signal the failure of the last worker -- we use it in the crash bench eval
            //
            println(s"Signal failure of: ${idOfWorkerStartedLastEx} -- as that is the id of the worker who started the last executor")
            network.signalFail(idOfWorkerStartedLastEx)
          }

        case _ =>
      }
  }

  def materURl: String = rpcEnv.address.toString()
}