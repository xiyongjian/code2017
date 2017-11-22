package gosigma.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogUtil {

	private static Logger _log = null;

	public static void initLog(String feedId, String logDir) throws JoranException {
		System.out.println("initialize logback");
		System.setProperty("feed_id", feedId);
		System.setProperty("log_dir", logDir);
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		ci.autoConfig();
		_log = LoggerFactory.getLogger(LogUtil.class);
	}

	public static Logger log() {
		if (_log == null)
			_log = LoggerFactory.getLogger(LogUtil.class);
		return _log;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
