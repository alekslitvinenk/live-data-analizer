

lazy val root = (project in file("."))
.settings(
  name := "Scarvy",
  version := "0.1",
  scalaVersion := "2.12.8",
  mainClass := Some("com.myapp.ConsumerMain"),

  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  ),

  scalacOptions ++= Seq("-Ypartial-unification"),
)