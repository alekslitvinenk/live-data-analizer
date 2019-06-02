package com.myapp

import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}

import scala.concurrent.ExecutionContext

object ConsumerMain extends App {

  implicit val blockingEC: ExecutionContext = ExecutionContext.fromExecutorService(
    // These values seem good to go from empirical point of view
    new ThreadPoolExecutor(
      3,
      20,
      120,
      TimeUnit.SECONDS,
      new SynchronousQueue[Runnable](true),
    )
  )

  // Constants
  val Port = 9000
  val ChunkSize = 1000
  val AggregateSize = ChunkSize

  // Services
  val fileWriter = new FileWriterStub()
  val dataProcessor = new DataProcessor(AggregateSize, fileWriter)
  val dataReader = new PortDataReader(Port, ChunkSize, dataProcessor)
  dataReader.start()
}
