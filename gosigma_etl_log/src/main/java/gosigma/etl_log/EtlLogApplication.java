package gosigma.etl_log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class EtlLogApplication implements ApplicationRunner {
	public static Logger log = LoggerFactory.getLogger(EtlLogApplication.class);

	// public static String _etlDir = null;
	@Value("${etl.dir}")
	private String _etlDir;

	public static void main(String[] args) {
		SpringApplication.run(EtlLogApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// List<String> dirs = args.getOptionValues("etl.dir");
		// if (dirs != null && !dirs.isEmpty())
		// _etlDir = dirs.get(0);
		log.info("etl.dir :  " + _etlDir);

		internalBeans();
	}

	@Autowired
	private ApplicationContext applicationContext;

	private void internalBeans() {
		// log.info("Debugging...", new Throwable("stack"));
		log.info("Entering...");
		log.info("appliation context, class : " + applicationContext.getClass().getName());

		Set<String> ids = new HashSet<>();
		for (String n : applicationContext.getBeanDefinitionNames()) {
			Object o = applicationContext.getBean(n);
			if (o == null)
				continue;
			String id = objId(o);
			log.info("app ctx bean : " + n + ", " + id);
			ids.add(id);
			for (Field field : o.getClass().getDeclaredFields()) {
				field.setAccessible(true); // You might want to set modifier to public first.
				Object value = null;
				try {
					value = field.get(o);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// e.printStackTrace();
					log.info("     field : " + field.getName() + ", NULL, exception");
				}
				if (value != null) {
					log.info("     field : " + field.getName() + ", ref : " + objId(value));
				}
			}
		}
		
	}

	private String objId(Object o) {
		String cn = o.getClass().getName();
		String hash = Integer.toHexString(o.hashCode());
		return cn + "@" + hash;
	}

}
