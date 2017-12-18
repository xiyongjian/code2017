package gosigma.etl;

import java.io.File;
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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PJM_DA_Prices extends FeedBase {
	public String _urlPattern = null;
	public String _flagString = null;
	public String _flagArgs = null;

	public PJM_DA_Prices() {
		super("PJM_DA_Prices");
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

			log.info("find records : " + this._flagString);
			List<CSVRecord> recFounds = Utils.findCsvRecords(records, this._flagString, this._flagArgs);
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
						this._table, insCols, date, record.get(5), Utils.getValueFromCsvRecord(record, indexes));
				if (Utils.shouldDump(i))
					log.info("insert sql " + i + " : " + sql);
				sqls.add(sql);
			}
		}

		return key;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(PJM_DA_Prices.class);
	}

	@Override
	public void loadProperties(Properties prop) throws IOException, EtlException {
		super.loadProperties(prop);

		_urlPattern = getProperty(prop, _feedId + ".url.pattern", true);
		_flagString = getProperty(prop, _feedId + ".flag.string", true);
		_flagArgs = getProperty(prop, _feedId + ".flag.args", true);
		
		log.info("setup url pattern");
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date); // assigns calendar to given date 
		calendar.add(Calendar.DATE, 1);	// plus one day for this feed
		date = calendar.getTime();
		log.info("  get +l date : " + date.toString());
		this._feedUrl = String.format(this._feedUrl, new SimpleDateFormat(_urlPattern).format(date));
		log.info("set feed url : " + this._feedUrl);
	}

	public static void main(String[] args) {
		FeedBase feed = new PJM_DA_Prices();
		feed.doEtl(args);
	}

	// for testing purpose, will comment out later.
	@Override
	public void download(String feedUrl, String targetFile) throws IOException {
		log.info("Enetering..., feedUrl : " + feedUrl + ", targetFile : " + targetFile);
		super.download(feedUrl, targetFile);

		//		String srcFilename = "e:\\tmp\\20171217-da.csv";
		//		log.info("source file : " + srcFilename);
		//		// Files.copy(new File(srcFilename).toPath(), new File(targetFile).toPath());
		//		File file = new File(targetFile);
		//		file.getParentFile().mkdirs();
		//		log.info("copy to : " + targetFile);
		//		FileUtils.copyFile(new File(srcFilename), file);

		log.info("Leaving...");
	}

}
