package gosigma.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostBody
 */
@WebServlet(urlPatterns = "/postbody/*")
public class PostBody extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostBody() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(p("doGet()"));
		for (Entry<String, String[]> e : request.getParameterMap().entrySet()) {
			sb.append(p("[" + e.getKey() + "] - [" + String.join(",", e.getValue()) + "]"));
		}

		sb.append(p("Content-type : " + request.getContentType()));
		sb.append(p("header Referer : " + request.getHeader("Referer")));
		sb.append(p("requestURL : " + request.getRequestURL().toString()));
		sb.append(p("requestURI : " + request.getRequestURI().toString()));

		PrintWriter writer = response.getWriter();
		writer.append(sb.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder();
		sb.append(p("doPost()"));
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
		String s = "PostBody - " + msg;
		System.out.println(s);
		return s + "\n";
	}
}
