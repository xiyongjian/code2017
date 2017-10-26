package gosigma.study.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

// import com.cognos.CAM_AAA.authentication.INamespaceConfiguration;

public class NgLog4j {

	public static FileAppender _appender = null;
	// public static String _pattern = "%d [%t|%p|%c|%C{1}|%C{2}] %m%n";
	public static String _pattern = "[%d %t|%p|%c|%M] - %m%n";
	public static Layout _layout = new PatternLayout(_pattern);
	public static ConsoleAppender _console = null;

	//	public static void init(INamespaceConfiguration theNamespaceConfiguration) {
	//		try {
	//			Logger logger = NgLog4j.getLogger("NgLogger.init(configuration)");
	//			logger.info("using theNamespaceConfiguration initialize NgLogger");
	//			String installLocation = theNamespaceConfiguration.getInstallLocation();
	//			String logProp = installLocation + File.separator + "configuration" + File.separator
	//					+ "NgLogger.properties";
	//			Properties prop = new Properties();
	//			prop.load(new FileInputStream(logProp));
	//			NgLog4j.init(prop.getProperty("logfile"));
	//			logger.info("done, using theNamespaceConfiguration initialize NgLogger");
	//
	//			logSysInfo();
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} finally {
	//		}
	//	}

	public static void init(String file) {
		String pid = ManagementFactory.getRuntimeMXBean().getName();
		// pid = pid.substring(0, pid.indexOf('@'));
		if (_appender == null) {
			_appender = new FileAppender();
			_appender.setFile(file + "." + pid);
			_appender.setName("FileLogger");
			_appender.setLayout(_layout);
			_appender.setThreshold(Level.DEBUG);
			_appender.setAppend(true);
			_appender.activateOptions();
			Logger.getRootLogger().addAppender(_appender);
		}
	}

	public static void logSysInfo() {
		Logger logger = NgLog4j.getLogger("NgLog4j.logSysInfo()");
		// record system information
		String dir = System.getProperty("user.dir");
		logger.info("[user dir] - " + dir);

		Path currentRelativePath = Paths.get("");
		dir = currentRelativePath.toAbsolutePath().toString();
		logger.info("[current dir] - " + dir);

		String classpathStr = System.getProperty("java.class.path");
		logger.info("[classpath] - \n" + classpathStr.replaceAll(";", "\n"));

		logger.info("system properties : ");
		Properties props = System.getProperties();
		for (Object k : props.keySet()) {
			logger.info("[" + k.toString() + "] - " + props.getProperty((String) k));
		}
		logger.info("get current PID : " + ManagementFactory.getRuntimeMXBean().getName());
	}

	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);

		if (_console == null) {
			_console = new ConsoleAppender(_layout);
			Logger.getRootLogger().addAppender(_console);
		}

		return logger;
	}

	public static void main(String[] args) {
		final String dir = System.getProperty("user.dir");
		System.out.println("current dir = " + dir);
		String classpathStr = System.getProperty("java.class.path");
		System.out.println(classpathStr.replaceAll(";", "\n"));

		Logger logger = NgLog4j.getLogger(NgLog4j.class.getName());

		try {
			// InputStream in = new FileInputStream(file);
			InputStream in = NgLog4j.class.getClassLoader().getResourceAsStream("NgLogger.properties");
			if (in != null) {
				Properties prop = new Properties();
				prop.load(in);
				String file = prop.getProperty("logfile");
				logger.info("property, logfile : " + file);
				init(file);
				logSysInfo();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

		// logger.addAppender(new ConsoleAppender(_layout));

		logger.info("hello from NgLog4j");
	}

	// usage :
	// NgLog.getlogger(<name>); -> get logger (log4j stuff)
	// NgLog.init(<prop file>); when prop file location is ready
	// NgLog.properties
	// logfile=c:/temp/NgLog.log

}
