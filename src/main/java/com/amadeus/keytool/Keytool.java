package com.amadeus.keytool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This keytool allows to manage a keystore.
 
 * @author hiran.chaudhuri
 */
public class Keytool {
    private static final Logger log = LogManager.getLogger(Keytool.class);
    
    /**
     * Creates a new Keytool to manage keystores.
     * Prints the version number into the logfile.
     */
    public Keytool() {
        String msg = Keytool.class.getPackage().getImplementationTitle() + " " + getClass().getPackage().getImplementationVersion();
        log.info(msg);
    }

    /**
     * Creates an empty keystore.
     * 
     * @param keystore the file to save the keystore into
     * @param pwdArray the keystore password
     * @throws KeyStoreException something went wrong
     * @throws java.io.IOException something went wrong
     * @throws CertificateException something went wrong
     * @throws NoSuchAlgorithmException  something went wrong
     */
    public void createKeystore(File keystore, char[]pwdArray ) throws KeyStoreException, java.io.IOException, CertificateException, NoSuchAlgorithmException {
        log.debug("createKeystore(...)");
        if (keystore == null) {
            throw new IllegalArgumentException("keystore must not be null");
        }
        
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, pwdArray);
        try (FileOutputStream fos = new FileOutputStream(keystore)) {
            ks.store(fos, pwdArray);
        }

    }
    
    /**
     * Imports an existing certificate from a file into the keystore.
     * 
     * @param keystore the file to save the keystore into
     * @param pwdArray the keystore password
     * @param alias the alias under which to store the 
     * @param certificateFile the certificate to import
     * @throws KeyStoreException something went wrong
     * @throws java.io.IOException something went wrong
     * @throws CertificateException something went wrong
     * @throws NoSuchAlgorithmException  something went wrong
     */
    public void importCertificate(File keystore , char[]pwdArray, String alias, File certificateFile) throws KeyStoreException, java.io.IOException, CertificateException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("keystore must not be null");
        }
        if (certificateFile == null) {
            throw new IllegalArgumentException("certificate must not be null");
        }
        if (alias == null) {
            throw new IllegalArgumentException("alias must not be null");
        }

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(keystore)) {
            ks.load(fis, pwdArray);
        }

        try (FileInputStream fis = new FileInputStream(certificateFile);
             BufferedInputStream bis = new BufferedInputStream(fis);) {
            int count = 0;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                count++;
                //System.out.println(cert.toString());
                if (count <=1) {
                    ks.setCertificateEntry(alias, cert);
                } else {
                    ks.setCertificateEntry(String.format("%s[%d]", alias, count), cert);
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(keystore)) {
            ks.store(fos, pwdArray);
        }
    }
    
    /**
     * Lists the certificates in a given keystore.
     * 
     * @param keystore the file to read the keystore from
     * @param pwdArray the keystore password
     * @param verbose if true, the full certificate will be printed - otherwise it's the alias and a hash.
     * @throws KeyStoreException something went wrong
     * @throws java.io.IOException something went wrong
     * @throws CertificateException something went wrong
     * @throws NoSuchAlgorithmException  something went wrong
     */
    public void listCertificate(File keystore, char[]pwdArray, boolean verbose) throws KeyStoreException, java.io.IOException, CertificateException, NoSuchAlgorithmException {
        log.debug("listCertificate(...)");
        if (keystore == null) {
            throw new IllegalArgumentException("keystore must not be null");
        }
        
        KeyStore mykeystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(keystore)) {
            mykeystore.load(fis, pwdArray);

            int count = 0;
            Enumeration<String> enumeration = mykeystore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                Certificate certificate = mykeystore.getCertificate(alias);
                
                if (verbose) {
                    System.out.println("alias name: " + alias);
                    System.out.println(certificate.toString());
                } else {
                    System.out.println(String.format("  alias: %30s   hash: %h", alias, certificate.hashCode()));
                }
                count++;
            }
            
            System.out.println(String.format("Found %d certificates", count));
        }

    }
    
}
