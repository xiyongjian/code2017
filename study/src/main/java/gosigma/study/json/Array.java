package gosigma.study.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class Array {
	public static void main(String[] args) {
		JSONObject user = new JSONObject();
		user.put("TIMEZONE", "America/Chicago");
		user.put("USERNAME", "jxi_test04");
		user.put("PASSWORD", "12345qwert(1");
		user.put("CONFIRM_PASSWORD", "12345qwert(1");
		user.put("EMAIL_ADDRESS", "xiyongjian@gmailb.com");
		user.put("ACCOUNT_NAME", "dev");
		user.put("FIRST_NAME", "yongjian");
		user.put("LAST_NAME", "test");
		user.put("OFFICE_PHONE", "1111111111");
		user.put("LANGUAGE", "en-US");

		JSONArray arr = new JSONArray();
		arr.put(user);

		JSONObject body = new JSONObject();
		body.put("ACCOUNTS", arr);
		body.put("USERS", arr);
		body.put("CONTACTS", arr);
		// mainObj.put("employees", ja)

		System.out.println("\nJSON : \n" + body.toString().replaceAll(",", ",\n"));
		
		JSONObject clone = new JSONObject(body.toString());
		clone.put("USERS", "");
		((JSONObject)((JSONArray)clone.get("ACCOUNTS")).get(0)).put("USERNAME",  "XXXX");
		System.out.println("\nclone and changed JSON : \n" + clone.toString().replaceAll(",", ",\n"));

		System.out.println("\noriginal JSON : \n" + body.toString().replaceAll(",", ",\n"));
	}

}

/*
{
  "ACCOUNTS":[{
    "TIMEZONE":"America/Chicago",
      "USERNAME":"jxi_test04",
      "PASSWORD":"12345qwert(1",
      "CONFIRM_PASSWORD":"12345qwert(1",
      "EMAIL_ADDRESS":"xiyongjian@gmailb.com",
      "ACCOUNT_NAME":"dev",
      "FIRST_NAME":"yongjian",
      "LAST_NAME":"test",
      "OFFICE_PHONE":"1111111111",
      "LANGUAGE":"en-US"
  }],
    "USERS":[{
      "TIMEZONE":"America/Chicago",
      "USERNAME":"jxi_test04",
      "PASSWORD":"12345qwert(1",
      "CONFIRM_PASSWORD":"12345qwert(1",
      "EMAIL_ADDRESS":"xiyongjian@gmailb.com",
      "ACCOUNT_NAME":"dev",
      "FIRST_NAME":"yongjian",
      "LAST_NAME":"test",
      "OFFICE_PHONE":"1111111111",
      "LANGUAGE":"en-US"
    }],
    "CONTACTS":[{
      "TIMEZONE":"America/Chicago",
      "USERNAME":"jxi_test04",
      "PASSWORD":"12345qwert(1",
      "CONFIRM_PASSWORD":"12345qwert(1",
      "EMAIL_ADDRESS":"xiyongjian@gmailb.com",
      "ACCOUNT_NAME":"dev",
      "FIRST_NAME":"yongjian",
      "LAST_NAME":"test",
      "OFFICE_PHONE":"1111111111",
      "LANGUAGE":"en-US"
    }]
}
*/
