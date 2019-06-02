package com.myapp

class FileWriterStub extends FileWriter {

  override def createFile(aggregatedUserData: Protocol.AggregatedUserData): Unit = {
    println("Created file")
  }
}
