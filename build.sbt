name := """JohnyWalker"""

organization := "com.traffic-simulations.org"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
  "com.softwaremill.macwire" %% "macros" % "2.2.0" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.2.0"
)

pipelineStages := Seq(digest)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.traffic-simulations.org.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.traffic-simulations.org.binders._"
