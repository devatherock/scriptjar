[![CircleCI](https://circleci.com/gh/devatherock/scriptjar.svg?style=svg)](https://circleci.com/gh/devatherock/scriptjar)
[![Version](https://img.shields.io/docker/v/devatherock/scriptjar?sort=semver)](https://hub.docker.com/r/devatherock/scriptjar/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e334a59aeeac473f8c0138bc538ed4f6)](https://www.codacy.com/gh/devatherock/scriptjar/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=devatherock/scriptjar&amp;utm_campaign=Badge_Grade)
[![Docker Pulls](https://img.shields.io/docker/pulls/devatherock/vela-groovy-script-to-jar.svg)](https://hub.docker.com/r/devatherock/scriptjar/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/scriptjar.svg?sort=date)](https://hub.docker.com/r/devatherock/scriptjar/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# scriptjar

CI plugin to convert Groovy scripts to JVM executable JAR. It packages all `@Grab` dependencies and Groovy libraries, so it can be run without Groovy on target machine. Main class name is the same as original script name.

## Usage
### Docker

```shell
docker run --rm \
  -v /path/to/target-repo:/work \
  -w=/work \
  -e PARAMETER_SCRIPT_PATH=/work/TargetRepoScript.groovy \
  -e PARAMETER_OUTPUT_FILE=/work/docker/TargetRepoScript.jar \
  -e PARAMETER_STATIC_COMPILE=false \
  devatherock/scriptjar:1.0.0
```  

### vela
#### Config

The following parameters can be set to configure the plugin.

*   **script_path** - Relative path to the groovy script file
*   **output_file** - Relative path to the output file. Optional, defaults to	`<script-name>.jar`
*   **static_compile** - Indicates whether to compile the script statically. Defaults to `false`

#### Examples

**Sample vela config:**

```yaml
steps:
  - name: groovy_script_to_jar
    ruleset:
      branch: master
      event: push
    image: devatherock/scriptjar:1.0.0
    parameters:
      script_path: groovy/MyScript.groovy
      output_file: build/libs/my-script.jar
```

### CircleCI

```yaml
version: 2.1
jobs:
  groovy_script_to_jar:
    docker:
      - image: devatherock/scriptjar:1.0.0
    working_directory: ~/my-repo
    environment:
      PARAMETER_SCRIPT_PATH: groovy/MyScript.groovy                  # Relative path to the groovy script file
      PARAMETER_OUTPUT_FILE: build/libs/my-script.jar                # Relative path to the output file. Optional, defaults to	<script-name>.jar
      PARAMETER_STATIC_COMPILE: false                                # Indicates whether to compile the script statically. Defaults to `false`
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh
```

## Tests
To test the latest plugin images, run the below command

```shell
make test
```
