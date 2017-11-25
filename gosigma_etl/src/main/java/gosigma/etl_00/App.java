package gosigma.etl_00;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		Options options = new Options();
		options.addOption(Option.builder("d").longOpt("directory").desc("output file location").hasArg()
				.argName("OUTPUT DIR").build());
		options.addOption(
				Option.builder("i").longOpt("input").desc("input feed file").hasArg().argName("FEED FILE").build());
		options.addOption(Option.builder("p").longOpt("parse").desc("download and parse feed file to SQL").build());

		CommandLineParser commandLineParser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine commandLine = null;
		try {
			commandLine = commandLineParser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			helpFormatter.printHelp("utility-name", options);
			System.exit(1);
		}

		helpFormatter.printHelp("utility-name", options);

		String dir = commandLine.getOptionValue('d');
		System.out.println("dir : " + dir);
		System.out.println("-i : " + commandLine.hasOption('i'));
		System.out.println("-p : " + commandLine.hasOption('p'));
	}

}
