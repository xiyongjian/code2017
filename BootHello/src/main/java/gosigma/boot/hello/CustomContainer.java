package gosigma.boot.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomContainer implements EmbeddedServletContainerCustomizer {

	private static final Logger log = LoggerFactory.getLogger(CustomContainer.class);

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		log.info("---- customized container", new Throwable());

		container.setPort(8088);
		// container.setContextPath("/mkyong");

	}

}