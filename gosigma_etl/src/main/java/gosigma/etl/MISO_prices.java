package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MISO_prices extends FeedBase {
	public String _dateFlagString = null;
	public String _dateFlagArgs = null;
	public String _flagString = null;
	public String _flagArgs = null;
	public String _table2 = null;
	public String _cols2 = null;

	public MISO_prices() {
		super("MISO_prices");
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_dateFlagString = getProperty(prop, _feedId + ".date.flag.string", true);
		_dateFlagArgs = getProperty(prop, _feedId + ".date.flag.args", true);

		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		_table2 = getProperty(prop, _feedId + ".table2", true);
		_cols2 = getProperty(prop, _feedId + ".cols2", true);
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		log.info("Entering...  targetFile : " + targetFile);

		if (this._cDebug != true) {
			int df = 30;
			Utils.setDumpFactor(df);
		}

		log.info("parse column string : " + _cols);
		List<Integer> indexes = new ArrayList<>();
		String insCols = this.parseCols(this._cols, indexes);

		log.info("parse column(2) string : " + _cols2);
		List<Integer> indexes2 = new ArrayList<>();
		String insCols2 = this.parseCols(this._cols2, indexes2);

		String key = null;
		String sql = null;
		try (Reader reader = new FileReader(targetFile)) {
			List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();

			log.info("finding date, hour, interval");
			List<CSVRecord> recFounds = Utils.findCsvRecords(records, this._dateFlagString, this._dateFlagArgs);
			if (recFounds.isEmpty())
				throw new EtlException("Can't find date flag to parse");
			String dateStr = null;
			int hour = 0;
			int interval = 0;
			{
				String line = Utils.recordString(recFounds.get(0));
				// "RefId=18-Dec-2017 12:20";
				Pattern regex = Pattern.compile("RefId=((\\d{2})-(\\w{3})-(\\d{4}) (\\d{2}):(\\d{2}))");
				Matcher m = regex.matcher(line);
				if (!m.find())
					throw new EtlException("Can't parse data : " + line);

				DateKey dateKey = null;
				try {
					dateKey = DateKey.build(m.group(1), "dd-MMM-yyyy HH:mm");
				} catch (ParseException e) {
					throw new EtlException("can't parse data string : " + m.group(1));
				}
				
				dateStr = dateKey.getKeyDate();
				hour = dateKey.getKeyHour();
				interval = dateKey.getKeyInterval();

				//				Locale locale = new Locale("en", "CA");
				//				DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
				//				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm", dateFormatSymbols);
				//				Date date = null;
				//				try {
				//					date = df.parse(m.group(1));
				//				} catch (ParseException e) {
				//					throw new EtlException("can't parse data string : " + m.group(1));
				//				}

				//				Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
				//				calendar.setTime(date); // assigns calendar to given date 
				//				int year = calendar.get(Calendar.YEAR);
				//				int month = calendar.get(Calendar.MONTH) + 1; // gets month number, NOTE this is zero based!
				//				int day = calendar.get(Calendar.DAY_OF_MONTH);
				//				dateStr = String.format("%04d%02d%02d", year, month, day);
				//				log.info("date string : " + dateStr);

				//				hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
				//				// calendar.get(Calendar.HOUR);        // gets hour in 12h format
				//				log.info("hour : " + hour);
				//
				//				int minute = calendar.get(Calendar.MINUTE);
				//				interval = minute / 5 + 1;
				//				log.info("interval : " + interval);
			}

			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord record = recFounds.get(i);
				if (i == 0) { // first, delete old ones
					sql = String.format("delete from %s where DDATE=\"%s\" and HOUR=%d and DINTERVAL=%d",
							this._table, dateStr, hour, interval);
					sqls.add(sql);
					if (Utils.shouldDump(i))
						log.info("del sql " + i + " : " + sql);

					sql = String.format("delete from %s where DDATE=\"%s\" and HOUR=%d",
							this._table2, dateStr, hour);
					sqls.add(sql);
					if (Utils.shouldDump(i))
						log.info("del sql " + i + " : " + sql);
				}

				sql = String.format("insert into %s (DDATE, HOUR, DINTERVAL, %s) values (\"%s\", %d,  %d, %s)",
						this._table, insCols, dateStr, hour, interval,
						Utils.getValueStringFromCsvRecord(record, indexes));
				sqls.add(sql);
				if (Utils.shouldDump(i))
					log.info("insert sql " + i + " : " + sql);

				sql = String.format("insert into %s (DDATE, HOUR, %s) values (\"%s\", %d, %s)",
						this._table2, insCols2, dateStr, hour,
						Utils.getValueStringFromCsvRecord(record, indexes2));
				sqls.add(sql);
				if (Utils.shouldDump(i))
					log.info("insert sql " + i + " : " + sql);
			}

			key = String.format("%s-%02d%02d", dateStr, hour, interval);
		}

		return key;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(MISO_prices.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new MISO_prices();
		feed.doEtl(args);
	}

}
