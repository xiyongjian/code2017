package gosigma.etl;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

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
		// TODO Auto-generated method stub
		return null;
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
