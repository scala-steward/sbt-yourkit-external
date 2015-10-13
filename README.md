# sbt-yourkit
Adds YourKit agent to Java packages built using sbt-native-packager.

The plugin adds the YourKit shared library for a target platform into
the package built by sbt-native-packager, and adds the library as an
agent at startup.

The YourKit Java Profiler is a profiling tool for memory, CPU, threads,
exceptions, and other aspects of your Java VM.
See https://www.yourkit.com/features/ for more details.

## Usage

1. Add the following to your project build, e.g. `project/plugins.sbt`:

  ```scala
  addSbtPlugin("com.gilt.sbt" % "sbt-yourkit" % "0.0.1")
  ```

2. Add the `YourKit` plugin to your project, e.g.:

  ```scala
  enablePlugins(YourKit)
  ```
  
## Customization
Two SBT keys are intended to be customized for your purposes:

| Key name                   | Purpose                       | Default value                  |
|----------------------------|-------------------------------|--------------------------------|
| yourKitAgentPlatforms      | Shared object target platform | `Seq("linux-x86-64")`          |
| yourKitAgentStartupOptions | Startup options for YourKit   | `sessionname=${project_name},` |

The startup options for the YourKit agent are described at
https://www.yourkit.com/docs/80/help/startup_options.jsp

## Requirements
The plugin requires SBT >= 0.13.5 (as it is an SBT AutoPlugin). It also requires Java 7+.

Your project should use [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) >= 1.0.0
for packaging, and use the `JavaAppPackaging` AutoPlugin (or some plugin that in turn
depends on this). This is standard for packaged applications which run on the Java VM.

## Limitations
While it would be nice to include support for multiple platforms in the archive, and
then choose between them at runtime, this requires some runtime checks in the start
script. This is possible, but error-prone without access to the appropriate systems
for testing.
