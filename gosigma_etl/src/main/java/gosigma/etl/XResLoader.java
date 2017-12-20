package gosigma.etl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import ch.qos.logback.core.joran.spi.JoranException;

//  X pattern :
// singleton, with static API access. API can be chained
// hiden internal implementation, user can't new 

public class XResLoader extends URLClassLoader {
	public static Logger log = UtilsLog.getLogger(XResLoader.class);

	// --------------- internal implemenation, simple extends -------------------
	private XResLoader(URL[] urls) {
		super(urls);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (URL u : this.getURLs()) {
			if (sb.length() > 0)
				sb.append('\n');
			sb.append(u.toString());
		}
		return sb.toString();
	}

	// --------------- access API via static method -------------------
	// usage : XResLoader.x().xxxxx

	private static XResLoader __x = null;

	public static XResLoader x(XResLoader cl) {
		if (cl != null)
			__x = cl;
		return x();
	}

	public static XResLoader x() {
		ClassLoader cl = XResLoader.class.getClassLoader();
		if (__x == null) {
			URL[] urls = new URL[0];
			if (cl instanceof URLClassLoader)
				urls = ((URLClassLoader) cl).getURLs();
			__x = new XResLoader(urls);
		}
		return __x;
	}

	public static XResLoader post(URL url) {
		if (url == null)
			return x();

		List<URL> urls = new ArrayList<>();
		urls.addAll(Arrays.asList(x().getURLs()));
		urls.add(url);
		return x(new XResLoader(urls.toArray(new URL[urls.size()])));
	}

	public static XResLoader pre(URL url) {
		if (url == null)
			return x();

		List<URL> urls = new ArrayList<>();
		urls.add(url);
		urls.addAll(Arrays.asList(x().getURLs()));
		return x(new XResLoader(urls.toArray(new URL[urls.size()])));
	}

	public static void main(String[] args) throws JoranException {
		UtilsLog.getLogger(args);
		UtilsLog.resetLogger(true);
		log.info("---- before add URL ----");
		log.info("x() URLs : " + XResLoader.x().toString());
		log.info("x() add current  URLs : " + XResLoader.pre(Utils.fileUrl(System.getProperty("user.dir"))));
		log.info("x() add current  URLs : " + XResLoader.post(Utils.fileUrl("hello")));
	}

}
