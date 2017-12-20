package gosigma.etl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;

import ch.qos.logback.core.joran.spi.JoranException;

public class UtilsProp {
	public static Logger log = UtilsLog.getLogger(UtilsProp.class);

	// --------------- recording --------------------------
	private final static int REC_SIZE = 1000;
	private static List<String> __records = new ArrayList<>(REC_SIZE);

	private static void record(String rec) {
		if (__records.size() < REC_SIZE)
			__records.add(rec);
	}

	public static void dumpRecord() {
		log.info("list of getPropertie call");
		for (String r : __records) {
			log.info(r);
		}
	}

	// --------------- access API via static method -------------------
	// usage : UtilsProp.x().xxxxx

	public static UtilsProp __x = null;

	public static UtilsProp x() {
		if (__x == null)
			__x = new UtilsProp();
		return __x;
	}

	public static UtilsProp load(Properties props) {
		x().append_(props);
		hidePassword();
		return x();
	}

	public static UtilsProp loadFile(String file) throws FileNotFoundException, IOException {
		return loadInputStream(new FileInputStream(file));
	}

	public static UtilsProp loadInputStream(InputStream in) throws IOException {
		Properties props = new Properties();
		props.load(in);
		return load(props);
	}

	public static UtilsProp loadResource(String file) throws IOException {
		return loadInputStream(XResLoader.x().getResourceAsStream(file));
	}

	public static String getProperty(String key) {
		return x().getProperty_(key, null);
	}

	public static String getProperty(String key, String defaultVal) {
		return x().getProperty_(key, defaultVal);
	}

	public static UtilsProp setProperty(String key, String val) {
		x().setProperty_(key, val);
		return x();
	}

	public static UtilsProp hidePassword(boolean b) {
		x().setProperty("etl.log.hide_credential", (b == true ? "true" : "false"));
		x().hidePassword_(b);
		return x();
	}

	public static UtilsProp hidePassword() {
		x().hidePassword_(x().getProperty("etl.log.hide_credential", "true").equalsIgnoreCase("true"));
		return x();
	}
	
	// --------------- object intenal implement -------------------

	private List<Properties> _propsList = new ArrayList<>();
	private boolean _hidePassword = true;

	private UtilsProp() {
		log.info("setup initial config from system properties");
		append_(System.getProperties());
	}

	private void hidePassword_(boolean b) {
		log.info("hide password : " + b);
		_hidePassword = b;
	}

	private void append_(Properties props) {
		log.info("append properties at the end");
		_propsList.add(props);
	}

	private String getProperty_(String key, String defaultVal) {
		String val = null;
		for (int i = this._propsList.size() - 1; i >= 0; --i) {
			val = _propsList.get(i).getProperty(key);
			if (val == null)
				continue;
		}
		if (val == null)
			val = defaultVal;

		String show = val;
		String show2 = defaultVal;
		if (_hidePassword == true && key.contains("password")) {
			show = "xxx";
			show2 = "xxx";
		}
		log.info("get property, " + key + " : " + show);

		// bookkeeping first 1000 call for tracing back
		record(String.format("getProerty, %-30s : %-20s | %s", key, show, show2));
		return val;
	}

	private void setProperty_(String key, String val) {
		_propsList.get(_propsList.size() - 1).setProperty(key, val);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this._propsList.size(); ++i) {
			Properties props = _propsList.get(i);
			Set<Object> keys = new TreeSet(props.keySet());
			for (Object key : keys) {
				Object value = props.get(key);
				if (sb.length() > 0)
					sb.append('\n');
				sb.append("[" + i + "] property " + key + " : " + value);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws JoranException, IOException {
		UtilsLog.resetLogger(true);

		log.info("initial config/properties : " + UtilsProp.x().toString());
		log.info("load etl.properties: " + UtilsProp.x().loadResource("etl.properties").toString());

		UtilsProp.x().getProperty("testing");
		UtilsProp.x().getProperty("etl.log.hide_credential", "false");
		UtilsProp.x().getProperty("jdbc.password");
		UtilsProp.dumpRecord();
		UtilsProp.x().hidePassword(true);
		log.info("final : " + UtilsProp.x().toString());
		UtilsProp.dumpRecord();
	}

}
