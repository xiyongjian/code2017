package gosigma.song;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class HtmlUnitExample {
	final static Logger logger = LoggerFactory.getLogger(HtmlUnitExample.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		logger.info("loading html via HtmlUnit, after javascript run");
		// TODO Auto-generated method stub
		String url = "http://localhost:8080/webapp/dynamic.html";
		try {
			loadHtml(url);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		}
		
		url = "http://reports.ieso.ca/public/RealtimeConstTotals/PUB_RealtimeConstTotals.xml";
		try {
			loadXml(url);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("",  e);;
		}
		

		logger.info("Leaving...");
	}
	
	public static void loadHtml(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		logger.info("Entering... - url");
        WebClient webClient = new WebClient();
        System.out.println("Loading page now: "+url);
        HtmlPage page = webClient.getPage(url);

        String pageText01 = page.asXml();
        webClient.waitForBackgroundJavaScript(5 * 1000); /* will wait JavaScript to execute up to 30s */
        String pageText = page.asXml();

        logger.info("#first html source : \n"+pageText01);
        logger.info("#FULL source after JavaScript execution:\n "+pageText);

		logger.info("Leaving...");
	}

	public static void loadXml(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		logger.info("Entering... - url");
        WebClient webClient = new WebClient();
        System.out.println("Loading page now: "+url);
        XmlPage page = webClient.getPage(url);

        // webClient.waitForBackgroundJavaScript(5 * 1000); /* will wait JavaScript to execute up to 30s */
        String pageText = page.asXml();

        logger.info("#FULL XML :\n "+pageText);

		logger.info("Leaving...");
	}

}
