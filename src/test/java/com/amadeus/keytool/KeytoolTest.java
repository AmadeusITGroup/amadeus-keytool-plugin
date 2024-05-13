/*
 */
package com.amadeus.keytool;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hiran.chaudhuri
 */
public class KeytoolTest {
    private static final Logger log = LogManager.getLogger();
    
    public KeytoolTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createKeystore method, of class Keytool.
     */
    @Test
    public void testCreateKeystore() throws Exception {
        log.info("createKeystore");
        File keystore = null;
        char[] pwdArray = null;
        Keytool instance = new Keytool();

        try {
            instance.createKeystore(keystore, pwdArray);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("keystore must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of importCertificate method, of class Keytool.
     */
    @Test
    public void testImportCertificate() throws Exception {
        log.info("importCertificate");
        
        File keystore = null;
        char[] pwdArray = null;
        String alias = null;
        File certificateFile = null;
        Keytool instance = new Keytool();

        try {
            instance.importCertificate(keystore, pwdArray, alias, certificateFile);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("keystore must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of importCertificate method, of class Keytool.
     */
    @Test
    public void testImportCertificate2() throws Exception {
        log.info("importCertificate2");
        
        File keystore = new File("target/test/keystore.jks");
        char[] pwdArray = null;
        String alias = null;
        File certificateFile = null;
        Keytool instance = new Keytool();

        try {
            instance.importCertificate(keystore, pwdArray, alias, certificateFile);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("certificate must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of importCertificate method, of class Keytool.
     */
    @Test
    public void testImportCertificate3() throws Exception {
        log.info("importCertificate3");
        
        File keystore = new File("target/test/keystore.jks");
        char[] pwdArray = null;
        String alias = null;
        File certificateFile = new File("target/test/certificate.crt");
        Keytool instance = new Keytool();

        try {
            instance.importCertificate(keystore, pwdArray, alias, certificateFile);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("alias must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of importCertificate method, of class Keytool.
     */
    @Test
    public void testImportCertificate4() throws Exception {
        log.info("importCertificate4");
        
        File keystore = new File("target/test/keystore.jks");
        char[] pwdArray = null;
        String alias = "alias";
        File certificateFile = new File("target/test/certificate.crt");
        Keytool instance = new Keytool();

        try {
            instance.importCertificate(keystore, pwdArray, alias, certificateFile);
            fail("Expected exception");
        } catch (FileNotFoundException e) {
            assertTrue(e.getMessage().contains("target" + File.separator + "test" + File.separator + "keystore.jks"));
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of listCertificate method, of class Keytool.
     */
    @Test
    public void testListCertificate() throws Exception {
        log.info("listCertificate");
        
        File keystore = null;
        char[] pwdArray = null;
        boolean verbose = false;
        Keytool instance = new Keytool();
        
        try {
            instance.listCertificate(keystore, pwdArray, verbose);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("keystore must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
}
