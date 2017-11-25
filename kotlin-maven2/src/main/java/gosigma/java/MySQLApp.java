package gosigma.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gosigma.kotlin.MySQL;

public class MySQLApp {
	static Logger logger = LoggerFactory.getLogger(MySQLApp.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		// TODO Auto-generated method stub
		MySQL sql = new MySQL();
		sql.getConnection();
		sql.executeMySQLQuery();

		logger.info("Leaving...");
	}

}
