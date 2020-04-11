## Config

The following parameters can be set to configure the plugin.

* **script_path** - Relative path to the groovy script file
* **output_file** - Relative path to the output file. Optional, defaults to	`<script-name>.jar`

## Examples

**Sample drone config:**
```yaml
pipeline:
  groovy_script_to_jar:
    when:
      branch: master
      event: push
    image: devatherock/drone-groovy-script-to-jar:2.4-alpine
    script_path: groovy/MyScript.groovy
    output_file: build/libs/my-script.jar
```

**Sample vela config:**
```yaml
steps:
  - name: groovy_script_to_jar:
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-groovy-script-to-jar:2.4-alpine
    parameters:
      script_path: groovy/MyScript.groovy
      output_file: build/libs/my-script.jar
```