package com.myapp

import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicInteger

import com.myapp.Protocol.{AggregatedResult, Chunk, UserTotals}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class DataProcessor()(implicit ec: ExecutionContext) {

  private val runningFileCount = new AtomicInteger()

  def process(queue: mutable.Queue[Chunk]) = {

    // Head is writable, so checking if tail is long enough
    while (queue.tail.nonEmpty) {

      val chunk = queue.dequeue()

      // To avoid raising back pressure process data on another thread
      Future {
        val groupedByUser = chunk.groupBy(_.userId)

        AggregatedResult(
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
        val fileId = runningFileCount.incrementAndGet()
        val fileName = fileId + ".txt"
        val pw = new PrintWriter(fileName)
        pw.println(agr.sumInt2)
        pw.println(agr.usersCount)
        agr.usersTotals.foreach(userTotal => pw.println(userTotal))
        pw.close()

        println(s"File $fileName created")
      }
    }
  }
}
