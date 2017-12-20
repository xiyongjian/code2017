package gosigma.etl;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import ch.qos.logback.core.joran.spi.JoranException;

public class XConfLoader extends URLClassLoader {
	public static Logger log = UtilsLog.getLogger(XConfLoader.class);

	public XConfLoader(URL[] urls) {
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

	private static XConfLoader _x = null;

	public static XConfLoader x(XConfLoader cl) {
		if (cl != null)
			_x = cl;
		return x();
	}

	public static XConfLoader x() {
		ClassLoader cl = XConfLoader.class.getClassLoader();
		if (_x == null) {
			URL[] urls = new URL[0];
			if (cl instanceof URLClassLoader)
				urls = ((URLClassLoader) cl).getURLs();
			_x = new XConfLoader(urls);
		}
		return _x;
	}

	public static XConfLoader post(URL url) {
		if (url == null)
			return x();

		List<URL> urls = new ArrayList<>();
		urls.addAll(Arrays.asList(x().getURLs()));
		urls.add(url);
		return x(new XConfLoader(urls.toArray(new URL[urls.size()])));
	}

	public static XConfLoader pre(URL url) {
		if (url == null)
			return x();

		List<URL> urls = new ArrayList<>();
		urls.add(url);
		urls.addAll(Arrays.asList(x().getURLs()));
		return x(new XConfLoader(urls.toArray(new URL[urls.size()])));
	}

	public static void main(String[] args) throws JoranException {
		UtilsLog.getLogger(args);
		UtilsLog.resetLogger(true);
		log.info("---- before add URL ----");
		log.info("x() URLs : " + XConfLoader.x().toString());
		log.info("x() add current  URLs : " + XConfLoader.x().pre(Utils.fileUrl(System.getProperty("user.dir"))));
	}

}
