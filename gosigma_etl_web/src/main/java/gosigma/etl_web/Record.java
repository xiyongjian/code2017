package gosigma.etl_web;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// @Entity(name = "`IESO_RealtimeConstTotals`")	// for case sensitive mariadb
@Table(name = "IESO_RealtimeConstTotals")	// for case sensitive mariadb
public class Record {
	@Id
	@EmbeddedId
	private RecordKey key;

	@Column
	// @Column(name = "total_energy")
	double total_energy;

	@Column
	double total_10s;

	@Column
	double total_10n;

	@Column
	double total_30r;

	@Column
	double total_disp_load;

	@Column
	double total_load;

	@Column
	double total_loss;

	public RecordKey getKey() {
		return key;
	}

	public void setKey(RecordKey key) {
		this.key = key;
	}

	public double getTotal_energy() {
		return total_energy;
	}

	public void setTotal_energy(double total_energy) {
		this.total_energy = total_energy;
	}

	public double getTotal_10s() {
		return total_10s;
	}

	public void setTotal_10s(double total_10s) {
		this.total_10s = total_10s;
	}

	public double getTotal_10n() {
		return total_10n;
	}

	public void setTotal_10n(double total_10n) {
		this.total_10n = total_10n;
	}

	public double getTotal_30r() {
		return total_30r;
	}

	public void setTotal_30r(double total_30r) {
		this.total_30r = total_30r;
	}

	public double getTotal_disp_load() {
		return total_disp_load;
	}

	public void setTotal_disp_load(double total_disp_load) {
		this.total_disp_load = total_disp_load;
	}

	public double getTotal_load() {
		return total_load;
	}

	public void setTotal_load(double total_load) {
		this.total_load = total_load;
	}

	public double getTotal_loss() {
		return total_loss;
	}

	public void setTotal_loss(double total_loss) {
		this.total_loss = total_loss;
	}

	@Override
	public String toString() {
		return "Record [key=" + key + ", total_energy=" + total_energy + ", total_10s=" + total_10s + ", total_10n="
				+ total_10n + ", total_30r=" + total_30r + ", total_disp_load=" + total_disp_load + ", total_load="
				+ total_load + ", total_loss=" + total_loss + "]";
	}

}
