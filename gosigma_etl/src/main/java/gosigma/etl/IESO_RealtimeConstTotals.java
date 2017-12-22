package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IESO_RealtimeConstTotals extends FeedBase {
	public String _dateFlagString = null;
	public String _dateFlagArgs = null;

	public String _flagString = null;
	public String _flagArgs = null;

	public IESO_RealtimeConstTotals() {
		super("IESO_PUB_RealtimeConstTotals");
	}

	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_dateFlagString = getProperty(prop, _feedId + ".date.flag.string", true);
		_dateFlagArgs = getProperty(prop, _feedId + ".date.flag.args", true);

		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);
	}

	@Override
	public Logger getLogger() {
		return UtilsLog.getLogger(IESO_RealtimeConstTotals.class);
	}

	// parse target file, build sqls, and return key to rename targetFile
	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws IOException, EtlException {
		log.info("Entering...  targetFile : " + targetFile);

		String key = null;
		String sql = null;
		try (Reader reader = new FileReader(targetFile)) {
			List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();
			List<CSVRecord> recFounds = null;

			String dateString = null;
			{
				log.info("extract date from : " + this._dateFlagString + " and " + this._dateFlagArgs);
				recFounds = Utils.findCsvRecords(records, this._dateFlagString, this._dateFlagArgs);
				log.info("found date record : " + Utils.recordString(recFounds.get(0)));
				if (recFounds.size() < 1)
					throw new EtlException("no date line found");

				CSVRecord record = recFounds.get(0);
				Pattern regex = Pattern.compile("CREATED.*FOR (\\d{4}/\\d{2}/\\d{2})");

				String recString = Utils.recordString(record);
				Matcher m = regex.matcher(recString);
				if (m.find()) {
					log.info("record : " + recString);
					log.info("Found match: " + m.group(0));
					log.info("Found date: " + m.group(1));
					// example : 2017/12/22
					String str = m.group(1);
					dateString = str.replaceAll("/", "");
					log.info("dateString : " + dateString);
				} else
					throw new EtlException("cant parse date");
			}

			log.info("parse column string : " + _cols);
			List<Integer> indexes = new ArrayList<>();
			String insCols = this.parseCols(this._cols, indexes);

			log.info("find records : " + this._flagString);
			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			log.info("found records# : " + recFounds.size());

			int hour, interval;
			for (int i = 0; i < recFounds.size(); ++i) {
				CSVRecord record = recFounds.get(i);
				hour = Integer.parseInt(record.get(0).trim());
				interval = Integer.parseInt(record.get(1).trim());

				if (i == 0) { // first one get dateString
					sql = String.format("delete from %s where DDATE = \"%s\" and HOUR = %d",
							this._table, dateString, hour);
					log.info("del sql " + i + " : " + sql);
					sqls.add(sql);
				}
				//make key
				key = String.format("%s_%02d%02d", dateString, hour, interval);

				sql = String.format("insert into %s (DDATE, %s) values (\"%s\", %s)",
						this._table, insCols, dateString, 
						Utils.getValueStringFromCsvRecord(record, indexes, true));
				log.info("insert sql " + i + " : " + sql);
				sqls.add(sql);
			}
			
			log.info("get key : " + key);

		}

		return key;
	}

	// old one for reference
	// @Override
	public String parseFeed_old(String targetFile, List<String> sqls) throws IOException, EtlException {
		log.info("#### parse csv file : " + targetFile);
		String key = null;
		// new style try-resource block
		try (Reader reader = new FileReader(targetFile)) {
			Pattern regex = Pattern.compile("CREATED.*FOR (\\d{4}/\\d{2}/\\d{2})");
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
			String date = null;
			for (CSVRecord record : records) {
				Matcher m = regex.matcher(record.get(0));
				if (m.find()) {
					log.info("record : " + record.toString());
					log.info("Found match: " + m.group(0));
					log.info("Found date: " + m.group(1));
					date = m.group(1);
					break;
				}
			}
			if (date == null)
				throw new EtlException("can't find date in file");

			log.info("build sqls and key");
			// List<String> sqls = new ArrayList<String>();
			for (CSVRecord record : records) {
				log.info("record, # of column : " + record.size() + " : " + record.toString());
				String values = "\"" + date + "\"";
				if (record.size() > 9) {
					for (int i = 0; i < 9; ++i)
						values += ", \"" + record.get(i).trim() + "\"";

					key = date + '_' + StringUtils.leftPad(record.get(0).trim(), 2, "0")
							+ StringUtils.leftPad(record.get(1).trim(), 2, "0");
					key = key.replaceAll("/", "");
				}
				String sql = "insert into " + _table + " (" + _cols + ") values (" + values + ")";
				if (sqls.size() == 0) {
					String del = "delete from " + _table + " where DDATE = " + "\"" + date + "\"" + " AND HOUR = "
							+ "\"" + record.get(0).trim() + "\"";
					sqls.add(del);
					log.info("sql/del : " + del);
				}
				log.info("sql : " + sql);
				sqls.add(sql);
			}
		}
		// reader.close();

		if (key == null)
			throw new EtlException("not record, can't build key, targetFile : " + targetFile);

		log.info("key : " + key);
		return key;
	}

	public static void main(String[] args) {
		FeedBase feed = new IESO_RealtimeConstTotals();
		feed.doEtl(args);
		// try {
		// feed.doEtl(args);
		// System.exit(0); // normal exit
		// } catch (EtlException e) {
		// feed.log.error("etl error", e);
		// feed.log.error(feed.toString());
		// System.exit(1); // something wrong, will record in cron log
		// }
	}

	@Override
	public String toString() {
		return ("IESO_RealtimeConstTotals [], " + super.toString()).replaceAll(",", ",\n");
	}
}
