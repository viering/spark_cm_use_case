package org.apache.spark.deploy.sessioncm


import com.typesafe.config.ConfigFactory
import org.apache.spark.deploy.Master.Messages.{DeployStartEx, ExNum, ExNumAck}
import org.apache.spark.deploy.launcher.Util
import org.apache.spark.deploy.{ApplicationDescription, Command}
import org.apache.spark.internal.config
import org.apache.spark.internal.config.Tests.IS_TESTING
import org.apache.spark.resource.ResourceUtils
import org.apache.spark.rpc._
import org.apache.spark.scheduler.TaskSchedulerImpl
import org.apache.spark.scheduler.cluster.{CoarseGrainedSchedulerBackend, SchedulerBackendUtils}
import org.apache.spark.util.Utils
import org.apache.spark.{SecurityManager, SparkConf, SparkContext, SparkException}
import pprint.pprintln

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SessionSchedulerEP(val scheduler : SessionScheduler, val rpcEnv: RpcEnv) extends ThreadSafeRpcEndpoint{
  override def receive: PartialFunction[Any, Unit] = {
    case m => println(s"[StaticNetworkEP] receive $m")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case m => println(s"[StaticNetworkEP] receive and replay to $m")
      context.reply(m)
    case _ => context.sendFailure(new SparkException(self + " won't reply anything"))
  }
}

class SessionScheduler(val scheduler: TaskSchedulerImpl, rpcEnv: RpcEnv, val sc: SparkContext) extends CoarseGrainedSchedulerBackend(scheduler, rpcEnv) {

  private val initialExecutors = SchedulerBackendUtils.getInitialTargetExecutorNumber(conf)
  val pConf = ConfigFactory.load()
  val mIP = pConf.getString("sessioncm.master_ip")
  val mPort= pConf.getInt("sessioncm.master_port")
  println(s"Connect to master: $mIP:$mPort")
  val mRef = rpcEnv.setupEndpointRef(RpcAddress(mIP,mPort),"m")
  val securityMgr = new SecurityManager(conf)


  var snEP : RpcEndpointRef= null

  println(s"My test id is: ${sc.conf.getOption("my_test_id").getOrElse("ERROR")})")
  sc.conf.getAll.foreach(println)

  println(s"sys probs: ${sys.props.mkString(",")}")



  override def start(): Unit = {
    super.start()
    mRef.send("static network connets")
    val re =  RpcEnv.create("driverClient", Utils.localHostName(), 0, conf, new SecurityManager(conf)) //SparkEnv.get.rpcEnv
    val executorEndpoint = new SessionSchedulerEP(this,re)
    snEP = re.setupEndpoint("SessionSchedulerBackendEndpoint", executorEndpoint)


    val rpcEnv =
      RpcEnv.create("driverClient", Utils.localHostName(), 0, conf, new SecurityManager(conf))



    // The endpoint for executors to talk to us
//    sc.conf.get(config.DRIVER_HOST_ADDRESS),
//    sc.conf.get(config.DRIVER_PORT),
    //this sets the driver rpc address which the executore uses to talk to
    val driverUrl = RpcEndpointAddress(driverEndpoint.address, driverEndpoint.name).toString

    println(s"[SesionScheduler] Driver url: $driverUrl")
    //RpcEndpointAddress(snEP.address,"LocalSchedulerBackendEndpoint").toString


//      RpcEndpointAddress(
//      sc.conf.get(config.DRIVER_HOST_ADDRESS),
//      sc.conf.get(config.DRIVER_PORT),
//      CoarseGrainedSchedulerBackend.ENDPOINT_NAME).toString
    val args = Seq(
      "--driver-url", driverUrl,
      "--executor-id", "{{EXECUTOR_ID}}",
      "--hostname", "{{HOSTNAME}}",
      "--cores", "{{CORES}}",
      "--app-id", "{{APP_ID}}",
      "--worker-url", "{{WORKER_URL}}")
    val extraJavaOpts = sc.conf.get(config.EXECUTOR_JAVA_OPTIONS)
      .map(Utils.splitCommandString).getOrElse(Seq.empty)
    val classPathEntries = sc.conf.get(config.EXECUTOR_CLASS_PATH)
      .map(_.split(java.io.File.pathSeparator).toSeq).getOrElse(Nil)
    val libraryPathEntries = sc.conf.get(config.EXECUTOR_LIBRARY_PATH)
      .map(_.split(java.io.File.pathSeparator).toSeq).getOrElse(Nil)

    // When testing, expose the parent class path to the child. This is processed by
    // compute-classpath.{cmd,sh} and makes all needed jars available to child processes
    // when the assembly is built with the "*-provided" profiles enabled.
    val testingClassPath =
    if (sys.props.contains(IS_TESTING.key)) {
      sys.props("java.class.path").split(java.io.File.pathSeparator).toSeq
    } else {
      Nil
    }

    // Start executors with a few necessary configs for registering with the scheduler
    val sparkJavaOpts = Utils.sparkJavaOpts(conf, SparkConf.isExecutorStartupConf)
    val javaOpts = sparkJavaOpts ++ extraJavaOpts
    val command = Command("org.apache.spark.executor.CoarseGrainedExecutorBackend",
      args, sc.executorEnvs, classPathEntries ++ testingClassPath, libraryPathEntries, javaOpts)
    val webUrl = sc.ui.map(_.webUrl).getOrElse("")
    val coresPerExecutor = Option(System.getenv(Util.SESSION_CM_CORES_PAIR_EX)).map(x =>x.toInt)
    assert(coresPerExecutor.nonEmpty)
      //conf.getOption(config.EXECUTOR_CORES.key).map(_.toInt)
    // If we're using dynamic allocation, set our initial executor limit to 0 for now.
    // ExecutorAllocationManager will send the real initial limit to the Master later.
    val initialExecutorLimit = None
//    if (Utils.isDynamicAllocationEnabled(conf)) {
//      Some(0)
//    } else {
//      None
//    }
    val executorResourceReqs = ResourceUtils.parseResourceRequirements(conf,
      config.SPARK_EXECUTOR_PREFIX)

    var cores = Option(System.getenv(Util.SESSION_CM_MAX_CORES)).map(x =>x.toInt)
    if(cores.isEmpty){
      System.err.println(s"[SessionScheduler] WARNING cores was set to $cores -- should be Some(n) with n >= 1")
      cores = Some(1)
    }
    var memPEx = sys.props.get(Util.SESSION_CM_MEM_P_EX).map(x =>x.toInt).get
    System.err.println(s"[SessionScheduler] Mem var: ${System.getenv(Util.SESSION_CM_MEM_P_EX)}")
    if(memPEx <= 512){
      System.err.println(s"[SessionScheduler] WARNING mem was $memPEx -- should be >= 512")
      memPEx = 512
    }

    val appDesc = ApplicationDescription(sc.appName, cores,memPEx, command,
      webUrl, sc.eventLogDir, sc.eventLogCodec, coresPerExecutor, initialExecutorLimit,
      resourceReqsPerExecutor = executorResourceReqs)
    assert(appDesc.maxCores.nonEmpty)

//    client = new StandaloneAppClient(sc.env.rpcEnv, masters, appDesc, this, conf)
//    client.start()
//    launcherBackend.setState(SparkAppHandle.State.SUBMITTED)
//    waitForRegistration()
//    launcherBackend.setState(SparkAppHandle.State.RUNNING)
//
////    val masterEndpoint: RpcEndpointRef = rpcEnv.setupEndpointRef(RpcAddress(Utils.localHostName(),8111),"Fake Master")

    val app = DeployStartEx(snEP,sys.props.get("sessioncm-appID").get.toInt ,appDesc)
    println(s"[SessionScheduler] Send out ${app} to ${mRef.address}")
    pprintln(app)
    mRef.send(app)

  }

  override def stop(): Unit = {
    super.stop()
  }


  override def reviveOffers(): Unit = {
    println("offer")
    println(s"target num: $initialExecutors")
    println(s"current ex: ${totalRegisteredExecutors.get()}")

    super.reviveOffers()
  }

  override def doRequestTotalExecutors(requestedTotal: Int): Future[Boolean] = {
    //do the requiest for more executores
    println(s"[SessionScheduler] do RequestTotalExecutors $requestedTotal")
    mRef.ask[ExNumAck](ExNum(requestedTotal)).map(_.b)
  }
  override def createDriverEndpoint(): DriverEndpoint = {
    new SessionDriverEndpoint()
  }

  class SessionDriverEndpoint extends DriverEndpoint {

  }

}


