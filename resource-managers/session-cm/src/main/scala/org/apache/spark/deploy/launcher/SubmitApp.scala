package org.apache.spark.deploy.launcher

import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.Master.Messages
import org.apache.spark.deploy.Master.Messages.SparkAppFinished
import org.apache.spark.deploy.launcher.Util.createSimpleApp
import org.apache.spark.rpc.{RpcAddress, RpcCallContext, RpcEnv, ThreadSafeRpcEndpoint}
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}
import pprint.pprintln

import java.util.concurrent.atomic.AtomicBoolean
object SubmitApp {
  def main(args: Array[String]): Unit = {

    args.toList match {
      case myIP :: myPort :: myName :: remoteIP :: remotePort :: rest =>

        val pConf = ConfigFactory.load()

        var numIter = 1
        var appName = ""
        var appArgs = Seq("")
        var cores = 2
        var menPerEx = 512
        var corePerEx = Some(2)
        var driverMem = 512
        var driverCores = 1
        var exPath = pConf.getString("sessioncm.app_path")

        def parseRest(as: List[String]): Unit = {
          //          println(as)
          as match {
            case cOP :: c :: tail if cOP == ("-cores") =>
              cores = c.toInt
              parseRest(tail)
            case memOO :: mem :: tail if memOO == ("-memEx") =>
              menPerEx = mem.toInt
              parseRest(tail)
            case cOP :: c :: tail if cOP == ("-coresEx") =>
              corePerEx = Some(c.toInt)
              parseRest(tail)
            case op :: a :: tail if op == ("-iter") =>
              numIter = a.toInt
              parseRest(tail)
            case op :: a :: tail if op == ("-dMem") =>
              driverMem = a.toInt
              parseRest(tail)
            case op :: a :: tail if op == ("-dCores") =>
              driverCores = a.toInt
              parseRest(tail)
            case op :: a :: tail if op == ("-appPath") =>
              exPath = a
              parseRest(tail)
            case aN :: aArgs =>
              appName = aN
              appArgs = aArgs
            case _ =>
              sys.error(s"could not parse $as")
          }
        }

        parseRest(rest)

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


        val piApp1 = createSimpleApp(exPath, appName, appArgs,
          driverCores = driverCores, driverMem = driverMem,
          maxCores = cores, menPerEx = menPerEx, corePerEx = corePerEx)


        pprintln(piApp1)

        var repCnt = 0

        while (repCnt < numIter.toInt) {
          repCnt += 1
          val sT = System.nanoTime()
          mRef.send(Messages.SubmitApp(piApp1.copy(subRPCEpRef = Some(myRef))))
          while (!done.get()) {
          }
          val eT = System.nanoTime()
          done.set(false)
          println(s"Start time: $sT end time: $eT => ${(eT - sT) / 1000000} msec")
        }


        rpcEnvM.awaitTermination()


    }


  }
}
