package gosigma.etl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class UtilsLog {
	static public void p(String s) {
		System.out.println(s);
	}

	static public Logger log = LoggerFactory.getLogger(UtilsLog.class);
	static private Set<Object> __objects = null;
	static {
		UtilsLog.register(UtilsLog.class);
	}

	public static void register(Object o) {
		if (__objects == null)
			__objects = new HashSet<>();
		__objects.add(o);
	}

	public static Logger getLogger(Object o) {
		Logger log = null;
		if (o instanceof Class)
			log = LoggerFactory.getLogger((Class) o);
		else
			log = LoggerFactory.getLogger(o.getClass());
		UtilsLog.register(o);
		return log;
	}

	public static void resetLogger(boolean debug) throws JoranException {
		if (debug == true)
			System.setProperty("etl.debug", "true");
		UtilsLog.resetLogger();
	}

	public static void resetLogger() throws JoranException {
		log.info("re-initialize logger, after some environment changes...");
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		ci.autoConfig();
		log = LoggerFactory.getLogger(UtilsLog.class);

		log.info("reset all registered loggers");
		for (Object o : __objects) {
			try {
				if (o instanceof Class) { // class static member 'log'
					Class c = (Class) o;
					log.info("reset log for class : " + c.getName());
					Field field = ((Class) o).getDeclaredField("log");
					field.setAccessible(true); // Suppress Java language access checking
					field.set(null, LoggerFactory.getLogger(c));

				} else { // object member 'log'
					log.info("reset log for object : " + o.getClass().getName() + "@" + o.hashCode());
					Field field = o.getClass().getDeclaredField("log");
					field.setAccessible(true); // Suppress Java language access checking
					field.set(o, LoggerFactory.getLogger(o.getClass()));
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				log.info("reset log", e);
				;
			}
		}
	}

	public int test = 0;

	public static class Test extends UtilsLog {
		public Logger log = LoggerFactory.getLogger(UtilsLog.class);

		public Test() {
			UtilsLog.register(this);
		}
	}

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, JoranException {
		Integer I = 1;
		p("I's class : " + I.getClass().getName());
		p("I's class's : " + I.getClass().getClass().getName());
		p("I's class is instance of Class : " + (I.getClass() instanceof Class));

		{
			Field f = UtilsLog.class.getField("test");
			p("field test : " + f.toGenericString() + ", " + f.toString());
		}

		{
			Field f = Test.class.getField("test");
			p("field test : " + f.toGenericString() + ", " + f.toString());
		}

		p("total register # : " + UtilsLog.__objects.size());
		Test test = new Test();
		p("total register # : " + UtilsLog.__objects.size());

		// System.setProperty("etl.debug", "true");
		UtilsLog.resetLogger(true);

		log.info("start here :...");

	}

}
