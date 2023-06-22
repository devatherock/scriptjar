# Changelog

## [Unreleased]
### Changed
- Updated dockerhub readme in CI pipeline using plugin version `3.4.2`
- Integration tested the arm image
- Upgraded gradle to 7

## [2.0.0] - 2023-05-25
### Changed
- Built a multi-arch docker image

## [1.0.0] - 2021-10-28
### Added
- Functional tests to CI pipeline

### Changed
- [#10](https://github.com/devatherock/scriptjar/issues/10): Upgraded groovy to `3.0.9`
- Published one docker image instead of two

## [0.7.0] - 2021-01-27
### Added
- test: Functional tests to test the built docker images

## [0.6.2] - 2020-06-21
### Changed
- Stopped using environment variable `VELA`

## [0.6.1] - 2020-06-21
### Changed
- Corrected a log statement

## [0.6.0] - 2020-06-20
### Added
- Option for static compilation

## [0.5.0] - 2020-04-11
### Added
- [Issue 4](https://github.com/devatherock/scriptjar/issues/4): Published images for Vela

### Changed
- [Issue 3](https://github.com/devatherock/scriptjar/issues/3): Disabled docker layer caching

## [0.4.0] - 2019-02-18
### Added
- Created plugin for `groovy 2.5`

## [0.3.0] - 2018-10-04
### Added
- PR check

### Changed
- Upgraded docker image version to `17.12.1-ce`

## [0.2.0] - 2018-10-02
### Changed
- Set the user as root to make writes work with drone

## [0.1.0] - 2018-09-29
### Added
- Packaged as a drone.io/CircleCI plugin with `groovy 2.4`
- Defaulted output jar name to main class name