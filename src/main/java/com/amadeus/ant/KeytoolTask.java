package com.amadeus.ant;

import com.amadeus.keytool.Keytool;
import java.io.File;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * Allows keytool functions to be invoked from Ant.
 * 
 * <p>
 * This is for creating a keystore:
 * <pre>
 *   &lt;target&gt;
 *     &lt;keytool action="create" keystore="file.jks"/&gt;
 *   &lt;/target&gt;
 * </pre>
 * </p><p>
 * This is for importing a single certificate.
 * <pre>
 *   &lt;target&gt;
 *     &lt;keytool action="import" keystore="file.jks" certificate="file.crt"/&gt;
 *   &lt;/target&gt;
 * </pre>
 * </p><p>
 * This is for importing a batch (directory of certificates):
 * <pre>
 *   &lt;target&gt;
 *     &lt;keytool action="import" keystore="file.jks"&gt;
 *         &lt;fileset dir="../../src/test/data/truststore"&gt;
 *             &lt;include name="*.crt"/&gt;
 *         &lt;/fileset&gt;
 *     &lt;/keytool&gt;
 *   &lt;/target&gt;
 * </pre>
 * </p>
 *
 * @author hiran.chaudhuri
 */
public class KeytoolTask extends Task {

    /** The action to execute. */
    private String action;
    
    /** The keystore to process. */
    private File keystore;
    
    /** The certificates to process. */
    private ArrayList<FileSet> filesets = new ArrayList<>();

    /** The password to the keystore. */
    private String password ;

    /** The alias to use. */
    private String alias ;
    
    /** The certifate to process (if no fileset). */
    private File certificateFile;
    
    /** Flag to indicate amount of logging. */
    private boolean verbose;
    
    /** 
     * Returns the chosen action.
     * 
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /** 
     * Sets the action.
     * 
     * @param action the action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /** 
     * Returns the keystore.
     * 
     * @return the keystore file
     */
    public File getKeystore() {
        return keystore;
    }

    /** 
     * Sets the keystore.
     * 
     * @param keystore the keystore file
     */
    public void setKeystore(File keystore) {
        this.keystore = keystore;
    }

    /** 
     * Returns the keystore password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /** 
     * Sets the keystore password.
     * 
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the alias.
     * 
     * @return the alias 
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias.
     * 
     * @param alias the alias 
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Returns the certificate file.
     * 
     * @return the certificate file 
     */
    public File getCertificateFile() {
        return certificateFile;
    }

    /**
     * Sets the certificate file.
     * 
     * @param certificateFile the certificate file 
     */
    public void setCertificateFile(File certificateFile) {
        this.certificateFile = certificateFile;
    }

    /**
     * Adds a fileset of certificates.
     * 
     * @param certificates the fileset to add 
     */
    public void addConfiguredFileSet(FileSet certificates) {
        filesets.add(certificates);
    }

    /**
     * Returns the verbose flag.
     * 
     * @return the flag 
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the verbose flag.
     * 
     * @param verbose the flag 
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /**
     * just dump the files we got.
     * Inspired by https://ant.apache.org/manual/tutorial-tasks-filesets-properties.html
     */
    private void listFiles() {
        for (FileSet fs: filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            for (String includedFile: ds.getIncludedFiles()) {
                
                File cert = new File(ds.getBasedir(), includedFile);
                log("have " + cert);
                
                // now import the cert file with alias cert.getName() into the keystore
                cert.getName();
            }
        }
    }

    /**
     * TODO: this code is extremely similar to the one in KeytoolMavenPlugin.
     */
    @Override
    public void execute() throws BuildException {
        try {
            if (action == null) {
                throw new IllegalArgumentException("Action must be set.");
            }
            if (password == null) {
                throw new IllegalArgumentException("password must be set.");
            }
            if ("create".equals(action)) {
                if (certificateFile != null || !filesets.isEmpty()) {
                    throw new  IllegalArgumentException("Must not have a fileset or a certificate");
                }
                
                if (keystore.exists()) {
                    log("Keystore file already exists");
                } else {
                    if (!keystore.getParentFile().exists()) {
                        keystore.getParentFile().mkdirs();
                    }
                    new Keytool().createKeystore(keystore, password.toCharArray());
                    log("created keystore" + keystore);
                }
            } else if ("import".equals(action) ) {
                if (certificateFile == null && filesets.isEmpty()) {
                    throw new  IllegalArgumentException("you must have either a fileset or a certificate");
                }
                if (certificateFile != null && !filesets.isEmpty()) {
                    throw new  IllegalArgumentException("you must have either a fileset or a certificate");
                }
                if (filesets.isEmpty()) {
                    new Keytool().importCertificate(keystore, password.toCharArray(), alias, certificateFile);
                    log("Imported 1 certificate");
                }else {
                    importAll();
                }
            }else if ("list".equals(action)) {
                if (certificateFile != null || !filesets.isEmpty()) {
                    throw new  IllegalArgumentException("Must not have a fileset or a certificate");
                }

                new Keytool().listCertificate(keystore,password.toCharArray(), verbose);
            }else {
                throw new IllegalArgumentException("unknown action " + action + " Allowed is [create, import, list]");
            }
        } catch (Exception e) {
            throw new BuildException("Could not execute '" + action + "'.", e);
        }
    }

    private void importAll()throws KeyStoreException, java.io.IOException, CertificateException, NoSuchAlgorithmException {
        int count = 0;
        for (FileSet fs : filesets) {
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            for (String includedFile : ds.getIncludedFiles()) {

                File cert = new File(ds.getBasedir(), includedFile);
                // now import the cert file with alias cert.getName() into the keystore
                new Keytool().importCertificate(keystore, password.toCharArray(), includedFile, cert);
                count++;
            }
        }
        log(String.format("Imported %d certificates", count));
    }
}
