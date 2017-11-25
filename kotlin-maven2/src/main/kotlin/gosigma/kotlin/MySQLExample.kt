package gosigma.kotlin

import java.sql.*
import java.util.Properties

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// var logger = LoggerFactory.getLogger("MySQL");
// var logger = LoggerFactory.getLogger(MySQL::class.toString());
var logger = LoggerFactory.getLogger(MySQL::class.java);

/**
 * Program to list databases in MySQL using Kotlin
 */
class MySQL {
	internal var conn: Connection? = null
	internal var username = "jxi" // provide the username
	internal var password = "password" // provide the corresponding password

	fun executeMySQLQuery() {
		logger.info("Entering...")
		logger.info("java class name : " + this.javaClass.name);
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
			conn = DriverManager.getConnection("jdbc:mysql://192.168.0.185:13306/test?autoReconnect=true&useSSL=false",
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


//fun main(args: Array<String>) {
//    println("Hello, world!")
//}

fun main(args: Array<String>) {
	logger.info("Enetering...")

	var o = MySQL();
	//	// make a connection to MySQL Server
	o.getConnection()
	//	// execute the query via connection object
	o.executeMySQLQuery()

	println("Hello, world!")
	logger.info(MySQL::class.toString())
	logger.info(MySQL::class.java.name)
	logger.info(o.javaClass.name)
	logger.info(o.javaClass.kotlin.toString())
	logger.info(o.javaClass.kotlin.qualifiedName)

	val t = Thread.currentThread();
	System.out.println("class name : " + t.javaClass.name)
	System.out.println("thread name : " + t.name)
	System.out.println("thread name : " + t.getName())
	logger.info("file name   : " + java.lang.Thread.currentThread().stackTrace[1].fileName)
	logger.info("line number : " + java.lang.Thread.currentThread().stackTrace[1].lineNumber)
	logger.info("class       : " + java.lang.Thread.currentThread().stackTrace[1].className)
	logger.info("Leaving...")
}
