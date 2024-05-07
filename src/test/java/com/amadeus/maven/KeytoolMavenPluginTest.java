/*
 */
package com.amadeus.maven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    /**
     * Test of execute method, of class KeytoolMavenPlugin.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        KeytoolMavenPlugin instance = new KeytoolMavenPlugin();
        
        try {
            instance.execute();
            fail("expected exception");
        } catch (BuildException e) {
            assertEquals("Could not execute 'null'.", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
}
