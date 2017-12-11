package gosigma.etl_log;

import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	public static Logger log = LoggerFactory.getLogger(Utils.class);

	public static void main(String[] args) {

	}

	public static String dump(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nHttpServletRequest : " + req.toString());
		sb.append("\nURI : " + req.getRequestURI());
		sb.append("\nURL : " + req.getRequestURL());
		sb.append("\nuser principal : " + req.getUserPrincipal());
		sb.append("\nremote user : " + req.getRemoteUser());

		for (String h : Collections.list(req.getHeaderNames()))
			sb.append("\nheader " + h + " : " + req.getHeader(h));
		for (String a : Collections.list(req.getAttributeNames()))
			sb.append("\nattribute " + a + " : " + req.getAttribute(a));
		if (req.getCookies() != null)
			for (Cookie c : req.getCookies())
				sb.append("\ncookie " + c.getName() + ", " + c.getDomain() + ", " + c.getValue());
		try {
			Scanner s = new Scanner(req.getInputStream()).useDelimiter("\\A");
			sb.append("\ncontent : " + (s.hasNext() ? s.next() : ""));
		} catch (IOException e) {
			sb.append("\ncontent : null");
		}

		return sb.toString();
	}

}
