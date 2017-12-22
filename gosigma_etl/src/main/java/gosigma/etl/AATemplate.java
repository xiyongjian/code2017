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

// template class as copy book
public class AATemplate extends FeedBase {
	public String _urlPattern = null;

	public String _dateFlagString = null;
	public String _dateFlagArgs = null;

	public String _flagString = null;
	public String _flagArgs = null;

	public String _flag2String = null;
	public String _flag2Args = null;
	public String _cols2 = null;
	public String _table2 = null;

	public String _flag3String = null;
	public String _flag3Args = null;
	public String _cols3 = null;
	public String _table3 = null;

	public AATemplate() {
		super("AATemplate");
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_urlPattern = getProperty(prop, _feedId + ".url.pattern", true);

		_dateFlagString = getProperty(prop, _feedId + ".date.flag.string", true);
		_dateFlagArgs = getProperty(prop, _feedId + ".date.flag.args", true);

		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		_flag2String = getProperty(prop, _feedId + ".flag2.string", true);
		_flag2Args = getProperty(prop, _feedId + ".flag2.args", true);
		_cols2 = getProperty(prop, _feedId + ".cols2", true);
		_table2 = getProperty(prop, _feedId + ".table2", true);

		_flag3String = getProperty(prop, _feedId + ".flag3.string", true);
		_flag3Args = getProperty(prop, _feedId + ".flag3.args", true);
		_cols3 = getProperty(prop, _feedId + ".cols3", true);
		_table3 = getProperty(prop, _feedId + ".table3", true);

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

			log.info("extract date from : " + this._dateFlagString + " and " + this._dateFlagArgs);
			// List<CSVRecord> recFounds = Utils.findCsvRecords(records, dateStart, 1, 1);
			recFounds = Utils.findCsvRecords(records, this._dateFlagString, this._dateFlagArgs);
			log.info("found date record : " + Utils.recordString(recFounds.get(0)));

			log.info("find records : " + this._flagString);
			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			log.info("found records# : " + recFounds.size());

			String endPnodeID = Utils.regexAll("End of Day Ahead LMP Data");
			log.info("setup end of record : " + endPnodeID);

			String date = null;
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord record = recFounds.get(i);
				if (i == 0) { // first one get date
					date = record.get(0);
					sql = String.format("delete from %s where DDATE = \"%s\"", this._table, date);
					if (Utils.shouldDump(i))
						log.info("del sql " + i + " : " + sql);
					sqls.add(sql);
					//make key
					key = date;
				}
				if (Utils.doubleMatch(record.get(0), endPnodeID)) {
					log.info("end of record matched.");
					break;
				}
				sql = String.format("insert into %s (DDATE, DTYPE, %s) values (\"%s\", \"%s\", %s)",
						this._table, insCols, date, record.get(5), Utils.getValueStringFromCsvRecord(record, indexes));
				if (Utils.shouldDump(i))
					log.info("insert sql " + i + " : " + sql);
				sqls.add(sql);
			}
		}

		return key;
	}

	@Override
	public Logger getLogger() {
		return UtilsLog.getLogger(AATemplate.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new AATemplate();
		feed.doEtl(args);
	}

}
