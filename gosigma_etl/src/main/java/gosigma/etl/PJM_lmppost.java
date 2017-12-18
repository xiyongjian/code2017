package gosigma.etl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PJM_lmppost extends FeedBase {
	public String _cols2 = null;
	public String _table2 = null;
	public String _cols3 = null;
	public String _table3 = null;

	public String _dateFlagString = null;
	public String _dateFlagArgs = null;
	public String _flagString = null;
	public String _flagArgs = null;
	public String _flag3String = null;
	public String _flag3Args = null;

	public PJM_lmppost() {
		super("PJM_lmppost");
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(NYISO_5_Minute_Price.class);
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		log.info("Entering... targetFile : " + targetFile);
		log.info("jsoup parse, to html doc");
		Document doc = Jsoup.parse(new File(targetFile), "UTF-8");

		log.info("html doc -> csv string");
		String csvString = new Utils.Ele2Csv().proc(doc.select(":root").get(0));
		log.info("csv string length : " + csvString.length());

		log.info("csv string to csv records list");
		StringReader in = new StringReader(csvString);
		List<CSVRecord> records = CSVFormat.EXCEL.parse(in).getRecords();
		log.info("csv records # : " + records.size());
		if (this._cDebug) // check debug condition
			for (CSVRecord r : records)
				log.info("record : " + Utils.recordString(r));

		log.info("extract date from : " + this._dateFlagString + " and " + this._dateFlagArgs);
		// List<CSVRecord> recFounds = Utils.findCsvRecords(records, dateStart, 1, 1);
		List<CSVRecord> recFounds = Utils.findCsvRecords(records, this._dateFlagString, this._dateFlagArgs);

		log.info("found record # : " + recFounds.size());
		String dateString = recFounds.get(0).get(0);
		log.info("dateString : " + dateString);

		Date date = null;
		int year, month, day, hour, minute;
		try {
			Locale locale = new Locale("en", "CA");
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy", dateFormatSymbols);
			date = df.parse(dateString);

			Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
			calendar.setTime(date); // assigns calendar to given date 
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH); // gets month number, NOTE this is zero based!
			day = calendar.get(Calendar.DAY_OF_MONTH);
			hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
			// calendar.get(Calendar.HOUR);        // gets hour in 12h format
			minute = calendar.get(Calendar.MINUTE);

			log.info(String.format("data yyyymmdd-hhmm : %04d%02d%02d-%02d%02d", year, month, day, hour, minute));
		} catch (ParseException e) {
			throw new EtlException("parsing date string", e);
		}

		int interval = minute / 5 + 1;
		String key = String.format("%04d%02d%02d_%02d%02d", year, month, day, hour, interval);
		log.info("key : " + key);
		String sDate = String.format("%04d/%02d/%02d", year, month, day);

		//PJM_lmppost.table=PJM_5_minute_Prices
		//PJM_lmppost.cols=Name,Type,Price
		//
		//PJM_lmppost.table2=PJM_Hourly_Price
		//PJM_lmppost.cols2=Name,Type,Price
		//
		{
			// String flagString = "Aggregate Locational Marginal Prices \\(LMP\\)";
			log.info("sql to table : " + this._table + ", search : " + this._flagString);
			log.info("sql to table2 : " + this._table2 + ", same search key");
			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			String sql = null;
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord r = recFounds.get(i);
				if (i == 0) {
					sql = String.format("delete from %s where ddate = \"%s\" and hour = %d and dinterval = %d",
							_table, sDate, hour, interval);
					log.info("delete sql : " + sql);
					sqls.add(sql);

					sql = String.format("delete from %s where ddate = \"%s\" and hour = %d",
							_table2, sDate, hour);
					log.info("delete sql : " + sql);
					sqls.add(sql);
				}
				sql = String.format(
						"insert into %s (DDATE, HOUR, DINTERVAL, %s) values (\"%s\", %d, %d, \"%s\", \"%s\", \"%s\")",
						_table, _cols, sDate, hour, interval, r.get(0), r.get(1), r.get(2));
				log.info("insert sql : " + sql);
				sqls.add(sql);

				sql = String.format(
						"insert into %s (DDATE, HOUR, %s) values (\"%s\", %d, \"%s\", \"%s\", \"%s\")",
						_table2, _cols2, sDate, hour, r.get(0), r.get(1), r.get(3));
				log.info("insert sql : " + sql);
				sqls.add(sql);
			}
		}

		//PJM_lmppost.table3=PJM_5_Minute_Actual_Load
		//PJM_lmppost.cols3=Area,Load
		log.info("sql to table3 : " + this._table3);
		{
			// String flagString = "PJM Instantaneous Load \\(MW\\)";
			log.info("sql to table3 : " + this._table3 + ", search : " + this._flag3String);
			recFounds = Utils.findCsvRecords(records, this._flag3String, this._flag3Args);
			String sql = null;
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord r = recFounds.get(i);
				if (i == 0) {
					sql = String.format("delete from %s where ddate = \"%s\" and hour = %d and dinterval = %d",
							_table3, sDate, hour, interval);
					log.info("delete sql : " + sql);
					sqls.add(sql);

				}
				sql = String.format(
						"insert into %s (DDATE, HOUR, DINTERVAL, %s) values (\"%s\", %d, %d, \"%s\", \"%s\")",
						_table3, _cols3, sDate, hour, interval, r.get(0), r.get(1));
				log.info("insert sql : " + sql);
				sqls.add(sql);
			}
		}

		return key;
	}

	//	// for testing purpose, will comment out later.
	//	@Override
	//	public void download(String feedUrl, String targetFile) throws IOException {
	//		log.info("Enetering..., feedUrl : " + feedUrl + ", targetFile : " + targetFile);
	//
	//		String srcFilename = "e:\\tmp\\LMP Oasis posting.html";
	//		log.info("source file : " + srcFilename);
	//		// Files.copy(new File(srcFilename).toPath(), new File(targetFile).toPath());
	//		File file = new File(targetFile);
	//		file.getParentFile().mkdirs();
	//		FileUtils.copyFile(new File(srcFilename), file);
	//
	//		log.info("Leaving...");
	//	}

	public static void main(String[] args) {
		FeedBase feed = new PJM_lmppost();
		feed.doEtl(args);
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		log.info("Entering...");
		super.loadProperties(prop);
		_cols2 = prop.getProperty(_feedId + ".cols2");
		_table2 = prop.getProperty(_feedId + ".table2");
		_cols3 = prop.getProperty(_feedId + ".cols3");
		_table3 = prop.getProperty(_feedId + ".table3");
		log.info("cols2 : " + _cols2);
		log.info("table2 : " + _table2);
		log.info("cols3 : " + _cols3);
		log.info("table3 : " + _table3);

		if (_cols2 == null || _table2 == null || _cols3 == null || _table3 == null)
			throw new EtlException("cols/tables 2/3 not setup in property file");

		_dateFlagString = getProperty(prop, _feedId + ".date.flag.string", true);
		_dateFlagArgs = getProperty(prop, _feedId + ".date.flag.args", true);

		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		_flag3String = getProperty(prop, _feedId + ".flag3.string", true);
		_flag3Args = getProperty(prop, _feedId + ".flag3.args", true);

		log.info("Leaving...");
	}

	@Override
	public String toString() {
		return ("PJM_lmppost [_cols2=" + _cols2 + ", _table2=" + _table2 + ", _cols3=" + _cols3 + ", _table3=" + _table3
				+ ", _dateFlagString=" + _dateFlagString + ", _dateFlagArgs=" + _dateFlagArgs + ", _flagString="
				+ _flagString + ", _flagArgs=" + _flagArgs + ", _flag3String=" + _flag3String + ", _flag3Args="
				+ _flag3Args + "]"
				+ super.toString()).replaceAll(",",  ",\n");
	}
}
