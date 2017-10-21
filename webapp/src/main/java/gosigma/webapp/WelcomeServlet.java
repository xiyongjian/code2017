package gosigma.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class WelcomeServlet
 */
@WebServlet(urlPatterns = "/welcome/*")
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/TestDB")
	private DataSource ds;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WelcomeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// response.getWriter().append("Served at: ").append(request.getContextPath());
	// }
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection con = ds.getConnection();
			stmt = con.createStatement();

			rs = stmt.executeQuery("select id, name, pos from testing");

			PrintWriter out = resp.getWriter();
			resp.setContentType("text/html");
			out.write("<html>");
			out.write("<body>");

			out.write("<h3>Database Records</h3>");
			while (rs.next()) {
				out.write("id: " + rs.getInt("id") + " ");
				out.write("name: " + rs.getString("name") + " ");
				out.write("pos: " + rs.getString("pos") + " ");
				out.write("</br>");
			}

			// lets print some DB information
			out.write("<h3>Database Meta Data</h3>");
			DatabaseMetaData metaData = con.getMetaData();
			out.write("Database Product: " + metaData.getDatabaseProductName() + "<br/>");
			out.write("Database Version: " + metaData.getDatabaseMajorVersion() + "."
					+ metaData.getDatabaseMinorVersion() + "<br/>");
			out.write("Database Driver: " + metaData.getDriverName() + "<br/>");
			out.write("Database Driver version: " + metaData.getDriverMajorVersion() + "."
					+ metaData.getDriverMinorVersion() + "<br/>");
			out.write("Database user: " + metaData.getUserName());
			out.write("</html>");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Exception in closing DB resources");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
