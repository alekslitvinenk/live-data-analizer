package com.myapp.io

import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicInteger

import com.myapp.domain.Protocol.AggregatedUserData

class FileWriterImpl extends FileWriter {

  private val runningFileCount = new AtomicInteger()

  override def createFile(aggregatedUserData: AggregatedUserData): Unit = {
    val fileId = runningFileCount.incrementAndGet()
    val fileName = fileId + ".txt"
    val pw = new PrintWriter(fileName)
    pw.println(aggregatedUserData.sumInt2)
    pw.println(aggregatedUserData.usersCount)
    aggregatedUserData.usersTotals.foreach(userTotal => pw.println(userTotal))
    pw.close()

    println(s"File $fileName created")
  }
}
