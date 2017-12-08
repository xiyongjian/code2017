package gosigma.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionApp {
	static Logger logger = LoggerFactory.getLogger(ReflectionApp.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		// TODO Auto-generated method stub
		try {
			Thread t = Thread.currentThread();
			logger.info("class name : " + t.getClass().getName());
			logger.info("method name : " + t.getName());
			logger.info("method name (0): " + t.getStackTrace()[0].getMethodName());
			logger.info("method name (1): " + t.getStackTrace()[1].getMethodName());
			logger.info("file name (1): " + t.getStackTrace()[1].getFileName());
			logger.info("class name (1): " + t.getStackTrace()[1].getClassName());
			logger.info("line number (1): " + t.getStackTrace()[1].getLineNumber());
			logger.info("method name (2): " + t.getStackTrace()[2].getMethodName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("", e);
		}
		logger.info("Leaving...");
	}

}
