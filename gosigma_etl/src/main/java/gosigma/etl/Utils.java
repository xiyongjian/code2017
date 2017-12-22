package gosigma.etl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Utils {
	public static Logger log = UtilsLog.getLogger(Utils.class);

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
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"PJM Transfer Interface Information (MW)\r\n" +
			"\r\n" +
			"\r\n" +
			"Interface,Actual flow,Warning Level,Transfer Limit\r\n" +
			"50045005,2407,3447,4028\r\n" +
			"AEP/DOM,2188,3238,3409\r\n" +
			"APSOUTH,3544,4144,4362\r\n" +
			"BED-BLA,1522,1808,1903\r\n" +
			"CENTRAL,790,3799,3999\r\n" +
			"CLVLND,1401,2619,2757\r\n" +
			"COMED,-3084,2430,2558\r\n" +
			"EAST,3792,6047,6366\r\n" +
			"WEST,3264,5341,5943\r\n" +
			"\r\n" +
			"Name does not indicate direction." +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Data Last Updated\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Sat Dec 16 19:40:00 EST 2017\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Aggregate Locational Marginal Prices (LMP)\r\n" +
			"\r\n" +
			"\r\n" +
			"\r\n" +
			"Name,Type,5 Minute Weighted Avg. LMP,Hourly Integrated LMP for Hour Ending 19\r\n" +
			"AECO,ZONE,33.9,35.6\r\n" +
			"AEP,ZONE,31.4,33.2\r\n" +
			"APS,ZONE,32.8,34.6\r\n" +
			"ATSI,ZONE,33.1,35.0\r\n" +
			"BGE,ZONE,34.2,36.1";

	/**
	 * find record at 'offset' after record which contains 'match' and with
	 * totalCols pass all empty line/record /**
	 * 
	 * @param records
	 * @param match
	 * @param offset
	 * @param totalCols
	 * @return
	 * @throws EtlException
	 */
	public static List<CSVRecord> findCsvRecords(List<CSVRecord> records, String match, String args)
			throws EtlException {
		log.info("Entering...  match : " + match + " , args : " + args);
		List<String> as = Arrays.asList(args.split(",", -1));
		if (as.size() < 2)
			throw new EtlException("args have less than 2 values");
		String stop = null;

		for (int i = 2; i < as.size(); ++i) {
			if (stop == null)
				stop = as.get(i);
			else
				stop = stop + "," + as.get(i);
		}
		if (stop != null)
			stop = stop.replaceAll("\\s", "");
		// stop is flag of end of records (pass 1)

		return findCsvRecords(records, match, Integer.parseInt(as.get(0)), Integer.parseInt(as.get(1)), stop);
	}

	public static List<CSVRecord> findCsvRecords(List<CSVRecord> records, String match, int offset, int totalCols) {
		return findCsvRecords(records, match, offset, totalCols, null);
	}

	public static List<CSVRecord> findCsvRecords(List<CSVRecord> records, String match, int offset, int totalCols,
			String stop) {
		log.info("Entering... match : " + match + " , offset : " + offset + " , totalCols : " + totalCols);
		log.info("stop : [" + stop + "]");
		if (stop != null)
			stop = stop.replaceAll("\\s", "");

		String regexAll = regexAll(match);
		// String regexS = regexAll(match.replace("\\s", "")); // without space, dangerous
		boolean found = false;
		int pass = 0;
		List<CSVRecord> ret = new ArrayList<CSVRecord>();

		for (int i = 0; i < records.size(); ++i) {
			CSVRecord record = records.get(i);
			String recordString = recordString(record);
			if (recordString.isEmpty())
				continue;
			if (found == false) {
				// if (recordString.matches(regexAll) || recordString.matches(regexS))
				if (Utils.doubleMatch(recordString, regexAll)) {
					log.info("found record : " + recordString);
					found = true;
				} else
					continue;
			}

			if (pass >= offset) {
				// stop condition
				if (stop != null
						&& Utils.recordString(record).replaceAll("\\s", "").toLowerCase()
								.contains(stop.toLowerCase())) {
					log.info("stop hit , break : " + Utils.recordString(record));
					break;
				}

				if (record.size() == totalCols) {
					ret.add(record);
					if (Utils.shouldDump(pass - offset))
						log.info("get record at offset : " + pass + ", record : " + recordString);
				} else {
					log.info("end of finding due to cols size mismatch : " + record.size() + " vs " + totalCols);
					break;
				}
			}
			++pass;
		}

		log.info("Leaving...");
		return ret;

	}

	public static boolean doubleMatch(String s, String regex) {
		return s.matches(regex) || s.replaceAll("\\s", "").matches(regex); // ignore space
	}

	public static String regexAll(String str) {
		return ".*(?i)" + str + ".*"; // ignore case
	}

	public static String recordString(CSVRecord rec) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rec.size(); ++i) {
			if (i > 0)
				sb.append(',');
			sb.append(rec.get(i));
		}
		return sb.toString();
	}

	public static void testParse() {
		try {
			// Reader in = new FileReader("path/to/file.csv");
			StringReader in = new StringReader(csvString);
			// Pattern regex = Pattern.compile("CREATED.*FOR (\\d{4}/\\d{2}/\\d{2})");
			List<CSVRecord> records = CSVFormat.EXCEL.parse(in).getRecords();

			{
				// String start = "Data Last Updated";
				String start = "Last Updated";
				List<CSVRecord> recs = null;

				recs = findCsvRecords(records, start, 0, 1);
				log.info("find record with : " + start);
				log.info("return rows : " + recs.size());
				for (CSVRecord r : recs)
					log.info("record : " + recordString(r));

				start = "Interface,Actual flow,Warning Level,Transfer Limi";
				log.info("find records follow : " + start);
				recs = findCsvRecords(records, start, 0, 4);
				log.info("return rows : " + recs.size());
				for (CSVRecord r : recs)
					log.info("record : " + recordString(r));
			}
			for (CSVRecord record : records) {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*************
	 * section : parse html element to csv style string
	 ******************/
	public static class Ele2Csv {
		private boolean rowLine = false;
		private StringBuilder sb = null;

		public String proc(Element e) {
			rowLine = false;
			sb = new StringBuilder();
			return conv(e);
		}

		private String conv(Element e) {
			// td tag for continue lines
			if (e.tagName().equalsIgnoreCase("td")) {
				if (rowLine == true)
					sb.append(',');
				sb.append(e.text());
				rowLine = true;
			} else {
				if (rowLine == true)
					sb.append('\n');
				rowLine = false;
				// if (!e.ownText().isEmpty())
				sb.append(e.ownText());
				if (!e.tagName().equalsIgnoreCase(("tr"))) // shrink tr's new line
					sb.append('\n');
				for (Element c : e.children()) {
					conv(c);
				}
			}
			return sb.toString();
		}
	}

	/**
	 * parse 'cols' string for insert SQL
	 * 
	 * @param cols
	 *            cols string from properties file
	 * @param idxs
	 *            return indexes of columns for values
	 * @return string xxx used in 'insert <tbl> ( xxx ) .....
	 */
	public String parseCols(String cols, List<Integer> indexes) {
		log.info("Entering...  cols : " + cols);
		String[] cl = cols.split(",");

		indexes.clear();
		// String insCols = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cl.length; ++i) {
			String s = cl[i].trim();
			if (s.equals(""))
				continue;
			if (!indexes.isEmpty())
				sb.append(",");
			sb.append(s);
			indexes.add(i);
		}
		// ensure
		assert sb.toString().split(",").length == indexes.size();

		log.info("indexes : " + String.join(",", indexes.stream().map(Object::toString).collect(Collectors.toList())));
		log.info("Leaving...  ret : " + sb.toString());

		return sb.toString();
	}

	// will insert 'null' when string is empty
	public static String getValueStringFromCsvRecord(CSVRecord record, List<Integer> indexes) {
		return getValueStringFromCsvRecord(record, indexes, false);
	}

	public static String getValueStringFromCsvRecord(CSVRecord record, List<Integer> indexes, boolean trimSpace) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indexes.size(); ++i) {
			if (i > 0)
				sb.append(',');
			String s = record.get(indexes.get(i));
			if (trimSpace)
				s = s.trim();
			if (s.isEmpty())
				sb.append("null");
			else
				sb.append("\"" + s + "\"");
		}
		return sb.toString();
	}

	/************* sequence dump control ******************/
	private static int _dumpFactor = 1;

	public static boolean shouldDump(int i) {
		if (_dumpFactor < 1)
			return true;
		return (i % _dumpFactor) == 0;
	}

	public static void setDumpFactor(int f) {
		log.info("set dump factor : " + f);
		_dumpFactor = f;

	}

	/************* sequence dump control ******************/
	public static void listSystemProperties() {
		log.info("Entering....");
		Properties props = System.getProperties();
		Set<Object> keys = new TreeSet(props.keySet());
		for (Object key : keys) {
			Object value = props.get(key);
			log.info("property " + key + " : " + value);
		}
	}

	public static URL fileUrl(String file) {
		try {
			return new File(file).toURI().toURL();
		} catch (MalformedURLException e) {
			log.info("file to url failed : " + file);
			return null;
		}
	}

	public static void testClassPath() throws MalformedURLException {

		File file = new File("test.txt");
		log.info("file test.txt, path : " + file.getAbsolutePath());
		log.info("file test.txt, path : " + file.getPath());
		log.info("file test.txt, URI : " + file.toURI());
		log.info("file test.txt, URL : " + file.toURI().toURL());
		log.info("current directory : " + System.getProperty("user.dir"));
		File path = new File(System.getProperty("user.dir"));

		log.info("---- before add URL ----");
		log.info("x() URLs : " + XResLoader.x().toString());
		log.info("x() add current  URLs : " + XResLoader.x().head(Utils.fileUrl(System.getProperty("user.dir"))));
	}

	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	/************* section : main() for testing ******************/
	public static void main(String[] args) {
		try {
			testInit();
			testParse();
			testClassPath();
		} catch (MalformedURLException e) {
			log.info("", e);
		}
	}

}
