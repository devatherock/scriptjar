## Config

The following parameters can be set to configure the plugin.

* **script_path** - Relative path to the groovy script file
* **output_file** - Relative path to the output file. Optional, defaults to	`<script-name>.jar`
* **static_compile** - Indicates whether to compile the script statically

## Examples

**Sample vela config:**
```yaml
steps:
  - name: groovy_script_to_jar
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-groovy-script-to-jar:0.6.0
    parameters:
      script_path: groovy/MyScript.groovy
      output_file: build/libs/my-script.jar
```