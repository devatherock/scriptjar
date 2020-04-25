[![CircleCI](https://circleci.com/gh/devatherock/scriptjar.svg?style=svg)](https://circleci.com/gh/devaprasadh/scriptjar)
[![Docker Pulls](https://img.shields.io/docker/pulls/devatherock/drone-groovy-script-to-jar.svg)](https://hub.docker.com/r/devatherock/drone-groovy-script-to-jar/)
[![Microbadger](https://images.microbadger.com/badges/image/devatherock/drone-groovy-script-to-jar.svg)](https://microbadger.com/images/devatherock/drone-groovy-script-to-jar)
# drone-groovy-script-to-jar

drone.io/CircleCI plugin to convert Groovy scripts to JVM executable JAR. It packages all `@Grab` dependencies and Groovy lib, so it can
be run without Groovy on target machine. Main class name is the same as original script name.

# Usage
## CircleCI
```yaml
version: 2
jobs:
  groovy_script_to_jar:
    docker:
      - image: devatherock/drone-groovy-script-to-jar:2.4-alpine
    working_directory: ~/my-repo
    environment:
      PLUGIN_SCRIPT_PATH: groovy/MyScript.groovy                  # Relative path to the groovy script file
      PLUGIN_OUTPUT_FILE: build/libs/my-script.jar                # Relative path to the output file. Optional, defaults to	<script-name>.jar
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh

workflows:
  version: 2
  script_to_jar:
    jobs:
      - sync:
          filters:
            branches:
              only: master                                        # Source branch
```

## drone.io and vela
Please refer [docs](DOCS.md)
