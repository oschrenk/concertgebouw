name := "concertgebouw"
organization := "com.oschrenk.amsterdam"
version := "0.1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-java8",
).map(_ % "0.9.1")

libraryDependencies ++= Seq(
  "io.circe" %% "circe-jackson29"
).map(_ % "0.9.0")

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.7.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "net.ruippeixotog" %% "scala-scraper" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
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

