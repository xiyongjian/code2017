package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;

public class MISO_DA_Prices extends FeedBase {
	public String _urlPattern = null;
	public String _dateFlagString = null;
	public String _dateFlagArgs = null;
	public String _flagString = null;
	public String _flagArgs = null;

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_urlPattern = getProperty(prop, _feedId + ".url.pattern", true);
		_dateFlagString = getProperty(prop, _feedId + ".date.flag.string", true);
		_dateFlagArgs = getProperty(prop, _feedId + ".date.flag.args", true);
		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		log.info("setup url pattern");
		Date date = new Date();
		DateKey dateKey = DateKey.build(new Date());
		log.info("  get +l date : " + dateKey.getDate().toString());
		this._feedUrl = String.format(this._feedUrl, dateKey.format(_urlPattern));
		log.info("set feed url : " + this._feedUrl);
	}

	public MISO_DA_Prices() {
		super("MISO_DA_Prices");
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		log.info("Entering...  targetFile : " + targetFile);

		if (this._cDebug != true) {
			int df = 100;
			Utils.setDumpFactor(df);
		}

		log.info("parse column string : " + _cols);
		List<Integer> indexes = new ArrayList<>();
		String insCols = this.parseCols(this._cols, indexes);

		String key = null;
		String sql = null;
		try (Reader reader = new FileReader(targetFile)) {
			List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();
			List<CSVRecord> recFounds = null;
			String str;

			log.info("extract date from : " + this._dateFlagString + " and " + this._dateFlagArgs);
			recFounds = Utils.findCsvRecords(records, this._dateFlagString, this._dateFlagArgs);
			log.info("found date record : " + Utils.recordString(recFounds.get(0)));
			// e.g : 12/22/2017
			str = recFounds.get(0).get(0);
			String date = str.substring(6, 10) + str.substring(0,2) + str.substring(3,5);
			key = date;
			log.info("key/date : " + key);

			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			log.info("found table records# : " + recFounds.size());
			
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord record = recFounds.get(i);
				if (i == 0) { // first one get date
					sql = String.format("delete from %s where DDATE = \"%s\"", this._table, date);
					if (Utils.shouldDump(i))
						log.info("del sql " + i + " : " + sql);
					sqls.add(sql);
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
		return UtilsLog.getLogger(MISO_DA_Prices.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new MISO_DA_Prices();
		feed.doEtl(args);
	}

}
