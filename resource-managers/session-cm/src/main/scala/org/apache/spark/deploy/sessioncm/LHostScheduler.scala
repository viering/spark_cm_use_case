package org.apache.spark.deploy.sessioncm

import org.apache.spark.TaskState.TaskState
import org.apache.spark.executor.{Executor, ExecutorBackend}
import org.apache.spark.internal.{Logging, config}
import org.apache.spark.launcher.{LauncherBackend, SparkAppHandle}
import org.apache.spark.resource.ResourceInformation
import org.apache.spark.rpc.{RpcCallContext, RpcEndpointRef, RpcEnv, ThreadSafeRpcEndpoint}
import org.apache.spark.scheduler.cluster.{ExecutorInfo, SchedulerBackendUtils}
import org.apache.spark.scheduler.{SchedulerBackend, SparkListenerExecutorAdded, TaskSchedulerImpl, WorkerOffer}
import org.apache.spark.util.Utils
import org.apache.spark.{SparkConf, SparkContext, SparkEnv, TaskState}

import java.io.File
import java.net.URL
import java.nio.ByteBuffer


class SessionCMSchedulerBackend(scheduler: TaskSchedulerImpl, rpcEnv: RpcEnv)
  extends SchedulerBackend with ExecutorBackend{


  val conf = scheduler.sc.conf
  ///(scheduler, rpcEnv)
  private var localEndpoint: RpcEndpointRef = null
  private val userClassPath = getUserClasspath(conf)
  private val listenerBus = scheduler.sc.listenerBus
  //  private val launcherBackend = new LauncherBackend() {
  //    override def conf: SparkConf = SessionCMSchedulerBackend.this.conf
  //    override def onStopRequest(): Unit = ???
  //  }
  //
  //  launcherBackend.connect()

  def getUserClasspath(conf: SparkConf): Seq[URL] = {
    val userClassPathStr = conf.get(config.EXECUTOR_CLASS_PATH)
    userClassPathStr.map(_.split(File.pathSeparator)).toSeq.flatten.map(new File(_).toURI.toURL)
  }


  private val initialExecutors = SchedulerBackendUtils.getInitialTargetExecutorNumber(conf)

  override def applicationId(): String = {
    conf.getOption("spark.app.id").map(_.toString).getOrElse(super.applicationId)
  }

  val totalCores =  2
  override def start(): Unit = {
    //    super.start()
    val rpcEnv = SparkEnv.get.rpcEnv
    val executorEndpoint = new LocalEndpoint(rpcEnv, userClassPath, scheduler, this, totalCores)
    localEndpoint = rpcEnv.setupEndpoint("LocalSchedulerBackendEndpoint", executorEndpoint)


    //    launcherBackend.setAppId(applicationId)
    //    launcherBackend.setState(SparkAppHandle.State.RUNNING)

  }

  override def statusUpdate(taskId: Long, state: TaskState, serializedData: ByteBuffer): Unit = {
    localEndpoint.send(StatusUpdate(taskId, state, serializedData))
  }

  override def stop(): Unit = {
    //    super.stop()

  }


  override def reviveOffers(): Unit = {
    localEndpoint.send(ReviveOffers)
  }

  override def defaultParallelism(): Int =
    scheduler.conf.getInt("spark.default.parallelism", totalCores)
  override def maxNumConcurrentTasks(): Int = totalCores / scheduler.CPUS_PER_TASK
}


private case class ReviveOffers()

private case class StatusUpdate(taskId: Long, state: TaskState, serializedData: ByteBuffer)

private case class KillTask(taskId: Long, interruptThread: Boolean, reason: String)

private case class StopExecutor()

private[spark] class LocalEndpoint(
                                    override val rpcEnv: RpcEnv,
                                    userClassPath: Seq[URL],
                                    scheduler: TaskSchedulerImpl,
                                    executorBackend: ExecutorBackend,
                                    private val totalCores: Int)
  extends ThreadSafeRpcEndpoint with Logging {

  private var freeCores = totalCores

  val localExecutorId = SparkContext.DRIVER_IDENTIFIER
  val localExecutorHostname = Utils.localCanonicalHostName()

  // local mode doesn't support extra resources like GPUs right now
  private val executor = new Executor(
    localExecutorId, localExecutorHostname, SparkEnv.get, userClassPath, isLocal = true,
    resources = Map.empty[String, ResourceInformation])

  override def receive: PartialFunction[Any, Unit] = {
    case ReviveOffers =>
      println("received offer")
      reviveOffers()

    case s@StatusUpdate(taskId, state, serializedData) =>
      println(s"received stats update : $s")
      scheduler.statusUpdate(taskId, state, serializedData)
      if (TaskState.isFinished(state)) {
        freeCores += scheduler.CPUS_PER_TASK
        reviveOffers()
      }

    case KillTask(taskId, interruptThread, reason) =>
      executor.killTask(taskId, interruptThread, reason)
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case StopExecutor =>
      executor.stop()
      context.reply(true)
  }

  def reviveOffers(): Unit = {
    // local mode doesn't support extra resources like GPUs right now
    val offers = IndexedSeq(new WorkerOffer(localExecutorId, localExecutorHostname, freeCores,
      Some(rpcEnv.address.hostPort)))
    for (task <- scheduler.resourceOffers(offers).flatten) {
      freeCores -= scheduler.CPUS_PER_TASK
      executor.launchTask(executorBackend, task)
    }
  }
}

private[spark] class LocalSchedulerBackend(
                                            conf: SparkConf,
                                            scheduler: TaskSchedulerImpl,
                                            val totalCores: Int)
  extends SchedulerBackend with ExecutorBackend with Logging {

  private val appId = "local-" + System.currentTimeMillis
  private var localEndpoint: RpcEndpointRef = null
  private val userClassPath = getUserClasspath(conf)
  private val listenerBus = scheduler.sc.listenerBus
  private val launcherBackend = new LauncherBackend() {
    override def conf: SparkConf = LocalSchedulerBackend.this.conf
    override def onStopRequest(): Unit = stop(SparkAppHandle.State.KILLED)
  }

  /**
   * Returns a list of URLs representing the user classpath.
   *
   * @param conf Spark configuration.
   */
  def getUserClasspath(conf: SparkConf): Seq[URL] = {
    val userClassPathStr = conf.get(config.EXECUTOR_CLASS_PATH)
    userClassPathStr.map(_.split(File.pathSeparator)).toSeq.flatten.map(new File(_).toURI.toURL)
  }

  launcherBackend.connect()

  override def start(): Unit = {
    val rpcEnv = SparkEnv.get.rpcEnv
    val executorEndpoint = new LocalEndpoint(rpcEnv, userClassPath, scheduler, this, totalCores)
    localEndpoint = rpcEnv.setupEndpoint("LocalSchedulerBackendEndpoint", executorEndpoint)
    listenerBus.post(SparkListenerExecutorAdded(
      System.currentTimeMillis,
      executorEndpoint.localExecutorId,
      new ExecutorInfo(executorEndpoint.localExecutorHostname, totalCores, Map.empty,
        Map.empty)))
    launcherBackend.setAppId(appId)
    launcherBackend.setState(SparkAppHandle.State.RUNNING)
  }

  override def stop(): Unit = {
    stop(SparkAppHandle.State.FINISHED)
  }

  override def reviveOffers(): Unit = {
    localEndpoint.send(ReviveOffers)
  }

  override def defaultParallelism(): Int =
    scheduler.conf.getInt("spark.default.parallelism", totalCores)

  override def killTask(
                         taskId: Long, executorId: String, interruptThread: Boolean, reason: String): Unit = {
    localEndpoint.send(KillTask(taskId, interruptThread, reason))
  }

  override def statusUpdate(taskId: Long, state: TaskState, serializedData: ByteBuffer): Unit = {
    localEndpoint.send(StatusUpdate(taskId, state, serializedData))
  }

  override def applicationId(): String = appId

  override def maxNumConcurrentTasks(): Int = totalCores / scheduler.CPUS_PER_TASK

  private def stop(finalState: SparkAppHandle.State): Unit = {
    localEndpoint.ask(StopExecutor)
    try {
      launcherBackend.setState(finalState)
    } finally {
      launcherBackend.close()
    }
  }

}