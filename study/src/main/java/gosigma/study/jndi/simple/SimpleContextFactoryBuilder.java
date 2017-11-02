package gosigma.study.jndi.simple;

import java.util.Hashtable;
import java.util.Map.Entry;

import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleContextFactoryBuilder implements InitialContextFactoryBuilder{

	final static Logger logger = LoggerFactory.getLogger(SimpleContextFactoryBuilder.class);

	@Override
	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
		logger.info("Entering...");
		logger.info("Environment ");
		for (Entry<?, ?> e : environment.entrySet()) {
			logger.info(e.getKey().toString() + " : " + e.getValue().toString());
		}
		InitialContextFactory icf = new SimpleContextFactory();
		logger.info("Leaving...");
		return icf;
	}

}
