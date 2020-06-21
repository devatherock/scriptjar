[![CircleCI](https://circleci.com/gh/devatherock/scriptjar.svg?style=svg)](https://circleci.com/gh/devatherock/scriptjar)
[![Docker Pulls](https://img.shields.io/docker/pulls/devatherock/vela-groovy-script-to-jar.svg)](https://hub.docker.com/r/devatherock/vela-groovy-script-to-jar/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/vela-groovy-script-to-jar.svg?sort=date)](https://hub.docker.com/r/devatherock/vela-groovy-script-to-jar/)
[![Docker Image Layers](https://img.shields.io/microbadger/layers/devatherock/vela-groovy-script-to-jar.svg)](https://microbadger.com/images/devatherock/vela-groovy-script-to-jar)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# vela-groovy-script-to-jar

CI plugin to convert Groovy scripts to JVM executable JAR. It packages all `@Grab` dependencies and Groovy lib, so it can
be run without Groovy on target machine. Main class name is the same as original script name.

# Usage
## Docker

```
docker run --rm \
  -v /path/to/target-repo:/work \
  -w=/work \
  -e PARAMETER_SCRIPT_PATH=/work/TargetRepoScript.groovy \
  -e PARAMETER_OUTPUT_FILE=/work/docker/TargetRepoScript.jar \
  -e PARAMETER_STATIC_COMPILE=true \
  devatherock/vela-groovy-script-to-jar:0.6.2
```  

## CircleCI
```yaml
version: 2.1
jobs:
  groovy_script_to_jar:
    docker:
      - image: devatherock/vela-groovy-script-to-jar:0.6.2
    working_directory: ~/my-repo
    environment:
      PARAMETER_SCRIPT_PATH: groovy/MyScript.groovy                  # Relative path to the groovy script file
      PARAMETER_OUTPUT_FILE: build/libs/my-script.jar                # Relative path to the output file. Optional, defaults to	<script-name>.jar
      PARAMETER_STATIC_COMPILE: true                                 # Indicates whether to compile the script statically
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh

workflows:
  version: 2.1
  script_to_jar:
    jobs:
      - sync:
          filters:
            branches:
              only: master                                        # Source branch
```

## vela
Please refer [docs](DOCS.md)
