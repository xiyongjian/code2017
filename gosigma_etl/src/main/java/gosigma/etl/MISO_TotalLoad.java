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

public class MISO_TotalLoad extends FeedBase {
	public String _flagString = null;
	public String _flagArgs = null;
	public String _flag2String = null;
	public String _flag2Args = null;
	public String _table2 = null;
	public String _cols2 = null;
	public String _flag3String = null;
	public String _flag3Args = null;
	public String _table3 = null;
	public String _cols3 = null;

	public MISO_TotalLoad() {
		super("MISO_TotalLoad");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);

		_flag2String = getProperty(prop, _feedId + ".flag2.string", true);
		_flag2Args = getProperty(prop, _feedId + ".flag2.args", true);
		_table2 = getProperty(prop, _feedId + ".table2", true);
		_cols2 = getProperty(prop, _feedId + ".cols2", true);

		_flag3String = getProperty(prop, _feedId + ".flag3.string", true);
		_flag3Args = getProperty(prop, _feedId + ".flag3.args", true);
		_table3 = getProperty(prop, _feedId + ".table3", true);
		_cols3 = getProperty(prop, _feedId + ".cols3", true);
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		Date pDate = this.getProcessingDate(); //this date as reference to decide the date
		log.info("Entering...  targetFile : " + targetFile + ", processing date : " + pDate.toString());

		DateKey dk = DateKey.build(pDate);
		int dayTotal = 24 * 12; // and use 1/3 2/3 as judge condition, 8 hour is enough

		String key = null;
		String sql = null;
		int dayRolling = 0;
		try (Reader reader = new FileReader(targetFile)) {
			log.info("---- process table 3 : " + this._table3);

			if (this._cDebug != true)
				Utils.setDumpFactor(30);
			List<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();
			List<CSVRecord> recFounds = Utils.findCsvRecords(records, this._flag3String, this._flag3Args);

			log.info("recFounds # " + recFounds.size() + ", key hour * 12 : " + dk.getKeyHour() * 12);
			log.info("processing time : " + pDate.toString() + ", # of records(table 3) : " + recFounds.size());
			if (recFounds.size() > dayTotal * 2 / 3 && dk.getKeyHour() < 8) {
				dayRolling = -1;
			} else if (recFounds.size() < dayTotal / 3 && dk.getKeyHour() > 16) {
				dayRolling = 1;
			} else
				log.info("process date and date key aligned");
			log.info("dayRolling : " + dayRolling);

			if (dayRolling != 0) {
				log.info("current date key : " + dk.getDate().toString());
				dk.addDays(dayRolling);
				dk.setKeyHour(24);
				log.info("adjust date key to : " + dk.getDate().toString());
			}

			{
				List<Integer> indexes = new ArrayList<>();
				String insCols = this.parseCols(this._cols3, indexes);

				for (int i = 0; i < recFounds.size(); ++i) {
					CSVRecord record = recFounds.get(i);
					if (i == 0) {
						sql = String.format("delete from %s where DDATE=\"%s\"", this._table3, dk.getKeyDate());
						sqls.add(sql);
						if (Utils.shouldDump(i))
							log.info("del sql " + i + " : " + sql);
					}

					// sample record : 00:05,72067
					String time = record.get(0);
					int hour = Integer.parseInt(time.substring(0, 2)) + 1;
					int interval = Integer.parseInt(time.substring(3, 5)) / 5 + 1;
					key = String.format("%s_%02d", dk.getKeyDate(), hour);
					sql = String.format("insert into %s (DDATE, HOUR, DINTERVAL, %s) values (\"%s\", %d, %d, %s)",
							this._table3, insCols, dk.getKeyDate(), hour, interval,
							Utils.getValueStringFromCsvRecord(record, indexes));

					sqls.add(sql);
					if (Utils.shouldDump(i))
						log.info("insert sql " + i + " : " + sql);
				}
				log.info("key : " + key);
			}

			log.info("---- process table 1 : " + this._table);
			recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
			{
				List<Integer> indexes = new ArrayList<>();
				String insCols = this.parseCols(this._cols, indexes);

				for (int i = 0; i < recFounds.size(); ++i) {
					CSVRecord record = recFounds.get(i);
					if (i == 0) {
						sql = String.format("delete from %s where DDATE = \"%s\"",
								this._table, dk.getKeyDate());
						sqls.add(sql);
						log.info("del sql " + i + " : " + sql);
					}
					// int hour = Integer.parseInt(record.get(0));
					sql = String.format("insert into %s (DDATE, %s) values (\"%s\", %s)",
							this._table, insCols, dk.getKeyDate(),
							Utils.getValueStringFromCsvRecord(record, indexes));
					sqls.add(sql);
					log.info("insert sql " + i + " : " + sql);
				}
			}

			log.info("---- process table 2 : " + this._table2);
			recFounds = Utils.findCsvRecords(records, this._flag2String, this._flag2Args);
			{
				List<Integer> indexes = new ArrayList<>();
				String insCols = this.parseCols(this._cols2, indexes);

				for (int i = 0; i < recFounds.size(); ++i) {
					CSVRecord record = recFounds.get(i);
					if (i == 0) {
						sql = String.format("delete from %s where DDATE = \"%s\"",
								this._table2, dk.getKeyDate());
						sqls.add(sql);
						log.info("del sql " + i + " : " + sql);
					}
					// int hour = Integer.parseInt(record.get(0));
					sql = String.format("insert into %s (DDATE, %s) values (\"%s\", %s)",
							this._table2, insCols, dk.getKeyDate(),
							Utils.getValueStringFromCsvRecord(record, indexes));
					sqls.add(sql);
					log.info("insert sql " + i + " : " + sql);
				}
			}
			
			if (dayRolling != 0)
				key = key + "_r" + dayRolling;
		}

		return key;
	}

	@Override
	public Logger getLogger() {
		return UtilsLog.getLogger(MISO_TotalLoad.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new MISO_TotalLoad();
		feed.doEtl(args);
	}

}
