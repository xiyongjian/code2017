package gosigma.etl_00;

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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class FeedBase {
	public Logger log = null;
	public String _logDir = null;
	public boolean _logToFile = true;

	public String _jdbcUrl = null;
	public String _jdbcUser = null;
	public String _jdbcPassword = null;

	public String _feedId = null;
	public String _dataDir = null;
	public String _feedDir = null;
	public String _feedUrl = null;
	public String _feedFile = null;
	public String _cols = null;
	public String _table = null;
	public String _targetFile = null;
	public String _arcFile = null;
	public String _key = null;

	public FeedBase(String feedId) {
		_feedId = feedId;
	}

	@Override
	public String toString() {
		return "FeedBase [log=" + log + ", _logDir=" + _logDir + ", _jdbcUrl=" + _jdbcUrl + ", _jdbcUser=" + _jdbcUser
				+ ", _jdbcPassword=" + _jdbcPassword + ", _feedId=" + _feedId + ", _dataDir=" + _dataDir + ", _feedDir="
				+ _feedDir + ", _feedUrl=" + _feedUrl + ", _feedFile=" + _feedFile + ", _cols=" + _cols + ", _table="
				+ _table + ", _targetFile=" + _targetFile + ", _arcFile=" + _arcFile + ", _key=" + _key + "]";
	}

	// derived class must implement following method
	// parse target file, build sqls, and return key to rename targetFile
	public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException {
		throw new EtlException("base parseFeed should never been called");
	}

	// derived class must implement following method
	public Logger getLogger() throws EtlException {
		// log = LoggerFactory.getLogger(<derived>.class);
		throw new EtlException("Should implement getLogger in derived class");
	}

	public void init() throws JoranException, EtlException, IOException {
		init(null, null);
	}

	public void init(String dataDir, String logDir) throws JoranException, EtlException, IOException {
		System.out.println("FeedBase init()");

		String userDir = System.getProperty("user.dir");
		System.out.println("[current dir] - " + userDir);
		if (logDir != null)
			_logDir = logDir;
		else
			_logDir = userDir + File.separator + "logs";
		System.out.println("initialize logback");
		System.setProperty("feed_id", _feedId);
		System.setProperty("log_dir", _logDir);
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		ci.autoConfig();
		log = this.getLogger();

		log.info("feedId : " + _feedId);
		log.info("[current dir] : " + userDir);
		String classPathStr = System.getProperty("java.class.path");
		log.info("[classpath] : " + classPathStr.replaceAll(";", "\n"));
		log.info("logDir : " + _logDir);

		if (dataDir != null)
			_dataDir = dataDir;
		else
			_dataDir = userDir + File.separator + "data";
		log.info("dataDir - " + _dataDir);

		this.loadProperties();
	}

	public void doEtl() throws EtlException {
		try {
			init();
			// loadProperties();
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
		InputStream inProp = FeedBase.class.getClassLoader().getResourceAsStream("etl.properties");
		Properties prop = new Properties();
		prop.load(inProp);

		_jdbcUrl = prop.getProperty("jdbc.url");
		_jdbcUser = prop.getProperty("jdbc.user");
		_jdbcPassword = prop.getProperty("jdbc.password");

		_feedDir = prop.getProperty(_feedId + ".dir");
		_feedUrl = prop.getProperty(_feedId + ".url");
		_feedFile = prop.getProperty(_feedId + ".file");
		_cols = prop.getProperty(_feedId + ".cols");
		_table = prop.getProperty(_feedId + ".table");

		log.info("jdbcUrl : " + _jdbcUrl);
		log.info("jdbcUser : " + _jdbcUser);
		log.info("jdbcPassword : " + _jdbcPassword.length());

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

		log.info("#### construct targetFile name");
		String targetDir = null;
		{
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyyMM/dd");
			targetDir = _dataDir + File.separator + _feedDir + File.separator + dateFormat.format(date);
			dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
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

			//			log.info("move file " + _targetFile + " to " + _arcFile);
			//			File target = new File(_targetFile);
			//			File arc = new File(_arcFile);
			//			boolean rsl = arc.delete();
			//			log.info("remove file : " + _arcFile + " : " + rsl);
			//			rsl = target.renameTo(arc);
			//			if (!rsl)
			//				log.error("failed to rename file " + _targetFile + " to " + _arcFile);
			moveFile(_targetFile, _arcFile);
		}

		log.info("execute update sqls");
		executeSql(sqls);

		log.info("Leaving...");
	}

	public void moveFile(String src, String dst) {
		log.info("move file " + src + " to " + dst);
		File target = new File(src);
		File arc = new File(dst);
		boolean rsl = arc.delete();
		log.info("remove file : " + dst + " : " + rsl);
		rsl = target.renameTo(arc);
		if (!rsl)
			log.error("failed to rename file " + src + " to " + dst);
		else
			log.info("succeed to rename file " + src + " to " + dst);
	}

	public void executeSql(List<String> sqls) throws SQLException, EtlException {
		Connection conn = null;

		try {
			log.info("create connection : " + _jdbcUrl + " with user/pass");
			conn = DriverManager.getConnection(_jdbcUrl, _jdbcUser, _jdbcPassword);
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
