package com.myapp

import java.net.ServerSocket
import java.util.Scanner

import com.myapp.Protocol.{Chunk, UserData}
import com.myapp.ProtocolFormat._

import scala.collection.mutable

class PortDataReader(port: Int, chunkSize: Int, dataProcessor: DataProcessor) {

  private val serverSocket = new ServerSocket(port)
  private val clientSocket = serverSocket.accept()
  private val inputStream = clientSocket.getInputStream
  private val scanner = new Scanner(inputStream)

  private val chunkedStorage: mutable.Queue[Chunk] = mutable.Queue(createNewChunk())

  def start(): Unit =
    while (true) {
      if (scanner.hasNextLine) {
        consumeLine(scanner.nextLine())
      }
    }

  private def consumeLine(userData: UserData): Unit = {

    val front = chunkedStorage.head

    if (front.size < chunkSize)
      front.enqueue(userData)
    else {
      chunkedStorage.enqueue(createNewChunk(Some(userData)))
      dataProcessor.process(chunkedStorage.dequeue())
    }
  }

  private def createNewChunk(firstElement: Option[UserData] = None): Chunk =
    firstElement.fold {
      mutable.Queue[UserData]()
    } { u =>
      mutable.Queue[UserData](u)
    }
}
