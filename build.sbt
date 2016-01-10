name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalaz" %% "scalaz-core" % "7.1.3",
  "org.webjars" % "swagger-ui" % "2.1.8-M1",
  "com.typesafe.play" %% "play-slick" % "1.0.1",
  "org.postgresql" % "postgresql" % "9.4-1202-jdbc42",
  specs2 % Test,
  "com.h2database" % "h2" % "1.4.190" % Test,
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "org.scalatestplus" %% "play" % "1.4.0-M4" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % Test
)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// Set our testing config file
javaOptions in Test += "-Dconfig.file=conf/application.test.conf"


fork in run := true