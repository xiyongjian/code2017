package gosigma.etl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Utils {
	public static Logger log = LoggerFactory.getLogger(Utils.class);

	public static void p(String s) {
		System.out.println(s);
	}

	/************* section : initialization ******************/
	/**
	 * Sun property pointing the main class and its arguments. Might not be defined
	 * on non Hotspot VM implementations.
	 */
	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	/**
	 * @return application command line
	 * @throws IOException
	 */
	public static String appCmdLine() throws IOException {
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			log.info("java binary : " + java);

			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			log.info("vmArguments : " + String.join(" ", vmArguments));

			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			log.info("vmArgsOneLine : " + vmArgsOneLine.toString());

			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
			log.info("init cmd : " + cmd.toString());

			// program main and program arguments
			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			log.info("mainCommand : " + String.join(" ", mainCommand));

			// program main is a jar
			if (mainCommand[0].endsWith(".jar")) {
				// if it's a jar, add -jar mainJar
				cmd.append("-jar " + new File(mainCommand[0]).getPath());
			} else {
				// else it's a .class, add the classpath and mainClass
				cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
			}
			// finally add program arguments
			for (int i = 1; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}

			log.info("final cmd : " + cmd.toString());

			return cmd.toString();
		} catch (Exception e) {
			// something went wrong
			throw new IOException("Error while trying to restart the application", e);
		}
	}

	/**
	 * @return application command line, as shell script
	 * @throws IOException
	 */
	public static String appShellScript() throws IOException {
		try {
			// java binary
			String java = System.getProperty("java.home") + "/bin/java";
			log.info("java binary : " + java);

			// vm arguments
			List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			log.info("vmArguments : " + String.join(" ", vmArguments));

			StringBuffer vmArgsOneLine = new StringBuffer();
			for (String arg : vmArguments) {
				// if it's the agent argument : we ignore it otherwise the
				// address of the old application and the new one will be in conflict
				if (!arg.contains("-agentlib")) {
					vmArgsOneLine.append(arg);
					vmArgsOneLine.append(" ");
				}
			}
			log.info("vmArgsOneLine : " + vmArgsOneLine.toString());

			// init the command to execute, add the vm args
			final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
			log.info("init cmd : " + cmd.toString());

			// program main and program arguments
			String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			log.info("mainCommand : " + String.join(" ", mainCommand));
			int startIdx = 0;

			List<String> cp = new ArrayList<String>();
			// program main is a jar
			if (mainCommand[0].endsWith(".jar")) {
				cp.add(new File(mainCommand[0]).getPath());
				++startIdx;
			}
			cp.addAll(Arrays.asList(System.getProperty("java.class.path").split(File.pathSeparator)));

			// finally add program arguments
			for (int i = startIdx; i < mainCommand.length; i++) {
				cmd.append(" ");
				cmd.append(mainCommand[i]);
			}

			log.info("final cmd : " + cmd.toString());
			log.info("final cp : " + String.join("\n", cp));

			StringBuilder cpExport = new StringBuilder();
			StringBuilder cpCopy = new StringBuilder();

			cpExport.append("## setup/export CLASSPATH ##\n");
			cpExport.append("export CLASSPATH=$CLASSPATH:.\n");
			cpCopy.append("## copy jar file to . ##\n");
			for (String jar : cp) {
				cpExport.append("export CLASSPATH=$CLASSPATH:lib/"
						+ jar.substring(jar.lastIndexOf('\\') + 1, jar.length()) + "\n");

				cpCopy.append("COPY " + jar + " .\n");
			}

			return cpCopy.toString() + cpExport.toString() + cmd.toString();
		} catch (Exception e) {
			// something went wrong
			throw new IOException("Error while trying to restart the application", e);
		}
	}

	public static void initLog(Logger log) {
		Utils.log = log;
	}

	public static void initLog() throws JoranException {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset(); // override default configuration

		URL xmlFile = Utils.class.getClassLoader().getResource("logback_utils.xml");
		System.out.println("xml file path : " + xmlFile.getPath());
		// context.putProperty("application-name", "Utils.main");
		// jc.doConfigure(xmlFile.getPath());

		// inject the name of the current application as "application-name"
		// property of the LoggerContext
		context.putProperty("application-name", "Utils.main");
		// context.putProperty("logback.debug", "true"); -- this one not work
		System.setProperty("logback.debug", "true");
		jc.doConfigure(Utils.class.getClassLoader().getResourceAsStream("logback_utils.xml"));

		log = LoggerFactory.getLogger(Utils.class);
	}

	public static void testInit() {
		System.out.println("Utils::main()");
		try {
			initLog();
			log.info("hello, world");

			String cmd = Utils.appCmdLine();
			log.info("app cmd : " + cmd);

			String sh = Utils.appShellScript();
			log.info("app shell : \n" + sh);
		} catch (JoranException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/************* section : parsing util ******************/
	public static String csvString = "\r\n" +
			"Data Last Updated\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Sat Dec 16 19:40:00 EST 2017,\r\n" +
			"\r\n" +
			"\r\n" +
			"PJM Transfer Interface Information (MW)\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Interface,Actual flow,Warning Level,Transfer Limit,\r\n" +
			"50045005,2407,3447,4028,\r\n" +
			"AEP/DOM,2188,3238,3409,\r\n" +
			"APSOUTH,3544,4144,4362,\r\n" +
			"BED-BLA,1522,1808,1903,\r\n" +
			"CENTRAL,790,3799,3999,\r\n" +
			"CLVLND,1401,2619,2757,\r\n" +
			"COMED,-3084,2430,2558,\r\n" +
			"EAST,3792,6047,6366,\r\n" +
			"WEST,3264,5341,5943,\r\n" +
			"Name does not indicate direction.\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"PJM Instantaneous Dispatch Rates\r\n" +
			"\r\n" +
			"PJM Dispatch Rates\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"PJM Instantaneous Load (MW)\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Area,Instantaneous Load,\r\n" +
			"PJM RTO,100392,\r\n" +
			"PJM MID ATLANTIC REGION,35829,\r\n" +
			"PJM SOUTHERN REGION,13175,\r\n" +
			"PJM WESTERN REGION,51389,\r\n" +
			"Loads are calculated from raw telemetry data and are approximate.\r\n" +
			"The displayed values are NOT official PJM Loads.\r\n" +
			"\r\n" +
			"";

	/**
	 * find record at 'offset' after record which 'col' column contains 'match'
	 * 
	 * pass all empty line/record
	 * 
	 * @param records
	 * @param col
	 * @param start
	 * @param offset
	 * @return
	 */
	public static CSVRecord findRecord(List<CSVRecord> records, int col, String match, int offset) {
		log.info("Entering... col : " + col + ", match : " + match + " , offset : " + offset);
		Pattern regex = Pattern.compile(match);
		boolean found = false;
		int pass = 0;
		CSVRecord ret = null;
		for (int i = 0; i < records.size(); ++i) {
			CSVRecord record = records.get(i);
			if (record.size() < 2 && (record.size() == 1 && record.get(0).trim().isEmpty()))
				continue;
			if (found == false) {
				if (record.size() > col && regex.matcher(record.get(col).trim()).find()) {
					log.info("found record : " + record.toString());
					found = true;
				} else
					continue;
			}

			if (pass == offset) {
				ret = record;
				log.info("get record at offset : " + ret.toString());
				break;
			}
			++pass;
		}

		log.info("Leaving...");
		return ret;
	}

	public static void testParse() {
		try {
			// Reader in = new FileReader("path/to/file.csv");
			StringReader in = new StringReader(csvString);
			// Pattern regex = Pattern.compile("CREATED.*FOR (\\d{4}/\\d{2}/\\d{2})");
			List<CSVRecord> records = CSVFormat.EXCEL.parse(in).getRecords();

			{
				String start = "Data Last Updated";
				CSVRecord rec = findRecord(records, 0, start, 1);
				if (rec != null) {
					log.info("found : " + rec.toString());
					log.info("get data : " + rec.get(0));
				}
			}
			for (CSVRecord record : records) {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/************* section : main() for testing ******************/
	public static void main(String[] args) {
		testInit();
		testParse();
	}
}
