* Why?

The Amadeus Keytool Plugin allows to build java keystores (and truststores) within
the Maven build. This per se does not make it unique. But if you need to add 
multiple certificates, Amadeus Keytool Plugin is still with you.

* Usage

To make use of the plugin, just add some lines to your projct's pom.xml.
Below you see a huge example with many single executions. Very likely
you will not need them all.

	  <plugins>
		<plugin>
		  <groupId>com.amadeus</groupId>
		  <artifactId>keytool-maven-plugin</artifactId>
		  <version>0.0.1-SNAPSHOT</version>
		  <executions>
			  <execution>
				  <id>keytool-create</id>
				  <phase>compile</phase>
				  <goals>
					  <goal>keytool</goal>
				  </goals>
				  <configuration>
					  <action>create</action>
					  <keystore>target/truststore.jks</keystore>
					  <password>${keytool.password}</password>
				  </configuration>
			  </execution>
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
			  <execution>
				  <id>keytool-import-1</id>
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
			  <execution>
				  <id>keytool-list-1</id>
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
			  <execution>
				  <id>keytool-import-n</id>
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
			  <execution>
				  <id>keytool-list-n</id>
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


* Build & Deployment

The project itself is a Maven build. Thus you can run Maven to build and run
unit tests.

We intend to make the built plugins available on the standard Maven repositories
so most users will not have to think about using the plugin.
