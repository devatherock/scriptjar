[![CircleCI](https://circleci.com/gh/devatherock/scriptjar.svg?style=svg)](https://circleci.com/gh/devatherock/scriptjar)
[![Version](https://img.shields.io/docker/v/devatherock/vela-groovy-script-to-jar?sort=semver)](https://hub.docker.com/r/devatherock/vela-groovy-script-to-jar/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e334a59aeeac473f8c0138bc538ed4f6)](https://www.codacy.com/gh/devatherock/scriptjar/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=devatherock/scriptjar&amp;utm_campaign=Badge_Grade)
[![Docker Pulls](https://img.shields.io/docker/pulls/devatherock/vela-groovy-script-to-jar.svg)](https://hub.docker.com/r/devatherock/vela-groovy-script-to-jar/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/vela-groovy-script-to-jar.svg?sort=date)](https://hub.docker.com/r/devatherock/vela-groovy-script-to-jar/)
[![Docker Image Layers](https://img.shields.io/microbadger/layers/devatherock/vela-groovy-script-to-jar.svg)](https://microbadger.com/images/devatherock/vela-groovy-script-to-jar)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# vela-groovy-script-to-jar

CI plugin to convert Groovy scripts to JVM executable JAR. It packages all `@Grab` dependencies and Groovy lib, so it can
be run without Groovy on target machine. Main class name is the same as original script name.

## Usage
### Docker

```shell
docker run --rm \
  -v /path/to/target-repo:/work \
  -w=/work \
  -e PARAMETER_SCRIPT_PATH=/work/TargetRepoScript.groovy \
  -e PARAMETER_OUTPUT_FILE=/work/docker/TargetRepoScript.jar \
  -e PARAMETER_STATIC_COMPILE=true \
  devatherock/vela-groovy-script-to-jar:latest
```  

### CI
Please refer [docs](DOCS.md)

## Tests
To test the latest plugin images, run the below command
```shell
./gradlew test
```
