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

			//			String url = "https://www.misoenergy.org/ria/Consolidated.aspx?format=csv";
			//			fromUrl(url);
			//			
			//			withHeader();
			
			parseCsvString(csvString);
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
	
	public static void parseCsvString(String csv) throws IOException {
		logger.info("Entering...");
		
		StringReader in = new StringReader(csv);
		// Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		// Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "CustomerNo", "Name", "Misc").parse(in);
		
		for (CSVRecord record : records) {
			logger.info("record, " + record.size() + " column : " + record.toString());
		}
		logger.info("Leaving...");
	}
	
	public static String csvString = "\r\n" + 
			"PJM Operational Data\r\n" + 
			"\r\n" + 
			"This is provided for informational purposes ONLY and should\r\n" + 
			"\r\n" + 
			"not be relied upon by any party for the actual billing values.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Data Last Updated\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			",\r\n" + 
			"Sat Dec 16 19:40:00 EST 2017\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Aggregate Locational Marginal Prices (LMP)\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			",Name\r\n" + 
			",Type\r\n" + 
			",5 Minute Weighted Avg. LMP\r\n" + 
			",Hourly Integrated LMP for Hour Ending 19\r\n" + 
			"\r\n" + 
			"AECO,ZONE,33.9,35.6,\r\n" + 
			"AEP,ZONE,31.4,33.2,\r\n" + 
			"APS,ZONE,32.8,34.6,\r\n" + 
			"ATSI,ZONE,33.1,35.0,\r\n" + 
			"BGE,ZONE,34.2,36.1,\r\n" + 
			"COMED,ZONE,28.6,31.7,\r\n" + 
			"DAY,ZONE,33.8,34.2,\r\n" + 
			"DEOK,ZONE,36.3,33.2,\r\n" + 
			"DOM,ZONE,33.4,35.2,\r\n" + 
			"DPL,ZONE,34.5,36.5,\r\n" + 
			"DUQ,ZONE,32.5,34.3,\r\n" + 
			"EKPC,ZONE,31.6,32.4,\r\n" + 
			"JCPL,ZONE,33.9,35.9,\r\n" + 
			"METED,ZONE,33.5,35.5,\r\n" + 
			"PECO,ZONE,33.5,35.4,\r\n" + 
			"PENELEC,ZONE,33.1,35.1,\r\n" + 
			"PEPCO,ZONE,33.8,35.6,\r\n" + 
			"PPL,ZONE,33.3,35.2,\r\n" + 
			"PSEG,ZONE,34.0,35.9,\r\n" + 
			"RECO,ZONE,34.0,36.0,\r\n" + 
			"CPLEEXP,INTERFACE,31.9,33.5,\r\n" + 
			"CPLEIMP,INTERFACE,31.4,33.1,\r\n" + 
			"DUKEXP,INTERFACE,31.5,33.1,\r\n" + 
			"DUKIMP,INTERFACE,31.2,32.9,\r\n" + 
			"HUDSONTP,INTERFACE,35.4,37.4,\r\n" + 
			"IMO,INTERFACE,31.0,34.3,\r\n" + 
			"LINDENVFT,INTERFACE,34.0,36.0,\r\n" + 
			"MISO,INTERFACE,27.6,31.5,\r\n" + 
			"NCMPAEXP,INTERFACE,31.4,33.0,\r\n" + 
			"NCMPAIMP,INTERFACE,31.4,33.0,\r\n" + 
			"NEPTUNE,INTERFACE,33.8,35.7,\r\n" + 
			"NIPSCO,INTERFACE,28.8,31.8,\r\n" + 
			"NORTHWEST,INTERFACE,27.9,31.0,\r\n" + 
			"NYIS,INTERFACE,33.7,35.7,\r\n" + 
			"OVEC,INTERFACE,30.4,32.0,\r\n" + 
			"SOUTHEXP,INTERFACE,29.8,32.3,\r\n" + 
			"SOUTHIMP,INTERFACE,29.8,32.3,\r\n" + 
			"AEC - AP,AGGREGATE,32.6,34.4,\r\n" + 
			"AEC - JC,AGGREGATE,35.1,37.2,\r\n" + 
			"AEC - ME,AGGREGATE,33.3,35.2,\r\n" + 
			"AEC - PN,AGGREGATE,33.2,35.2,\r\n" + 
			"AELC,AGGREGATE,28.4,31.4,\r\n" + 
			"AEP W.O. MON POWER,AGGREGATE,31.3,33.1,\r\n" + 
			"AEPOHIO W.O. MON POWER,AGGREGATE,32.1,33.5,\r\n" + 
			"AK STEEL,AGGREGATE,28.1,31.2,\r\n" + 
			"AMP-ATSI,AGGREGATE,32.9,34.8,\r\n" + 
			"AMP-ATSI OH,AGGREGATE,32.9,34.8,\r\n" + 
			"AMP-ATSI PA,AGGREGATE,32.8,34.7,\r\n" + 
			"AMP-METED,AGGREGATE,33.5,35.3,\r\n" + 
			"AMP-OHIO,AGGREGATE,31.8,33.4,\r\n" + 
			"AURORA CT 1-4,AGGREGATE,28.4,31.5,\r\n" + 
			"BATAVIA,AGGREGATE,28.4,31.5,\r\n" + 
			"BATH COUNTY GEN,AGGREGATE,31.7,33.2,\r\n" + 
			"BAYONNE TECH,AGGREGATE,34.4,36.3,\r\n" + 
			"BAYVIEW,AGGREGATE,39.8,42.0,\r\n" + 
			"BERLIN DPL,AGGREGATE,35.8,37.8,\r\n" + 
			"BLAKELY BORO,AGGREGATE,33.5,35.3,\r\n" + 
			"BLUE RIDGE,AGGREGATE,32.1,33.6,\r\n" + 
			"BRANDONSH,AGGREGATE,33.9,35.8,\r\n" + 
			"BRUNSWICK,AGGREGATE,33.7,35.7,\r\n" + 
			"BUCK-CIN,AGGREGATE,35.7,33.3,\r\n" + 
			"BUCK-FE,AGGREGATE,33.0,35.0,\r\n" + 
			"BUCKEYE - AEP,AGGREGATE,32.1,33.5,\r\n" + 
			"BUCKEYE - DPL,AGGREGATE,33.8,34.7,\r\n" + 
			"BUCKEYE - WEC,AGGREGATE,31.1,32.6,\r\n" + 
			"CAMDEN TECH,AGGREGATE,33.4,35.3,\r\n" + 
			"CATAWISSA,AGGREGATE,31.9,33.8,\r\n" + 
			"CHAMBERS CCLP,AGGREGATE,33.2,35.0,\r\n" + 
			"CITIZENS,AGGREGATE,32.6,34.5,\r\n" + 
			"CLINCH RIVER 1-3,AGGREGATE,30.5,31.9,\r\n" + 
			"CONOWINGO GEN 1-11,AGGREGATE,33.4,35.3,\r\n" + 
			"COOKSTOWN,AGGREGATE,33.6,35.6,\r\n" + 
			"CORDOVA,AGGREGATE,27.3,30.4,\r\n" + 
			"CPP,AGGREGATE,33.4,35.3,\r\n" + 
			"CRISFIELD,AGGREGATE,37.0,39.1,\r\n" + 
			"DECAM COAL GEN,AGGREGATE,33.4,32.3,\r\n" + 
			"DECAM GAS GEN,AGGREGATE,30.9,32.5,\r\n" + 
			"DEK,AGGREGATE,36.3,33.0,\r\n" + 
			"DELI,AGGREGATE,27.5,30.6,\r\n" + 
			"DEMEC,AGGREGATE,33.8,35.7,\r\n" + 
			"DENAWASH,AGGREGATE,30.9,32.3,\r\n" + 
			"DOVER,AGGREGATE,33.6,35.5,\r\n" + 
			"DPL NORTH,AGGREGATE,33.2,35.1,\r\n" + 
			"DPL SOUTH,AGGREGATE,35.6,37.6,\r\n" + 
			"DPL_ODEC,AGGREGATE,35.8,37.9,\r\n" + 
			"DRESDEN CC,AGGREGATE,30.7,32.0,\r\n" + 
			"DUNCANNON,AGGREGATE,32.6,34.5,\r\n" + 
			"EAGLE POINT,AGGREGATE,33.3,35.2,\r\n" + 
			"EAST BEND 2,AGGREGATE,18.6,30.4,\r\n" + 
			"EASTON,AGGREGATE,34.9,36.9,\r\n" + 
			"ECRRF,AGGREGATE,34.2,36.1,\r\n" + 
			"EKPC-DEOK LOAD,AGGREGATE,38.9,33.1,\r\n" + 
			"ELGIN EC3,AGGREGATE,28.5,31.5,\r\n" + 
			"ELIZABETHTOWN,AGGREGATE,31.6,33.3,\r\n" + 
			"EPHRATA,AGGREGATE,33.6,35.6,\r\n" + 
			"FAIRLAWN,AGGREGATE,34.3,36.3,\r\n" + 
			"FE GEN,AGGREGATE,32.3,34.1,\r\n" + 
			"FE OHIO,AGGREGATE,33.0,34.9,\r\n" + 
			"FRONT ROYAL - AP,AGGREGATE,32.8,34.5,\r\n" + 
			"GENEVA,AGGREGATE,28.5,31.5,\r\n" + 
			"GIRARD,AGGREGATE,33.4,35.4,\r\n" + 
			"GOLDSBORO,AGGREGATE,33.3,35.0,\r\n" + 
			"GORSUCH,AGGREGATE,31.3,33.0,\r\n" + 
			"GRAND POINT,AGGREGATE,34.6,36.4,\r\n" + 
			"GRANDPOINT T1,AGGREGATE,34.6,36.4,\r\n" + 
			"GREENUP,AGGREGATE,31.6,32.5,\r\n" + 
			"HAGERSTOWN - AP,AGGREGATE,33.8,35.7,\r\n" + 
			"HANDSOME LAKE,AGGREGATE,32.8,34.7,\r\n" + 
			"HATFIELD - PL,AGGREGATE,33.3,35.3,\r\n" + 
			"HAZLETON 1-4,AGGREGATE,32.9,34.8,\r\n" + 
			"HAZLETON 2-3,AGGREGATE,32.9,34.8,\r\n" + 
			"HAZLETON 3-4,AGGREGATE,32.9,34.8,\r\n" + 
			"HILLSDALE_PARKRIDGE,AGGREGATE,34.1,36.1,\r\n" + 
			"HOMERCIT,AGGREGATE,32.1,33.8,\r\n" + 
			"HOMERCIT UNIT1,AGGREGATE,32.1,33.8,\r\n" + 
			"HOMERCIT UNIT2,AGGREGATE,32.2,33.9,\r\n" + 
			"HOMERCIT UNIT3,AGGREGATE,32.2,33.9,\r\n" + 
			"HREA - AP,AGGREGATE,32.5,34.3,\r\n" + 
			"HUDSON BC,AGGREGATE,34.3,36.2,\r\n" + 
			"HUDSON-LINDEN ABC,AGGREGATE,34.1,36.1,\r\n" + 
			"IRONWOOD,AGGREGATE,32.4,34.3,\r\n" + 
			"JOLIET 7,AGGREGATE,28.6,31.6,\r\n" + 
			"KENDALL 1-2,AGGREGATE,28.3,31.3,\r\n" + 
			"KENDALL 3 CC,AGGREGATE,28.3,31.3,\r\n" + 
			"KENDALL CT3,AGGREGATE,28.3,31.3,\r\n" + 
			"KITTATNY 230,AGGREGATE,33.9,35.9,\r\n" + 
			"LANSDALE,AGGREGATE,33.3,35.3,\r\n" + 
			"LEHIGHTON,AGGREGATE,33.1,35.0,\r\n" + 
			"LEWES DPL,AGGREGATE,35.5,37.6,\r\n" + 
			"LIBERTY,AGGREGATE,33.3,35.2,\r\n" + 
			"LIDA - AP,AGGREGATE,34.6,36.5,\r\n" + 
			"LINDEN A,AGGREGATE,33.9,35.9,\r\n" + 
			"LUMBERTON,AGGREGATE,31.6,33.3,\r\n" + 
			"MADISON,AGGREGATE,34.1,32.7,\r\n" + 
			"MANITOU,AGGREGATE,33.7,35.6,\r\n" + 
			"MARION,AGGREGATE,34.3,36.2,\r\n" + 
			"MERIDIAN EWHITLEY,AGGREGATE,28.7,32.2,\r\n" + 
			"MEYERSDALE WF,AGGREGATE,31.7,33.4,\r\n" + 
			"MICHFE,AGGREGATE,31.6,33.6,\r\n" + 
			"MIFFLINBURG,AGGREGATE,32.6,34.5,\r\n" + 
			"MISO (PRE-ATSI),AGGREGATE,29.3,32.1,\r\n" + 
			"MITCHELL 1,AGGREGATE,31.3,32.9,\r\n" + 
			"MON POWER,AGGREGATE,31.3,32.9,\r\n" + 
			"MONT ALTO - AP,AGGREGATE,34.2,36.0,\r\n" + 
			"MONTVILLE,AGGREGATE,34.1,36.0,\r\n" + 
			"MOTIVA,AGGREGATE,32.8,34.7,\r\n" + 
			"MUDDY RUN,AGGREGATE,32.6,34.5,\r\n" + 
			"NAAMANS,AGGREGATE,33.2,35.1,\r\n" + 
			"NAPERVILLE,AGGREGATE,28.5,31.6,\r\n" + 
			"NEW CASTLE DIESEL,AGGREGATE,32.8,34.7,\r\n" + 
			"NEWARK BAY,AGGREGATE,34.2,36.1,\r\n" + 
			"NEWCHURCH CT1-2,AGGREGATE,36.5,38.6,\r\n" + 
			"NEWMARTINSVILLE-AP,AGGREGATE,31.6,33.3,\r\n" + 
			"NLEBNON,AGGREGATE,33.0,34.9,\r\n" + 
			"OCCIDENTAL,AGGREGATE,32.8,34.7,\r\n" + 
			"OLYPHANT,AGGREGATE,33.5,35.3,\r\n" + 
			"ONTARIO,AGGREGATE,32.0,34.2,\r\n" + 
			"ONTELAUNEE,AGGREGATE,32.7,34.7,\r\n" + 
			"PAINESVILLE,AGGREGATE,33.1,35.0,\r\n" + 
			"PENN POWER,AGGREGATE,32.8,34.7,\r\n" + 
			"PENNTECH,AGGREGATE,33.0,35.2,\r\n" + 
			"PEPCO DC,AGGREGATE,33.8,35.7,\r\n" + 
			"PEPCO MD,AGGREGATE,33.8,35.6,\r\n" + 
			"PEPCO SMECO,AGGREGATE,33.4,35.2,\r\n" + 
			"PERKASIE,AGGREGATE,33.3,35.3,\r\n" + 
			"PERRYMAN 1-2,AGGREGATE,34.0,35.9,\r\n" + 
			"PERRYMAN 3-4,AGGREGATE,34.0,35.9,\r\n" + 
			"PHILIPPI - AP,AGGREGATE,32.0,33.8,\r\n" + 
			"POTTSTOWN NUG,AGGREGATE,34.4,36.5,\r\n" + 
			"PPL_ALLUGI,AGGREGATE,33.4,35.4,\r\n" + 
			"QUAKERTOWN,AGGREGATE,33.3,35.3,\r\n" + 
			"REDOAK CC,AGGREGATE,33.7,35.6,\r\n" + 
			"RICHMOND,AGGREGATE,28.2,32.2,\r\n" + 
			"RIVER,AGGREGATE,28.9,31.9,\r\n" + 
			"ROCHELLE,AGGREGATE,28.6,31.8,\r\n" + 
			"ROCK FALLS,AGGREGATE,27.4,30.6,\r\n" + 
			"ROCKFORD,AGGREGATE,27.9,31.0,\r\n" + 
			"RTEP B0287 SOURCE,AGGREGATE,32.6,34.5,\r\n" + 
			"RTEP B0328 SOURCE,AGGREGATE,31.9,33.6,\r\n" + 
			"RTEP B0329 SOURCE,AGGREGATE,33.1,34.7,\r\n" + 
			"SCHUYLKILL HAVEN,AGGREGATE,32.2,34.0,\r\n" + 
			"SENECA,AGGREGATE,32.5,35.0,\r\n" + 
			"SOUTH BEND 1-4,AGGREGATE,31.9,33.7,\r\n" + 
			"SOUTHEAST,AGGREGATE,31.8,33.4,\r\n" + 
			"SOUTHEAST (PRE-DOM),AGGREGATE,32.1,33.7,\r\n" + 
			"SOUTHRIV 230,AGGREGATE,33.7,35.7,\r\n" + 
			"SOUTHWEST,AGGREGATE,30.8,31.9,\r\n" + 
			"ST CLAIR,AGGREGATE,32.2,34.0,\r\n" + 
			"ST. CHARLES,AGGREGATE,28.5,31.5,\r\n" + 
			"SULLIVAN,AGGREGATE,32.2,34.0,\r\n" + 
			"SUNBURY BUS1E,AGGREGATE,32.6,34.5,\r\n" + 
			"SUNBURY BUS1W,AGGREGATE,32.6,34.5,\r\n" + 
			"SUNBURY BUS2E,AGGREGATE,32.6,34.5,\r\n" + 
			"SUNBURY BUS2W,AGGREGATE,32.6,34.5,\r\n" + 
			"SUNBURY LBRG,AGGREGATE,32.6,34.5,\r\n" + 
			"SUNOIL,AGGREGATE,33.4,35.4,\r\n" + 
			"TANSBORO AE,AGGREGATE,33.3,35.2,\r\n" + 
			"TARENTUM - AP,AGGREGATE,31.8,33.6,\r\n" + 
			"TEMPLEU,AGGREGATE,33.8,35.8,\r\n" + 
			"THURMONT - AP,AGGREGATE,34.0,35.8,\r\n" + 
			"TOSCO LOAD BUS,AGGREGATE,33.9,35.9,\r\n" + 
			"TRAYNOR,AGGREGATE,34.0,36.0,\r\n" + 
			"TRI-COUNTY,AGGREGATE,34.3,36.3,\r\n" + 
			"UGI,AGGREGATE,33.4,35.4,\r\n" + 
			"VINELAND,AGGREGATE,33.9,35.1,\r\n" + 
			"VP KERR DAM 1-7,AGGREGATE,32.9,34.5,\r\n" + 
			"WALDWICK JK,AGGREGATE,33.9,35.9,\r\n" + 
			"WARREN COUNTY CC,AGGREGATE,32.6,34.3,\r\n" + 
			"WATERFORD CC,AGGREGATE,30.8,32.2,\r\n" + 
			"WATSONTOWN,AGGREGATE,32.2,34.0,\r\n" + 
			"WEATHERLY,AGGREGATE,32.9,34.8,\r\n" + 
			"WELLSBORO,AGGREGATE,33.4,35.3,\r\n" + 
			"WESTVACO,AGGREGATE,32.4,34.2,\r\n" + 
			"WILLIAMSPORT - AP,AGGREGATE,33.7,35.5,\r\n" + 
			"WILLIAMSTOWN,AGGREGATE,32.3,32.1,\r\n" + 
			"WINNETKA,AGGREGATE,28.7,31.8,\r\n" + 
			"ZION 1,AGGREGATE,28.3,31.5,\r\n" + 
			"AEP GEN HUB,HUB,29.6,32.1,\r\n" + 
			"AEP-DAYTON HUB,HUB,31.1,33.0,\r\n" + 
			"ATSI GEN HUB,HUB,32.6,34.5,\r\n" + 
			"CHICAGO GEN HUB,HUB,28.3,31.3,\r\n" + 
			"CHICAGO HUB,HUB,28.7,31.8,\r\n" + 
			"DOMINION HUB,HUB,33.3,34.9,\r\n" + 
			"EASTERN HUB,HUB,34.6,36.6,\r\n" + 
			"N ILLINOIS HUB,HUB,28.6,31.6,\r\n" + 
			"NEW JERSEY HUB,HUB,33.9,35.8,\r\n" + 
			"OHIO HUB,HUB,30.9,33.0,\r\n" + 
			"WEST INT HUB,HUB,32.3,34.1,\r\n" + 
			"WESTERN HUB,HUB,33.1,35.0,\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"500 KV Bus Locational Marginal Prices (LMP)\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			",Bus Name\r\n" + 
			",5 Minute LMP\r\n" + 
			",Hourly Integrated LMP for Hour Ending 19\r\n" + 
			"\r\n" + 
			"112 WILTON,28.4,31.4,\r\n" + 
			"167 PLANO,28.3,31.3,\r\n" + 
			"23 COLLINS,28.4,31.3,\r\n" + 
			"502JCT,31.7,33.4,\r\n" + 
			"ALBURTIS,33.1,35.0,\r\n" + 
			"AMOS,30.6,32.1,\r\n" + 
			"AXTON,31.2,32.7,\r\n" + 
			"BAKER,30.7,32.2,\r\n" + 
			"BATH COUNTY,31.7,33.2,\r\n" + 
			"BEDINGTON,33.0,34.8,\r\n" + 
			"BELMONT,31.1,32.7,\r\n" + 
			"BLACKOAK,32.4,34.2,\r\n" + 
			"BRAMBLET,33.2,35.0,\r\n" + 
			"BRANCHBURG,33.5,35.4,\r\n" + 
			"BRIGHTON,33.4,35.2,\r\n" + 
			"BRISTERS,33.1,34.9,\r\n" + 
			"BRNGSVIL,33.0,34.9,\r\n" + 
			"BROADFORD,31.0,32.5,\r\n" + 
			"BRUNSSWY,32.4,34.0,\r\n" + 
			"BURCHESHILL,33.4,35.2,\r\n" + 
			"CABOT,32.0,33.7,\r\n" + 
			"CALVERTC,33.3,35.1,\r\n" + 
			"CARSON,32.9,34.5,\r\n" + 
			"CENTERPT,33.2,35.1,\r\n" + 
			"CHALKPT,33.4,35.2,\r\n" + 
			"CHANCELLOR,33.1,34.9,\r\n" + 
			"CHELTENH,33.4,35.2,\r\n" + 
			"CHICKAHOMINY,33.3,35.1,\r\n" + 
			"CLIFTON,33.2,35.0,\r\n" + 
			"CLOVER,32.4,33.9,\r\n" + 
			"CLOVERDALE,31.5,33.0,\r\n" + 
			"CONASTONE,32.9,34.7,\r\n" + 
			"CONEMAUGH,32.0,33.7,\r\n" + 
			"COOK,28.8,31.7,\r\n" + 
			"CRANBERRY,32.0,33.7,\r\n" + 
			"CULLODEN,30.7,32.2,\r\n" + 
			"CUNNINGHAM,31.5,33.0,\r\n" + 
			"DEANS,33.5,35.5,\r\n" + 
			"DLTAPLNT,32.7,34.6,\r\n" + 
			"DOOMS,31.7,33.2,\r\n" + 
			"DOUBS,33.2,35.0,\r\n" + 
			"DUMONT,28.8,31.8,\r\n" + 
			"ELMONT,33.3,35.0,\r\n" + 
			"ELROY,33.2,35.2,\r\n" + 
			"ELROYPL,33.2,35.2,\r\n" + 
			"EWINDSOR,33.5,35.5,\r\n" + 
			"FENTRESS,33.2,34.8,\r\n" + 
			"FLATLICK,30.7,32.2,\r\n" + 
			"FLUVANNA,31.4,33.0,\r\n" + 
			"FORTMARTIN,31.7,33.4,\r\n" + 
			"FREEDOM2,32.7,34.6,\r\n" + 
			"FRONT ROYAL,32.6,34.4,\r\n" + 
			"GAVIN,30.6,32.2,\r\n" + 
			"GOOSECRE,33.2,35.0,\r\n" + 
			"GREENLAND GAP,32.2,33.9,\r\n" + 
			"GRNSVIL,32.4,34.0,\r\n" + 
			"HANGING ROCK,30.5,32.1,\r\n" + 
			"HARRISON,31.6,33.3,\r\n" + 
			"HATFIELD,31.8,33.6,\r\n" + 
			"HOPATCON,33.8,35.7,\r\n" + 
			"HOPECREEK,32.8,34.7,\r\n" + 
			"HOSENSACK,33.1,35.0,\r\n" + 
			"HUNTERSTOWN,32.6,34.4,\r\n" + 
			"JACKSONS FERRY,31.2,32.7,\r\n" + 
			"JEFFERSON,28.7,31.6,\r\n" + 
			"JOSHUA FALLS,31.5,33.1,\r\n" + 
			"JUNIATA,32.6,34.4,\r\n" + 
			"KAMMER,31.4,33.0,\r\n" + 
			"KEENEY,32.9,34.7,\r\n" + 
			"KEYSTONE,31.9,33.7,\r\n" + 
			"LACKAWAN,33.2,35.1,\r\n" + 
			"LADYSMITH,33.1,34.9,\r\n" + 
			"LAUSCHTO,32.9,34.8,\r\n" + 
			"LEXINGTON,31.6,33.1,\r\n" + 
			"LIMERICK,33.1,35.0,\r\n" + 
			"LOUDOUN,33.2,35.0,\r\n" + 
			"MALISZEWSKI,31.0,32.7,\r\n" + 
			"MARQUIS,30.8,32.1,\r\n" + 
			"MARYSVILLE,30.8,32.6,\r\n" + 
			"MEADOWBROOK,32.6,34.3,\r\n" + 
			"MIDLOTHIAN,33.0,34.6,\r\n" + 
			"MORRISVILLE,33.1,34.8,\r\n" + 
			"MOSBY,33.2,35.0,\r\n" + 
			"MOUNTAINEER,30.7,32.2,\r\n" + 
			"MT STORM,32.2,33.8,\r\n" + 
			"NAGELAEP,30.7,32.3,\r\n" + 
			"NEWFREEDOM,33.0,34.9,\r\n" + 
			"NORTH ANNA,33.0,34.7,\r\n" + 
			"NORTH LONGVIEW,31.7,33.4,\r\n" + 
			"NORTH PROCTORVILLE,30.6,32.1,\r\n" + 
			"ORCHARD,32.9,34.8,\r\n" + 
			"OX,33.2,35.0,\r\n" + 
			"PEACHBOTTOM,32.7,34.6,\r\n" + 
			"PLEASANT VIEW,33.2,35.0,\r\n" + 
			"PLEASANTS,31.1,32.7,\r\n" + 
			"POSSUM POINT,33.3,35.0,\r\n" + 
			"PRUNTYTOWN,31.8,33.4,\r\n" + 
			"RAWLINGS,32.6,34.2,\r\n" + 
			"REDLION,32.9,34.7,\r\n" + 
			"REYNOLD2,28.4,31.7,\r\n" + 
			"RHODESLN,32.0,33.7,\r\n" + 
			"ROCKPORT,28.1,31.2,\r\n" + 
			"ROCKSPRINGS,32.8,34.6,\r\n" + 
			"ROGERSRD,32.4,34.0,\r\n" + 
			"RONCO,31.8,33.5,\r\n" + 
			"ROSELAND,33.9,35.9,\r\n" + 
			"SALEM,32.8,34.6,\r\n" + 
			"SEPTA,33.0,34.6,\r\n" + 
			"SHICKSHI,32.7,34.6,\r\n" + 
			"SKFFSCRK,34.2,36.0,\r\n" + 
			"SMITHBURG,33.6,35.5,\r\n" + 
			"SORENSON,28.6,32.1,\r\n" + 
			"SOUTH CANTON,31.8,33.5,\r\n" + 
			"SOUTHBEND,31.9,33.7,\r\n" + 
			"SPOTSLV,33.0,34.8,\r\n" + 
			"STEELCITY,33.0,35.0,\r\n" + 
			"SUFFOLK,33.1,34.7,\r\n" + 
			"SULLIVAN-AEP,27.9,31.2,\r\n" + 
			"SUNBURY,32.6,34.4,\r\n" + 
			"SURRY,33.0,34.6,\r\n" + 
			"SUSQUEHANNA,32.6,34.5,\r\n" + 
			"TMI,32.7,34.6,\r\n" + 
			"VALLEY,31.8,33.3,\r\n" + 
			"VASSELL,31.1,32.8,\r\n" + 
			"WAUGHCHAPEL,33.5,35.3,\r\n" + 
			"WESCOSVL,33.0,34.9,\r\n" + 
			"WHITPAIN,33.2,35.1,\r\n" + 
			"WMORELND,32.0,33.7,\r\n" + 
			"WYLIERIDGE,31.9,33.7,\r\n" + 
			"WYOMING,30.9,32.4,\r\n" + 
			"YADKIN,33.2,34.8,\r\n" + 
			"YUKON,32.0,33.7,\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"PJM Transfer Interface Information (MW)\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			",Interface\r\n" + 
			",Actual flow\r\n" + 
			",Warning Level\r\n" + 
			",Transfer Limit\r\n" + 
			"\r\n" + 
			"50045005,2407,3447,4028,\r\n" + 
			"AEP/DOM,2188,3238,3409,\r\n" + 
			"APSOUTH,3544,4144,4362,\r\n" + 
			"BED-BLA,1522,1808,1903,\r\n" + 
			"CENTRAL,790,3799,3999,\r\n" + 
			"CLVLND,1401,2619,2757,\r\n" + 
			"COMED,-3084,2430,2558,\r\n" + 
			"EAST,3792,6047,6366,\r\n" + 
			"WEST,3264,5341,5943,\r\n" + 
			"Name does not indicate direction.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"PJM Instantaneous Dispatch Rates\r\n" + 
			"\r\n" + 
			"PJM Dispatch Rates\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"PJM Instantaneous Load (MW)\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			",Area\r\n" + 
			",Instantaneous Load\r\n" + 
			"\r\n" + 
			"PJM RTO,100392,\r\n" + 
			"PJM MID ATLANTIC REGION,35829,\r\n" + 
			"PJM SOUTHERN REGION,13175,\r\n" + 
			"PJM WESTERN REGION,51389,\r\n" + 
			"Loads are calculated from raw telemetry data and are approximate.\r\n" + 
			"The displayed values are NOT official PJM Loads.\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"Current PJM Transmission Limits\r\n" + 
			"Contingency EBEND 345 KV EBEND 1579 CB EBEND 345 KV EBEND 1583 CB LINE 345 KV EBEND-TERMINAL 4516 TERMINAL 345 KV TERMINAL 1301 CB TERMINAL 345 KV TERMINAL 1307 CB Monitor TANNERSC-MIAMIFOR 4504 B 345 KV -> Redispatch\r\n" + 
			"\r\n" + 
			"";
}
