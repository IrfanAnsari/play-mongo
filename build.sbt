name := "play-mongo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(play.PlayScala)

scalaVersion := "2.11.1"

val nexusRealm: String = "Sonatype Nexus Repository Manager"

resolvers ++= Seq("Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/" )

libraryDependencies ++= Seq(
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23" withSources(),
  "com.softwaremill.macwire" %% "macros" % "0.6",
  "com.softwaremill.macwire" %% "runtime" % "0.6",
  "org.scalatestplus" %% "play" % "1.1.0" % "test",
  "org.mongodb" %% "casbah" % "2.7.2",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.github.simplyscala" % "scalatest-embedmongo_2.10" % "0.2.1" % "test"
)
