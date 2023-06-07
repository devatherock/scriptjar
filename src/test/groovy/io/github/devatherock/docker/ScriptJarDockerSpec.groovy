package io.github.devatherock.docker

import java.nio.file.Files
import java.nio.file.Paths

import io.github.devatherock.util.ProcessUtil

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Test class to test the built docker images
 */
class ScriptJarDockerSpec extends Specification {

    @Shared
    def config = [
            'drone': [
                    'envPrefix': 'PLUGIN_'
            ],
            'vela' : [
                    'envPrefix': 'PARAMETER_'
            ]
    ]

    void setupSpec() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
        ProcessUtil.executeCommand("docker pull devatherock/scriptjar:latest")
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
                                                       'devatherock/scriptjar:latest'])
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
