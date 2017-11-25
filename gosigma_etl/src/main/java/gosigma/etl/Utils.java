package gosigma.etl;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class Utils {
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

	public static Logger log = null;

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

	public static void main(String[] args) {
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
}
