package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IESO_RealtimeConstTotals extends FeedBase {

	public IESO_RealtimeConstTotals(String feedId) {
		super(feedId);
	}

	@Override
	public Logger getLogger() throws EtlException {
		return LoggerFactory.getLogger(IESO_RealtimeConstTotals.class);
	}

	// parse target file, build sqls, and return key to rename targetFile
	@Override
	public String parseFeed(String targetFile, List<String> sqls) throws IOException, EtlException {
		log.info("#### parse csv file : " + targetFile);
		Reader reader = new FileReader(targetFile);
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
		String key = null;
		for (CSVRecord record : records) {
			log.info("record, # of column : " + record.size() + " : " + record.toString());
			String values = "\"" + date + "\"";
			if (record.size() > 9) {
				for (int i = 0; i < 9; ++i)
					values += ", \"" + record.get(i).trim() + "\"";

				key = date + '_' + StringUtils.leftPad(record.get(0).trim(), 2, "0")
						+ StringUtils.leftPad(record.get(1).trim(), 2, "0");
			}
			String sql = "insert into " + _table + " (" + _cols + ") values (" + values + ")";
			if (sqls.size() == 0) {
				String del = "delete from " + _table + " where DDATE = " + "\"" + date + "\"" + " AND HOUR = " + "\""
						+ record.get(0).trim() + "\"";
				sqls.add(del);
				log.info("sql/del : " + del);
			}
			log.info("sql : " + sql);
			sqls.add(sql);
		}

		reader.close();
		if (key == null)
			throw new EtlException("not record, can't build key, targetFile : " + targetFile);

		key = key.replaceAll("/", "");
		log.info("key : " + key);
		return key;
	}


	public static void main(String[] args) {
		FeedBase feed = new IESO_RealtimeConstTotals("IESO_PUB_RealtimeConstTotals");
		try {
			feed.doEtl(args);
		} catch (EtlException e) {
			feed.log.error("etl error", e);
			feed.log.error(feed.toString());
		}
	}

	@Override
	public String toString() {
		return ("IESO_RealtimeConstTotals [], " + super.toString()).replaceAll(",", ",\n");
	}
}
