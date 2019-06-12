package com.myapp

import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}

import com.myapp.io._

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
  private val Port          = 9000
  private val ChunkSize     = 1000
  private val AggregateSize = ChunkSize

  // Services
  private val fileWriter    = new FileWriterStub()
  private val dataProcessor = new DataProcessor(AggregateSize, fileWriter)
  private val dataSource    = new SocketDataSource(Port)
  private val dataReader    = new DataReader(dataSource, ChunkSize, dataProcessor)
  dataReader.start()
}
