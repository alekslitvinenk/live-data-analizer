import sbt._

object Dependencies {
  
  private val catsVersion = "1.5.0"
  private val catsEffectVersion = "1.1.0"
  private val scalaLoggingVersion = "3.9.0"
  private val logbackVersion = "1.2.3"

  private val http4sVersion = "0.20.1"

  object Cats {
    val Core = "org.typelevel" %% "cats-core" % catsVersion
    val Effect = "org.typelevel" %% "cats-effect" % catsEffectVersion
  }
  
  object Http4s {
    val Dsl = "org.http4s" %% "http4s-dsl" % http4sVersion
    val BlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
    val BlazeClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
  }

  val ScalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  val Logback = "ch.qos.logback" % "logback-classic" % logbackVersion
}
