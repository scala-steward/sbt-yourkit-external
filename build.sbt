organization := "co.vitaler"

name := "sbt-yourkit"

sbtPlugin := true

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-encoding", "UTF-8"
)

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.6" % Provided)
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4" % Provided)

enablePlugins(SbtPlugin)

// GPG settings
credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "B9513278AF9A10374E07A88FAA24C7523BD70F36",
  "ignored"
)

// Publishing
bintrayRepository := "sbt-plugins"
bintrayOrganization := Some("vitaler")
bintrayPackageLabels := Seq("sbt", "yourkit", "sbt-native-packager", "sbt-javaagent")
publishMavenStyle := false
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
