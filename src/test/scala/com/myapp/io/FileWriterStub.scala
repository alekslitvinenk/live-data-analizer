package com.myapp.io

import com.myapp.domain.Protocol.AggregatedUserData

import scala.collection.mutable

class FileWriterStub extends FileWriter {

  private val files: mutable.Queue[AggregatedUserData] = new mutable.Queue()

  override def createFile(aggregatedUserData: AggregatedUserData): Unit = {
    files.enqueue(aggregatedUserData)
  }

  def getFiles = files.toList
}
