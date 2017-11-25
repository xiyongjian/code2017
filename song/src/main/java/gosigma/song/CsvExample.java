package gosigma.song;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvExample {

	final static Logger logger = LoggerFactory.getLogger(CsvExample.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		// TODO Auto-generated method stub

		try {
			basic();

			String url = "https://www.misoenergy.org/ria/Consolidated.aspx?format=csv";
			fromUrl(url);
			
			withHeader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Leaving...");
	}

	public static void basic() throws IOException {
		logger.info("Entering...");

		// Reader in = new FileReader("path/to/file.csv");
		String csv = "\"a\"\"\",b,c,d\n1,2,3,4\n5,6,7,8";
		StringReader in = new StringReader(csv);
		// Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		for (CSVRecord record : records) {
			System.out.println(record.toString());
			// String lastName = record.get("Last Name");
		}

		logger.info("Leaving...");
	}

	public static void fromUrl(String urlStr) throws IOException {
		logger.info("Entering... url:" + urlStr);
		
		final URL url = new URL(urlStr);
		final Reader reader = new InputStreamReader(new BOMInputStream(url.openStream()), "UTF-8");
		// final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		// final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
		// System.out.println("header : " + parser.getHeaderMap().toString());
		final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
		try {
		    for (final CSVRecord record : parser) {
		        System.out.println("record, columns " + record.size() + " : " + record.toString());
		    }
		} finally {
		    parser.close();
		    reader.close();
		}
		        
		logger.info("Leaving...");
	}
	
	/**
	* Creates a reader capable of handling BOMs.
	*/
	public InputStreamReader newReader(final InputStream inputStream) {
	    return new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
	}
	        
	public static void withHeader() throws IOException {
		logger.info("Entering...");
		
		String csv = "\"a\"\"\",b,c,d\n1,2,3,4\n5,6,7,8";
		StringReader in = new StringReader(csv);
		// Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		// Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "CustomerNo", "Name", "Misc").parse(in);
		
		for (CSVRecord record : records) {
			System.out.println(record.toString());
			System.out.println(record.get("ID") + "," + record.get("Name"));
			// String lastName = record.get("Last Name");
		}
		logger.info("Leaving...");
	}
	
}
