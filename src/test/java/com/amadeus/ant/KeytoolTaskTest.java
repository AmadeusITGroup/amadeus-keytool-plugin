/*
 */
package com.amadeus.ant;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
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
public class KeytoolTaskTest {
    private static final Logger log = LogManager.getLogger();
    
    public KeytoolTaskTest() {
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
     * Test of setAction method, of class KeytoolTask.
     */
    @Test
    public void testSetGetAction() {
        log.info("setGetAction");
        KeytoolTask instance = new KeytoolTask();

        assertNull(instance.getAction());
        instance.setAction("action");
        assertEquals("action", instance.getAction());        
    }

    /**
     * Test of setKeystore method, of class KeytoolTask.
     */
    @Test
    public void testSetGetKeystore() {
        log.info("setGetKeystore");
        File keystore = new File(".");
        KeytoolTask instance = new KeytoolTask();
        
        assertNull(instance.getKeystore());
        instance.setKeystore(keystore);
        assertEquals(keystore, instance.getKeystore());
    }

    /**
     * Test of getPassword method, of class KeytoolTask.
     */
    @Test
    public void testGetPassword() {
        log.info("getPassword");
        KeytoolTask instance = new KeytoolTask();
        String expResult = "";
        
        assertNull(instance.getPassword());
        instance.setPassword(expResult);
        assertEquals(expResult, instance.getPassword());
    }

    /**
     * Test of setPassword method, of class KeytoolTask.
     */
    @Test
    public void testSetPassword() {
        log.info("setPassword");
        String password = "";
        KeytoolTask instance = new KeytoolTask();
        
        assertNull(instance.getPassword());
        instance.setPassword(password);
        assertEquals(password, instance.getPassword());
    }

    /**
     * Test of getAlias method, of class KeytoolTask.
     */
    @Test
    public void testSetGetAlias() {
        log.info("setGetAlias");
        KeytoolTask instance = new KeytoolTask();
        String expResult = "alias";
        String result = instance.getAlias();
        
        assertNull(instance.getAlias());
        instance.setAlias(expResult);
        assertEquals(expResult, instance.getAlias());
    }

    /**
     * Test of setCertificateFile method, of class KeytoolTask.
     */
    @Test
    public void testSetGetCertificateFile() {
        log.info("setGetCertificateFile");
        File certificateFile = new File(".");
        KeytoolTask instance = new KeytoolTask();
        
        assertNull(instance.getCertificateFile());
        instance.setCertificateFile(certificateFile);
        assertEquals(certificateFile, instance.getCertificateFile());
    }

    /**
     * Test of addConfiguredFileSet method, of class KeytoolTask.
     */
    @Test
    public void testAddConfiguredFileSet() {
        log.info("addConfiguredFileSet");

        // todo: field is readonly
    }

    /**
     * Test of setVerbose method, of class KeytoolTask.
     */
    @Test
    public void testSetIsVerbose() {
        log.info("setIsVerbose");
        boolean verbose = true;
        KeytoolTask instance = new KeytoolTask();
        
        assertFalse(instance.isVerbose());
        instance.setVerbose(verbose);
        assertTrue(instance.isVerbose());
    }

    /**
     * Test of execute method, of class KeytoolTask.
     */
    @Test
    public void testExecute() {
        log.info("execute");
        KeytoolTask instance = new KeytoolTask();
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("Could not execute 'null'.", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
}
