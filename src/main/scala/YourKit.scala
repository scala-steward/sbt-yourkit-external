package com.gilt.sbt.yourkit

import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging.autoImport.bashScriptExtraDefines

private[yourkit] case class PlatformData(sourceFile: File, targetPath: String, shellFragment: String)

object YourKit extends AutoPlugin {

  object autoImport {
    val yourKitAgentPlatforms = settingKey[Seq[String]]("Supported platforms (mac/win/linux-x86-64)")
    val yourKitAgentStartupOptions = settingKey[String]("Startup options passed to YourKit agent")
  }

  import autoImport._

  val yourKitAgents = taskKey[Seq[PlatformData]]("New Relic agent Jar locations, mappings, and code to enable")

  override def requires = JavaAppPackaging

  override lazy val projectSettings = Seq(
    yourKitAgentPlatforms := Seq("linux-x86-64"),
    yourKitAgentStartupOptions := s"sessionname=${normalizedName.value},",
    yourKitAgents := findYourKitAgents(yourKitAgentPlatforms.value, (target in Compile).value),
    mappings in Universal ++= yourKitAgents.value.map(agent => agent.sourceFile -> agent.targetPath),
    bashScriptExtraDefines ++= yourKitExtraDefines(yourKitAgents.value.map(_.shellFragment), yourKitAgentStartupOptions.value)
  )

  private def soName(platform: String): String = {
    if (platform == "mac")
      "libyjpagent.jnilib"
    else if (platform.startsWith("win"))
      "yjpagent.dll"
    else
      "libyjpagent.so"
  }

  private def startYourKitScript(defaultStartupOptions: String): String = """
if [[ -z "$YOURKIT_AGENT_DISABLED" ]]; then
  if [[ -z "$YOURKIT_AGENT_STARTUP_OPTIONS" ]]; then
    YOURKIT_AGENT_STARTUP_OPTIONS="""" + defaultStartupOptions + """"
    export YOURKIT_AGENT_STARTUP_OPTIONS
  fi
"""

  private val endYourKitScript: String = """
fi
"""

  private def yourKitExtraDefines(lines: Seq[String], defaultStartupOptions: String): Seq[String] = {
    Seq(startYourKitScript(defaultStartupOptions)) ++ lines ++ Seq(endYourKitScript)
  }

  private def findYourKitAgents(platforms: Seq[String], targetDir: File): Seq[PlatformData] = {
    if (platforms.size != 1) {
      sys.error("yourKitAgentPlatforms must currently be of length 1, sorry about the inconvenience.")
    }

    platforms map { p =>
      val yjpBuild = "yjp-2016.02"
      val so = soName(p)
      val ext = so.split('.').last
      val mapping = s"yourkit/$p/yourkit.$ext"
      val path = s"/$yjpBuild/bin/$p/$so"
      val stream = Option(getClass.getResourceAsStream(path))
      stream match {
        case Some(s) =>
          val tempFile = targetDir / "yourkit" / p / s"yourkit.$ext"
          tempFile.getParentFile.mkdirs()
          IO.transferAndClose(s, new java.io.FileOutputStream(tempFile))
          val shellFragment = """addJava "-agentpath:${app_home}/../""" + mapping + """=${YOURKIT_AGENT_STARTUP_OPTIONS}""""
          PlatformData(tempFile, mapping, shellFragment)

        case None =>
          sys.error(s"Unknown platform: $p")
      }
    }
  }
}
