import scala.io.{ Codec, Source }

name := "disabled"

lazy val root = (project in file("."))
  .enablePlugins(
    JavaAppPackaging
  )

Universal / packageName := "app"
executableScriptName := "start"
Compile / doc / sources := Seq.empty
Compile / packageDoc / publishArtifact := false
publish / skip := true
Universal / packageZipTarball / universalArchiveOptions := Seq("--force-local", "-pcvf")

yourKitAgentStartupOptions ++= Map("listen" -> "local", "port" -> "10001")
Universal / yourKitAgentStartupOptions ++= Map("listen" -> "all")
yourKitAgentPlatform := "linux-x86-64"
yourKitVersion := "2019.8"

// Test assertions

TaskKey[Unit]("checkNoTarget") := {
  if (file("target/universal/stage/conf/application.ini").exists()) {
    val ini: List[String] = Source.fromFile(file("target/universal/stage/conf/application.ini"))(Codec.UTF8).getLines().toList
    if (ini.exists(_.contains("YourKit"))) {
      println(ini.mkString("\n"))
      sys.error("Found unexpected agent path argument in application.ini")
    }
    ()
  } else {
    () // There may be no arguments at all if we are disabled
  }
}
