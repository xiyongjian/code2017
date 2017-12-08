package gosigma.study.json;

import org.json.JSONArray;
import org.json.JSONObject;

import gosigma.study.json.Duplication.JSONObjectIgnoreDuplicates;

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
		
		JSONObject user2 = new JSONObject();
		user2.put("TIMEZONE", "America/Chicago");
		user2.put("USERNAME", "jxi_test05");
		user2.put("PASSWORD", "12345qwert(1");
		user2.put("CONFIRM_PASSWORD", "12345qwert(1");
		user2.put("EMAIL_ADDRESS", "xiyongjian@gmailb.com");

		JSONArray arr = new JSONArray();
		arr.put(user);
		arr.put(user2);
		// arr.put(user);

		JSONObject body = new JSONObject();
		body.put("ACCOUNTS", arr);
		body.put("USERS", arr);
		body.put("CONTACTS", arr);
		// mainObj.put("employees", ja)

		System.out.println("\nJSON : \n" + body.toString(4));
		
		JSONObject clone = new JSONObject(body.toString());
		clone.put("USERS", "");
		((JSONObject)((JSONArray)clone.get("ACCOUNTS")).get(0)).put("USERNAME",  "XXXX");
		System.out.println("\nclone and changed JSON : \n" + clone.toString(4));

		System.out.println("\noriginal JSON : \n" + body.toString(4));
		
		JSONObjectIgnoreDuplicates json = new JSONObjectIgnoreDuplicates(clone.toString());
		System.out.println("parse string to json : " + json.toString(4));
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
