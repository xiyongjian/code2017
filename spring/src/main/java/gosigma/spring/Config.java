package gosigma.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:Spring-Bean.xml")
public class Config {
	@Bean
	public HelloWorld hello() {
		HelloWorld o = new HelloWorld();
		o.setName("From Config Class");
		return o;
	}
}
