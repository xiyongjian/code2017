package gosigma.etl_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class EtlDirConfiguration extends WebMvcConfigurerAdapter {
	public static Logger log = LoggerFactory.getLogger(EtlDirConfiguration.class);
	
    @Value("${etl.dir}")
    private String _etlDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("register etl_files/** to " + _etlDir);
		registry.addResourceHandler("/etl_files/**").addResourceLocations("file:///" + _etlDir + "/");
	}
}
