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

class LocalContext extends InitialContext implements InitialContextFactoryBuilder, InitialContextFactory {

	final static Logger logger = LoggerFactory.getLogger(LocalContext.class);

	public static LocalContext createLocalContext(String databaseDriver) throws SimpleException {
		logger.info("Entering... driver:" + databaseDriver);

		try {
			LocalContext ctx = new LocalContext();
			Class.forName(databaseDriver);
			NamingManager.setInitialContextFactoryBuilder(ctx);
			logger.info("Leaving...");
			return ctx;
		} catch (Exception e) {
			throw new SimpleException("Error Initializing Context: " + e.getMessage(), e);
		}

	}

	Map<Object, Object> dataSources;

	LocalContext() throws NamingException {
		super();
		logger.info("Entering...");
		dataSources = new HashMap<Object, Object>();
		logger.info("Leaving...");
	}

	public void addDataSource(String name, String connectionString, String username, String password) {
		logger.info("Entering...");
		this.dataSources.put(name, new LocalDataSource(connectionString, username, password));
		logger.info("Leaving...");
	}

	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> hsh) throws NamingException {
		logger.info("Entering...");
		dataSources.putAll(hsh);
		logger.info("Leaving...");
		return this;
	}

	public Context getInitialContext(Hashtable<?, ?> arg0) throws NamingException {
		logger.info("Entering...");
		logger.info("Leaving...");
		return this;
	}

	@Override
	public Object lookup(String name) throws NamingException {
		logger.info("Entering...");
		Object ret = dataSources.get(name);
		logger.info("Leaving...");
		return ret;
		// return (ret != null) ? ret : super.lookup(name);
	}
}