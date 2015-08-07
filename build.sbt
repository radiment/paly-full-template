name := """play-full-template"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "be.objectify"  %% "deadbolt-java"     % "2.4.0",
  "com.feth" %% "play-authenticate" % "0.7.0-SNAPSHOT",
  "org.webjars" % "bootstrap" % "3.3.5" exclude("org.webjars", "jquery"),
  "org.easytesting" % "fest-assert" % "1.4" % "test"
)

// add resolver for deadbolt and easymail snapshots
resolvers += Resolver.sonatypeRepo("snapshots")


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
