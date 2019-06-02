package com.myapp

import com.myapp.domain.Protocol.{AggregatedUserData, Chunk, UserData, UserTotals}
import com.myapp.io.FileWriter

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class DataProcessor(val aggregateSize: Int, val fileWriter: FileWriter)(implicit ec: ExecutionContext) {

  private var usersData: Chunk =  mutable.Queue[UserData]()

  def process(rawChunk: Chunk): Unit = {

    usersData ++= rawChunk

    while (usersData.size >= aggregateSize) {

      val (chunk, reminder) = usersData.splitAt(aggregateSize)
      usersData = reminder

      // To avoid raising back pressure process data on another thread
      Future {
        val groupedByUser = chunk.groupBy(_.userId)

        AggregatedUserData(
          sumInt2 = chunk.map(_.int2).sum,
          usersCount = groupedByUser.size,
          usersTotals = groupedByUser.map { kv =>
            val (key, value) = kv

            UserTotals(
              userId = key,
              avgWeight = value.map(_.weight).sum / value.size,
              latestInt1 = value.last.int1
            )
          }.toList
        )
      } map { agr =>
        fileWriter.createFile(agr)
      }
    }
  }
}
