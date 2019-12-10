# Changelog

<!-- Follow the guidelines at: https://keepachangelog.com/ -->

## [Unreleased]

## [0.2.1] - 2019-12-10

### Changed

- Moved `yourKitEnabled` to a project setting

## [0.2.0] - 2019-12-10

### Added

- Added `yourKitEnabled` boolean setting, which controls whether the agent is ever
  actually added to JVM options

## [0.1.3] - 2019-07-24

### Added

- Will now only attempt to add the profiler to the run javaOptions if the dynamic
  library is found at the expected path

## [0.1.2] - 2019-07-24

### Changed

- Changed the type of the startup options setting to be a `Map[String, String]`
  of options
- Ensure native-packaged runs and normal runs can use separate startup options

## [0.1.1] - 2019-07-18

### Changed

- Actual agent binaries are no longer distributed alongside this plugin

### Removed

- Removed environment variable support for disabling the agent or passing extra
  startup options

## [0.0.3]

### Added

- Initial version imported from upstream

[Unreleased]: https://github.com/vital-software/sbt-update-lines/compare/v0.2.1...HEAD
[0.2.1]: https://github.com/vital-software/sbt-update-lines/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/vital-software/sbt-update-lines/compare/v0.1.3...v0.2.0
[0.1.3]: https://github.com/vital-software/sbt-update-lines/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/vital-software/sbt-update-lines/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/vital-software/sbt-update-lines/compare/v0.0.3...v0.1.1
[0.0.3]: https://github.com/vital-software/sbt-update-lines/releases/tag/v0.0.3
