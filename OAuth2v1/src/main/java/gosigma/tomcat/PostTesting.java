package gosigma.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostTesting
 */
public class PostTesting extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostTesting() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter writer = response.getWriter();
		writer.append("Served at: ").append(request.getContextPath()).append("\n" + p("doPost"))
				.append(p("param name : " + request.getParameter("name")))
				.append(p("param num : " + request.getParameter("num")))
				.append(p("param details : " + request.getParameter("details")));

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		writer.append(p("body : " + sb.toString()));
		
		writer.append(p("header Referer : " + request.getHeader("Referer")));
		writer.append(p("requestURL : " + request.getRequestURL().toString()));
		writer.append(p("requestURI : " + request.getRequestURI().toString()));

		// doGet(request, response);
	}

	static public String p(String msg) {
		String s = "PostTesting - " + msg;
		System.out.println(s);
		return s + "\n";
	}
}
