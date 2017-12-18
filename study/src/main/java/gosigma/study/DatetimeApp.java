package gosigma.study;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatetimeApp {
	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) {
		// test01();
		testDatePlus();
	}

	// SimpleDateFormat 
	//	G	Era designator (before christ, after christ)
	//	y	Year (e.g. 12 or 2012). Use either yy or yyyy.
	//	M	Month in year. Number of M's determine length of format (e.g. MM, MMM or MMMMM)
	//	d	Day in month. Number of d's determine length of format (e.g. d or dd)
	//	h	Hour of day, 1-12 (AM / PM) (normally hh)
	//	H	Hour of day, 0-23 (normally HH)
	//	m	Minute in hour, 0-59 (normally mm)
	//	s	Second in minute, 0-59 (normally ss)
	//	S	Millisecond in second, 0-999 (normally SSS)
	//	E	Day in week (e.g Monday, Tuesday etc.)
	//	D	Day in year (1-366)
	//	F	Day of week in month (e.g. 1st Thursday of December)
	//	w	Week in year (1-53)
	//	W	Week in month (0-5)
	//	a	AM / PM marker
	//	k	Hour in day (1-24, unlike HH's 0-23)
	//	K	Hour in day, AM / PM (0-11)
	//	z	Time Zone
	//	'	Escape for text delimiter
	//	'	Single quote

	public static void test01() {
		p("test01(), parsing date");
		try {
			// Locale locale = new Locale("en", "US");
			Locale locale = new Locale("en", "CA");
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy", dateFormatSymbols);

			String str = "Sat Dec 16 19:40:00 EST 2017";
			// String str = "Thu 11/16/2017";
			// DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy", dateFormatSymbols);
			p("current date : " + df.format(new Date()));

			p("try to parse : " + str);
			Date date = df.parse(str);
			p("result date : " + date.toString());
			p("result date : " + new SimpleDateFormat("yyyyMMdd-HHmm").format(date));

			str = "Sun Dec 16 19:40:00 EST 2017";
			p("try to parse : " + str);
			df = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", dateFormatSymbols);
			date = df.parse(str.substring(4).replace("EST ", ""));
			p("result date : " + new SimpleDateFormat("yyyyMMdd-HHmm").format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void testDatePlus() {
		try {
			p("testDatePlus(),  date +/- testing");
			Locale locale = new Locale("en", "CA");
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy", dateFormatSymbols);
			p("current date : " + df.format(new Date()));

			String str = "Sat Dec 29 19:40:00 EST 2017";
			// String str = "Thu 11/16/2017";
			// DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'EST' yyyy", dateFormatSymbols);
			Date date = df.parse(str);
			p("parse : " + str + ", result : " + date.toString());
			
			Calendar c = Calendar.getInstance(); 
			c.setTime(date); 
			c.add(Calendar.DATE, 1);
			date = c.getTime();
			p("plus 1 days : " + date.toString());

			c.add(Calendar.DATE, 10);
			date = c.getTime();
			p("plus another 10 days : " + date.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
