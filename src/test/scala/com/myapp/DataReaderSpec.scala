package com.myapp

import com.myapp.io.{ClasspathDataSource, FileWriterStub}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class DataReaderSpec extends WordSpec
  with Matchers
  with MockFactory
  with BeforeAndAfter
  with ScalaFutures {

  // Single-thread executor to make all operations happen consequently
  implicit val synchronousExecutionContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(_.run())

  private val AggregateSize = 5
  private val ChunkSize     = 5
  private val DataPath      = "TestData.csv"

  private val fileWriter    = new FileWriterStub()
  private val dataProcessor = new DataProcessor(AggregateSize, fileWriter)
  private val dataSource    = new ClasspathDataSource(DataPath)
  private val dataReader    = new DataReader(dataSource, ChunkSize, dataProcessor)

  "DataReader" should {
    "correctly read and aggregate data from stream" in {

      Future { dataReader.start(false) }.futureValue

      val files = fileWriter.getFiles
      files.length should be(2)
    }
  }
}
