package gosigma.etl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

		try (Reader reader = new FileReader(targetFile)) {
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
			
		}
		return "NULL";
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(NYISO_5_Minute_Price.class);
	}

	public static void main(String[] args) {
		FeedBase feed = new NYISO_5_Minute_Price();
		feed.doEtl(args);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("NYISO_5_Minute_Price []," + super.toString()).replaceAll(",", ",\n");
	}

}
