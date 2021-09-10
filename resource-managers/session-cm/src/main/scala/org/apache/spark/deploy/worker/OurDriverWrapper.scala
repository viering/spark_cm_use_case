/*
initally copyed from spark
 */

package org.apache.spark.deploy.worker

import org.apache.commons.lang3.StringUtils
import org.apache.spark.deploy.launcher.Util
import org.apache.spark.deploy.{DependencyUtils, SparkHadoopUtil}
import org.apache.spark.internal.{Logging, config}
import org.apache.spark.util._
import org.apache.spark.{SecurityManager, SparkConf}

import java.io.File
import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * Utility object for launching driver programs such that they share fate with the Worker process.
 * This is used in standalone cluster mode only.
 */
object OurDriverWrapper extends Logging {
  def main(args: Array[String]): Unit = {
    args.toList match {
      /*
       * IMPORTANT: Spark 1.3 provides a stable application submission gateway that is both
       * backward and forward compatible across future Spark versions. Because this gateway
       * uses this class to launch the driver, the ordering and semantics of the arguments
       * here must also remain consistent across versions.
       */
      case appID :: workerUrl :: userJar :: mainClass :: extraArgs =>
        val conf = new SparkConf()
        val host: String = Utils.localHostName()
        val port: Int = sys.props.getOrElse(config.DRIVER_PORT.key, "0").toInt
        //        val rpcEnv = RpcEnv.create("Driver", host, port, conf, new SecurityManager(conf))
        //        logInfo(s"Driver address: ${rpcEnv.address}")
        //rpcEnv.setupEndpoint("workerWatcher", new WorkerWatcher(rpcEnv, workerUrl))

        println(s"[OurDriverWrapper] ${args.mkString(",")}")
        println(s"[OurDriverWrapper] ${sys.props.mkString(",")}")
        println(s"[OurDriverWrapper] ${System.getenv.asScala.mkString(",")}")

        conf.setMaster("sessioncm://127.0.0.1")
        conf.set("deploy-mode", "cluster")
        conf.set("sessioncm-appID", appID)

        val memPEx =System.getenv(Util.SESSION_CM_MEM_P_EX)
        val corePEx = System.getenv(Util.SESSION_CM_CORES_PAIR_EX)
        val maxCores = System.getenv(Util.SESSION_CM_MAX_CORES)

        conf.set(Util.SESSION_CM_MEM_P_EX,memPEx)
        conf.set(Util.SESSION_CM_CORES_PAIR_EX,corePEx)
        conf.set(Util.SESSION_CM_MAX_CORES,maxCores)

        //        conf.setMaster("spark://mv-dsp:7077")
        //        conf.set("deploy-mode","cluster")

        //        conf.set("deploy-mode","")


        val currentLoader = Thread.currentThread.getContextClassLoader
        val userJarUrl = new File(userJar).toURI().toURL()
        val loader =
          if (sys.props.getOrElse(config.DRIVER_USER_CLASS_PATH_FIRST.key, "false").toBoolean) {
            new ChildFirstURLClassLoader(Array(userJarUrl), currentLoader)
          } else {
            new MutableURLClassLoader(Array(userJarUrl), currentLoader)
          }
        Thread.currentThread.setContextClassLoader(loader)
        setupDependencies(loader, userJar)

        // Delegate to supplied main class

        val sysProps = conf.getAll.toMap
        sysProps.foreach { case (k, v) =>
          sys.props(k) = v
        }

        val clazz = Utils.classForName(mainClass)
        val mainMethod = clazz.getMethod("main", classOf[Array[String]])
        mainMethod.invoke(null, extraArgs.toArray[String])

      //        rpcEnv.shutdown()
    }
  }

  private def setupDependencies(loader: MutableURLClassLoader, userJar: String): Unit = {
    val sparkConf = new SparkConf()
    val secMgr = new SecurityManager(sparkConf)
    val hadoopConf = SparkHadoopUtil.newConfiguration(sparkConf)

    val Seq(packagesExclusions, packages, repositories, ivyRepoPath, ivySettingsPath) =
      Seq(
        "spark.jars.excludes",
        "spark.jars.packages",
        "spark.jars.repositories",
        "spark.jars.ivy",
        "spark.jars.ivySettings"
      ).map(sys.props.get(_).orNull)

    val resolvedMavenCoordinates = DependencyUtils.resolveMavenDependencies(packagesExclusions,
      packages, repositories, ivyRepoPath, Option(ivySettingsPath))
    val jars = {
      val jarsProp = sys.props.get(config.JARS.key).orNull
      if (!StringUtils.isBlank(resolvedMavenCoordinates)) {
        DependencyUtils.mergeFileLists(jarsProp, resolvedMavenCoordinates)
      } else {
        jarsProp
      }
    }
    val localJars = DependencyUtils.resolveAndDownloadJars(jars, userJar, sparkConf, hadoopConf,
      secMgr)
    DependencyUtils.addJarsToClassPath(localJars, loader)
  }
}
