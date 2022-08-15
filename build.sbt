val AkkaVersion = "2.6.19"
val AlpakkaKafkaVersion = "3.0.0"
val AkkaManagementVersion = "1.1.3"
val AkkaHttpVersion = "10.2.9"
val LogbackVersion = "1.2.11"
val circeVersion = "0.14.2"
val slf4jVersion = "1.7.36"
val scalatestVersion = "3.2.12"

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / scalacOptions in Compile ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint")
ThisBuild / javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

Global / cancelable := true // ctrl-c
Global / onChangedBuildSource := ReloadOnSourceChanges

fork := true

lazy val root = project
  .in(file("."))
  .enablePlugins(AkkaGrpcPlugin, JavaAgent)
  .settings(javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.9" % "runtime;test")
  .settings(libraryDependencies ++= Seq(
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "io.dropwizard.metrics" % "metrics-core" % "4.2.9",
        "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
        "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "3.0.4",
      "com.typesafe.akka" %% "akka-stream-kafka" % AlpakkaKafkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka-cluster-sharding" % AlpakkaKafkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-discovery" % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.5",
      "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-persistence-testkit" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion,
      "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management-cluster-http" % AkkaManagementVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.12" % Test,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion))
  .settings(resolvers += Resolver.bintrayRepo("akka", "snapshots"))
