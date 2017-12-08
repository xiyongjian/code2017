package gosigma.study.json;

import gosigma.study.json.Duplication.JSONObjectIgnoreDuplicates;

public class App {
	public static void p(String msg) { System.out.println(msg); }

	public static void main(String[] args) {
		usage01();
	}
	
	public static void usage01() {
		p("usage01()");
		String str = "{foo : x, bar: {baz: null, 1.0: 2}}";
		JSONObjectIgnoreDuplicates json = new JSONObjectIgnoreDuplicates(str);
		p("parse string to json : " + json.toString(4));
	}

}
