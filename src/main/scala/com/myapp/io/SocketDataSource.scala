package com.myapp.io

import java.io.InputStream
import java.net.ServerSocket

class SocketDataSource(port: Int) extends DataSource {

  private val serverSocket = new ServerSocket(port)
  private val clientSocket = serverSocket.accept()
  private val _inputStream = clientSocket.getInputStream

  override def inputStream(): InputStream = _inputStream
}
