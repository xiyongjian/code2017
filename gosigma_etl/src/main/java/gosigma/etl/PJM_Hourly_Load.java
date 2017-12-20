package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PJM_Hourly_Load extends FeedBase {
	public String _urlPattern = null;
	public String _flagString = null;
	public String _flagArgs = null;

	public PJM_Hourly_Load() {
		super("PJM_Hourly_Load");
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_urlPattern = getProperty(prop, _feedId + ".url.pattern", true);
		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		log.info("setup url pattern");
		Date date = new Date();
		DateKey dateKey = DateKey.build(new Date());
		// dateKey.addDays(1);
		log.info("  get +l date : " + dateKey.getDate().toString());
		this._feedUrl = String.format(this._feedUrl, dateKey.format(_urlPattern));
		log.info("set feed url : " + this._feedUrl);
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		log.info("Entering...  targetFile : " + targetFile);

		log.info("parse column string : " + _cols);
		List<Integer> indexes = new ArrayList<>();
		String insCols = this.parseCols(this._cols, indexes);

		String key = null;
		String sql = null;

		try (Reader reader = new FileReader(targetFile)) {
			List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();

			log.info("find records : " + this._flagString);
			List<CSVRecord> recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			log.info("found records# : " + recFounds.size());

			String date = null;
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord record = recFounds.get(i);
				if (i == 0) { // first one get date
					// parse 12/17/2017 to 20171217 (yyyymmdd)
					Pattern regex = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
					Matcher m = regex.matcher(record.get(0));
					if (!m.find())
						throw new EtlException("cant parse data from record 0, string : " + record.get(0));
					date = m.group(3) + m.group(1) + m.group(2);
					log.info("resemble date as : " + date);
					
					sql = String.format("delete from %s where DDATE = \"%s\"", this._table, date);
					if (Utils.shouldDump(i))
						log.info("del sql " + i + " : " + sql);
					sqls.add(sql);
					//make key
					key = date;
				}
				sql = String.format("insert into %s (DDATE, %s) values (\"%s\", %s)",
						this._table, insCols, date, Utils.getValueStringFromCsvRecord(record, indexes));
				if (Utils.shouldDump(i))
					log.info("insert sql " + i + " : " + sql);
				sqls.add(sql);
			}
		}

		return key;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(PJM_Hourly_Load.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new PJM_Hourly_Load();
		feed.doEtl(args);
	}

}
