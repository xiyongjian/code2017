package gosigma.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gosigma.spring.HelloWorld;

@Configuration
public class Config2 {
	@Bean
	public HelloWorld hello2() {
		HelloWorld o = new HelloWorld();
		o.setName("From Config2 Class");
		return o;
	}
}
