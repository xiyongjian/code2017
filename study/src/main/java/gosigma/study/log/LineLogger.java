package gosigma.study.log;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LineLogger {

	// public static String _pattern = "[%d %t|%p|%c|%M|%l] - %m%n";
	public static String _pattern = "[%d %t|%l] - %m%n";
	public static Layout _layout = new PatternLayout(_pattern);
	public static ConsoleAppender _console = null;

	public static Logger logger = Logger.getLogger(LineLogger.class.getName());

	// testing part
	public static void main(String[] args) {
		System.out.println("start.");

		_console = new ConsoleAppender(_layout);
		Logger.getRootLogger().addAppender(_console);
		
		logger.info("added console");
		logger.info("Hello, world");

		System.out.println("done.");
	}

}
