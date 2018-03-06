name := "concertgebouw"
organization := "com.oschrenk.amsterdam"
version := "0.1.0"

scalaVersion := "2.12.4"

val Http4sVersion = "0.18.0"
val CirceVersion = "0.9.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-java8",
).map(_ % CirceVersion)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-blaze-client",
  "org.http4s" %% "http4s-circe"
).map(_ % Http4sVersion)

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

resolvers += Resolver.bintrayRepo("oschrenk", "maven")
libraryDependencies += "com.oschrenk.spacetime" %% "ical-scala" % "0.0.2"

scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-Ypartial-unification",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Ywarn-nullary-unit",
    "-Xfatal-warnings",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Xfuture")

initialCommands := "import com.oschrenk.amsterdam.concertgebouw._"

