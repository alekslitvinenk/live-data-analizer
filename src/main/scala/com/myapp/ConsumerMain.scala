package com.myapp

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Scanner
import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

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

  type Chunk = mutable.Queue[UserData]

  val serverSocket = new ServerSocket(Port)
  val clientSocket = serverSocket.accept()
  val inputStream = clientSocket.getInputStream
  val scanner = new Scanner(inputStream)
  val buffer: mutable.Queue[Chunk] = mutable.Queue(createNewChunk())

  val runningFileCount = new AtomicInteger()

  while (true) {
    if (scanner.hasNextLine) {
      consumeLine(scanner.nextLine())
      processData(buffer)
    }
  }

  private def consumeLine(line: String): Unit = {
    val front = buffer.head

    if(front.size < AggregateCount)
      front.enqueue(UserData(line))
    else
      buffer.enqueue(createNewChunk())
  }

  private def createNewChunk(): Chunk = new mutable.Queue[UserData]

  private def processData(queue: mutable.Queue[Chunk]) = {

    // Head is writable, so checking if tail is long enough
    if (queue.tail.size > 1) {

      val chunk = queue.dequeue()

      // To avoid raising back pressure process data on another thread
      Future {
        val groupedByUser = chunk.groupBy(_.userId)

        AggregatedResult(
          sumInt2 = chunk.map(_.int2).sum,
          usersCount = groupedByUser.size,
          userTotals = groupedByUser.map { kv =>
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
        val pw = new PrintWriter(fileId + ".txt")
        pw.println(agr.sumInt2)
        pw.println(agr.usersCount)
        agr.userTotals.foreach(userTotal => pw.println(userTotal))
        pw.close()
      }
    }
  }
}
