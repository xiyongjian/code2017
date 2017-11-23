package gosigma.etl_00;

import static gosigma.etl_00.LogUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class IESO_PUB_RealtimeConstTotals {

	public static String _feedId = "IESO_PUB_RealtimeConstTotals";
	public static String _dataDir = null;
	public static String _feedDir = null;
	public static String _feedUrl = null;
	public static String _feedFile = null;
	public static String _jdbcUrl = null;
	public static String _cols = null;
	public static String _table = null;
	public static String _targetFile = null;
	public static String _arcFile = null;
	public static String _key = null;
	
	// public static final Logger log =
	// LoggerFactory.getLogger(IESO_PUB_RealtimeConstTotals.class);

	public static void init() throws JoranException {
		// output system information
		String userDir = System.getProperty("user.dir");
		System.out.println("[current dir] - " + userDir);
		String classPathStr = System.getProperty("java.class.path");
		System.out.println("[classpath] - " + classPathStr.replaceAll(";", "\n"));

		String logDir = System.getProperty("log_dir");
		if (logDir == null)
			logDir = userDir + File.separator + "logs";
		System.out.println("init log, logDir - " + logDir);
		initLog(_feedId, logDir);

		_dataDir = System.getProperty("data_dir");
		if (_dataDir == null)
			_dataDir = userDir + File.separator + "data";
		log().info("dataDir - " + _dataDir);
		log().info("feedId - " + _feedId);
	}

	public static void main(String[] args) {
		try {
			doEtl();
		} catch (EtlException e) {
			log().error("etl error", e);
		}
	}

	public static void doEtl() throws EtlException {
		try {
			init();
			process();
		} catch (FileNotFoundException e) {
			throw new EtlException("file not found", e);
		} catch (IOException e) {
			throw new EtlException("io issue", e);
		} catch (JoranException e) {
			throw new EtlException("Joran(logback.xml parsing) issue", e);
		} catch (SQLException e) {
			throw new EtlException("sql error", e);
		}
	}

	public static void loadProperties() throws IOException, EtlException {
		log().info("#### load properties");
		InputStream inProp = Template.class.getClassLoader().getResourceAsStream("etl.properties");
		Properties prop = new Properties();
		prop.load(inProp);

		_jdbcUrl = prop.getProperty("jdbc.url");

		_feedDir = prop.getProperty(_feedId + ".dir");
		_feedUrl = prop.getProperty(_feedId + ".url");
		_feedFile = prop.getProperty(_feedId + ".file");
		_cols = prop.getProperty(_feedId + ".cols");
		_table = prop.getProperty(_feedId + ".table");

		log().info("jdbcUrl : " + _jdbcUrl);

		log().info("feedDir  : " + _feedDir);
		log().info("feedUrl  : " + _feedUrl);
		log().info("feedFile : " + _feedFile);
		log().info("cols : " + _cols);
		log().info("table : " + _table);

		if (_feedDir == null || _feedUrl == null || _feedFile == null || _jdbcUrl == null || _cols == null
				|| _table == null)
			throw new EtlException("feedDir/feedUrl/feedFile/jdbcUrl/cols/tables not setup in property file");

	}

	public static void download(String feedUrl, String targetFile) throws IOException {
		log().info("#### fetching file");
		log().info("fetch file from : " + feedUrl);
		URL url = new URL(feedUrl);
		InputStream inUrl = url.openStream();

		log().info("save url to : " + targetFile);
		File file = new File(targetFile);
		file.getParentFile().mkdirs();
		FileUtils.copyInputStreamToFile(inUrl, file);
	}

	// parse target file, build sqls, and return key to rename targetFile
	public static String parseFeed(String targetFile, List<String> sqls) throws IOException, EtlException {
		log().info("#### parse csv file : " + targetFile);
		Reader reader = new FileReader(targetFile);
		Pattern regex = Pattern.compile("CREATED.*FOR (\\d{4}/\\d{2}/\\d{2})");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		String date = null;
		for (CSVRecord record : records) {
			Matcher m = regex.matcher(record.get(0));
			if (m.find()) {
				log().info("record : " + record.toString());
				log().info("Found match: " + m.group(0));
				log().info("Found date: " + m.group(1));
				date = m.group(1);
				break;
			}
		}
		if (date == null)
			throw new EtlException("can't find date in file");

		log().info("build sqls and key");
		// List<String> sqls = new ArrayList<String>();
		String key = null;
		for (CSVRecord record : records) {
			log().info("record, # of column : " + record.size() + " : " + record.toString());
			String values = "\"" + date + "\"";
			if (record.size() > 9) {
				for (int i = 0; i < 9; ++i)
					values += ", \"" + record.get(i).trim() + "\"";

				key = date + StringUtils.leftPad(record.get(0).trim(), 2, "0")
						+ StringUtils.leftPad(record.get(1).trim(), 2, "0");
			}
			String sql = "insert into " + _table + " (" + _cols + ") values (" + values + ")";
			if (sqls.size() == 0) {
				String del = "delete from " + _table + " where DDATE = " + "\"" + date + "\"" + " AND HOUR = " + "\""
						+ record.get(0).trim() + "\"";
				sqls.add(del);
				log().info("sql/del : " + del);
			}
			log().info("sql : " + sql);
			sqls.add(sql);
		}

		reader.close();
		if (key == null)
			throw new EtlException("not record, can't build key, targetFile : " + targetFile);

		key = key.replaceAll("/", "");
		log().info("key : " + key);
		return key;
	}

	public static void process() throws IOException, EtlException, SQLException {
		log().info("Entering...");

		loadProperties();

		log().info("#### construct targetFile name");
		String targetDir = null;
		{
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyyMM/dd");
			targetDir = _dataDir + File.separator + _feedDir + File.separator + dateFormat.format(date);
			dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
			String fileName = String.format(_feedFile, dateFormat.format(date));
			log().info("targetDir : " + targetDir);
			log().info("fileName  : " + fileName);

			_targetFile = targetDir + File.separator + fileName;
			log().info("targetFile  : " + _targetFile);
		}

		download(_feedUrl, _targetFile);

		List<String> sqls = new ArrayList<String>();
		{
			_key = parseFeed(_targetFile, sqls);

			_arcFile = targetDir + File.separator + String.format(_feedFile, _key);
			String sqlFile = _arcFile + ".sql";
			log().info("record sql to file : " + sqlFile);
			FileUtils.writeStringToFile(new File(sqlFile), String.join(";\n", sqls), (String) null);

			log().info("move file " + _targetFile + " to " + _arcFile);
			File target = new File(_targetFile);
			File arc = new File(_arcFile);
			boolean rsl = arc.delete();
			if (!rsl)
				log().error("failed to remove file : " + _arcFile);
			rsl = target.renameTo(arc);
			if (!rsl)
				log().error("failed to rename file " + _targetFile + " to " + _arcFile);
		}

		log().info("execute update sqls");
		executeSql(sqls);

		log().info("Leaving...");
	}

	public static void executeSql(List<String> sqls) throws SQLException, EtlException {
		Connection conn = null;

		try {
			log().info("create connection : " + _jdbcUrl);
			conn = DriverManager.getConnection(_jdbcUrl);
			Statement statement = conn.createStatement();

			for (int i = 0; i < sqls.size(); ++i) {
				String sql = sqls.get(i);
				log().info("run sql : " + sql);
				try {
					int ret = statement.executeUpdate(sql);
					log().info("return : " + ret);
				} catch (SQLException e) {
					throw new EtlException("run [" + i + "] : " + sql, e);
				}
			}
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
