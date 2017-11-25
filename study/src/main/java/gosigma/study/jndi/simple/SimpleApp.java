package gosigma.study.jndi.simple;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApp {

	final static Logger logger = LoggerFactory.getLogger(SimpleContextFactory.class);

	public static void main(String[] args) throws NamingException {
		logger.info("Entering...");
		simple2();
		simple();
		logger.info("Leaving...");
	}

	public static void simple() throws NamingException {
		logger.info("Entering...");
		Hashtable environnement = new Hashtable();
		environnement.put(Context.INITIAL_CONTEXT_FACTORY, "gosigma.study.jndi.simple.SimpleContextFactory");
		Context ctx = new InitialContext(environnement);
		ctx.bind("jndiName", "hello, world");

		Object value = ctx.lookup("jndiName");

		ctx.close();

		logger.info("jndiName : " + value.toString());
		logger.info("Leaving...");
	}

	public static void simple2() throws NamingException {
		logger.info("Entering...");
		SimpleContextFactoryBuilder builder = new SimpleContextFactoryBuilder();
		logger.info("set initial context factory builder");
		NamingManager.setInitialContextFactoryBuilder(builder);

		logger.info("new InitialContext");
		Context ctx = new InitialContext();
		ctx.bind("jndiName2", "hello, world again");

		logger.info("lookup jndiName2");
		Object value = ctx.lookup("jndiName2");

		ctx.close();

		logger.info("jndiName2 : " + value.toString());
		logger.info("Leaving...");
	}

}
