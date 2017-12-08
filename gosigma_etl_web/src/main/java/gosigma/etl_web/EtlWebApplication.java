package gosigma.etl_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EtlWebApplication {

	public static void main(String[] args) {
		Utils.log.info("SpringApplication hierarchy :\n" + Utils.getSupers(SpringApplication.class));
		Utils.log.info("EtlWebApplication hierarchy :\n" + Utils.getSupers(EtlWebApplication.class));
		SpringApplication.run(EtlWebApplication.class, args);
	}
}
