## Config

The following parameters can be set to configure the plugin.

* **script_path** - Relative path to the groovy script file
* **output_file** - Relative path to the output file. Optional, defaults to	`<script-name>.jar`

## Examples

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