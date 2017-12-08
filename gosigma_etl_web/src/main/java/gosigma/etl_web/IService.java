package gosigma.etl_web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface IService {
	Page<Record> listAllByPage(Pageable pageable);
	Page<Record> listAllByPage2(Pageable pageable);
	Page<Record> listAllByPage3(Pageable pageable);
	Page<Record> listAllByPage4(Pageable pageable);
}
