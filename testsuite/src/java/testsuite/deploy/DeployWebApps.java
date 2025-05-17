/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2025 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     Hazendaz (Jeremy Landis).
 */
package testsuite.deploy;

import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import testsuite.config.Application;
import testsuite.config.ConfigException;
import testsuite.config.ConfigReader;
import testsuite.config.Server;

/**
 * Class to deploy application to all the servers. To use, just call constructor with config.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class DeployWebApps {

	private static final String LINE       = "----------------------------------------------------";
	private static final String DOUBLELINE = "====================================================";
	private static final String INDENT     = "  ";

	/**
	 * DeployWebApps using configuarion.
	 */
	public DeployWebApps( ConfigReader config ) throws IOException {

		Application application = config.getApplication();

		print( DOUBLELINE );
		print( "Deploying " + application.getName() + " to servers" );

		// loop through each server for deployment
		for( Iterator servers = config.getServers().iterator(); servers.hasNext(); ) {
			Server server = (Server) servers.next();
			print( LINE );
			print( server.getName() + " version " + server.getVersion() );

			switch ( server.getType() ) {

				// copy warfile
				case Server.TYPE_WAR:
					print( INDENT + "copying warfile to " + server.getDeployment() );
					copyFile( application.getWarLocation(), server.getDeployment() );
					break;

				// copy earfile
				case Server.TYPE_EAR:
					print( INDENT + "copying earfile to " + server.getDeployment() );
					copyFile( application.getEarLocation(), server.getDeployment() );
					break;

				// unpack warfile
				case Server.TYPE_UNPACK:
					print( INDENT + "unpacking warfile to " + server.getDeployment() );
					//delTree( server.getDeployment() );
					unPack( application.getWarLocation(), server.getDeployment() );
					break;

			}

		}

		print( DOUBLELINE );

	}

	/**
	 * Print something to stdout
	 */
	private void print( String s ) {
		System.out.println( s );
	}

	/**
	 * Copy a file.
	 */
	private void copyFile( File src, File dest ) throws IOException {
		InputStream in = Files.newInputStream(src.toPath());
		OutputStream out = Files.newOutputStream(dest.toPath());
    pipe(in, out);
		in.close();
		out.close();
	}

  private void pipe(InputStream in, OutputStream out) throws IOException {
    int lengthRead;
    byte[] buffer = new byte[10240];
    while((lengthRead = in.read(buffer)) >= 0)
      out.write(buffer, 0, lengthRead);
  }

	/**
	 * Unpack war file as a directory.
	 */
	private void unPack( File war, File dir ) throws IOException {
		JarFile jar = new JarFile( war );
		dir.mkdir();
		for ( Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
			JarEntry entry = (JarEntry) entries.nextElement();
			File file = new File( dir, entry.getName() );
			if ( entry.isDirectory() ) {
				if ( !file.exists() ) file.mkdir();
			}
			else {
				InputStream in = jar.getInputStream( entry );
				OutputStream out = Files.newOutputStream(file.toPath());
        pipe(in, out);
				in.close();
				out.close();
			}
		}
	}

	/**
	 * Command line access.
	 * Load config from file. If cmd line arg given, use that file, otherwise tests.xml
   */
	public static void main( String[] args ) throws ConfigException, IOException {

		File configFile = new File( args.length == 0 ? "tests.xml" : args[0] );
		ConfigReader config = new ConfigReader( configFile );

		new DeployWebApps( config );

	}

}
