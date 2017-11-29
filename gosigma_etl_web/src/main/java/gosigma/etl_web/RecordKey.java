package gosigma.etl_web;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class RecordKey implements Serializable {
	@Column // (name = "customer_id")
	private Date ddate;

	@Column // (name = "customer_id")
	private int hour;

	@Column // (name = "customer_id")
	private int dinterval;

	public Date getDdate() {
		return ddate;
	}

	public void setDdate(Date ddate) {
		this.ddate = ddate;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getDinterval() {
		return dinterval;
	}

	public void setDinterval(int dinterval) {
		this.dinterval = dinterval;
	}
}
