

// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers ++=Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "sbt-plugin-releasess" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/" ,
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.1")