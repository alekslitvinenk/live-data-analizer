package com.myapp.io

import java.io._

class ClasspathDataSource(path: String) extends DataSource {

  override def inputStream(): InputStream =
    getClass.getClassLoader.getResourceAsStream(sanitizeClasspath(path))

  private def sanitizeClasspath(path: String): String = {
    path.stripPrefix("/")
  }
}
