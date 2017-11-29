package gosigma.etl_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {
	
	final IService service;
	
	@Autowired
	public Controller( IService service ){
		this.service = service;
	}
	
	@RequestMapping(value="/records",method=RequestMethod.GET)
	public Page<Record> list( Pageable pageable){
		Page<Record> persons = service.listAllByPage(pageable);
		return persons;
	} 

	@RequestMapping(value="/records2",method=RequestMethod.GET)
	public Page<Record> list2( Pageable pageable){
		Page<Record> persons = service.listAllByPage2(pageable);
		return persons;
	} 

	@RequestMapping(value="/records3",method=RequestMethod.GET)
	public Page<Record> list3( Pageable pageable){
		Page<Record> persons = service.listAllByPage3(pageable);
		return persons;
	} 

	@RequestMapping(value="/records4",method=RequestMethod.GET)
	public Page<Record> list4( Pageable pageable){
		Page<Record> persons = service.listAllByPage4(pageable);
		return persons;
	} 
}