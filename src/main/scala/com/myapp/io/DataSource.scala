package com.myapp.io

import java.io.InputStream

trait DataSource {

  def inputStream(): InputStream
}
