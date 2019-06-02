package com.myapp

import com.myapp.domain.Protocol.{AggregatedUserData, Chunk, UserData, UserTotals}
import com.myapp.io.FileWriter

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class DataProcessor(val aggregateSize: Int, val fileWriter: FileWriter)(implicit ec: ExecutionContext) {

  private var usersData: Chunk =  mutable.Queue[UserData]()

  def process(rawChunk: Chunk): Future[Unit] = {

    synchronized {
      usersData ++= rawChunk
    }

    processInternal()
  }

  private def processInternal(): Future[Unit] = {
    if (usersData.size >= aggregateSize) {

      val (chunk, reminder) = usersData.splitAt(aggregateSize)

      synchronized {
        usersData = reminder
      }

      for {
        agr <- createAggregatedUserData(chunk)
        _   <- writeToFile(agr)
        _   <- processInternal()
      } yield ()
    } else Future.successful()
  }

  private def createAggregatedUserData(chunk: Chunk): Future[AggregatedUserData] = {
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
    }
  }

  private def writeToFile(agr: AggregatedUserData): Future[Unit] = {
    Future {
      fileWriter.createFile(agr)
    }
  }
}
