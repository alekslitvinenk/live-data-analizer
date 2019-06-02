package com.myapp

import java.util.Scanner

import com.myapp.domain.Protocol.{Chunk, UserData}
import com.myapp.domain.ProtocolFormat._
import com.myapp.io.DataSource

import scala.collection.mutable

class DataReader(dataSource: DataSource, chunkSize: Int, dataProcessor: DataProcessor) {

  private val scanner = new Scanner(dataSource.inputStream())

  private val chunkedStorage: mutable.Queue[Chunk] = mutable.Queue(createNewChunk())

  def start(loop: Boolean = true): Unit = {

    def readWhileHasData(): Unit = {
      while (scanner.hasNextLine) {
        consumeLine(scanner.nextLine())
      }
    }

    readWhileHasData()

    if (loop) {
      while (true) {
        readWhileHasData()
      }
    }
  }

  private def consumeLine(userData: UserData): Unit = {

    if (chunkedStorage.isEmpty)
      chunkedStorage.enqueue(createNewChunk())

    def front = chunkedStorage.head

    if (front.size < chunkSize) {
      front.enqueue(userData)
    } else {
      chunkedStorage.enqueue(createNewChunk(Some(userData)))
    }

    if (front.size == chunkSize)
      dataProcessor.process(chunkedStorage.dequeue())
  }

  private def createNewChunk(firstElement: Option[UserData] = None): Chunk =
    firstElement.fold {
      mutable.Queue[UserData]()
    } { u =>
      mutable.Queue[UserData](u)
    }
}
