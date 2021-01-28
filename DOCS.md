## vela
### Config

The following parameters can be set to configure the plugin.

* **script_path** - Relative path to the groovy script file
* **output_file** - Relative path to the output file. Optional, defaults to	`<script-name>.jar`
* **static_compile** - Indicates whether to compile the script statically

### Examples

**Sample vela config:**
```yaml
steps:
  - name: groovy_script_to_jar
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-groovy-script-to-jar:latest
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
      - image: devatherock/vela-groovy-script-to-jar:latest
    working_directory: ~/my-repo
    environment:
      PARAMETER_SCRIPT_PATH: groovy/MyScript.groovy                  # Relative path to the groovy script file
      PARAMETER_OUTPUT_FILE: build/libs/my-script.jar                # Relative path to the output file. Optional, defaults to	<script-name>.jar
      PARAMETER_STATIC_COMPILE: true                                 # Indicates whether to compile the script statically
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh
```