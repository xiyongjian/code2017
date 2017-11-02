package gosigma.song;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LoadTable {
	final static Logger logger = LoggerFactory.getLogger(LoadTable.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		// TODO Auto-generated method stub

		try {
			String url = "http://localhost:8080/webapp/dynamic.html";
			String text = loadUrl(url);
			logger.info("text : \n" + text);

			url = "http://reports.ieso.ca/docrefs/stylesheet/RealtimeConstTotals_HTML_t1-3.xsl";
			String xsl = loadUrl(url);
			logger.info("text : \n" + xsl);

			url = "http://reports.ieso.ca/public/RealtimeConstTotals/PUB_RealtimeConstTotals.xml";
			String xml = loadUrl(url);
			logger.info("text : \n" + xml);
			
			{
				Document doc = Jsoup.parse(xml,  "", Parser.xmlParser());
				logger.info("Title : " + doc.select("DocTitle").text());
				Elements ies = doc.select("IntervalEnergy");
				for (Element ie : doc.select("IntervalEnergy")) {
					logger.info("select mq : " + ie.select("> MQ").text());
					//					String i = ie.select("Interval").get(0).text();
					//					logger.info("IntervalEnergy, Interval : " + i);
					//					for (Element mq : ie.select("MQ")) {
					//						String s = mq.select("MarketQuantity").first().text() + " : "
					//								+ mq.select("EnergyMW").first().text();
					//						logger.info("mq : " + s);
					//					}
				}
				
				logger.info("path select : " + doc.select(":root DocBody DeliveryDate"));
			
			}

			if (false) {
				TransformerFactory tFactory = TransformerFactory.newInstance();

				StringReader readerXml = new StringReader(xml);
				StringReader readerXsl = new StringReader(xsl);

				Source xslDoc = new StreamSource(readerXsl);
				Source xmlDoc = new StreamSource(readerXml);

				ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
				Transformer transformer = tFactory.newTransformer(xslDoc);
				transformer.transform(xmlDoc, new StreamResult(htmlStream));
				
				String html = htmlStream.toString();
				logger.info("html : \n" + html);
			}

			if (false) {
				// Document doc = Jsoup.parse(text);
				Document doc = Jsoup.parse(text, "", Parser.xmlParser());
				logger.info("doc : " + doc.toString());
				Elements eles = doc.getAllElements();
				for (Element e : eles) {
					logger.info("ele : " + e.toString());
				}
			}
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			logger.info("", e);
		}

		logger.info("Leaving...");
	}

	public static String loadUrl(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		logger.info("Entering... - url : " + url);
		WebClient webClient = new WebClient();
		System.out.println("Loading page now: " + url);
		webClient.waitForBackgroundJavaScript(5 * 1000); /* will wait JavaScript to execute up to 30s */

		SgmlPage page = webClient.getPage(url);
		logger.info("return object'type is : " + page.getClass().getName());
		logger.info("return object is : " + page.toString());

		// String pageText01 = page.asXml();
		// webClient.waitForBackgroundJavaScript(5 * 1000); /* will wait JavaScript to
		// execute up to 30s */
		// String pageText = page.asXml();

		// logger.info("#first html source : \n"+pageText01);
		// logger.info("#FULL source after JavaScript execution:\n "+pageText);

		logger.info("Leaving...");
		return page.asXml();
	}
}
