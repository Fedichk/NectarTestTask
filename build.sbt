name := "NectarTestTask"

version := "0.1"

scalaVersion := "2.12.9"

val http4sVersion = "0.20.7"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-json4s-native" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-literal" % "0.9.3",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.7.0",
  "org.slf4j" % "slf4j-simple" % "1.7.28",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

scalacOptions ++= Seq("-Ypartial-unification")