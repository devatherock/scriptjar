package io.github.devatherock.docker

import io.github.devatherock.util.ProcessUtil
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Test class to test the built docker images
 */
class ScriptJarDockerSpec extends Specification {

    @Shared
    def config = [
            'drone': [
                    'image'    : 'devatherock/drone-groovy-script-to-jar:latest',
                    'envPrefix': 'PLUGIN_'
            ],
            'vela' : [
                    'image'    : 'devatherock/vela-groovy-script-to-jar:latest',
                    'envPrefix': 'PARAMETER_'
            ]
    ]

    void setupSpec() {
        config.each { ci ->
            ProcessUtil.executeCommand("docker pull ${ci.value['image']}")
        }
    }

    void cleanup() {
        Files.deleteIfExists(Paths.get("${System.properties['user.dir']}/build/Hello.jar"))
    }

    @Unroll
    void 'test convert groovy script to jar - ci: #ci'() {
        when:
        def dockerOutput = ProcessUtil.executeCommand(['docker', 'run', '--rm',
                                                       '-v', "${System.properties['user.dir']}:/work",
                                                       '-w=/work',
                                                       '-e', "${config[ci].envPrefix}SCRIPT_PATH=/work/src/test/resources/Hello.groovy",
                                                       '-e', "${config[ci].envPrefix}OUTPUT_FILE=/work/build/Hello.jar",
                                                       config[ci].image])
        then:
        dockerOutput[0] == 0

        when:
        def jarOutput = ProcessUtil.executeCommand(['java', '-jar',
                                                    "${System.properties['user.dir']}/build/Hello.jar",
                                                    '-n', 'World'])

        then:
        jarOutput[0] == 0
        jarOutput[1].contains('Hello World')

        where:
        ci << ['drone', 'vela']
    }
}
