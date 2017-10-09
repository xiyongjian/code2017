package gosigma.study;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import gosigma.study.loader.IClient;

public class DerbyClient implements IClient {
	Connection conn = null;

	public void process(String in) {
		System.out.println("process : " + in);
		
		// TODO Auto-generated method stub
		if (conn == null) {
			System.out.println("connection is null");
			return;
		}
		
		try {
			String sql = "select * from app.test";
			System.out.println("run query : " + sql);
			PreparedStatement stat = conn.prepareStatement(sql);
			ResultSet rs = stat.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			System.out.println("result");
			int cols = rsmd.getColumnCount();
			for (int i=1; i<=cols; ++i) {
				System.out.print(String.format("%20s",  rsmd.getColumnName(i)));
			}
			System.out.println("");

			while (rs.next()) {
				for (int i = 1; i <= cols; ++i) {
					System.out.print(String.format("%20s", rs.getString(i)));
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public DerbyClient()  {
		conn = getConnection();
	}
	
	public Connection getConnection() {
		System.out.println("getConnection()");
		Connection conn = null;
		try {
			// DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
			String className = "org.apache.derby.jdbc.EmbeddedDriver";
			System.out.println("obtain class : " + className);
			Class.forName(className);

			String url = "jdbc:derby:e:\\code\\derby\\Test";

			System.out.println("get driver : " + url);
			Driver driver = DriverManager.getDriver(url);
			System.out.println("driver : " + driver.toString());

			System.out.println("connect : " + url);
			conn = DriverManager.getConnection(url); // ;create=true
			System.out.println("connection : " + conn.toString());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		System.out.println("DerbyClient begin");
		IClient client = new DerbyClient();
		client.process("hello, world");
		System.out.println("done");
	}
}
