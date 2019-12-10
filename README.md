# sbt-yourkit-external

[![Build status](https://badge.buildkite.com/b75b135f9ddc041dc9c16d46e1f57d98f4793c233438e1f6af.svg)](https://buildkite.com/vital/sbt-yourkit-external)

Adds a YourKit agent javaOption to the `run` and `Universal` (native-packaging) configurations. The YourKit Java
Profiler is a profiling tool for memory, CPU, threads, exceptions, and other aspects of your Java VM. See
https://www.yourkit.com/features/ for more details.

Unlike the upstream `sbt-yourkit` plugin this is based on, this plugin doesn't contain the profiler binary itself, nor
add it to your classpath. Instead, you're expected to install the agent binary yourself. Typically, this means doing
something like this in your Docker build:

```
RUN wget https://www.yourkit.com/download/docker/YourKit-JavaProfiler-2019.1-docker.zip -P /tmp/ && \
  unzip /tmp/YourKit-JavaProfiler-2019.1-docker.zip -d /usr/local && \
  rm /tmp/YourKit-JavaProfiler-2019.1-docker.zip
```

The advantage is that your can use the newest agent versions without updating this plugin.

## Usage

Add the plugin to your project build, e.g. `project/plugins.sbt`:

```scala
resolvers += Resolver.bintrayIvyRepo("vitaler", "sbt-plugins")
addSbtPlugin("co.vitaler" % "sbt-yourkit-external" % "0.2.1") // Latest release
```

(You'll need to add the resolver too, as we're not yet syncing into the main sbt-plugins community repo)

### Configuration

The most important configuration values are:

Key | Type | Purpose | Default value
--- | --- | --- | ---
`yourKitVersion` | `String` | Version of YourKit installed | `"2019.1"`
`yourKitInstallDir` | `String` | Path to the root of the YourKit agent installation | `s"/usr/local/YourKit-JavaProfiler-${yourKitVersion.value}"`
`yourKitAgentStartupOptions` | `Map[String, String]` | [Startup options for YourKit](https://www.yourkit.com/docs/java/help/startup_options.jsp) | `Map("sessionname" -> s"$project")`
`yourKitAgentPlatform` | `String` | Platform name, [according to YourKit](https://www.yourkit.com/docs/java/help/agent.jsp), e.g. `linux-ppc-64` | Should be automatically detected, if you're running in a 64-bit architecture.

These settings are combined into a `yourKitJavaOption` setting, which is added to `Universal / javaOptions` and `run / javaOptions` (you'll need
`run / fork` to be true for the latter to take effect)

## Security Note

The YourKit agent opens a TCP port which allows access to the profiling options available in the VM. This should be
secured from access by arbitrary people:

 - You can place the running service in a network with appropriate network restrictions, so that only allowed machines
   can connect to the service.
 - You can add [startup options](https://www.yourkit.com/docs/java/help/startup_options.jsp) to limit the network
   interfaces on which the agent listens.
