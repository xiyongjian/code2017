package gosigma.song;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedLoadParseApp {
	final static Logger logger = LoggerFactory.getLogger(FeedLoadParseApp.class);

	public static void main(String[] args) throws IOException {
		logger.info("Entering...");
		// TODO Auto-generated method stub
		// loadHtml();

		load_lmppost();

		logger.info("Leaving...");
	}

	public static void loadHtml() throws IOException {
		logger.info("Entering...");

		String url = "http://localhost:8080/webapp/dynamic.html";
		Document doc = Jsoup.connect(url).get();
		String title = doc.title();
		System.out.println(title);

		// System.out.println(doc.html());

		logger.info("Leaving...");

	}

	public static void load_lmppost() throws IOException {
		logger.info("Entering...");

		String url = "http://www.pjm.com/pub/account/lmpgen/lmppost.html";
		Document doc = Jsoup.connect(url).get();
		String title = doc.title();
		logger.info(title);
		
		logger.info("---- all children ----");
		listChildren(doc.select(":root").get(0), "root");
		
		//		logger.info("---- doc html ----");
		//		logger.info(doc.html());
		
		

		logger.info("Leaving...");

	}

	public static void listChildren(Element e, String prefix) {
		String p = prefix + "." + e.tagName();
		System.out.println(p + ":" + e.ownText());
		for (Element c : e.children()) {
			listChildren(c, p);
		}
	}
}
