package gosigma.etl_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestRestController {
	public static Logger log = LoggerFactory.getLogger(TestRestController.class);

	@RequestMapping(value= {"/01"}, method = RequestMethod.POST)
	public String post01(@RequestBody String body) {
		// log.info("Entering...", new Throwable("Debugging..."));
		log.info("Entering...");
		log.info("get body : " + body);
		return body;
	}

}
