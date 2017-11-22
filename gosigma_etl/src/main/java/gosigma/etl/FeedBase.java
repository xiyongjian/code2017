package gosigma.etl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class FeedBase {
	public Logger log = null;
	public String _logDir = null;
	
	public String _feedId = null;
	public String _dataDir = null;
	public String _feedDir = null;
	public String _feedUrl = null;
	public String _feedFile = null;
	public String _jdbcUrl = null;
	public String _cols = null;
	public String _table = null;
	public String _targetFile = null;
	public String _arcFile = null;
	public String _key = null;
	
	public FeedBase(String feedId) {
		_feedId = feedId;
	}
	
	// derived class must implement following method
	// parse target file, build sqls, and return key to rename targetFile
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		throw new EtlException("base parseFeed should never been called");
	}

	// derived class must implement following method
	public Logger getLogger() throws EtlException {
		throw new EtlException("Should implement getLogger in derived class");
	}


	public void initLog(String feedId, String logDir) throws JoranException, EtlException {
		System.out.println("initialize logback");
		System.setProperty("feed_id", feedId);
		System.setProperty("log_dir", logDir);
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		ci.autoConfig();
		// log = LoggerFactory.getLogger(FeedBase.class);
		log = this.getLogger();
	}

	public void init() throws JoranException, EtlException {
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
		log.info("dataDir - " + _dataDir);
		log.info("feedId - " + _feedId);
	}
	
	public void doEtl() throws EtlException {
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

	public void loadProperties() throws IOException, EtlException {
		log.info("#### load properties");
		InputStream inProp = Template.class.getClassLoader().getResourceAsStream("etl.properties");
		Properties prop = new Properties();
		prop.load(inProp);

		_jdbcUrl = prop.getProperty("jdbc.url");

		_feedDir = prop.getProperty(_feedId + ".dir");
		_feedUrl = prop.getProperty(_feedId + ".url");
		_feedFile = prop.getProperty(_feedId + ".file");
		_cols = prop.getProperty(_feedId + ".cols");
		_table = prop.getProperty(_feedId + ".table");

		log.info("jdbcUrl : " + _jdbcUrl);

		log.info("feedDir  : " + _feedDir);
		log.info("feedUrl  : " + _feedUrl);
		log.info("feedFile : " + _feedFile);
		log.info("cols : " + _cols);
		log.info("table : " + _table);

		if (_feedDir == null || _feedUrl == null || _feedFile == null || _jdbcUrl == null || _cols == null
				|| _table == null)
			throw new EtlException("feedDir/feedUrl/feedFile/jdbcUrl/cols/tables not setup in property file");

	}

	public void download(String feedUrl, String targetFile) throws IOException {
		log.info("#### fetching file");
		log.info("fetch file from : " + feedUrl);
		URL url = new URL(feedUrl);
		InputStream inUrl = url.openStream();

		log.info("save url to : " + targetFile);
		File file = new File(targetFile);
		file.getParentFile().mkdirs();
		FileUtils.copyInputStreamToFile(inUrl, file);
	}

	public void process() throws IOException, EtlException, SQLException {
		log.info("Entering...");

		loadProperties();

		log.info("#### construct targetFile name");
		String targetDir = null;
		{
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyyMM/dd");
			targetDir = _dataDir + File.separator + _feedDir + File.separator + dateFormat.format(date);
			dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
			String fileName = String.format(_feedFile, dateFormat.format(date));
			log.info("targetDir : " + targetDir);
			log.info("fileName  : " + fileName);

			_targetFile = targetDir + File.separator + fileName;
			log.info("targetFile  : " + _targetFile);
		}

		download(_feedUrl, _targetFile);

		List<String> sqls = new ArrayList<String>();
		{
			_key = parseFeed(_targetFile, sqls);

			_arcFile = targetDir + File.separator + String.format(_feedFile, _key);
			String sqlFile = _arcFile + ".sql";
			log.info("record sql to file : " + sqlFile);
			FileUtils.writeStringToFile(new File(sqlFile), String.join(";\n", sqls), (String) null);

			log.info("move file " + _targetFile + " to " + _arcFile);
			File target = new File(_targetFile);
			File arc = new File(_arcFile);
			boolean rsl = arc.delete();
			if (!rsl)
				log.error("failed to remove file : " + _arcFile);
			rsl = target.renameTo(arc);
			if (!rsl)
				log.error("failed to rename file " + _targetFile + " to " + _arcFile);
		}

		log.info("execute update sqls");
		executeSql(sqls);

		log.info("Leaving...");
	}

	public void executeSql(List<String> sqls) throws SQLException, EtlException {
		Connection conn = null;

		try {
			log.info("create connection : " + _jdbcUrl);
			conn = DriverManager.getConnection(_jdbcUrl);
			Statement statement = conn.createStatement();

			for (int i = 0; i < sqls.size(); ++i) {
				String sql = sqls.get(i);
				log.info("run sql : " + sql);
				try {
					int ret = statement.executeUpdate(sql);
					log.info("return : " + ret);
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
