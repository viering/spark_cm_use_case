package org.apache.spark.deploy

import com.typesafe.config.ConfigFactory
import event_lang.EndPoint
import event_lang.network.SpawnMain
import event_lang.network.netty.EPAddr
import event_lang.semantic.OperationalSemantic
import event_lang.types.{Role, RoleSet}
import org.apache.spark.{SecurityManager, SparkConf}
import org.apache.spark.deploy.Master.{MasterRPCEndPoint, SessionMaster, StateM}
import org.apache.spark.deploy.Master.Messages.{SparkData, WorkerData}
import org.apache.spark.deploy.ZkMockup.SessionZK
import org.apache.spark.deploy.launcher.{ConnectionBootstrap, LaunchEndPoint}
import org.apache.spark.deploy.launcher.Util.createSimpleApp
import org.apache.spark.deploy.test.{PiandTC, SimplePi, TwoSimplePi, TwoSimplePiOverProv}
import org.apache.spark.deploy.types.SesCMPaperTypes.{M, RS}
import org.apache.spark.deploy.types.SesCMPaperTypes.M.PExSchedule_m_M
import org.apache.spark.deploy.types.SesCMPaperTypes.RS.{W, ZK}
import org.apache.spark.deploy.worker.{SessionWorker, WorkerRCPEndPoint}
import org.apache.spark.rpc.RpcEnv
import org.apache.spark.util.Utils
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import scala.reflect.io.Directory

class SimpleSessionCMTests extends AnyFunSuite with BeforeAndAfterAll {

  //  override def beforeEach() {
  //    val directory = new Directory(new File("output/test"))
  //    directory.deleteRecursively()
  //  }
  override def beforeAll() {
    val directory = new Directory(new File("output/test"))
    directory.deleteRecursively()
  }

  def getOutputLines(folder: File, pred: String => Boolean): Seq[String] = {
    def recAllFiles(f: File): Seq[File] = {
      val fs = f.listFiles()
      fs ++ fs.filter(_.isDirectory).flatMap(recAllFiles(_))
    }

    val allFiles = recAllFiles(folder).filter(_.getName.contains("stdout")).flatMap(f => {
      scala.io.Source.fromFile(f).getLines().filter(pred)
    })
    allFiles
  }

  def testPI(folder: File, numRes: Int = 1): Unit = {
    val lines = getOutputLines(folder, s => s.contains("Pi is roughly"))

    assert(lines.size == numRes)
    lines.foreach(s => {
      val pi = lines.head.split(" ").last.toDouble
      assert(Math.abs(pi - Math.PI) < 0.5)
    })
  }

  def testTC(folder: File, numRes: Int = 1): Unit = {
    val lines = getOutputLines(folder, s => s.contains("TC has "))
    //TC has 6254 edges."
    assert(lines.size == numRes)
    lines.foreach(s => {
      val edges = lines.head.split(" ").toSeq.apply(2).toInt
      assert(edges == 6254)
    })
  }

//  test("Pi and TC") {
//    import scala.sys.process._
//
//    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiandTC""").lineStream.foreach(println)
//    testPI(new File(PiandTC.outDir))
//    testTC(new File(PiandTC.outDir))
//  }

  test("Simple PI") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.SimplePi""").lineStream.foreach(println)

    testPI(new File(SimplePi.outDir))
  }



  test("PiFailureDriverInExSchedule") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiFailureDriverInExSchedule""").lineStream.foreach(println)

    testPI(new File(SimplePi.outDir))
  }

  test("PiFailureExecutorInPExexutor") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiFailureExecutorInPExexutor""").lineStream.foreach(println)

    testPI(new File(SimplePi.outDir))
  }

  test("PiFailuremInDriver") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiFailuremInDriver""").lineStream.foreach(println)

    //    testPI(new File(SimplePi.outDir))
  }


  test("PiFailuremInMain") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiFailuremInDriver""").lineStream.foreach(println)

    //    testPI(new File(SimplePi.outDir))
  }

  test("PiFailureTmpWorkerInExSchedule") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.PiFailureTmpWorkerInExSchedule""").lineStream.foreach(println)

    testPI(new File(SimplePi.outDir))
  }


  test("Two simple PI") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.TwoSimplePi""").lineStream.foreach(println)

    testPI(new File(TwoSimplePi.outDir), 2)
  }
  test("Tow simple pi over prev") {
    import scala.sys.process._

    Process("""mvn exec:java -Dexec.mainClass=org.apache.spark.deploy.test.TwoSimplePiOverProv""").lineStream.foreach(println)

    testPI(new File(TwoSimplePiOverProv.outDir), 2)
  }


}
