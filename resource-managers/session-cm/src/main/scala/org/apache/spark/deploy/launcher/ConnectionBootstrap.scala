package org.apache.spark.deploy.launcher

import event_lang.EndPoint
import event_lang.network.SpawnMain
import event_lang.network.netty.EPAddr
import event_lang.types.{Role, RoleSet}
import org.apache.spark.deploy.types.SesCMPaperTypes.RS._

import scala.util.Try

object ConnectionBootstrap {
  def main(args: Array[String]): Unit = {
    val DEFAULT_BOOTSTRAP_ADDR = EPAddr(args(0), 22688)

    val zkrole = Role("zk", ZK)
    val mrole = Role("m", M)
    val dNumW = 3

    val numW = (if (args.length > 1) {
      Try(args(1).toInt).getOrElse(dNumW)
    } else {
      dNumW
    }) + 2
    bootstrap(DEFAULT_BOOTSTRAP_ADDR.addr, DEFAULT_BOOTSTRAP_ADDR.port, Map((0, zkrole), (1, mrole)), Map((0, ZK), (1, M)) ++ (2 until numW).map((_, W))).start()

  }

  def bootstrap(bootstrapIP: String, bootstrapPort: Int, roles: Map[Int, Role], roleSets: Map[Int, RoleSet]): Thread = {
    val DEFAULT_BOOTSTRAP_ADDR = EPAddr(bootstrapIP, bootstrapPort)

    val mainSes = SpawnMain(0, 0, roles, roleSets, Map())
    println(s"[ConBootstrab] $mainSes")

    (new Thread(new Runnable {
      override def run(): Unit = {
        EndPoint.createNettyConnectionBootstrapManger(DEFAULT_BOOTSTRAP_ADDR, mainSes)
      }
    }))

  }
}
