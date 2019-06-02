package com.myapp

import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}

import scala.concurrent.ExecutionContext

object ConsumerMain extends App {

  implicit val blockingEC: ExecutionContext = ExecutionContext.fromExecutorService(
    // These values seem good to go from empirical point of view
    new ThreadPoolExecutor(
      10,
      20,
      120,
      TimeUnit.SECONDS,
      new SynchronousQueue[Runnable](true),
    )
  )

  // Constants
  val Port = 9000
  val AggregateCount = 1000

  // Services
  val dataProcessor = new DataProcessor()
  val dataReader = new PortDataReader(Port, AggregateCount, dataProcessor)
  dataReader.start()
}
