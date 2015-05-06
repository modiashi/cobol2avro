package com.legstar.avro.translator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exposes the Translator utility as a command line tool.
 * 
 */
public class Xsd2AvroTranslatorMain {

    /** Options that can be setup. */
    private static final String OPTION_AVRO_NAMESPACE_PREFIX = "namespace";

    private static final String OPTION_OUTPUT = "output";

    private static final String OPTION_INPUT = "input";

    private static final String OPTION_HELP = "help";

    private static final String OPTION_VERSION = "version";

    private static Logger log = LoggerFactory.getLogger(Xsd2AvroTranslatorMain.class);

    /** The defaults. */
    private static final String DEFAULT_INPUT_FOLDER_PATH = "xsd";

    private static final String DEFAULT_OUTPUT_FOLDER_PATH = "target/gen";

    private static final String DEFAULT_AVRO_NAMESPACE_PREFIX = "example.avro";

    /**
     * A file containing COBOL annotated XML schemas used to generate Avro schemas.
     * Defaults to xsd relative folder.
     */
    private File xsdInput;

    /**
     * A folder containing generated artifacts. Defaults to target relative
     * folder.
     */
    private File output;

    /**
     * Used as package name prefix for the generated java classes.
     */
    private String avroNamespacePrefix;

    /**
     * @param args translator options. Provides help if no arguments passed.
     */
    public static void main(final String[] args) {
        Xsd2AvroTranslatorMain main = new Xsd2AvroTranslatorMain();
        main.execute(args);
    }

    /**
     * Process command line options and run translator.
     * <p/>
     * If no options are passed, prints the help. Help is also printed if the
     * command line options are invalid.
     * 
     * @param args translator options
     */
    public void execute(final String[] args) {
        try {
            Options options = createOptions();
            if (collectOptions(options, args)) {
                setDefaults();
                generate();
            }
        } catch (Exception e) {
            log.error("COBOL to Avro translation failure", e);
            throw new RuntimeException(e);
        }
    }
    
    private void generate() {
        try {
            FileUtils.forceMkdir(output);

            Xsd2AvroTranslator translator = new Xsd2AvroTranslator();
            Map<String, String> mapSchemas = translator.translate(xsdInput,
                    avroNamespacePrefix);
            for (Entry<String, String> entry : mapSchemas.entrySet()) {

                // Write shema file
                FileUtils.writeStringToFile(new File(output, entry.getKey()
                        + ".avsc"), entry.getValue());
            }

        } catch (IOException e) {
            log.error("Generation failed for input " + xsdInput.getName(), e);
        }

    }

    /**
     * @return the command line options
     */
    private Options createOptions() {
        Options options = new Options();

        Option version = new Option("v", OPTION_VERSION, false,
                "print the version information and exit");
        options.addOption(version);

        Option help = new Option("h", OPTION_HELP, false,
                "print the options available");
        options.addOption(help);

        Option input = new Option("i", OPTION_INPUT, true,
                "file or folder holding the COBOL annotated XML schemas to translate."
                        + " Name is relative or absolute");
        options.addOption(input);

        Option output = new Option("o", OPTION_OUTPUT, true,
                "folder receiving the generated artifacts");
        options.addOption(output);

        Option namespacePrefix = new Option("np", OPTION_AVRO_NAMESPACE_PREFIX, true,
                "avro namespace prefix to use for generated java packages");
        options.addOption(namespacePrefix);

        return options;
    }

    /**
     * Take arguments received on the command line and setup corresponding
     * options.
     * <p/>
     * No arguments is valid. It means use the defaults.
     * 
     * @param options the expected options
     * @param args the actual arguments received on the command line
     * @return true if arguments were valid
     * @throws Exception if something goes wrong while parsing arguments
     */
    private boolean collectOptions(final Options options, final String[] args)
            throws Exception {
        if (args != null && args.length > 0) {
            CommandLineParser parser = new PosixParser();
            CommandLine line = parser.parse(options, args);
            return processLine(line, options);
        }
        return true;
    }

    /**
     * Process the command line options selected.
     * 
     * @param line the parsed command line
     * @param options available
     * @return false if processing needs to stop, true if its ok to continue
     * @throws Exception if line cannot be processed
     */
    private boolean processLine(final CommandLine line, final Options options)
            throws Exception {
        if (line.hasOption(OPTION_VERSION)) {
            log.info(getVersion(true));
            return false;
        }
        if (line.hasOption(OPTION_HELP)) {
            produceHelp(options);
            return false;
        }
        if (line.hasOption(OPTION_INPUT)) {
            setXsdInput(line.getOptionValue(OPTION_INPUT).trim());
        }
        if (line.hasOption(OPTION_OUTPUT)) {
            setOutput(line.getOptionValue(OPTION_OUTPUT).trim());
        }
        if (line.hasOption(OPTION_AVRO_NAMESPACE_PREFIX)) {
            setAvroNamespacePrefix(line.getOptionValue(OPTION_AVRO_NAMESPACE_PREFIX).trim());
        }

        return true;
    }

    /**
     * Retrieve the current version.
     * 
     * @parm verbose when true will also return the build date
     * @return the version number and build date
     */
    private String getVersion(boolean verbose) {
        try {
            InputStream stream = getClass().getResourceAsStream("/version.properties");
            Properties props = new Properties();
            props.load(stream);
            if (verbose) {
                return String.format("Version=%s, build date=%s",
                        props.getProperty("version"),
                        props.getProperty("buildDate"));
            } else {
                return props.getProperty("version");
            }
        } catch (IOException e) {
            log.error("Unable to retrieve version", e);
            return "unknown";
        }
    }

    /**
     * @param options options available
     * @throws Exception if help cannot be produced
     */
    private void produceHelp(final Options options) throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        String version = getVersion(false);
        formatter.printHelp("java -jar com.legstar.avro.translator-" + version
                + "-exe.jar followed by:", options);
    }

    /**
     * Make sure mandatory parameters have default values.
     */
    private void setDefaults() {
        if (xsdInput == null) {
            setXsdInput(DEFAULT_INPUT_FOLDER_PATH);
        }
        if (output == null) {
            setOutput(DEFAULT_OUTPUT_FOLDER_PATH);
        }
        if (avroNamespacePrefix == null) {
            setAvroNamespacePrefix(DEFAULT_AVRO_NAMESPACE_PREFIX);
        }

    }

    /**
     * Check the input parameter and keep it only if it is valid.
     * 
     * @param xsdInputPath a file or folder name (relative or absolute)
     */
    public void setXsdInput(final String xsdInputPath) {
        if (xsdInputPath == null) {
            throw (new IllegalArgumentException(
                    "You must provide a source folder or file"));
        }
        File xsdInput = new File(xsdInputPath);
        if (xsdInput.exists()) {
            if (xsdInput.isDirectory() && xsdInput.list().length == 0) {
                throw new IllegalArgumentException("Folder '" + xsdInputPath
                        + "' is empty");
            }
        } else {
            throw new IllegalArgumentException("Input file or folder '"
                    + xsdInputPath + "' not found");
        }
        this.xsdInput = xsdInput;
    }

    /**
     * Check the output parameter and keep it only if it is valid.
     * 
     * @param output a file or folder name (relative or absolute)
     */
    public void setOutput(final String output) {
        if (output == null) {
            throw (new IllegalArgumentException(
                    "You must provide a target folder or file"));
        }
        this.output = new File(output);
    }

    /**
     * @param avroNamespacePrefix the package name prefix for the generated avro classes to set
     */
    public void setAvroNamespacePrefix(final String avroNamespacePrefix) {
        this.avroNamespacePrefix = avroNamespacePrefix;
    }
}
