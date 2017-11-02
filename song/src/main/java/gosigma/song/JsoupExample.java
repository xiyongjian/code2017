package gosigma.song;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupExample {
	final static Logger logger = LoggerFactory.getLogger(JsoupExample.class);

	public static void main(String[] args) throws IOException {
		logger.info("Entering...");
		// TODO Auto-generated method stub
		loadHtml();

		logger.info("Leaving...");
	}

	public static void loadHtml() throws IOException {
		logger.info("Entering...");

		String url = "http://localhost:8080/webapp/dynamic.html";
		Document doc = Jsoup.connect(url).get();
		String title = doc.title();
		System.out.println(title);
		
		System.out.println(doc.html());

		logger.info("Leaving...");

	}

}
