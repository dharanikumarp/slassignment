name := "streamviewer-server"

version := "1.0"

scalaVersion := "2.12.7"

crossScalaVersions := Seq("2.11.12", "2.12.4")

// https://github.com/sbt/junit-interface
testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-deprecation", "-Xlint")

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.webjars" %% "webjars-play" % "2.6.2"
libraryDependencies += "org.webjars" % "bootstrap" % "2.3.2"
libraryDependencies += "org.webjars" % "flot" % "0.8.3"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.9.1"
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.27.0"
libraryDependencies += "com.google.apis" % "google-api-services-youtube" % "v3-rev206-1.25.0"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.8.0" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "3.0.0" % Test

LessKeys.compress := true
