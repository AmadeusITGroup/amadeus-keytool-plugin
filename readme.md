# Amadeus Keytool Plugin

## Why another keytool plugin?

The Amadeus Keytool Plugin allows to build java keystores (and truststores) within
the Maven build. This per se does not make it unique. But if you need to add 
multiple certificates, Amadeus Keytool Plugin is still with you.

In the beginning we created a simple bean that could handle essential
operations on the keystore. This would be handled by a CLI. The command lines
to run on different operating systems was too system specific, so we added an
Ant task. This allowed us to run the plugin not only from CLI but also from
Ant based build scripts and even from Maven using the Maven AntRun Plugin.

But some IDEs still had problems with that, and we finally added a native
Maven interface. With that now you have a choice how to run this jar
and thus can integrate it into any build.

## Usage in Maven

To make use of the plugin, just add some lines to your project's pom.xml.
Below you see a huge example with many single executions. Very likely
you will not need them all.

### Create a keystore

    <?xml version="1.0"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0">
        <modelVersion>4.0.0</modelVersion>

        <groupId>com.amadeus.ops</groupId>
        <artifactId>testproject</artifactId>
        <version>1.0-SNAPSHOT</version>

        <build>
            <plugins>
                <plugin>
                    <groupId>com.amadeus</groupId>
                    <artifactId>amadeus-keytool-plugin</artifactId>
                    <version>0.1.0</version>
                    <executions>
                        <execution>
                            <id>keytool-list</id>
                            <phase>compile</phase>
                            <goals>
                                <goal>keytool</goal>
                            </goals>
                            <configuration>
                                <action>create</action>
                                <keystore>target/truststore.jks</keystore>
                                <password>changeme</password>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>

### List content of a keystore

    <?xml version="1.0"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0">
        <modelVersion>4.0.0</modelVersion>

        <groupId>com.amadeus.ops</groupId>
        <artifactId>testproject</artifactId>
        <version>1.0-SNAPSHOT</version>

        <build>
            <plugins>
                <plugin>
                    <groupId>com.amadeus</groupId>
                    <artifactId>amadeus-keytool-plugin</artifactId>
                    <version>0.1.0</version>
                    <executions>
                        <execution>
                            <id>keytool-list0</id>
                            <phase>compile</phase>
                            <goals>
                                <goal>keytool</goal>
                            </goals>
                            <configuration>
                                <action>list</action>
                                <keystore>target/truststore.jks</keystore>
                                <password>${keytool.password}</password>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>

### Import one certificate into a keystore

    <?xml version="1.0"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0">
        <modelVersion>4.0.0</modelVersion>

        <groupId>com.amadeus.ops</groupId>
        <artifactId>testproject</artifactId>
        <version>1.0-SNAPSHOT</version>

        <build>
            <plugins>
                <plugin>
                    <groupId>com.amadeus</groupId>
                    <artifactId>amadeus-keytool-plugin</artifactId>
                    <version>0.1.0</version>
                    <executions>
                        <execution>
                            <id>keytool-list0</id>
                            <phase>compile</phase>
                            <goals>
                                <goal>keytool</goal>
                            </goals>
                            <configuration>
                                <action>import</action>
                                <keystore>target/truststore.jks</keystore>
                                <password>${keytool.password}</password>
                                <certificateFile>src/main/truststore/amacatech3.crt</certificateFile>
                                <certificateAlias>strange.alias</certificateAlias>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>

### Import many certificates into a keystore

    <?xml version="1.0"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0">
        <modelVersion>4.0.0</modelVersion>

        <groupId>com.amadeus.ops</groupId>
        <artifactId>testproject</artifactId>
        <version>1.0-SNAPSHOT</version>

        <build>
            <plugins>
                <plugin>
                    <groupId>com.amadeus</groupId>
                    <artifactId>amadeus-keytool-plugin</artifactId>
                    <version>0.1.0</version>
                    <executions>
                        <execution>
                            <id>keytool-list0</id>
                            <phase>compile</phase>
                            <goals>
                                <goal>keytool</goal>
                            </goals>
                            <configuration>
                                <action>import</action>
                                <keystore>target/truststore.jks</keystore>
                                <password>${keytool.password}</password>
                                <filesets>
                                    <fileset>
                                        <directory>src/main/truststore</directory>
                                        <includes>
                                            <include>*.crt</include>
                                        </includes>
                                    </fileset>
                                </filesets>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>

## Usage in Ant

## Usage via CLI

To run amadeus-keytool-plugin on the command line, run this command:

java -jar amadeus-keytool-plugin.jar [options]

Valid options are:

-gk, --generateKeystore              generate a new keystore
-ic, --importCertificate             import one certificate. Requires the certificate option.
-ks, --keystore <path>               path to the keystore
-kp, --keystorePassword <password>   password to be used for keystore
-crt, --certificate <path>           path to certificate
-a, --alias <name>                   Alias for the certificate in the keystore

## Build & Deployment

The project itself is a Maven build. Thus you can run Maven to build and run
unit tests.

We intend to make the built plugins available on the standard Maven repositories
so most users will not have to think about using the plugin. But until we are there,
the plugin will have to be installed manually.

For this download one of the release jar files: amadeus-keytool-plugin-&lt;version&gt;.jar
Then tell Maven to install it into it's local repository.

    mvn install:install-file -Dfile=amadeus-keytool-plugin-<version>.jar

From now on the above lines should execute without problem.