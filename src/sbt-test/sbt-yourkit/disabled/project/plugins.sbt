addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.6")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("co.vitaler" % "sbt-yourkit-external" % x)
  case _       => sys.error(
    """|The system property 'plugin.version' is not defined.
       |Specify this property using the scriptedLaunchOpts -D.""".stripMargin
  )
}
