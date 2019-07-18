package co.vitaler.sbt.yourkit

import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import sbt.Keys.normalizedName
import sbt._
import Keys._

object YourKit extends AutoPlugin {
  object autoImport {
    val yourKitAgentPlatform = settingKey[String]("Supported platform (mac/win/linux-x86-64)")
    val yourKitInstallDir = settingKey[String]("Install directory for YourKit, usually /usr/local/")
    val yourKitVersion = settingKey[String]("Version of YourKit Agent installed, e.g. 2019.1")
    val yourKitAgentStartupOptions = settingKey[Seq[String]]("Startup options passed to YourKit agent")

    val yourKitPath =
      settingKey[String]("Resolved path to YourKit bin location, based on platform, version, and install dir")
    val yourKitJavaOption = settingKey[String]("Resolved java option to load the YourKit agent")
  }

  import autoImport._

  override def trigger = allRequirements

  override def requires: Plugins = JavaAppPackaging

  override lazy val projectSettings = Seq(
    // Guess platform based on os.name, assuming 64-bit
    yourKitAgentPlatform := {
      System.getProperty("os.name").toLowerCase match {
        case mac if mac.contains("mac")       => "mac"
        case linux if linux.contains("linux") => "linux-x86-64"
        case win if win.contains("win")       => "win64"
        case _                                => throw new RuntimeException("Unknown platform, configure yourKitAgentPlatform setting manually")
      }
    },
    yourKitVersion := "2019.1",
    yourKitAgentStartupOptions := Seq(s"sessionname=${normalizedName.value}"),
    yourKitInstallDir := s"/usr/local/YourKit-JavaProfiler-${yourKitVersion.value}",
    yourKitPath := s"${yourKitInstallDir.value}/bin/${yourKitAgentPlatform.value}/${soName(yourKitAgentPlatform.value)}",
    yourKitJavaOption := s"-J-agentpath:${yourKitPath.value}=${yourKitAgentStartupOptions.value.mkString(",")}",

    Universal / javaOptions += yourKitJavaOption.value,
    run / javaOptions += yourKitJavaOption.value,
  )

  private def soName(platform: String): String =
    if (platform == "mac")
      "libyjpagent.jnilib"
    else if (platform.startsWith("win"))
      "yjpagent.dll"
    else
      "libyjpagent.so"
}
