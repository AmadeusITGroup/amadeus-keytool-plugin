/*
 */
package com.amadeus.maven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * Add plugin testing.
 * As documented in https://maven.apache.org/plugin-developers/plugin-testing.html
 * but always leads to
 * <pre>org.apache.maven.plugin.testing.ConfigurationException: Cannot find a configuration element for a plugin with an artifactId of amadeus-keytool-plugin.</pre>
 * 
 * @author hiran.chaudhuri
 */
public class MojoTest extends AbstractMojoTestCase {
    private static final Logger log = LogManager.getLogger();

//    @BeforeEach
//    @Override
//    protected void setUp() throws Exception {
//        log.info("setUp()");
//        super.setUp(); // required for mojo lookups to work
//    }
    
//    @Test
//    public void testImportMany() throws Exception {
//        log.info("testImportMany()");
//        
//        File testPom = new File(getBasedir(), "src/test/data/testpom.xml");
//        
//        KeytoolMavenPlugin mojo = (KeytoolMavenPlugin)lookupMojo("keytool", testPom);
//        assertNotNull(mojo);
//    }
}
