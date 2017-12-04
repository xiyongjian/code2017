package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NYISO_5_Minute_Price extends FeedBase {

	public NYISO_5_Minute_Price() {
		super("NYISO_5_Minute_Price");
	}

	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		log.info("#### parse csv file : " + targetFile);

		List<Integer> indexes = new ArrayList<>();
		String insCols = this.parseCols(this._cols, indexes);

		// "12/01/2017 02:30:00","WEST",61752,2.07,-0.04,0.00
		Pattern regDate = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4}) (\\d{2}):(\\d{2}):(\\d{2})");
		// DateFormat dateFormat = new SimpleDateFormat("yyyyMM/dd");
		String key = null;
		String sql = null;
		try (Reader reader = new FileReader(targetFile)) {
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
			for (CSVRecord record : records) {
				log.info("record : " + record.toString());
				if (record.size() < 6)
					continue;
				Matcher m = regDate.matcher(record.get(0));
				if (!m.matches()) {
					log.info("no date matches");
					continue;
				}

				String MM = m.group(1);
				String DD = m.group(2);
				String YYYY = m.group(3);
				int hour = Integer.parseInt(m.group(4));
				int minute = Integer.parseInt(m.group(5));
				int dinterval = minute / 5 + 1;
				String date = YYYY + "/" + MM + "/" + DD;

				if (key == null) {
					// key = (date + "_" + m.group(4) + m.group(5)).replaceAll("/", "");
					key = String.format("%8s_%02d%02d", date, hour, dinterval).replaceAll("/", "");
					String del = "delete from " + this._table + " where " + " ddate = " + "\"" + date + "\""
							+ " and hour = " + hour + " and dinterval = " + dinterval;
					log.info("sql/delete : " + del);
					sqls.add(del);
				}

				sql = "insert into " + this._table + " (ddate, hour, dinterval," + insCols + ") values (" + "\"" + date
						+ "\"," + hour + "," + dinterval;
				for (int i = 0; i < indexes.size(); ++i) {
					sql += ",\"" + record.get(indexes.get(i)) + "\"";
				}
				sql += ")";

				log.info("sql : " + sql);
				sqls.add(sql);
			}
		}

		if (key == null)
			throw new EtlException("not record, can't build key, targetFile : " + targetFile);

		log.info("key : " + key);
		return key;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(NYISO_5_Minute_Price.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new NYISO_5_Minute_Price();
		feed.doEtl(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("NYISO_5_Minute_Price []," + super.toString()).replaceAll(",", ",\n");
	}

}
