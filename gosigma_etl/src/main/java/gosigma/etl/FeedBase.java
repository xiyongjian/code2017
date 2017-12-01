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
import java.util.stream.Collectors;

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

public abstract class FeedBase {
	public Logger log = null;

	// section for command line argument/control, _cXXX
	public String _cDir = null;
	public String _cInputFile = null;
	public boolean _cParseOnly = false;
	public boolean _cIsCronJob = false;
	public boolean _cDebug = false;

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
		return "FeedBase [log=" + log + ", _cDir=" + _cDir + ", _cInputFile=" + _cInputFile + ", _cParseOnly="
				+ _cParseOnly + ", _cIsCronJob=" + _cIsCronJob + ", _cDebug=" + _cDebug + ", _logDir=" + _logDir
				+ ", _logToFile=" + _logToFile + ", _jdbcUrl=" + _jdbcUrl + ", _jdbcUser=" + _jdbcUser
				+ ", _jdbcPassword=" + "xxxx" + ", _feedId=" + _feedId + ", _dataDir=" + _dataDir + ", _feedDir="
				+ _feedDir + ", _feedUrl=" + _feedUrl + ", _feedFile=" + _feedFile + ", _cols=" + _cols + ", _table="
				+ _table + ", _targetFile=" + _targetFile + ", _arcFile=" + _arcFile + ", _key=" + _key + "]";
	}

	/**
	 * parsing feed file, generate sql statements and timestamp key
	 * 
	 * @param targetFile
	 *            - feed file to be parsed
	 * @param sqls
	 *            - store result sql statements
	 * @return - timestamp key
	 * @throws EtlException
	 * @throws IOException
	 */
	abstract public String parseFeed(String targetFile, List<String> sqls) throws EtlException, IOException;
	// {
	// throw new EtlException("base parseFeed should never been called");
	// }

	// derived class must implement following method
	abstract public Logger getLogger();
	// {
	// // log = LoggerFactory.getLogger(<derived>.class);
	// throw new EtlException("Should implement getLogger in derived class");
	// }

	public void init() throws JoranException, EtlException, IOException {
		init(null, null);
	}

	public void init(String dataDir, String logDir) throws JoranException, EtlException, IOException {
		log.info("Entering... args : " + dataDir + ", " + logDir);

		String userDir = System.getProperty("user.dir");
		log.info("[current dir] - " + userDir);
		if (_cDir != null)
			_logDir = _cDir;
		else if (logDir != null)
			_logDir = logDir;
		else
			_logDir = userDir + File.separator + "logs";

		log.info("initialize logback");
		log.info("log_dir : " + _logDir);
		log.info("feed_id : " + _feedId);
		log.info("_cIsCronJob : " + _cIsCronJob);

		System.setProperty("feed_id", _feedId);
		if (this._cIsCronJob == true)
			System.setProperty("log_dir", _logDir);
		else
			System.setProperty("etl.command", "true");
		if (this._cDebug == true)
			System.setProperty("etl.debug", "true");

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(lc);
		lc.reset();
		ci.autoConfig();

		// update log
		log = this.getLogger();

		if (this._cDebug == true) {
			Utils.initLog(log);
			log.info("command line : \n" + Utils.appCmdLine());
			log.info("shell/script : \n" + Utils.appShellScript());
		}
		log.info("feedId : " + _feedId);
		log.info("[current dir] : " + userDir);
		String classPathStr = System.getProperty("java.class.path");
		log.info("[classpath] : " + classPathStr.replaceAll(";", "\n"));

		if (_cDir != null)
			_dataDir = _cDir;
		else if (dataDir != null)
			_dataDir = dataDir;
		else
			_dataDir = userDir + File.separator + "data";
		log.info("dataDir - " + _dataDir);

		this.loadProperties();

		log.info("Leaving...");
	}

	public void parseArgs(String[] args) throws EtlException {
		log.info("Entering... args : " + String.join("|", args));

		Options options = new Options();
		options.addOption(Option.builder("d").longOpt("directory").desc("output file location").hasArg()
				.argName("OUTPUT DIR").build());
		options.addOption(
				Option.builder("i").longOpt("input").desc("input feed file").hasArg().argName("FEED FILE").build());
		options.addOption(Option.builder("p").longOpt("parse").desc("parse feed file to SQL only").build());
		options.addOption(Option.builder("h").longOpt("help").desc("usage").build());
		options.addOption(Option.builder("c").longOpt("cron").desc("run as cron job").build());
		options.addOption(Option.builder().longOpt("debug").desc("debug mode").build());

		log.info("parsing args");
		CommandLineParser commandLineParser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine cl = null;
		try {
			cl = commandLineParser.parse(options, args);
		} catch (ParseException e) {
			helpFormatter.printHelp("etl application", options);
			throw new EtlException("command line incorrect.\n" + helpFormatter.toString());
		}

		if (cl.hasOption('h')) {
			helpFormatter.printHelp("elt application", options);
			System.exit(0);
		}

		if (cl.hasOption('d')) {
			_cDir = cl.getOptionValue('d');
			log.info("_cDir : " + _cDir);
		}

		if (cl.hasOption('i')) {
			_cInputFile = cl.getOptionValue('i');
			log.info("_cInputFile : " + _cInputFile);
		}

		if (cl.hasOption('p'))
			_cParseOnly = true;
		log.info("_cParseOnly : " + _cParseOnly);

		if (cl.hasOption('c'))
			_cIsCronJob = true;
		log.info("_cIsCronJob : " + _cIsCronJob);

		if (cl.hasOption("debug"))
			this._cDebug = true;
		log.info("_cDebug : " + _cDebug);

		log.info("Leaving...");
	}

	public void doEtl(String[] args) {
		try {
			// get initial log
			log = this.getLogger();

			log.info("parse arguments");
			this.parseArgs(args);

			log.info("initialize feed etl");
			init();

			log.info("start etl process");
			process();
		} catch (JoranException | IOException | SQLException | EtlException e) {
			this.log.error("etl error", e);
			this.log.error(this.toString());
			System.exit(1); // something wrong, will record in cron log
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
			if (_cDir != null)
				targetDir = _cDir;
			else
				targetDir = _dataDir + File.separator + _feedDir + File.separator + dateFormat.format(date);
			log.info("targetDir : " + targetDir);

			dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String fileName = String.format(_feedFile, dateFormat.format(date));
			log.info("fileName  : " + fileName);

			if (_cInputFile != null)
				_targetFile = _cInputFile;
			else
				_targetFile = targetDir + File.separator + fileName;
			log.info("targetFile  : " + _targetFile);
		}

		if (_cInputFile == null)
			download(_feedUrl, _targetFile);

		log.info("parsing feed into sql");
		List<String> sqls = new ArrayList<String>();
		{
			_key = parseFeed(_targetFile, sqls);

			_arcFile = targetDir + File.separator + String.format(_feedFile, _key);
			log.info("_arcFile : " + _arcFile);

			String sqlFile = null;
			if (_cInputFile == null)
				sqlFile = _arcFile + ".sql";
			else
				sqlFile = _cInputFile + ".sql";
			log.info("record sql to file : " + sqlFile);
			FileUtils.writeStringToFile(new File(sqlFile), String.join(";\n", sqls), (String) null);

			if (_cInputFile == null)
				moveFile(_targetFile, _arcFile);
		}

		if (this._cParseOnly != true) {
			log.info("execute update sqls");
			executeSql(sqls);
		}

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
			conn.setAutoCommit(false);
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

			log.info("commit transaction");
			conn.commit();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * parse 'cols' string for insert SQL
	 * 
	 * @param cols
	 *            cols string from properties file
	 * @param idxs
	 *            return indexes of columns for values
	 * @return string xxx used in 'insert <tbl> ( xxx ) .....
	 */
	public String parseCols(String cols, List<Integer> indexes) {
		log.info("Entering...  cols : " + cols);
		String[] cl = cols.split(",");

		indexes.clear();
		// String insCols = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cl.length; ++i) {
			String s = cl[i].trim();
			if (s.equals(""))
				continue;
			if (!indexes.isEmpty())
				sb.append(",");
			sb.append(s);
			indexes.add(i);
		}
		// ensure
		assert sb.toString().split(",").length == indexes.size();

		log.info("indexes : " + String.join(",", indexes.stream().map(Object::toString).collect(Collectors.toList())));
		log.info("Leaving...  ret : " + sb.toString());

		return sb.toString();
	}

}
