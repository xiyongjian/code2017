package gosigma.webapp;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Chandan Singh
 */
public class DBConnection {
	public static Connection getConnection() throws SQLException, ClassNotFoundException, NamingException {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
		System.out.println("DataSource : " + (ds == null ? "null" : ds.toString()));
		Connection connection = ds.getConnection();
		System.out.println("connection : " + (connection == null ? "null" : connection.toString()));

		return connection;
	}

	public static String getSystemInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("system info : \n");
		String dir = System.getProperty("user.dir");
		sb.append("[current dir] - " + dir);
		sb.append("\n");
		String classpathStr = System.getProperty("java.class.path");
		sb.append("[classpath] - " + classpathStr.replaceAll(";", "\n"));
		sb.append("\n");

		sb.append("system properties : ");
		sb.append("\n");
		Properties props = System.getProperties();
		for (Object k : props.keySet()) {
			sb.append("[" + k.toString() + "] - " + props.getProperty((String) k).replaceAll("(.{100})", "$1\n"));
			sb.append("\n");
		}

		sb.append("get current PID : " + ManagementFactory.getRuntimeMXBean().getName());
		sb.append("\n");

		return sb.toString();
	}

	private static Iterator list(ClassLoader CL)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class CL_class = CL.getClass();
		while (CL_class != java.lang.ClassLoader.class) {
			CL_class = CL_class.getSuperclass();
		}
		java.lang.reflect.Field ClassLoader_classes_field = CL_class.getDeclaredField("classes");
		ClassLoader_classes_field.setAccessible(true);
		Vector classes = (Vector) ClassLoader_classes_field.get(CL);
		return classes.iterator();
	}

	public static String getLoadedClasses() {
		System.out.println("getLoadedClasses");
		StringBuilder sb = new StringBuilder();
		try {
			ClassLoader myCL = Thread.currentThread().getContextClassLoader();
			System.out.println("loader : " + myCL);
			while (myCL != null) {
				sb.append("ClassLoader: " + myCL + "\n");
				for (Iterator iter = list(myCL); iter.hasNext();) {
					sb.append("\t" + iter.next() + "\n");
				}
				myCL = myCL.getParent();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		
		return sb.toString();
	}
}
