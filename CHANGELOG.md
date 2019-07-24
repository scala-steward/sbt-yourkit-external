# Changelog

<!-- Follow the guidelines at: https://keepachangelog.com/ -->

## [Unreleased]

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

[Unreleased]: https://github.com/vital-software/sbt-update-lines/compare/v0.1.1...HEAD
[0.1.1]: https://github.com/vital-software/sbt-update-lines/compare/v0.0.3...v0.1.1
[0.0.3]: https://github.com/vital-software/sbt-update-lines/releases/tag/v0.0.3
