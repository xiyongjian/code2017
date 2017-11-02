package gosigma.study.jndi.local;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalContextFactory  {

	final static Logger logger = LoggerFactory.getLogger(LocalContextFactory.class);
	
	private static Context _localContext = null;

	/**
	 * do not instantiate this class directly. Use the factory method.
	 */
	LocalContextFactory() {
		logger.info("Entering...");
		logger.info("Leaving...");
	}

	public static LocalContext createLocalContext(String databaseDriver) throws SimpleException {
		logger.info("Entering... driver:" + databaseDriver);

		try {
			LocalContext ctx = new LocalContext();
			Class.forName(databaseDriver);
			NamingManager.setInitialContextFactoryBuilder(ctx);
			logger.info("Leaving...");
			_localContext = ctx;
			return ctx;
		} catch (Exception e) {
			throw new SimpleException("Error Initializing Context: " + e.getMessage(), e);
		}

	}

	//	@Override
	//	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
	//		logger.info("Entering...");
	//		InitialContextFactory icf = new LocalContextFactory();
	//		logger.info("Leaving...");
	//		return icf;
	//	}
	//
	//	@Override
	//	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
	//		logger.info("Entering...");
	//		logger.info("Entering...");
	//		return _localContext;
	//	}
}