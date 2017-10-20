package gosigma.study.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
	static Logger logger = Logger.getLogger(Logging.class.getName());
	static Logger logger2 = Logger.getLogger("testing");

	public static void print(Logger log) {
		if (log == null) {
			System.out.println(" is null");
		} else {
			System.out.println(" toString() : " + log.toString());
			System.out.println(" name : " + log.getName());
			System.out.println(" parent : " + ((log.getParent() == null) ? "null" : log.getParent().toString()));
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println("logger");
			print(logger);
			System.out.println("logger2");
			print(logger2);

			System.out.println("logger's parent");
			print(logger.getParent());
			System.out.println("logger2's parent");
			print(logger2.getParent());

			Handler fh = new FileHandler("c:/tmp/logging.log");
			logger.addHandler(fh);
			fh.setFormatter(new SimpleFormatter());
			logger.info("Hello, world");
			
			Handler[] hs = logger.getHandlers();
			for (int i=0; i<hs.length; ++i)
				System.out.println("handler : " + hs[i].getClass().getName());
			

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
