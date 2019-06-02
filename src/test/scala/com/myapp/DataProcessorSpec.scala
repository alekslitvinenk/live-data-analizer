package com.myapp

import com.myapp.common.TestData._
import com.myapp.domain.Protocol.{Chunk, UserData}
import com.myapp.io.FileWriter
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class DataProcessorSpec extends WordSpec
  with Matchers
  with MockFactory
  with BeforeAndAfter
  with ScalaFutures {

  // Single-thread executor to make all operations happen consequently
  implicit val synchronousExecutionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(_.run())

  private var fileWriter: FileWriter = _
  private var aggregateSize: Int = _
  private var dataProcessor: DataProcessor = _

  before {
    fileWriter = mock[FileWriter]
    aggregateSize = 5
    dataProcessor = new DataProcessor(aggregateSize, fileWriter)
  }

  "DataProcessor" should {
    "correctly process 5 entries of data stream and create 1 file" in {
      val entity = stringToUserData1._2
      val queue = enqueueN(entity, aggregateSize)
      (fileWriter.createFile _).expects(*).once()
      dataProcessor.process(queue).futureValue
    }

    "correctly process 10 entries of data stream and create 2 files" in {
      val entity = stringToUserData1._2
      val queue = enqueueN(entity, aggregateSize * 2)
      (fileWriter.createFile _).expects(*).twice()
      dataProcessor.process(queue).futureValue
    }

    "correctly process 13 entries of data stream and create 2 files" in {
      val entity = stringToUserData1._2
      val queue = enqueueN(entity, aggregateSize * 2 + 3)
      (fileWriter.createFile _).expects(*).twice()
      dataProcessor.process(queue).futureValue
    }
  }

  private def enqueueN(element: UserData, n: Int): Chunk = {
    val queue: Chunk = mutable.Queue()

    for {
      _ <- 1 to n
    } yield queue.enqueue(element)

    queue
  }
}
