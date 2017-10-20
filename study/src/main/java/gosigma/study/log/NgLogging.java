package gosigma.study.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class NgLogging {
	public static Handler handler = null;

	public static void init() {
		init("c:/tmp/NgLogging.log");
	}

	public static void init(String file) {
		if (handler == null) {
			try {
				handler = new FileHandler(file, true);
				handler.setFormatter(new SimpleFormatter());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Logger getLogger(String name) {
		init();

		Logger logger = Logger.getLogger(name);
		if (handler != null)
			logger.addHandler(handler);

		return logger;
	}

	public static void main(String[] args) {
		Logger logger = NgLogging.getLogger(NgLogging.class.getName());
		Handler ch = new ConsoleHandler();
		ch.setFormatter(handler.getFormatter());

		logger.info("hello from NgLog");
	}
}
