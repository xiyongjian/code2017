package gosigma.kotlin

import java.sql.*
import java.util.Properties

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Program to list databases in MySQL using Kotlin
 */
object MySQLExample {

	internal var conn: Connection? = null
	internal var username = "jxi" // provide the username
	internal var password = "password" // provide the corresponding password

	internal var logger = LoggerFactory.getLogger(MySQLExample.javaClass);

	@JvmStatic fun main(args: Array<String>) {
		logger.info("Enetering...")
		// make a connection to MySQL Server
		getConnection()
		// execute the query via connection object
		executeMySQLQuery()
		logger.info("Leaving...")
	}

	fun executeMySQLQuery() {
		logger.info("Entering...")
		var stmt: Statement? = null
		var resultset: ResultSet? = null

		try {
			stmt = conn!!.createStatement()
			resultset = stmt!!.executeQuery("SHOW DATABASES;")

			if (stmt.execute("SHOW DATABASES;")) {
				resultset = stmt.resultSet
			}

			while (resultset!!.next()) {
				println(resultset.getString("Database"))
			}
		} catch (ex: SQLException) {
			// handle any errors
			ex.printStackTrace()
		} finally {
			// release resources
			if (resultset != null) {
				try {
					resultset.close()
				} catch (sqlEx: SQLException) {
				}

				resultset = null
			}

			if (stmt != null) {
				try {
					stmt.close()
				} catch (sqlEx: SQLException) {
				}

				stmt = null
			}

			if (conn != null) {
				try {
					conn!!.close()
				} catch (sqlEx: SQLException) {
				}

				conn = null
			}
		}
		logger.info("Leaving...")
	}

	/**
	 * This method makes a connection to MySQL Server
	 * In this example, MySQL Server is running in the local host (so 127.0.0.1)
	 * at the standard port 3306
	 */
	fun getConnection() {
		logger.info("Entering...")
		val connectionProps = Properties()
		connectionProps.put("user", username)
		connectionProps.put("password", password)
		try {
			// Class.forName("com.mysql.jdbc.Driver").newInstance()
			//			conn = DriverManager.getConnection( "jdbc:mysql://192.168.0.185:13306/test?autoReconnect=true&useSSL=false",
			//					connectionProps)
			conn = DriverManager.getConnection( "jdbc:mysql://192.168.0.185:13306/test?autoReconnect=true&useSSL=false",
					username, password)
		} catch (ex: SQLException) {
			// handle any errors
			ex.printStackTrace()
		} catch (ex: Exception) {
			// handle any errors
			ex.printStackTrace()
		}
		logger.info("Leaving...")
	}
}