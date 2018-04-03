name := "akka-streams-task"
version := "0.1"
scalaVersion := "2.12.5"

scalacOptions := Seq("-target:jvm-1.8", "-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV          = "2.4.19"
  val akkaHttpV      = "10.0.9"
  val logbackV       = "1.2.3"
  val macwireV       = "2.3.0"
  val phantomV = "2.14.5"

  Seq("com.typesafe.akka"        %% "akka-actor"           % akkaV,
      "com.typesafe.akka"        %% "akka-slf4j"           % akkaV,
      "com.typesafe.akka"        %% "akka-stream"          % akkaV,
      "com.typesafe.akka"        %% "akka-testkit"         % akkaV,
      "com.typesafe.akka"        %% "akka-http-core"       % akkaHttpV,
      "com.typesafe.akka"        %% "akka-http"            % akkaHttpV,
      "com.typesafe.akka"        %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka"        %% "akka-http-xml"        % akkaHttpV,
      "com.outworkers"           %% "phantom-dsl"          % phantomV,
      "com.softwaremill.macwire" %% "macros"               % macwireV,
      "com.softwaremill.macwire" %% "util"                 % macwireV,
      "ch.qos.logback"           % "logback-classic"       % logbackV)
}
