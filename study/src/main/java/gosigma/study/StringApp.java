package gosigma.study;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringApp {
	public static void p(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		// test01();
		// regexMatch();
		regexSearch();
	}

	public static void test01() {
		String s = "hello, 		, world 	and here no		matt";
		p("string : " + s);
		p("remove space \\\\s: " + s.replaceAll("\\s", ""));
		p("remove space ' ': " + s.replaceAll(" ", ""));

		p("match Worl : " + s.matches(".*Worl.*"));
		p("match (?i)Worl : " + s.matches(".*(?i)Worl.*"));

		p("%s_ test : " + String.format("xx %s_xx", "hello"));
	}

	public static void regexMatch() {
		try {
			String str = "RefId=18-Dec-2017 12:20";
			Pattern regex = Pattern.compile("RefId=((\\d{2})-(\\w{3})-(\\d{4}) (\\d{2}):(\\d{2}))");
			p("string : " + str);
			p("regex : " + regex.toString());
			Matcher m = regex.matcher(str);
			if (m.matches()) {
				p("match group 0 : " + m.group(0));
				p("match group 1 : " + m.group(1));
				p("match group 2 : " + m.group(2));
				p("match group 3 : " + m.group(3));
				p("match group 4 : " + m.group(4));
				p("match group 5 : " + m.group(5));
				p("match group 6 : " + m.group(6));

				Locale locale = new Locale("en", "CA");
				DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm", dateFormatSymbols);
				Date d = df.parse(m.group(1));
				p("parse date : " + d.toString());
			} else
				p("not match");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void regexSearch() {
		String str = "abcdefghijklmnopq";
		Pattern regex = Pattern.compile("e(\\w{3})");
		p("string : " + str);
		p("regex : " + regex.toString());
		Matcher m = regex.matcher(str);
		if (m.find()) {
			p("found : " + m.group(0) + ", " + m.group(1));
		}
	}
}
