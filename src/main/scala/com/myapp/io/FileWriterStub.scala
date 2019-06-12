package com.myapp.io
import com.myapp.domain.Protocol

class FileWriterStub extends FileWriter {
  override def createFile(aggregatedUserData: Protocol.AggregatedUserData): Unit = {
    println("Created file")
  }
}
