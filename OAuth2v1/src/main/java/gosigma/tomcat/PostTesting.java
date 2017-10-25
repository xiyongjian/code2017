package gosigma.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

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
		
		StringBuilder sb = new StringBuilder();
		sb.append(p("doPost()\n"));
		for (Entry<String, String[]> e : request.getParameterMap().entrySet()) {
			sb.append(p("[" + e.getKey() + "] - [" + String.join(",", e.getValue()) + "]"));
		}

		StringBuilder sb2 = new StringBuilder();
		BufferedReader br = request.getReader();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb2.append(str);
		}
		sb.append(p("body : " + sb2.toString()));
		
		sb.append(p("Content-type : " + request.getContentType()));
		
		sb.append(p("header Referer : " + request.getHeader("Referer")));
		sb.append(p("requestURL : " + request.getRequestURL().toString()));
		sb.append(p("requestURI : " + request.getRequestURI().toString()));

		PrintWriter writer = response.getWriter();
		writer.append(sb.toString());

		// doGet(request, response);
	}

	static public String p(String msg) {
		String s = "PostTesting - " + msg;
		System.out.println(s);
		return s + "\n";
	}
}
