import Dependencies._

lazy val root = (project in file("."))
.settings(
  // Only necessary for SNAPSHOT releases
  resolvers += Resolver.sonatypeRepo("snapshots"),
  name := "Scarvy",
  version := "0.1",
  scalaVersion := "2.12.8",
  
  libraryDependencies ++= Seq(
    Cats.Core,
    Cats.Effect,
    ScalaLogging,
    Http4s.Dsl,
    Http4s.BlazeServer,
    Http4s.BlazeClient,
    Logback,
  ),

  scalacOptions ++= Seq("-Ypartial-unification"),
)