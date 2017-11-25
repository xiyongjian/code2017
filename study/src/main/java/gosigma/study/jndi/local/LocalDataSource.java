package gosigma.study.jndi.local;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

class LocalDataSource implements DataSource , Serializable {

	final static org.slf4j.Logger logger = LoggerFactory.getLogger(LocalDataSource.class);
	
	private String connectionString;
    private String username;
    private String password;
    
    LocalDataSource(String connectionString, String username, String password) {
		logger.info("Entering...");
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
		logger.info("Leaving...");
    }
    
    public Connection getConnection() throws SQLException
    {
		logger.info("Processing...");
        return DriverManager.getConnection(connectionString, username, password);
    }

	public Connection getConnection(String username, String password)
			throws SQLException {return null;}
	public PrintWriter getLogWriter() throws SQLException {return null;}
	public int getLoginTimeout() throws SQLException {return 0;}
	public void setLogWriter(PrintWriter out) throws SQLException {	}
	public void setLoginTimeout(int seconds) throws SQLException {}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}