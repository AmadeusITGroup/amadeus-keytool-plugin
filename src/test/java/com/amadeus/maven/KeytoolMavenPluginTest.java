/*
 */
package com.amadeus.maven;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.tools.ant.BuildException;
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
public class KeytoolMavenPluginTest {
    private static final Logger log = LogManager.getLogger();
    
    public KeytoolMavenPluginTest() {
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
    
    @Test
    public void testSetGetAction() {
        log.info("testSetGetAction");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        assertNull(instance.getAction());
        instance.setAction("blubb");
        assertEquals("blubb", instance.getAction());
    }
    
    @Test
    public void testSetGetKeystore() {
        log.info("testSetGetKeystore");
        File f = new File("blubb");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        assertNull(instance.getKeystore());
        instance.setKeystore(f);
        assertEquals(f, instance.getKeystore());
    }
    
    @Test
    public void testSetGetCertificateFile() {
        log.info("testSetGetCertificateFile");
        File f = new File("blubb");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        assertNull(instance.getCertificateFile());
        instance.setCertificateFile(f);
        assertEquals(f, instance.getCertificateFile());
    }
    
    @Test
    public void testSetGetCertificateFilesets() {
        log.info("testSetGetCertificateFilesets");

        FileSet filesets[] = new FileSet[0];
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        assertNull(instance.getFilesets());
        assertFalse(instance.haveFileset());
        instance.setFilesets(filesets);
        assertEquals(filesets, instance.getFilesets());
        assertFalse(instance.haveFileset());
        
        filesets = new FileSet[1];
        instance.setFilesets(filesets);
        assertEquals(filesets, instance.getFilesets());
        assertTrue(instance.haveFileset());
    }
    
    @Test
    public void testSetGetAlias() {
        log.info("testSetGetAlias");

        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        assertNull(instance.getCertificateAlias());
        instance.setCertificateAlias("alias");
        assertEquals("alias", instance.getCertificateAlias());
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute() throws Exception {
        log.info("execute");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: action must be set", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute2() throws Exception {
        log.info("execute2");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("blah");
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: keystore must be set", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute3() throws Exception {
        log.info("execute3");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("blah");
        instance.setKeystore(new File("blah"));
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: password must be set", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute4() throws Exception {
        log.info("execute4");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("blah");
        instance.setKeystore(new File("blah"));
        instance.setPassword("password");
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: unknown action blah Allowed is [create, import, list]", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute51() throws Exception {
        log.info("execute51");
        File keystore = new File("target/test/keystore.jks");
        keystore.delete(); // delete just in case

        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");

        assertFalse(keystore.exists());
        instance.execute();
        assertTrue(keystore.exists());
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute52() throws Exception {
        log.info("execute52");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(new File("blah"));
        instance.setPassword("blah");
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: you must have either a fileset or a certificate", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute521() throws Exception {
        log.info("execute521");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(new File("blah"));
        instance.setPassword("blah");
        instance.setCertificateFile(new File("blah"));
        instance.setFilesets(new FileSet[]{new FileSet()});
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: you must have either a fileset or a certificate", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute522() throws Exception {
        log.info("execute522");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(new File("blah"));
        instance.setPassword("blah");
        instance.setCertificateFile(new File("blah"));
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: certificateAlias is not set", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testExecute523() throws Exception {
        log.info("execute523");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(new File("blah"));
        instance.setPassword("blah");
        instance.setCertificateFile(new File("blah"));
        instance.setCertificateAlias("alias");
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertTrue(e.getMessage().contains("java.io.FileNotFoundException"));
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testExecute524() throws Exception {
        log.info("execute524");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(new File("blah"));
        instance.setPassword("blah");
        instance.setFilesets(new FileSet[]{new FileSet()});
        
        instance.execute();
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute53() throws Exception {
        log.info("execute53");
        File keystore = new File("target/test/keystore.jks");
        keystore.delete(); // just in case
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("list");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertTrue(e.getMessage().contains("java.io.FileNotFoundException"));
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute531() throws Exception {
        log.info("execute531");
        File keystore = new File("target/test/keystore.jks");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.execute(); // create the keystore
        
        instance.setAction("list");
        instance.execute();
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute532() throws Exception {
        log.info("execute532");
        File keystore = new File("target/test/keystore.jks");
        File certfile = new File("doesnotexist.crt");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setCertificateFile(certfile);
        
        instance.setAction("list");
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: Must not have a fileset or a certificate", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute533() throws Exception {
        log.info("execute533");
        File keystore = new File("target/test/keystore.jks");
        File certfile = new File("doesnotexist.crt");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setCertificateFile(certfile);
        instance.setFilesets(new FileSet[1]);
        
        instance.setAction("list");
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: Must not have a fileset or a certificate", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute534() throws Exception {
        log.info("execute534");
        File keystore = new File("target/test/keystore.jks");
        File certfile = new File("doesnotexist.crt");
        FileSet fs = new FileSet();
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setFilesets(new FileSet[]{fs});
        
        instance.setAction("list");
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("java.lang.IllegalArgumentException: Must not have a fileset or a certificate", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute535() throws Exception {
        log.info("execute535");
        File keystore = new File("target/test/keystore535.jks");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.execute(); // create the keystore
        
        instance.execute(); // create the keystore again - well, we are idempotent
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute6() throws Exception {
        log.info("execute6");
        File keystore = new File("target/test/keystore6.jks");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.execute(); // create the keystore
        
        instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setCertificateFile(new File("src/test/data/certificates/USERTrust RSA Certification Authority.crt"));
        instance.setCertificateAlias("USERTrust");
        instance.execute();
        
        instance = new KeytoolMavenPlugin();
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setAction("list");
        instance.execute();
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute7() throws Exception {
        log.info("execute7");
        File keystore = new File("target/test/keystore7.jks");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.execute(); // create the keystore
        // after create keystore
        assertEquals(0, instance.getEntriesTouched());
        
        FileSet fileset = new FileSet();
        fileset.setDirectory("src/test/data/certificates");
        fileset.addInclude("**.crt");
        
        instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setFilesets(new FileSet[]{fileset});
        instance.execute();
        // after import
        assertEquals(2, instance.getEntriesTouched());
        
        instance = new KeytoolMavenPlugin();
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setAction("list");
        instance.execute();
        // after list
        assertEquals(0, instance.getEntriesTouched());
    }
    
    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute8() throws Exception {
        log.info("execute8");
        File keystore = new File("target/test/keystore8.jks");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        instance.setAction("create");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.execute(); // create the keystore
        // after create keystore
        assertEquals(0, instance.getEntriesTouched());
        
        FileSet fileset = new FileSet();
        fileset.setDirectory("src/test/data/othercerts");
        fileset.addInclude("**.crt");
        
        instance = new KeytoolMavenPlugin();
        instance.setAction("import");
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setFileset(fileset);
        instance.execute();
        // after import
        assertEquals(1, instance.getEntriesTouched());
        
        instance = new KeytoolMavenPlugin();
        instance.setKeystore(keystore);
        instance.setPassword("blah");
        instance.setAction("list");
        instance.execute();
        // after list
        assertEquals(0, instance.getEntriesTouched());
    }
}
