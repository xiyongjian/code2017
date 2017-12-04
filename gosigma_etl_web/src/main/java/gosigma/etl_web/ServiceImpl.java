package gosigma.etl_web;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Transactional
@Service("Service")
public class ServiceImpl implements IService {

	final Repository repository;

	@Autowired
	public ServiceImpl(Repository repository) {
		this.repository = repository;
		Utils.log.info("repository hierarchy :\n" + Utils.getSupers(repository.getClass()));
	}

	// note: in rest api, set order by parameter,
	// url - localhost:8080/records?page=10&size=5&sort=key.dinterval,key.hour
	@Override
	public Page<Record> listAllByPage(Pageable pageable) {
		return this.repository.findAll(pageable);
	}

	@Override
	public Page<Record> listAllByPage2(Pageable pageable) {
		return this.repository.findAllDesc(pageable);
	}

	@Override
	public Page<Record> listAllByPage3(Pageable pageable) {
		return this.repository.findAllAsc(pageable);
		// return this.repository.findAllOrderByKeyDdateAndKeyHourAndKeyDinterval(pageable);
	}

	@Override
	public Page<Record> listAllByPage4(Pageable pageable) {
		PageRequest pr = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
				new Sort(new Order(Sort.Direction.ASC, "key.ddate"), 
						 new Order(Sort.Direction.ASC, "key.dinterval")));
		// return this.repository.findAll(pr);
		return this.repository.findAllCusomized(pr);
	}

}