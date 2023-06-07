#!/usr/bin/env groovy

import static org.codehaus.groovy.control.Phases.CLASS_GENERATION

import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.logging.Logger

import groovy.cli.commons.CliBuilder
import groovy.grape.Grape
import groovy.io.FileType
import groovy.transform.CompileStatic
import groovy.transform.Field
import groovy.transform.TypeChecked

import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.tools.GroovyClass

System.setProperty('java.util.logging.SimpleFormatter.format',
        '%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tL%1$tz %4$s %5$s%6$s%n')
@Field static final Logger LOGGER = Logger.getLogger('scriptjar.log')

def cli = new CliBuilder(usage: 'groovy scriptjar.groovy [options]')
cli.i(longOpt: 'input', args: 1, argName: 'input', 'Input file name')
cli.o(longOpt: 'output', args: 1, argName: 'output', 'Output file name')
cli.s(longOpt: 'static', args: 0, argName: 'static', 'Flag indicating if compilation should be static')
cli.n(longOpt: 'no-siblings', args: 0, argName: 'no-siblings', 'Flag indicating that only the input groovy file should be compiled')

def options = cli.parse(args)
if (!options.i) {
    cli.usage()
    System.exit(1)
}

@Field boolean isCompileStatic = false
@Field boolean noSiblings = false

isCompileStatic = options.s
noSiblings = options.n

// Find output file name
String inputFileName = options.i
LOGGER.info("Input file name: $inputFileName")
File file = new File(inputFileName)

// Extract only the file name out of input path
String prefix
if (inputFileName.contains(System.properties['file.separator'])) {
    prefix = inputFileName.substring(inputFileName.lastIndexOf(System.properties['file.separator']) + 1, inputFileName.lastIndexOf('.'))
} else {
    prefix = inputFileName.substring(0, inputFileName.lastIndexOf('.'))
}
String outputFileName = options.o ?: "${prefix}.jar"

// Create output folder path
if (outputFileName.contains(System.properties['file.separator'])) {
    String folderPath = outputFileName.substring(0, outputFileName.lastIndexOf(System.properties['file.separator']))
    Files.createDirectories(Paths.get(folderPath))
}

new File(outputFileName).withOutputStream {
    it << createUberjar(file, prefix)
}

/**
 * Compiles the input groovy file
 *
 * @param prefix
 * @param file
 * @return
 */
List<GroovyClass> compile(String prefix, File file) {
    List<GroovyClass> classes = [] as List<GroovyClass>

    // set up classloader with all the grapes loaded
    final GroovyClassLoader classLoader = new GroovyClassLoader()
    classLoader.parseClass(file.text)

    if (!noSiblings) {
        getSiblingGroovyFiles(file).each {
            classLoader.parseClass(it.text)
        }
    }

    // disable groovy grapes - we're resolving these ahead of time
    CompilerConfiguration compilerConfig = new CompilerConfiguration()
    Set disabledTransforms = ['groovy.grape.GrabAnnotationTransformation'] as Set
    compilerConfig.setDisabledGlobalASTTransformations(disabledTransforms)

    // Enable static compilation
    if (isCompileStatic) {
        LOGGER.info('Enabling static compilation')
        compilerConfig.addCompilationCustomizers(new ASTTransformationCustomizer(CompileStatic))
        compilerConfig.addCompilationCustomizers(new ASTTransformationCustomizer(TypeChecked))
    }

    // compile main class
    CompilationUnit unit = new CompilationUnit(compilerConfig, null, classLoader)
    unit.addSource(SourceUnit.create(prefix, file.text))
    LOGGER.info("${file.name} => ${prefix} (main class)")
    unit.compile(CLASS_GENERATION)
    classes += unit.getClasses()

    // compile groovy files in same folder
    if (!noSiblings) {
        getSiblingGroovyFiles(file).each {
            CompilationUnit dependentUnit = new CompilationUnit(compilerConfig, null, classLoader)
            def className = it.name.replaceAll(/\.groovy$/, '')
            LOGGER.info("${it.name} => ${className} (lib)")
            dependentUnit.addSource(SourceUnit.create(className, it.text))
            dependentUnit.compile(CLASS_GENERATION)

            classes += dependentUnit.getClasses()
        }
    }

    return classes
}

/**
 * Gets a list of groovy files in the same folder as the input file
 *
 * @param mainGroovyFile
 * @return
 */
List<File> getSiblingGroovyFiles(File mainGroovyFile) {
    def groovyFileRe = /.*\.groovy$/
    List<File> files = [] as List<File>
    mainGroovyFile.getAbsoluteFile().getParentFile().eachFile(FileType.FILES) {
        if (!it.hidden && it.name =~ groovyFileRe && it.name != mainGroovyFile.name && it.name != 'scriptjar.groovy') {
            files << it
        }
    }

    return files
}

/**
 * Gets groovy library jar files
 *
 * @param neededJars
 * @return
 */
List<File> getGroovyLibs(List neededJars) {
    def libs = new File('.')
    if (System.getenv('GROOVY_HOME')) {
        libs = new File(System.getenv('GROOVY_HOME'), 'lib')
    } else if (System.getProperty("user.home") &&
            new File(System.getProperty("user.home"), '.groovy/grapes').exists()) {
        libs = new File(System.getProperty("user.home"), '.groovy/grapes')
    } else {
        LOGGER.info("Can't find GROOVY_HOME")
        System.exit(1)
    }
    def groovylibs = libs.listFiles().findAll { jar ->
        neededJars.any { needed -> jar.name =~ needed }
    }
    if (groovylibs) {
        return groovylibs
    } else {
        LOGGER.info("Can't find Groovy lib in ${libs.absolutePath}, specify it manually as Grab dependency")
        System.exit(1)
    }
}

/**
 * Gets all Grape dependencies of the input groovy file
 *
 * @param source
 * @return
 */
List dependencies(File source) {
    final GroovyClassLoader classLoader = new GroovyClassLoader()
    classLoader.parseClass(source.text)
    getSiblingGroovyFiles(source).each {
        classLoader.parseClass(it.text)
    }
    def files = Grape.resolve([:], Grape.listDependencies(classLoader)).collect { new JarFile(it.path) }
    files.addAll(getGroovyLibs([/groovy-\d+.\d+.\d+.jar/]).collect { new JarFile(it) })

    return files
}

/**
 * Adds a class file into a jar file
 *
 * @param jos
 * @param entry
 * @param data
 */
void writeJarEntry(JarOutputStream jos, JarEntry entry, byte[] data) {
    entry.setSize(data.length)
    jos.putNextEntry(entry)
    jos.write(data)
}

/**
 * Creates an executable jar
 *
 * @param file
 * @param prefix
 * @return
 */
byte[] createUberjar(File file, String prefix) {
    ByteArrayOutputStream output = new ByteArrayOutputStream()
    JarOutputStream jos = new JarOutputStream(output)

    jos.putNextEntry(new JarEntry('META-INF/'))
    writeJarEntry(jos, new JarEntry('META-INF/MANIFEST.MF'), "Manifest-Version: 1.0\nMain-Class: ${prefix}\n".getBytes())
    compile(prefix, file).each {
        writeJarEntry(jos, new JarEntry("${it.name}.class"), it.bytes)
    }

    def directories = ['META-INF/', 'META-INF/MANIFEST.MF']

    dependencies(file).each { jar ->
        LOGGER.info("Merging ${jar.name}")
        jar.entries().each { JarEntry entry ->
            if (!directories.contains(entry.name)) {
                writeJarEntry(jos, entry, jar.getInputStream(entry).getBytes())
                directories << entry.name
            }
        }
    }

    jos.close()
    return output.toByteArray()
}