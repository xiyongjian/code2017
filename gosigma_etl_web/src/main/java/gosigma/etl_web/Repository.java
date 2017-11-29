package gosigma.etl_web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface Repository extends JpaRepository<Record, RecordKey> {
	// note: use class name 'Record' instead of actual table name
	@Query("select r from Record r order by key.ddate desc, key.hour desc, key.dinterval desc")
	public Page<Record> findAllDesc(Pageable pageable);

	@Query("select r from Record r order by key.ddate asc, key.hour asc, key.dinterval asc")
	public Page<Record> findAllAsc(Pageable pageable);

	// note: access composite key for order
	// this one NOT work!!!
	public Page<Record> findAllOrderByKeyDdateAndKeyHourAndKeyDinterval(Pageable pageable);

	@Query("select r from Record r")
	public Page<Record> findAllCusomized(Pageable pageable);
}
