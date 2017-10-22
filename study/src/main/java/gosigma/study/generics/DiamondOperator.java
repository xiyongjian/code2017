package gosigma.study.generics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DiamondOperator {

	public static void main(String[] args) {
		test01();
		test02();
	}

	public static void test01() {
		System.out.println("test01");

		Map<String, String> map = new HashMap<>();
		map.put("key", "value");

		for (Entry<String, String> x : map.entrySet()) {
			System.out.println("key : " + x.getKey() + ", value : " + x.getValue());
		}

		Map<String, String> map2 = new HashMap();
	}

	/** Use of "raw" type. */
	private static Set<String> rawWithoutExplicitTyping() {
		final Set<String> names = new HashSet();
		addNames(names);
		return names;
	}

	/** Explicitly specifying generic class's instantiation parameter type. */
	private static Set<String> explicitTypingExplicitlySpecified() {
		final Set<String> names = new HashSet<String>();
		addNames(names);
		return names;
	}

	/**
	 * Inferring generic class's instantiation parameter type with JDK 7's 'Diamond
	 * Operator.'
	 */
	private static Set<String> explicitTypingInferredWithDiamond() {
		final Set<String> names = new HashSet<>();
		addNames(names);
		return names;
	}

	private static void addNames(final Set<String> namesToAddTo) {
		namesToAddTo.add("Dustin");
		namesToAddTo.add("Rett");
		namesToAddTo.add("Homer");
	}

	/**
	 * Main executable function.
	 */
	public static void test02() {
		System.out.println("test02()");
		System.out.println(rawWithoutExplicitTyping());
		System.out.println(explicitTypingExplicitlySpecified());
		System.out.println(explicitTypingInferredWithDiamond());
	}
}
