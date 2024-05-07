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
	
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("  action=" + action);
        getLog().debug("  keystore=" + keystore);
        getLog().debug("  password= **********");
        getLog().debug("  certificateFile=" + certificateFile);
        getLog().debug("  filesets=" + filesets);
		
        try {
            if (action == null) {
                throw new IllegalArgumentException("Action must be set.");
            }
            if (password == null) {
                throw new IllegalArgumentException("password must be set.");
            }
            if ("create".equals(action)) {
                if (certificateFile != null || haveFileset()) {
                    throw new  IllegalArgumentException("Must not have a fileset or a certificate");
                }
	            
                if (keystore.exists()) {
                    getLog().info("Keystore file already exists");
                } else {
                    if (!keystore.getParentFile().exists()) {
                        keystore.getParentFile().mkdirs();
                    }
                    new Keytool().createKeystore(keystore, password.toCharArray());
                    getLog().info("created keystore" + keystore);
                }
            } else if ("import".equals(action) ) {
                if (certificateFile == null && !haveFileset()) {
                    throw new  IllegalArgumentException("you must have either a fileset or a certificate");
                }
                if (certificateFile != null && haveFileset()) {
                    throw new  IllegalArgumentException("you must have either a fileset or a certificate");
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
                    throw new  IllegalArgumentException("Must not have a fileset or a certificate");
                }

                new Keytool().listCertificate(keystore, password.toCharArray(), verbose);
            }else {
                throw new IllegalArgumentException("unknown action " + action + " Allowed is [create, import, list]");
            }
        } catch (Exception e) {
            throw new BuildException("Could not execute '" + action + "'.", e);
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
        int count = 0;
  
        FileSetManager fileSetManager = new FileSetManager();

        for (FileSet fileset: filesets) {
            String[] files = fileSetManager.getIncludedFiles(fileset);
            for (String includedFile: files) {
                File cert = new File(fileset.getDirectory() + "/" + includedFile);
                getLog().info("importing " + cert);

                // now import the cert file with alias cert.getName() into the keystore
                new Keytool().importCertificate(keystore, password.toCharArray(), includedFile, cert);
                count++;
            }
        }
        getLog().info(String.format("Imported %d certificates", count));
    }
    
    private boolean haveFileset() {
    	getLog().debug("filesets=" + filesets);
    	
    	if (filesets == null) {
            return false;
    	}
    	
    	return filesets.length > 0;
    }
}
