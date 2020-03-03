import sbtrelease.ReleaseStateTransformations._

organization := "co.vitaler"
name := "sbt-yourkit-external"

sbtPlugin := true
crossSbtVersions := Vector("1.3.7")
scalaVersion := "2.12.10"
pluginCrossBuild / sbtVersion := "1.2.8"

scriptedLaunchOpts += ("-Dplugin.version=" + version.value)
scriptedBufferLog := false

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-encoding", "UTF-8"
)

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.25" % Provided)
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
bintrayPackageLabels := Seq("sbt", "yourkit", "sbt-native-packager")
publishMavenStyle := false
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

// Release settings
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  updateLines,
  commitReleaseVersion,
  publishArtifacts,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)

val unreleasedCompare = """^\[Unreleased\]: https://github\.com/(.*)/compare/(.*)\.\.\.HEAD$""".r
updateLinesSchema := Seq(
  UpdateLine(
    file("README.md"),
    _.matches("addSbtPlugin.* // Latest release"),
    (v, _) => s"""addSbtPlugin("co.vitaler" % "sbt-yourkit-external" % "$v") // Latest release"""
  ),
  UpdateLine(
    file("CHANGELOG.md"),
    _.matches("## \\[Unreleased\\]"),
    (v, _) => s"## [Unreleased]\n\n## [$v] - ${java.time.LocalDate.now}"
  ),
  UpdateLine(
    file("CHANGELOG.md"),
    unreleasedCompare.unapplySeq(_).isDefined,
    (v, compareLine) => compareLine match {
      case unreleasedCompare(project, previous) =>
        s"[Unreleased]: https://github.com/$project/compare/v$v...HEAD\n[$v]: https://github.com/$project/compare/$previous...v$v"
    }
  )
)
