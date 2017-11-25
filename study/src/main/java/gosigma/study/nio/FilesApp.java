package gosigma.study.nio;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class FilesApp {
	public static Logger log = (Logger) LoggerFactory.getLogger(FilesApp.class);

	public static void main(String[] args) {
		usePath();
		
	}

	public static void usePath() {
		Path path = Paths.get("data/logging.properties");
		log.info("path : " + path.toString());

		boolean pathExists =
		        Files.exists(path,
		            new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
		log.info("exist : " + pathExists);

	}

}
