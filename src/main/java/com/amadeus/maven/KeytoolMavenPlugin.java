package com.amadeus.maven;

import com.amadeus.keytool.Keytool;
import java.io.File;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.apache.tools.ant.BuildException;

/**
 * Creates/manages a java keystore.
 * If uses filesets, see
 * https://maven.apache.org/shared/file-management/examples/mojo.html
 */
@Mojo(name = "keytool", defaultPhase = LifecyclePhase.INITIALIZE)
public class KeytoolMavenPlugin extends AbstractMojo {
	
    /**
     * The project reference.
     */
    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    /**
     * The action to execute.
     */
    @Parameter(property = "keytool.action", required = true)
    private String action;

    /**
     * The password to encrypt with.
     */
    @Parameter(property = "keytool.password", required = true)
    private String password;

    /**
     * The keystore to modify.
     */
    @Parameter(property = "keytool.keystore", required = true)
    private File keystore;

    /**
     * The certificate to import.
     */
    @Parameter(property = "keytool.certificateFile")
    private File certificateFile;

    /**
     * The alias to store the certificate as.
     */
    @Parameter(property = "keytool.certificateAlias")
    private String certificateAlias;

    /**
     * The flag whether to be verbose.
     */
    @Parameter(property = "keytool.verbose", defaultValue = "false")
    private boolean verbose;
	
    /**
     * A list of <code>fileSet</code> rules to select certificates.
     */
    @Parameter
    private FileSet[] filesets;

    /**
     * Returns the action to execute. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action to execute. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param action the action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Sets the keystore password. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the keystore. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @return the keystore
     */
    public File getKeystore() {
        return keystore;
    }

    /**
     * Sets the keystore. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param keystore the keystore file
     */
    public void setKeystore(File keystore) {
        this.keystore = keystore;
    }

    /**
     * Returns the certificate file. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @return the certificate file
     */
    public File getCertificateFile() {
        return certificateFile;
    }

    /**
     * Returns the certificate filesets. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @return the filesets
     */
    public FileSet[] getFilesets() {
        return filesets;
    }

    /**
     * Sets the certificate filesets. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param filesets the filesets
     */
    public void setFilesets(FileSet[] filesets) {
        this.filesets = filesets;
    }

    /**
     * Returns the certificate alias. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @return the alias
     */
    public String getCertificateAlias() {
        return certificateAlias;
    }

    /**
     * Sets the certificate alias. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param certificateAlias the alias
     */
    public void setCertificateAlias(String certificateAlias) {
        this.certificateAlias = certificateAlias;
    }

    /**
     * Sets the certificate file. Maven has access based on annotations,
     * this method is introduced for unit testing.
     * 
     * @param certificateFile the certificate file
     */
    public void setCertificateFile(File certificateFile) {
        this.certificateFile = certificateFile;
    }
	
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("  action=" + action);
        getLog().debug("  keystore=" + keystore);
        getLog().debug("  password= **********");
        getLog().debug("  certificateFile=" + certificateFile);
        getLog().debug("  filesets=" + filesets);
		
        try {
            if (action == null) {
                throw new IllegalArgumentException("action must be set");
            }
            if (keystore == null) {
                throw new IllegalArgumentException("keystore must be set");
            }
            if (password == null) {
                throw new IllegalArgumentException("password must be set");
            }
            if ("create".equals(action)) {
                if (certificateFile != null || haveFileset()) {
                    throw new IllegalArgumentException("Must not have a fileset or a certificate");
                }
	            
                if (keystore.exists()) {
                    getLog().info("Keystore file already exists");
                } else {
                    if (keystore.getParentFile() != null && !keystore.getParentFile().exists()) {
                        keystore.getParentFile().mkdirs();
                    }
                    new Keytool().createKeystore(keystore, password.toCharArray());
                    getLog().info("created keystore" + keystore);
                }
            } else if ("import".equals(action) ) {
                if (certificateFile == null && !haveFileset()) {
                    throw new IllegalArgumentException("you must have either a fileset or a certificate");
                }
                if (certificateFile != null && haveFileset()) {
                    throw new IllegalArgumentException("you must have either a fileset or a certificate");
                }
                if (certificateFile != null) {
                    if (certificateAlias == null) {
                            throw new IllegalArgumentException("certificateAlias is not set");
                    }
                    new Keytool().importCertificate(keystore, password.toCharArray(), certificateAlias, certificateFile);
                    getLog().info("Imported 1 certificate");
                } else {
                    importAll();
                }
            } else if ("list".equals(action)) {
                if (certificateFile != null || haveFileset()) {
                    throw new IllegalArgumentException("Must not have a fileset or a certificate");
                }

                new Keytool().listCertificate(keystore, password.toCharArray(), verbose);
            } else {
                throw new IllegalArgumentException("unknown action " + action + " Allowed is [create, import, list]");
            }
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            throw new BuildException(e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Imports all certificates from the fileset.
     * TODO: similar code as in KeytoolTask
     * 
     * @throws KeyStoreException something went wrong
     * @throws java.io.IOException something went wrong
     * @throws CertificateException something went wrong
     * @throws NoSuchAlgorithmException something went wrong
     * 
     */
    private void importAll() throws KeyStoreException, java.io.IOException, CertificateException, NoSuchAlgorithmException {
        if (filesets == null) {
            throw new IllegalStateException("filesets must not be null");
        }
        
        int count = 0;
  
        FileSetManager fileSetManager = new FileSetManager();

        for (FileSet fileset: filesets) {
            if (fileset == null) {
                throw new IllegalStateException("filesets must not contain null entries");
            }
            String[] files = null;
            try {
                files = fileSetManager.getIncludedFiles(fileset);

                for (String includedFile: files) {
                    File cert = new File(fileset.getDirectory() + "/" + includedFile);
                    getLog().info("importing " + cert);

                    // now import the cert file with alias cert.getName() into the keystore
                    new Keytool().importCertificate(keystore, password.toCharArray(), includedFile, cert);
                    count++;
                }
            } catch (NullPointerException e) {
                getLog().warn("FileSet has no included files");
            }
        }
        getLog().info(String.format("Imported %d certificates", count));
    }
    
    boolean haveFileset() {
    	getLog().debug("filesets=" + filesets);
    	
    	if (filesets == null) {
            return false;
    	}
    	
    	return filesets.length > 0;
    }
}
