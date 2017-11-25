package gosigma.study.jndi.local;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Entering...");

		try {
			// LocalContext ctx =
			// LocalContextFactory.createLocalContext("com.mysql.jdbc.Driver");
			// LocalContext ctx = LocalContextFactory.createLocalContext("com.mysql.cj.jdbc.Driver");
			LocalContext ctx = LocalContext.createLocalContext("com.mysql.cj.jdbc.Driver");
			ctx.addDataSource("jdbc/js1", "jdbc:mysql://192.168.0.185:13306/test?autoReconnect=true&useSSL=false",
					"jxi", "password");
			ctx.addDataSource("jdbc/js2", "jdbc:mysql://dbserver1/dboneB", "username", "xxxpass");
			
			Context ctx2 = new InitialContext();

			logger.info("lookup jdbc/js1");
			DataSource ds = (DataSource) ctx2.lookup("jdbc/js1");
			Connection con = ds.getConnection();

			logger.info("lookup jdbc/ds1");
			ds = (DataSource) ctx2.lookup("jdbc/ds1");
			con = ds.getConnection();

			// MyServiceBean b = new MyServiceBean();
			// b.startProcess();
		} catch (SimpleException e) {
			logger.warn("", e);
		} catch (NamingException e) {
			logger.warn("", e);
		} catch (SQLException e) {
			logger.warn("", e);
		}

		logger.info("Leaving...");
	}
}
