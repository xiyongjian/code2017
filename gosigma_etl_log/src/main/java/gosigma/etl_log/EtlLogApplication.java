package gosigma.etl_log;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
	}

}
