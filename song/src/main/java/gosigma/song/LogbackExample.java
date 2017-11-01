package gosigma.song;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackExample {
	final static Logger logger = LoggerFactory.getLogger(LogbackExample.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        logger.info("Msg #1");
        logger.warn("Msg #2");
        logger.error("Msg #3");
        logger.debug("Msg #4");
        
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);

		final String dir = System.getProperty("user.dir");
		System.out.println("[current dir] - " + dir);
		String classpathStr = System.getProperty("java.class.path");
		System.out.println("[classpath] - " + classpathStr.replaceAll(";",  "\n"));
	}

}
