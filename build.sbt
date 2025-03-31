ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "rockthejvm-http4s-article"
  )

/** Rock the JVM
 * val Http4sVersion = "1.0.0-M21"
 * val CirceVersion = "0.14.0-M5"
 */
val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"

/**
 * val circeVersion = "0.14.1"
 * val http4sVersion = "0.23.30"
 */
libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"      %% "http4s-circe"        % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "io.circe"        %% "circe-generic"       % CirceVersion
)

