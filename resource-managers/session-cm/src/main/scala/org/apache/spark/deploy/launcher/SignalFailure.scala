package org.apache.spark.deploy.launcher

import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.Master.Messages
import org.apache.spark.deploy.Master.Messages.SparkAppFinished
import org.apache.spark.rpc.{RpcAddress, RpcCallContext, RpcEnv, ThreadSafeRpcEndpoint}
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}

import java.util.concurrent.atomic.AtomicBoolean

object SignalFailure {
  def main(args: Array[String]): Unit = {

    args.toList match {
      case myIP :: myPort :: myName :: remoteIP :: remotePort :: failID :: rest =>

        val pConf = ConfigFactory.load()


        //        :: numNun :: appName :: appArgs =>
        def startRpcEnvAndEndpoint(name: String,
                                   host: String,
                                   port: Int,
                                   webUiPort: Int,
                                   conf: SparkConf): RpcEnv = {
          val securityMgr = new SecurityManager(conf)
          val rpcEnv = RpcEnv.create(name, host, port, conf, securityMgr)
          rpcEnv
        }

        val done = new AtomicBoolean(false)

        class SubmitRPCEP(val rpcEnv: RpcEnv) extends ThreadSafeRpcEndpoint {
          override def receive: PartialFunction[Any, Unit] = {
            // replay from master goes here
            case m: SparkAppFinished =>
              println(s"receive $m")
              //              this.stop()
              done.set(true)
            case m => println(s"receive $m")
          }

          override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
            case m => println(s"receive $m")
          }
        }
        val conf = new SparkConf
        conf.set("SPARK_SCALA_VERSION", "2.12")
        val rpcEnvM = startRpcEnvAndEndpoint(myName, Utils.localHostName(), myPort.toInt, 0, conf)
        val myRef = rpcEnvM.setupEndpoint("client", new SubmitRPCEP(rpcEnvM))

        //        val mRef = rpcEnvM.setupEndpoint("m", new SubmitRPCEP(rpcEnvM))
        val mRef = rpcEnvM.setupEndpointRef(RpcAddress(remoteIP, remotePort.toInt), "m")

        mRef.send(Messages.SignalFailure(failID.toInt))

        //rpcEnvM.awaitTermination()


    }


  }
}
