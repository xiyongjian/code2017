package gosigma.song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupTest {
	final static Logger logger = LoggerFactory.getLogger(LoadTable.class);

	static public String xml = "<results>\r\n" + 
			"  <status>OK</status>\r\n" + 
			"  <totalTransactions>1</totalTransactions>\r\n" + 
			"  <language>english</language>\r\n" + 
			"  <taxonomy>\r\n" + 
			"    <element>\r\n" + 
			"      <label>/business and industrial/advertising and marketing/telemarketing</label>\r\n" + 
			"      <score>0.805156</score>\r\n" + 
			"    </element>\r\n" + 
			"    <element>\r\n" + 
			"      <confident>no</confident>\r\n" + 
			"      <label>/automotive and vehicles/certified pre-owned</label>\r\n" + 
			"      <score>0.23886</score>\r\n" + 
			"    </element>\r\n" + 
			"    <element>\r\n" + 
			"      <confident>no</confident>\r\n" + 
			"      <label>/shopping/retail</label>\r\n" + 
			"      <score>0.156721</score>\r\n" + 
			"    </element>\r\n" + 
			"  </taxonomy>\r\n" + 
			"</results>";
	
	public static void main(String[] args) {
		Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
		String status = doc.select("status").text();
		logger.info("status : " + status);


		//		if (status.equals("OK")) {
		//		    Elements elements = doc.getElementsByTag("element");
		//
		//		    for (Element e : elements) {
		//		            System.out.println(e.select("label").text() + ","
		//		                        + e.select("score").text());
		//		    }
		//		}
	}

}
