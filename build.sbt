ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "rockthejvm-http4s-article"
  )

// https://mvnrepository.com/artifact/org.http4s/http4s-core_3/0.23.30
// https://mvnrepository.com/artifact/org.http4s/http4s-core_3/1.0.0-M44
val Http4sVersion = "1.0.0-M44" // Dec 06, 2024

// https://mvnrepository.com/artifact/io.circe/circe-core
val CirceVersion = "0.14.12" // Mar 16, 2025

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
  "org.slf4j" % "slf4j-simple" % "2.0.17"
)

