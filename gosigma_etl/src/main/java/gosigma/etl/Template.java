package gosigma.etl;

import static gosigma.etl.LogUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class Template {

	public static final String feedId = "Template";
	// public static final Logger log = LoggerFactory.getLogger(Template.class);

	public static void main(String[] args) {

		try {
			// output system information
			String userDir = System.getProperty("user.dir");
			System.out.println("[current dir] - " + userDir);
			String classPathStr = System.getProperty("java.class.path");
			System.out.println("[classpath] - " + classPathStr.replaceAll(";", "\n"));
			
			String logDir = System.getProperty("log_dir");
			if (logDir == null)
				logDir = userDir + File.separator + "logs";
			System.out.println("logDir - " + logDir);
			
			String dataDir = System.getProperty("data_dir");
			if (dataDir == null)
				dataDir = userDir + File.separator + "data";
			System.out.println("dataDir - " + dataDir);
			
			System.out.println("feedId - " + feedId);

			initLog(feedId, logDir);

			log().info("load properties");

			InputStream in = Template.class.getClassLoader().getResourceAsStream("etl.properties");
			Properties prop = new Properties();
			prop.load(in);

			String dir = prop.getProperty(feedId + ".dir");
			String url = prop.getProperty(feedId + ".url");
			String file = prop.getProperty(feedId + ".file");

			log().info("dir  : " + dir);
			log().info("url  : " + url);
			log().info("file : " + file);
			log().error("to both file as error");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JoranException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}