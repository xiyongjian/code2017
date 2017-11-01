package gosigma.song;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

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

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class XmlParserA {
	final static Logger logger = LoggerFactory.getLogger(XmlParserA.class);

	public static void main(String[] args) throws TransformerException, IOException {
		logger.info("Entering...");
		// TODO Auto-generated method stub
		InputStream in = null;

		in = XmlParserA.class.getClassLoader().getResourceAsStream("RealtimeConstTotals_HTML_t1-3.xsl");
		String xsl = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
		// String xsl = XmlParserA.getStringFromInputStream(in);
		// String xsl = XmlParserA.InputStream2String(in);
		logger.info("xsl : \n" + xsl);

		in = XmlParserA.class.getClassLoader().getResourceAsStream("PUB_RealtimeConstTotals.xml");
		String xml = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
		// String xml = XmlParserA.getStringFromInputStream(in);
		// String xml = XmlParserA.InputStream2String(in);
		logger.info("xml : \n" + xml);

		{
			Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
			logger.info("Title : " + doc.select("DocTitle").text());
			Elements ies = doc.select("IntervalEnergy");
			for (Element ie : doc.select("IntervalEnergy")) {
				logger.info("select mq : " + ie.select("> MQ").text());
				// String i = ie.select("Interval").get(0).text();
				// logger.info("IntervalEnergy, Interval : " + i);
				// for (Element mq : ie.select("MQ")) {
				// String s = mq.select("MarketQuantity").first().text() + " : "
				// + mq.select("EnergyMW").first().text();
				// logger.info("mq : " + s);
				// }
			}

			logger.info("path select : " + doc.select(":root DocBody DeliveryDate"));

		}

		if (true) {
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
			
			loadHtml(html);
		}

		if (false) {
			// Document doc = Jsoup.parse(text);
			Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
			logger.info("doc : " + doc.toString());
			Elements eles = doc.getAllElements();
			for (Element e : eles) {
				logger.info("ele : " + e.toString());
			}
		}
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	private static String InputStream2String(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		// StandardCharsets.UTF_8.name() > JDK 7
		return result.toString("UTF-8");
	}
	
	public static String loadHtml(String html) throws IOException {
		logger.info("Entering...");
		
		URL url = new URL("http://www.example.com");
		StringWebResponse response = new StringWebResponse(html, url);
		WebClient client = new WebClient();
		HtmlPage page = HTMLParser.parseHtml(response, client.getCurrentWindow());
		
		String rsl = page.asXml();
		logger.info("in length : " + html.length());
		logger.info("out length : " + rsl.length());
		
		logger.info("out html : " + rsl);
		
		logger.info("Leaving...");
		return rsl;
	}

}
