import scala.io.{ Codec, Source }

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

// Test assertions

TaskKey[Unit]("checkTarget") := {
  val ini = Source.fromFile(file("target/universal/stage/conf/application.ini"))(Codec.UTF8).getLines().toList
  println(ini)

  if (!ini.contains("-J-agentpath:/usr/local/YourKit-JavaProfiler-2019.1/bin/linux-x86-64/libyjpagent.so=sessionname=root")) {
    sys.error("Could not find expected agent path argument in application.ini")
  }
  ()
}
