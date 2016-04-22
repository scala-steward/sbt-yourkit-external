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
  addSbtPlugin("com.gilt.sbt" % "sbt-yourkit" % "0.0.3")
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

## Overriding at runtime
You can override the behaviour of your packaged application at runtime by setting some
environment variables.

| Environment variable          | Purpose                                             |
|-------------------------------|-----------------------------------------------------|
| YOURKIT_AGENT_DISABLED        | If set (to any value), completely disable the agent |
| YOURKIT_AGENT_STARTUP_OPTIONS | If set, overrides startup options provided to agent |

## Requirements
The plugin requires SBT >= 0.13.5 (as it is an SBT AutoPlugin). It also requires Java 7+.

Your project should use [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) >= 1.0.0
for packaging, and use the `JavaAppPackaging` AutoPlugin (or some plugin that in turn
depends on this). This is standard for packaged applications which run on the Java VM.

## Security Note
The YourKit agent opens a TCP port which allows access to the profiling options available in the VM.
This should be secured from access by arbitrary people:

 - You can place the running service in a network with appropriate network restrictions, so that
   only allowed machines can connect to the service.

 - You can add flags to limit the network interfaces on which the agent listens, see some possible
   options at https://www.yourkit.com/docs/80/help/startup_options.jsp
   In particular, the `onlylocal` option allows connections only from the local machine.

## Limitations
While it would be nice to include support for multiple platforms in the archive, and
then choose between them at runtime, this requires some runtime checks in the start
script. This is possible, but error-prone without access to the appropriate systems
for testing.
