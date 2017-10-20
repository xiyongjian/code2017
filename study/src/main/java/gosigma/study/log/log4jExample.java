package gosigma.study.log;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class log4jExample {

	/* Get actual class name to be printed on */
	static Logger logger = Logger.getLogger(log4jExample.class.getName());

	public static void print(Logger log) {
		if (log == null) {
			System.out.println(" is null");
		} else {
			System.out.println(" toString() : " + log.toString());
			System.out.println(" name : " + log.getName());
			System.out.println(" parent : " + ((log.getParent() == null) ? "null" : log.getParent().toString()));
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("logger");
		print(logger);

		System.out.println("logger's parent");
		print(logger.getRootLogger());

		String pattern = "%d{yyyy-MM-dd}-%t-%x-%-5p-%-10c:%m%n";
		Layout layout = new PatternLayout(pattern);

		// FileAppender fa = new FileAppender(layout, "c:/tmp/log4jExample.log");

		FileAppender fa = new FileAppender();
		fa.setFile("c:/tmp/log4jExample.log");
		fa.setName("FileLogger");
		fa.setLayout(layout);
		fa.setThreshold(Level.DEBUG);
		fa.setAppend(true);
		fa.activateOptions();

		logger.getRootLogger().addAppender(fa);

		logger.debug("Hello this is a debug message");
		logger.info("Hello this is an info message");

	}
}