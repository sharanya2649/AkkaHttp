name := "AKkaPractice"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-http"   % "10.1.10",
"com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "com.h2database" % "h2" % "1.4.187" % "test",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-all" % "1.8.4",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.10",
  "org.keycloak" % "keycloak-adapter-core" % "3.2.0.Final",
  "org.keycloak" % "keycloak-core" % "3.2.0.Final",
  "org.jboss.logging" % "jboss-logging" % "3.3.0.Final",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.json4s" %% "json4s-native" % "3.7.0-M1",
  "com.softwaremill.sttp" %% "core" % "1.7.2"

)
