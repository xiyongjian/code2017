package gosigma.study.log;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

public class LogbackApp {

	public static void main(String[] args) {
		//		Logger foo = createLoggerFor("foo", "foo.log");
		//		Logger bar = createLoggerFor("bar", "bar.log");

		Logger foo = createLoggerFor("foo", "c:/tmp/logback.log");
		Logger bar = createLoggerFor("bar", "c:/tmp/logback.log");
		foo.info("test");
		bar.info("bar");

		Logger logger = (Logger) LoggerFactory.getLogger(LogbackApp.class);
		logger.info("logbackapp testing, should be in syslog@localhost");
		
	}

	private static Logger createLoggerFor(String string, String file) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		PatternLayoutEncoder ple = new PatternLayoutEncoder();

		ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
		ple.setContext(lc);
		ple.start();
		FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
		fileAppender.setFile(file);
		fileAppender.setEncoder(ple);
		fileAppender.setContext(lc);
		fileAppender.setAppend(true);
		fileAppender.start();
		
		PatternLayoutEncoder ple2 = new PatternLayoutEncoder();
		ple2.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
		ple2.setContext(lc);
		ple2.start();
		ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
		consoleAppender.setEncoder(ple2);
		consoleAppender.setContext(lc);
		consoleAppender.start();

		Logger logger = (Logger) LoggerFactory.getLogger(string);
		logger.addAppender(fileAppender);
		logger.addAppender(consoleAppender);
		logger.setLevel(Level.DEBUG);
		logger.setAdditive(false); /* set to true if root should log too */

		return logger;
	}

}