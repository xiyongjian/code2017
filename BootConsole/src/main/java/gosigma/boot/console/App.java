package gosigma.boot.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static java.lang.System.exit;

@SpringBootApplication
public class App implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	@Autowired
	private HelloMessageService helloService;

	public static void main(String[] args) throws Exception {

		// disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(App.class);
		// app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	// Put your logic here.
	public void run(String... args) throws Exception {
		logger.info("---- command line runner ----", new Throwable("Cmd Line Runner"));

		if (args.length > 0) {
			System.out.println(helloService.getMessage(args[0].toString()));
		} else {
			System.out.println(helloService.getMessage());
		}

		// exit(0);
	}

	@Bean
	@Order(2)
	public CommandLineRunner runner2() {
		return new CommandLineRunner() {
			public void run(String... args) throws Exception {
				logger.info("---- runner2() ----", new Throwable("Cmd Line Runner"));
			}
		};
	}

	@Bean
	@Order(3)
	public CommandLineRunner runner3() {
		return new CommandLineRunner() {
			public void run(String... args) throws Exception {
				logger.info("---- runner3() ----", new Throwable("Cmd Line Runner"));
			}
		};
	}
	
	@Bean
	@Order(1)
	public ApplicationRunner appRunner1() {
		return new ApplicationRunner() {
			public void run(ApplicationArguments arg0) throws Exception {
				logger.info("---- appRunner1() ----", new Throwable("App Runner"));
			}
		};
	}

	@Bean
	@Order(2)
	public ApplicationRunner appRunner2() {
		return new ApplicationRunner() {
			public void run(ApplicationArguments arg0) throws Exception {
				logger.info("---- appRunner2() ----", new Throwable("App Runner"));
			}
		};
	}
}
