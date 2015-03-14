name := "spray-rest"

organization  := "gkislin"

version       := "1.0"

scalaVersion := "2.10.2"

javaOptions := Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "io.spray"            %   "spray-can"       % "1.2-M8",
  "io.spray"            %   "spray-routing"   % "1.2-M8",
  "io.spray"            %   "spray-json_2.10" % "1.2.5",
  "joda-time"           %   "joda-time"       % "2.3",
  "org.joda"            %   "joda-convert"    % "1.2",
  "io.spray"            %   "spray-testkit"   % "1.2-M8" % "test",
  "com.typesafe.akka"   %%  "akka-actor"      % "2.2.0-RC1",
  "com.typesafe.akka"   %%  "akka-testkit"    % "2.2.0-RC1" % "test",
  "org.specs2"          %%  "specs2"          % "1.14" % "test"
)

seq(Revolver.settings: _*)
