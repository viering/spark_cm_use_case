package org.apache.spark.deploy.launcher

import java.util.concurrent.atomic.AtomicBoolean
import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.Master.Messages
import org.apache.spark.deploy.Master.Messages.SparkAppFinished
import org.apache.spark.deploy.launcher.Util.createSimpleApp
import org.apache.spark.deploy.master.DriverState.DriverState
import org.apache.spark.rpc.{RpcAddress, RpcCallContext, RpcEndpointRef, RpcEnv, ThreadSafeRpcEndpoint}
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf}
import pprint.pprintln

object SubmitBenchmark {
  def main(args: Array[String]): Unit = {

    args.toList match {
      case myIP :: myPort :: myName :: remoteIP :: remotePort :: rest =>

        val pConf = ConfigFactory.load()

        var numIter = 1
        var appName = ""
        var appArgs = Seq("")
        var cores = 2
        var menPerEx = 1024
        var corePerEx = Some(2)
        var driverMem = 512
        var driverCores = 1
        var exPath = pConf.getString("sessioncm.app_path")
        var startQ = 1
        var endQ = 1

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
            case op :: a :: tail if op == ("-startQ") =>
              startQ = a.toInt
              parseRest(tail)
            case op :: a :: tail if op == ("-endQ") =>
              endQ = a.toInt
              parseRest(tail)
            case aN :: aArgs =>
              appName = aN
              appArgs = aArgs
            case _ =>
              sys.error(s"could not parse $as")
          }
        }

        parseRest(rest)

        submitBenchmark(myPort.toInt, myName, remoteIP, remotePort, numIter, appName, appArgs, exPath, startQ, endQ, cores, menPerEx, corePerEx, driverMem, driverCores)

    }
  }


  def submitBenchmark(myPort: Int,
                      myName: String,
                      remoteIP: String,
                      remotePort: String,
                      numIter: Int,
                      appName: String,
                      appArgs: Seq[String],
                      exPath: String,
                      startQ: Int,
                      endQ: Int,
                      cores: Int = 2,
                      menPerEx: Int = 512,
                      corePerEx: Option[Int] = Some(1),
                      driverMem: Int = 512,
                      driverCores: Int = 1): RpcEndpointRef = {

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

    val numQs = endQ - startQ + 1
    val res = Array.ofDim[Long](numQs, numIter)
    val calcStatus = Array.ofDim[DriverState](numQs, numIter)
    var dState: DriverState = null
    class SubmitRPCEP(val rpcEnv: RpcEnv) extends ThreadSafeRpcEndpoint {
      override def receive: PartialFunction[Any, Unit] = {
        // replay from master goes here
        case m: SparkAppFinished =>
          println(s"receive $m")
          dState = m.status
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
    val mRef = rpcEnvM.setupEndpointRef(RpcAddress(remoteIP, remotePort.toInt), "m")

    //        val mRef = rpcEnvM.setupEndpoint("m", new SubmitRPCEP(rpcEnvM))



    assert(appArgs.size == 1)
    //Main benchmark loop
    for (qI <- 0 until numQs) {
      //iterate over the selected queries
      var repCnt = 0
      //create the to be scheduled app, i.e., the tpc-h query to run
      val nApp = createSimpleApp(exPath, appName, Seq(appArgs.head, (qI + startQ).toString),
        driverCores = driverCores, driverMem = driverMem,
        maxCores = cores, menPerEx = menPerEx, corePerEx = corePerEx)

      pprintln(nApp)
      //iterate over the number of iterations
      while (repCnt < numIter.toInt) {
        println(s"[submit Bench] ${Messages.SubmitApp(nApp.copy(subRPCEpRef = Some(myRef)))}")
        //start time
        val sT = System.nanoTime()
        //submit application to Session-CM
        mRef.send(Messages.SubmitApp(nApp.copy(subRPCEpRef = Some(myRef))))
        //wait for notification of completion
        while (!done.get()) {
        }
        //stop time
        val eT = System.nanoTime()
        //store information
        res(qI)(repCnt) = (eT - sT)
        calcStatus(qI)(repCnt) = dState
        done.set(false)
        println(s"Start time: $sT end time: $eT => ${(eT - sT) } nano seconds")
        repCnt += 1
      }

      println(s"Partial results: ")
      println((startQ to endQ).map(x => s"Q$x").mkString(";"))
      res.transpose.foreach(x => println(x.mkString(";")))
      println("\n\n")
      calcStatus.transpose.foreach(x => println(x.mkString(";")))

    }
    //end benchmark, stop Session cm and print results
    mRef.send(Messages.StopCM())
    Thread.sleep(4000)
    println("\n\n\n")
    println(s"Times: ")
    println((startQ to endQ).map(x => s"Q$x").mkString(";"))
    res.transpose.foreach(x => println(x.mkString(";")))
    println("\n\n")
    calcStatus.transpose.foreach(x => println(x.mkString(";")))
    mRef
  }
}
