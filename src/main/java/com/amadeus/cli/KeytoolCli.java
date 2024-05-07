package com.amadeus.cli;

import com.amadeus.keytool.Keytool;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Allows keytool functions to be invoked from command line.
 *
 * @author hiran.chaudhuri
 */
public class KeytoolCli {
    private static final Logger log = LogManager.getLogger(KeytoolCli.class);

    /**
     * This is the main entry point.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("gk", "generateKeystore", false, "generate the keystore");
        options.addOption("ic", "importCertificate", false, "Import the given certificate into the keystore");
        
        options.addOption("ks", "keystore", true, "path to the keystore");
        options.addOption("kp", "keystorePassword", true, "Password to be used on the keystore");
        options.addOption("crt", "certificate", true, "path to the certificate");
        options.addOption("a", "alias", true, "Alias for the certificate in the keystore");

        Keytool keytool = new Keytool();
        
        try {
            // evaluate command line
            CommandLineParser cliParser = new DefaultParser();
            CommandLine cmd = cliParser.parse(options, args);

            String keystore = cmd.getOptionValue("keystore");
            String keystorePassword = cmd.getOptionValue("keystorePassword");
            if (cmd.hasOption("generateKeystore")){
                keytool.createKeystore(new File(keystore), keystorePassword.toCharArray());
            }

            String certificate = cmd.getOptionValue("certificate");
            String alias = cmd.getOptionValue("alias");
            if (cmd.hasOption("importCertificate")) {
                keytool.importCertificate(new File(keystore), keystorePassword.toCharArray(), alias, new File(certificate));
            } else {
                throw new Exception("Certificate not specified.");
            }

        } catch (Exception e) {
            log.error("Could not execute", e);

            System.out.println();
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(KeytoolCli.class.getName(), options);
            
            System.exit(2);
        }
    }
}
