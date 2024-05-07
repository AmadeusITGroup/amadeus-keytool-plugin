/*
 */
package com.amadeus.cli;

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
public class KeytoolCliTest {
    private static final Logger log = LogManager.getLogger();
    
    public KeytoolCliTest() {
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
     * Test of main method, of class KeytoolCli.
     */
    @Test
    public void testMain() {
        log.info("main");
        // TODO: test main although it calls system.exit
    }
    
}
