package gosigma.study;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionBP {

	final static Logger logger = LoggerFactory.getLogger(CollectionBP.class);

	public static void main(String[] args) {
		logger.info("Entering...");
		logger.info("BP for Best Practice");

		readOnly();
		stream();

		logger.info("Leaving...");
	}

	public static void readOnly() {
		logger.info("Entering...");
		try {
			Set<String> set = new HashSet<String>();
			set.add("Java");
			set.add("JEE");
			set.add("Spring");
			set.add("Hibernate");
			logger.info("set type : " + set.getClass().getName());
			set = Collections.unmodifiableSet(set);
			logger.info("unmodifiable set type : " + set.getClass().getName());
			set.add("Ajax"); // not allowed.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		logger.info("Leaving...");
	}

	public static void stream() {
		logger.info("Entering...");
		List<String> myList = Arrays.asList("Java", "JEE", "JDBC", "jNDI", "Hibernate", "Spring");

		// Functional Programming
		myList.stream().filter(s -> s.startsWith("j")).map(String::toUpperCase).sorted().forEach(System.out::println);
		logger.info("Leaving...");
	}
}
