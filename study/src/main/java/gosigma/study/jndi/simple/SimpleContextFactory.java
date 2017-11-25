package gosigma.study.jndi.simple;

import java.util.Hashtable;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleContextFactory implements InitialContextFactory {
	 
	final static Logger logger = LoggerFactory.getLogger(SimpleContextFactory.class);

	@Override
	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		logger.info("Entering...");
		logger.info("Environment ");
		for (Entry<?, ?> e : environment.entrySet()) {
			logger.info(e.getKey().toString() + " : " + e.getValue().toString());
		}
		
		Context c = new SimpleContext();
		logger.info("Leaving...");
		return c;
	}
	
	public SimpleContextFactory() {
		super();
		logger.info("Entering...");
		logger.info("Leaving...");
	}
	  
}
