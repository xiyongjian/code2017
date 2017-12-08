/**
 *  Syslog Server Java:
 *  Multiplatform: Easy setup Syslog Server tool for recording network messages. 
 *  It opens the UDP port 514 on your computer to act as Syslog Server and displays 
 *  the messages immediately in your screen.
 *  
 *  Copyright (C)  Julio Molina Soler <jmolinaso@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Properties;
import net.sf.jlog514.main.Jlog514Main;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class jlog514 {

    /**
     * @param args
     * @throws org.apache.commons.cli.ParseException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws ParseException, InterruptedException {
        Properties props = new Properties();
        Options options = new Options();
        Option help = new Option( "help", "Print help information" );
        options.addOption(help);
        Option daemon = new Option( "daemon", "Run as daemon" );
        options.addOption(daemon);
        Option noui = new Option( "noui", "Don't start the UI, run only the command line" );
        options.addOption(noui);
        
        Option port = OptionBuilder.withArgName("port")
                                .hasArg()
                                .withDescription(  "Define the port to listen" )
                                .create( "port" );
        options.addOption(port);

        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse( options, args);
        HelpFormatter formatter = new HelpFormatter();
        
        if(cmd.hasOption("help")){
            formatter.printHelp( "jlog514", options );
            System.exit(0);
        }
        
        // Default values
        props.setProperty("ui", "true");
        props.setProperty("daemon", "false");
        props.setProperty("port", cmd.getOptionValue("port", "514"));
        
        // Override those values then
        if(cmd.hasOption("noui")){
            props.setProperty("ui", "false");
        }
        if(cmd.hasOption("daemon")){
            props.setProperty("daemon", "true");
        }
        
        Jlog514Main jlog514 = new Jlog514Main(props);
        Thread main = new Thread(jlog514);
        main.setName("MainThread");
        main.start();
        while (main.isAlive()){
            Thread.sleep(60000);
        }
    }
	

}
