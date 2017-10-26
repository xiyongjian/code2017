package gosigma.study.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

// import com.cognos.CAM_AAA.authentication.INamespaceConfiguration;

public class NgLog4j {

	public static RollingFileAppender _appender = null;
	// public static String _pattern = "%d [%t|%p|%c|%C{1}|%C{2}] %m%n";
	public static String _pattern = "[%d %t|%p|%c|%M] - %m%n";
	public static Layout _layout = new PatternLayout(_pattern);
	public static ConsoleAppender _console = null;

	public static Logger logger = NgLog4j.getLogger(NgLog4j.class.getName());

	// public static void init(INamespaceConfiguration theNamespaceConfiguration) {
	// try {
	// Logger logger = NgLog4j.getLogger("NgLogger.init(configuration)");
	// logger.info("using theNamespaceConfiguration initialize NgLogger");
	// String installLocation = theNamespaceConfiguration.getInstallLocation();
	// String logProp = installLocation + File.separator + "configuration" +
	// File.separator
	// + "NgLogger.properties";
	// Properties prop = new Properties();
	// prop.load(new FileInputStream(logProp));
	// NgLog4j.init(prop.getProperty("logfile"));
	// logger.info("done, using theNamespaceConfiguration initialize NgLogger");
	//
	// logSysInfo();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// }
	// }

	public static void init(String file) {
		logger.info("init with file : " + file);
		if (_appender != null && file != null) {
			File fnew = new File(file);
			logger.info("old file : " + _appender.getFile());
			File fold = new File(_appender.getFile());
			if (!fnew.equals(fold)) {
				logger.info("replace with new file");
				_appender.close();
				Logger.getRootLogger().removeAppender(_appender);
				_appender = null;
			} else
				logger.info("two file same, do nothing");
		}

		if (_appender == null) {
			_appender = new RollingFileAppender();
			_appender.setFile(file);
			_appender.setName("FileLogger");
			_appender.setLayout(_layout);
			_appender.setThreshold(Level.DEBUG);
			_appender.setAppend(true);
			_appender.activateOptions();
			_appender.setMaximumFileSize(1024 * 50);
			Logger.getRootLogger().addAppender(_appender);
		}
	}

	public static void logSysInfo() {
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

		try {
			// InputStream in = new FileInputStream(file);
			InputStream in = NgLog4j.class.getClassLoader().getResourceAsStream("NgLogger.properties");
			if (in != null) {
				Properties prop = new Properties();
				prop.load(in);
				String file = prop.getProperty("logfile");
				logger.info("property, logfile : " + file);
				init(file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}

		// logger.addAppender(new ConsoleAppender(_layout));

		logSysInfo();
		logger.info("hello from NgLog4j");

		listRootAppender();

		logger.info("add same appender");
		// add same appender, NO effect at all
		Logger.getRootLogger().addAppender(_appender);
		listRootAppender();

		init("c:\\tmp\\NgLog4j.prop.log");

		logger.info("remove appender");
		// _appender.close();
		Logger.getRootLogger().removeAppender(_appender);
		listRootAppender();

		logger.info("remove same appender again");
		Logger.getRootLogger().removeAppender(_appender);
		listRootAppender();

		Logger.getRootLogger().addAppender(_appender);
		logger.info("change console level to ERROR");
		_console.setThreshold(Level.ERROR);
		logger.info("info should not show");
		logger.error("error should show");

	}

	public static void listRootAppender() {
		logger.info("root logger properties, appenders");
		Enumeration e = Logger.getRootLogger().getAllAppenders();
		int i = 1;
		while (e.hasMoreElements()) {
			Object a = e.nextElement();
			logger.info(i + " - " + a.getClass().getName());
			if (a.getClass().equals(FileAppender.class)) {
				FileAppender fa = (FileAppender) a;
				logger.info("name : " + fa.getName() + ", file : " + fa.getFile() + ", encoding : " + fa.getEncoding());
			}
			if (a.getClass().equals(ConsoleAppender.class)) {
				ConsoleAppender ca = (ConsoleAppender) a;
				logger.info("name : " + ca.getName() + ", encoding : " + ca.getEncoding());
			}
			if (a.getClass().equals(RollingFileAppender.class)) {
				RollingFileAppender fa = (RollingFileAppender) a;
				logger.info("name : " + fa.getName() + ", file : " + fa.getFile() + ", encoding : " + fa.getEncoding()
						+ " , max size " + fa.getMaximumFileSize());
			}

			++i;
		}
	}

	// usage :
	// NgLog.getlogger(<name>); -> get logger (log4j stuff)
	// NgLog.init(<prop file>); when prop file location is ready
	// NgLog.properties
	// logfile=c:/temp/NgLog.log

}
