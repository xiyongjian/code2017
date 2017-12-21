package gosigma.etl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
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

	public static XResLoader x(URL[] urls) {
		__x = new XResLoader(urls);
		return x();
	}

	// double duty : 
	//	return __x if not null
	//  create default __x if null and return it
	public static XResLoader x() {
		// ClassLoader cl = XResLoader.class.getClassLoader();
		if (__x == null) {
			//			URL[] urls = new URL[0];
			//			if (cl instanceof URLClassLoader)
			//			urls = ((URLClassLoader) cl).getURLs();
			//			__x = new XResLoader(urls);
			__x = new XResLoader(new URL[0]);
		}
		return __x;
	}

	public static XResLoader tail(URL[] urls_) {
		List<URL> urls = new ArrayList<>();
		urls.addAll(Arrays.asList(x().getURLs()));
		urls.addAll(Arrays.asList(urls_));
		return x(new XResLoader(urls.toArray(new URL[urls.size()])));
	}

	public static XResLoader head(URL[] urls_) {
		List<URL> urls = new ArrayList<>();
		urls.addAll(Arrays.asList(urls_));
		urls.addAll(Arrays.asList(x().getURLs()));
		return x(new XResLoader(urls.toArray(new URL[urls.size()])));
	}

	public static XResLoader tail(String[] files) {
		return tail(toUrls(files));
	}

	public static XResLoader head(String[] files) {
		return head(toUrls(files));
	}

	public static XResLoader tail(URL url) {
		return tail(new URL[] { url });
	}

	public static XResLoader head(URL url) {
		return head(new URL[] { url });
	}

	public static XResLoader tail(String files) {
		return tail(files.split(" "));
	}

	public static XResLoader head(String files) {
		return head(files.split(" "));
	}

	private static URL[] toUrls(String[] files) {
		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; ++i)
			urls[i] = Utils.fileUrl(files[i]);
		return urls;
	}

	// key to children first resource search
	@Override
	public URL getResource(String name) {
		// log.info("Entering...., name : " + name);
		URL url = findResource(name);
		if (url == null) {
			// log.info("not found local, go parent");
			url = super.getResource(name);
		}
		return url;
	}

	//	@Override
	//	public void addURL(URL url) {
	//		super.addURL(url);
	//	}

	public static void main(String[] args) throws JoranException, URISyntaxException, IOException {
		UtilsLog.getLogger(args);
		UtilsLog.resetLogger(true);

		log.info("current class loader : " + XResLoader.class.getClassLoader() + ", parent : "
				+ XResLoader.class.getClassLoader().getParent());
		log.info("system class loader : " + ClassLoader.getSystemClassLoader() + ", parent : "
				+ ClassLoader.getSystemClassLoader().getParent());
		log.info("XResLoader class loader : " + XResLoader.x().getClass() + ", parent : " + XResLoader.x().getParent());

		// backup
		XResLoader xrl = XResLoader.x();
		// XResLoader xrl = XResLoader.x(new URL[] { Utils.fileUrl("e:\\") });
		{
			XResLoader.x().head("E:\\");
			log.info("load path : head e: ");
			log.info("get etl.properties : " + XResLoader.x().getResource("etl.properties"));
			log.info("get derby.log : " + XResLoader.x().getResource("derby.log"));
		}

		ClassLoader parent = XResLoader.x().getParent();
		log.info("parent : " + parent.toString());
		log.info("parent is URL loader ?: " + (parent instanceof URLClassLoader));
		log.info("get non.existing....: " + XResLoader.x().getResource("xxxxderby.log"));

		{
			XResLoader x = XResLoader.x(new URL[] { Utils.fileUrl("e:\\") });
			log.info("x : " + x.toString());
			log.info("x head : " + x.head("d:\\"));

			x = XResLoader.x(new URL[] { Utils.fileUrl("e:\\") });
			log.info("x tail : " + x.tail("d:\\"));

			x = XResLoader.x(new URL[] { Utils.fileUrl("e:\\") });
			x.addURL(Utils.fileUrl("d:\\"));
			log.info("x addUrl : " + x);
		}

		{
			XResLoader.x(xrl);
			XResLoader.x().tail("E:\\");
			log.info("load path : tail e: ");
			log.info("get etl.properties : " + XResLoader.x().getResource("etl.properties"));
			log.info("get logback.xml : " + XResLoader.x().getResource("logback.xml"));
			log.info("get derby.log : " + XResLoader.x().getResource("derby.log"));
			log.info("get xderby.log : " + XResLoader.x().getResource("xderby.log"));

			log.info("get S etl.properties : " + XResLoader.x().getResourceAsStream("etl.properties"));
			log.info("get S logback.xml : " + XResLoader.x().getResourceAsStream("logback.xml"));
			log.info("get S derby.log : " + XResLoader.x().getResourceAsStream("derby.log"));
			log.info("get S xderby.log : " + XResLoader.x().getResourceAsStream("xderby.log"));

			log.info("get SS etl.properties : " + XResLoader.x().getSystemResource("etl.properties"));
			log.info("get SS logback.xml : " + XResLoader.x().getSystemResource("logback.xml"));
			log.info("get SS derby.log : " + XResLoader.x().getSystemResource("derby.log"));
			log.info("get SS xderby.log : " + XResLoader.x().getSystemResource("xderby.log"));

			log.info("get SSS etl.properties : " + XResLoader.x().getSystemResourceAsStream("etl.properties"));
			log.info("get SSS logback.xml : " + XResLoader.x().getSystemResourceAsStream("logback.xml"));
			log.info("get SSS derby.log : " + XResLoader.x().getSystemResourceAsStream("derby.log"));
			log.info("get SSS xderby.log : " + XResLoader.x().getSystemResourceAsStream("xderby.log"));
		}
		
		{
			XResLoader.x(new URL[0]).head(".");
			log.info("initial : " + XResLoader.x());
			log.info("resoruce .classpath : " + XResLoader.x().getResource(".classpath"));
			log.info("resoruce .classpath : " + XResLoader.x().getResourceAsStream(".classpath"));
			String str = IOUtils.toString(XResLoader.x().getResourceAsStream(".classpath"), Charset.defaultCharset()); 
			log.info(".classpath content : " + str);
		}

	}

}
