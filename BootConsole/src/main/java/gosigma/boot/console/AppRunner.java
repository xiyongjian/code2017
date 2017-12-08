package gosigma.boot.console;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
	private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

	public void run(ApplicationArguments arg0) throws Exception {
    	logger.info("---- app runner ----", new Throwable("App Runner"));
		String strArgs = Arrays.stream(arg0.getSourceArgs()).collect(Collectors.joining("|"));
		logger.info("Application started with arguments:" + strArgs);
	}
}