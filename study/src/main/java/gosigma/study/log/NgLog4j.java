package gosigma.study.log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class NgLog4j {

	public static FileAppender appender = null;

	public static void init() {
		init("c:/tmp/NgLog.log");
	}

	public static void init(String file) {
		if (appender == null) {
			String pattern = "%d{yyyy-MM-dd} %-10c %t %x %-5p : %m%n";
			Layout layout = new PatternLayout(pattern);

			// FileAppender fa = new FileAppender(layout, "c:/tmp/log4jExample.log");
			appender = new FileAppender();
			appender.setFile(file);
			appender.setName("FileLogger");
			appender.setLayout(layout);
			appender.setThreshold(Level.DEBUG);
			appender.setAppend(true);
			appender.activateOptions();
		}

		for (Logger l : loggers) {
			l.addAppender(appender);
		}
	}

	public static Set<Logger> loggers = new HashSet<Logger>();

	public static Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		if (appender != null)
			logger.addAppender(appender);

		loggers.add(logger);
		return logger;
	}

	public static void main(String[] args) throws IOException {
		final String dir = System.getProperty("user.dir");
		System.out.println("current dir = " + dir);
		String classpathStr = System.getProperty("java.class.path");
		System.out.println(classpathStr.replaceAll(";",  "\n"));

		Logger logger = NgLog4j.getLogger(NgLog4j.class.getName());

		// InputStream in = new FileInputStream(file);
		InputStream in = NgLog4j.class.getClassLoader().getResourceAsStream("NgLog4j.properties");
		Properties prop = new Properties();
		prop.load(in);
		String file = prop.getProperty("logfile");
		init(file);

		logger.addAppender(new ConsoleAppender(appender.getLayout()));

		logger.info("hello from NgLog");
	}
	
	
	//	usage : 
	//		NgLog4j.getlogger(<name>); -> get logger (log4j stuff)	
	//		NgLog4j.init(<prop file>);	 when prop file location is ready
		
	
}
