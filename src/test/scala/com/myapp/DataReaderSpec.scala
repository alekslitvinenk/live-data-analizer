package com.myapp

import com.myapp.domain.Protocol.{AggregatedUserData, UserTotals}
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

      fileWriter.getFiles should be(
        List(
          AggregatedUserData(
            BigInt("30212888860247708058"),
            3,
            List(
              UserTotals(
                "0977dca4-9906-3171-bcec-87ec0df9d745",
                0.6794981485066369,
                1851028776
              ),
              UserTotals(
                "4d968baa-fe56-3ba0-b142-be9f457c9ff4",
                0.6532229483547558,
                1403876285
              ),
              UserTotals(
                "5fac6dc8-ea26-3762-8575-f279fe5e5f51",
                0.7626710614484215,
                1005421520
              )
            )
          ),
          AggregatedUserData(
            BigInt("25493180386520262311"),
            2,
            List(
              UserTotals(
                "0977dca4-9906-3171-bcec-87ec0df9d745",
                0.50374610727888,
                280709214
              ),
              UserTotals(
                "023316ec-c4a6-3e88-a2f3-1ad398172ada",
                0.3196604691859787,
                1579431460
              ),
            )
          )
        )
      )
    }
  }
}
