package gosigma.etl;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.JoranException;

public class DateKey {
	public Logger log = LoggerFactory.getLogger(DateKey.class);
	private DateFormatSymbols _dfs = null;
	private Date _date = null;
	private boolean _zeroBasedHour = false;
	private boolean _zeroBasedInterval = false;

	private String _keyDate = null;
	private int _keyHour = 0;
	private int _keyInterval = 0;

	public String getKeyDate() {
		return _keyDate;
	}

	public int getKeyHour() {
		return _keyHour;
	}

	public void setKeyHour(int h) {
		log.info("Entering...  hour : " + h + ", zeror based? : " + this.isZeroBasedHour());
		Date date = this.getDate();
		Calendar c = GregorianCalendar.getInstance(); // creates a new calendar instance
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, h - (this.isZeroBasedHour() ? 0 : 1));
		this.setDate(c.getTime());
	}

	public int getKeyInterval() {
		return _keyInterval;
	}

	public DateFormatSymbols getDfs() {
		if (_dfs == null) {
			Locale locale = new Locale("en", "US");
			_dfs = new DateFormatSymbols(locale);
		}
		return _dfs;
	}

	public void setDfs(DateFormatSymbols dfs) {
		_dfs = dfs;
	}

	public Date getDate() {
		return _date;
	}

	public void setDate(Date date) {
		_date = date;

		Calendar c = GregorianCalendar.getInstance(); // creates a new calendar instance
		c.setTime(date);

		this._keyDate = this.format("yyyyMMdd");
		this._keyHour = c.get(Calendar.HOUR_OF_DAY) + (this.isZeroBasedHour() ? 0 : 1);
		this._keyInterval = c.get(Calendar.MINUTE) / 5 + (this.isZeroBasedInterval() ? 0 : 1);
	}

	public void setDate(String dateString, String fmt) throws ParseException {
		log.info("Entering... str : " + dateString + ", format : " + fmt);
		DateFormat df = new SimpleDateFormat(fmt, this.getDfs());
		this.setDate(df.parse(dateString));
	}

	public boolean isZeroBasedHour() {
		return _zeroBasedHour;
	}

	public void setZeroBasedHour(boolean zeroBasedHour) {
		_zeroBasedHour = zeroBasedHour;
	}

	public boolean isZeroBasedInterval() {
		return _zeroBasedInterval;
	}

	public void setZeroBasedInterval(boolean zeroBasedInterval) {
		_zeroBasedInterval = zeroBasedInterval;
	}

	public void addHours(int hours) {
		log.info("Entering... " + hours);
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.getDate()); // assigns calendar to given date 
		calendar.add(Calendar.HOUR, hours); // minus one day for this feed (T-1)
		this.setDate(calendar.getTime());
	}

	public void addDays(int days) {
		log.info("Entering... " + days);
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(this.getDate()); // assigns calendar to given date 
		calendar.add(Calendar.DATE, days); // minus one day for this feed (T-1)
		this.setDate(calendar.getTime());
	}

	public String format(String fmt) {
		DateFormat df = new SimpleDateFormat(fmt, this.getDfs());
		return df.format(this.getDate());
	}

	@Override
	public String toString() {
		return "DateKey [log=" + log + ", _dfs=" + _dfs + ", _date=" + _date + ", _zeroBasedHour=" + _zeroBasedHour
				+ ", _zeroBasedInterval=" + _zeroBasedInterval + ", _keyDate=" + _keyDate + ", _keyHour=" + _keyHour
				+ ", _keyInterval=" + _keyInterval + "]";
	}

	public static DateKey build(Date date) {
		DateKey dk = new DateKey();
		dk.setDate(date);
		return dk;
	}

	public static DateKey build(String str, String fmt) throws ParseException {
		DateKey dk = new DateKey();
		dk.setDate(str, fmt);
		return dk;
	}

	public static void main(String[] args) throws JoranException, ParseException {
		DateKey dk = new DateKey();
		{
			Utils.initLog();
			String str = "18-Dec-2017 12:20";
			String fmt = "dd-MMM-yyyy HH:mm";
			dk.log.info("DateKey initial is : " + dk.toString());

			dk.setDate(str, fmt);
			dk.log.info("Date key set : " + dk.getDate().toString());
			dk.log.info("key date : " + dk.getKeyDate());
			dk.log.info("key hour : " + dk.getKeyHour());
			dk.log.info("key interval : " + dk.getKeyInterval());
			dk.log.info(
					"key : " + String.format("%s_%02d%02d", dk.format("yyyyMMdd"), dk.getKeyHour(),
							dk.getKeyInterval()));

			fmt = "dd-MMM-yyyy hh:mm"; // HH vs hh
			dk.setDate(str, fmt);
			dk.log.info("Date key set : " + dk.toString());
			dk.log.info("Date key set : " + dk.getDate().toString());
			dk.log.info("key date : " + dk.getKeyDate());
			dk.log.info("key hour : " + dk.getKeyHour());
			dk.log.info("key interval : " + dk.getKeyInterval());

			dk.log.info(
					"key : " + String.format("%s_%02d%02d", dk.format("yyyyMMdd"), dk.getKeyHour(),
							dk.getKeyInterval()));
		}

		{
			Calendar c = GregorianCalendar.getInstance(); // creates a new calendar instance
			c.setTime(new Date());
			dk.log.info("initial date : " + c.getTime().toString());

			c.add(Calendar.DAY_OF_MONTH, -1);
			dk.log.info("initial date : " + c.getTime().toString());

			c.set(Calendar.HOUR_OF_DAY, 23);
			dk.log.info("initial date : " + c.getTime().toString());

//			Date date = c.getTime();
//			dk.log.info("initial date : " + c.getTime().toString());
		}
		
		{
			dk.log.info("current dk : " + dk.getDate().toString());
			dk.setKeyHour(24);
			Calendar c = GregorianCalendar.getInstance();
			c.setTime(dk.getDate());
			
			dk.log.info("calender, hour : " + c.get(Calendar.HOUR));
			dk.log.info("calender, hour of day : " + c.get(Calendar.HOUR_OF_DAY));
			dk.log.info("calender, minute : " + c.get(Calendar.MINUTE));
			dk.log.info("calender, day : " + c.get(Calendar.DAY_OF_MONTH));

		}
	}

}
