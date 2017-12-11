package gosigma.study.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Duplication {
	public static String jsonStr = "{\"USER\":{\"CONTACT_ID\":8401,\"ACCOUNT_ID\":1381,\"SALUTATION\":null,\"FIRST_NAME\":\"yon\",\"LAST_NAME\":\"jiasn\",\"TITLE\":\"n/a\",\"CONTACT_OFFICE_PHONE\":\"1111111111\",\"CONTACT_OFFICE_PHONE_EXT\":null,\"CONTACT_CELL_PHONE\":null,\"EMAIL_ADDRESS\":\"xiyongjian@gmail.com\",\"ADDRESS_ID\":null,\"USER_ID\":402,\"USERNAME\":\"jxi001\",\"PASSWORD\":\"b10cfc4ceade36997192956ecf402d03|mx5u51wcq8C09COq\",\"USER_UUID\":\"186beefa-aeff-11e7-8dcc-c0a89b640000\",\"USER_VERIFIED\":null,\"COUNTRY\":\"\",\"TIMEZONE\":\"America/Chicago\",\"LANGUAGE\":\"en-US\",\"SIGNATURE\":null,\"LAST_IP\":null,\"LAST_BROWSER\":\"Apache-HttpClient/4.4.1 (Java/1.8.0)\",\"TICKETS_VIEWABLE\":\"N\",\"ACCOUNT_NAME\":\"abs\",\"ACCOUNT_OWNER_USER_ID\":null,\"ACCOUNT_OWNER_USERNAME\":null,\"ACCOUNT_OFFICE_PHONE\":null,\"ACCOUNT_OFFICE_PHONE_EXT\":null,\"DISABLED\":\"N\",\"COMPANY_ID\":169,\"COMPANY_ID\":169,\"COMPANY_NAME\":\"dev\",\"COMPANY_SUBDOMAIN\":\"dev\",\"COMPANY_UUID\":\"3260bcd6-8421-11e7-9754-c0a89b640000\",\"DATE_CREATED\":\"2017-08-18 14:26:17.198295\",\"TICKETS_VIEWABLE\":\"N\",\"USER_SESSION_TIMEOUT\":367,\"TICKET_REVIEWS_ENABLED\":\"Y\",\"TICKETING_TIME_TRACKING\":\"Y\",\"MAX_LOGIN_RETRIES\":10,\"GROUP_USER_ID\":3662,\"USER_ID\":402,\"GROUP_ID\":null,\"COMPANY_ID\":169,\"AUTHORIZATIONS\":[\"End-User\",41],\"CONTACT_EMAILS\":[{\"CONTACT_EMAIL_ID\":1929,\"CONTACT_ID\":8401,\"EMAIL_ADDRESS\":\"xiyongjian@gmail.com\",\"IS_PRIMARY\":\"Y\",\"COMPANY_ID\":169}],\"CONTACT_PHONES\":[],\"FEATURES\":{\"TICKETS_VIEWABLE\":\"N\",\"TICKETING_TIME_TRACKING\":\"Y\"}},\"AUTHENTICATION_TOKEN\":\"cb0354f8-b031-40f0-938e-c5fdb8a254fe\"}";

	public static class JSONObjectIgnoreDuplicates extends JSONObject {

		public JSONObjectIgnoreDuplicates(JSONTokener x) throws JSONException {
			super(x);
		}

		public JSONObjectIgnoreDuplicates(String json) throws JSONException {
			this(new JSONDupTokener(json));
		}

		@Override
		public JSONObject putOnce(String key, Object value) throws JSONException {
			Object storedValue;
			if (key != null && value != null) {
				if ((storedValue = this.opt(key)) != null) {
					if (!storedValue.toString().equals(value.toString())) // Only throw Exception for different values
																			// with same key
						// throw new JSONException("Duplicate key \"" + key + "\"");
						return this; 	// ignore, and always use the first one
					else
						return this;
				}
				this.put(key, value);
			}
			return this;
		}
	}

	public static class JSONDupTokener extends JSONTokener {

		public JSONDupTokener(String s) {
			super(s);
		}

		@Override
		public Object nextValue() throws JSONException {
			char c = this.nextClean();
			switch (c) {
			case '\"':
			case '\'':
				return this.nextString(c);
			case '[':
				this.back();
				return new JSONArray(this);
			case '{':
				this.back();
				return new JSONObjectIgnoreDuplicates(this);
			default:
				StringBuffer sb;
				for (sb = new StringBuffer(); c >= 32 && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
					sb.append(c);
				}

				this.back();
				String string = sb.toString().trim();
				if ("".equals(string)) {
					throw this.syntaxError("Missing value");
				} else {
					return JSONObject.stringToValue(string);
				}
			}
		}
	}

	public static void main(String[] args) {
		Duplication dup = new Duplication();
		try {
			dup.test01();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		try {
			dup.test02();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		try {
			dup.test03();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	public void test01() {
		System.out.println("\ntest01, source string : " + jsonStr);
		JSONObjectIgnoreDuplicates json = new JSONObjectIgnoreDuplicates(jsonStr);

		System.out.println("JSON : " + json.toString(4));

	}

	public void test02() {
		System.out.println("\ntest02, source string : " + jsonStr);
		JSONObject json = new JSONObject(jsonStr);

		System.out.println("JSON : " + json.toString());

	}

	public void test03() {
		System.out.println("\ntest03, double put");
		JSONObjectIgnoreDuplicates json = new JSONObjectIgnoreDuplicates("{'NONE':'yes'}");
		json.put("KEY",  "yes");
		json.put("KEY",  "no");

		System.out.println("JSON : " + json.toString(4));

	}
}
