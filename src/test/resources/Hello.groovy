@Grab(group = 'commons-cli', module = 'commons-cli', version = '1.4')
@Grab(group = 'org.codehaus.groovy', module = 'groovy-cli-commons', version = '3.0.9')

import groovy.cli.commons.CliBuilder

def cli = new CliBuilder(usage: 'groovy Hello.groovy [options]')
cli.n(longOpt: 'name', args: 1, argName: 'name', 'name')
def options = cli.parse(args)

println("Hello ${options.n}")