package org.apache.spark.deploy.sessioncm

import org.apache.spark.SparkContext
import org.apache.spark.scheduler.{ExternalClusterManager, SchedulerBackend, TaskScheduler, TaskSchedulerImpl}

class SessionCM extends ExternalClusterManager{
  override def canCreate(masterURL: String): Boolean = {

    println("session cm")

    true
  }

  override def createTaskScheduler(sc: SparkContext, masterURL: String): TaskScheduler = {
    println("create default task scheduler")
    new TaskSchedulerImpl(sc,2,true)
  }

  override def createSchedulerBackend(sc: SparkContext, masterURL: String, scheduler: TaskScheduler): SchedulerBackend = {
    println("create scheduler backend")
//    val backend =  new SessionCMSchedulerBackend(scheduler.asInstanceOf[TaskSchedulerImpl],sc.env.rpcEnv)

//    val backend = new org.apache.spark.scheduler.local.LocalSchedulerBackend(sc.getConf, scheduler.asInstanceOf[TaskSchedulerImpl], 2)
val backend =  new SessionScheduler(scheduler.asInstanceOf[TaskSchedulerImpl],sc.env.rpcEnv,sc)
    backend

  }

  override def initialize(scheduler: TaskScheduler, backend: SchedulerBackend): Unit = {
    scheduler.asInstanceOf[TaskSchedulerImpl].initialize(backend)
  }
}