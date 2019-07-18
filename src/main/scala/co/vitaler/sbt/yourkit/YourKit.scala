package co.vitaler.sbt.yourkit

import java.io.File

import com.lightbend.sbt.javaagent.JavaAgent
import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys.resolvedJavaAgents
import com.lightbend.sbt.javaagent.JavaAgent.{ AgentModule, AgentScope, ResolvedAgent }
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import sbt.Keys.normalizedName
import sbt.librarymanagement.ModuleID
import sbt.{ AutoPlugin, Plugins, settingKey }

object YourKit extends AutoPlugin {
  object autoImport {
    val yourKitAgentPlatform = settingKey[String]("Supported platform (mac/win/linux-x86-64)")
    val yourKitInstallDir = settingKey[String]("Install directory for YourKit, usually /usr/local/")
    val yourKitVersion = settingKey[String]("Version of YourKit Agent installed, e.g. 2019.1")

    val yourKitPath = settingKey[String]("Resolved path to YourKit bin location, based on platform, version, and install dir")
    val yourKitAgentStartupOptions = settingKey[String]("Startup options passed to YourKit agent")
  }

  import autoImport._

  override def requires: Plugins = JavaAppPackaging && JavaAgent

  override lazy val projectSettings = Seq(
    yourKitAgentPlatform := "linux-x86-64",
    yourKitVersion := "2019.1",
    yourKitAgentStartupOptions := s"sessionname=${normalizedName.value}",
    yourKitInstallDir := "/usr/local/YourKit-JavaProfiler-2019.1/bin/linux-x86-64/libyjpagent.so",
    yourKitPath := s"${yourKitInstallDir.value}/bin/${yourKitAgentPlatform.value}/${soName(yourKitAgentPlatform.value)}",
    resolvedJavaAgents ++= Seq(
      ResolvedAgent(
        AgentModule(
          "yourkit",
          ModuleID("com.yourkit", "yourkit-agent", yourKitVersion.value),
          AgentScope(true, true, true, true),
          yourKitAgentStartupOptions.value
        ),
        new File(yourKitPath.value)
      )),
  )



  private def soName(platform: String): String = {
    if (platform == "mac")
      "libyjpagent.jnilib"
    else if (platform.startsWith("win"))
      "yjpagent.dll"
    else
      "libyjpagent.so"
  }


  //  private def startYourKitScript(defaultStartupOptions: String): String = """
  //if [[ -z "$YOURKIT_AGENT_DISABLED" ]]; then
  //  if [[ -z "$YOURKIT_AGENT_STARTUP_OPTIONS" ]]; then
  //    YOURKIT_AGENT_STARTUP_OPTIONS="""" + defaultStartupOptions + """"
  //    export YOURKIT_AGENT_STARTUP_OPTIONS
  //  fi
  //"""
  //
  //  private val endYourKitScript: String = """
  //fi
  //"""
  //
  //  private def yourKitExtraDefines(lines: Seq[String], defaultStartupOptions: String): Seq[String] = {
  //    Seq(startYourKitScript(defaultStartupOptions)) ++ lines ++ Seq(endYourKitScript)
  //  }

  //  private def findYourKitAgents(platforms: Seq[String], targetDir: File): Seq[PlatformData] = {
  //    if (platforms.size != 1) {
  //      sys.error("yourKitAgentPlatforms must currently be of length 1, sorry about the inconvenience.")
  //    }
  //
  //    platforms map { p =>
  //      val yjpBuild = "yjp-2016.02"
  //      val so = soName(p)
  //      val ext = so.split('.').last
  //      val mapping = s"yourkit/$p/yourkit.$ext"
  //      val path = s"/$yjpBuild/bin/$p/$so"
  //      val stream = Option(getClass.getResourceAsStream(path))
  //      stream match {
  //        case Some(s) =>
  //          val tempFile = targetDir / "yourkit" / p / s"yourkit.$ext"
  //          tempFile.getParentFile.mkdirs()
  //          IO.transferAndClose(s, new java.io.FileOutputStream(tempFile))
  //          val shellFragment = """addJava "-agentpath:${app_home}/../""" + mapping + """=${YOURKIT_AGENT_STARTUP_OPTIONS}""""
  //          PlatformData(tempFile, mapping, shellFragment)
  //
  //        case None =>
  //          sys.error(s"Unknown platform: $p")
  //      }
  //    }
  //  }
}
