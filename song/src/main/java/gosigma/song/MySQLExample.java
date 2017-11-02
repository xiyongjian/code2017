package gosigma.song;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLExample {

	final static Logger logger = LoggerFactory.getLogger(MySQLExample.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		// TODO Auto-generated method stub

		Connection conn = null;
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			// Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver
			// class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered
			// via the SPI and manual loading of the driver class is generally unnecessary.

			// no need to register this one
			// Class.forName("com.mysql.cj.jdbc.Driver");

			// conn = DriverManager.getConnection("jdbc:mysql://192.168.0.185:13306/test", "jxi", "password");
			
			// to remove ssl warning, the simple way
			conn = DriverManager.getConnection("jdbc:mysql://192.168.0.185:13306/test?autoReconnect=true&useSSL=false", "jxi", "password");
			
			// another way
			//			Properties properties = new Properties();
			//			properties.setProperty("user", "jxi");
			//			properties.setProperty("password", "password");
			//			properties.setProperty("useSSL", "false");
			//			properties.setProperty("autoReconnect", "true");
			//			conn = DriverManager.getConnection("jdbc:mysql://192.168.0.185:13306/test", properties);

			String originalURL = conn.getMetaData().getURL();
			logger.info("original driver URL : " + originalURL);

			Driver drv = DriverManager.getDriver(originalURL);
			String driverClass = drv.getClass().getName();
			logger.info("driver class : " + driverClass);

			String sql = "select user()";
			logger.info(sql);
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			logger.info("first column : " + meta.getColumnName(1));
			if (rs.next()) {
				logger.info(rs.getString(1));
			}

			sql = "select id, name from test";
			logger.info(sql);
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			meta = rs.getMetaData();
			logger.info("first column : " + meta.getColumnName(1));
			int c = meta.getColumnCount();
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= c; ++i)
				sb.append(String.format("%10s |", meta.getColumnName(i)));
			logger.info(sb.toString());
			while (rs.next()) {
				sb.setLength(0);
				for (int i = 1; i <= c; ++i)
					sb.append(String.format("%10s |", rs.getString(i)));
				logger.info(sb.toString());
			}

			conn.close();
			// } catch (ClassNotFoundException e) {
			// // TODO Auto-generated catch block
			// logger.warn("", e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.warn("", e);
		} finally {
			try {
				if (conn != null) {
					logger.info("close conn");
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.warn("", e);
			}
		}

		logger.info("Leaving...");
	}

}
