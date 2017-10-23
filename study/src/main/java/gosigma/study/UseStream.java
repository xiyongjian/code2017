package gosigma.study;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.stream.Stream;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.lf5.util.StreamUtils;

import static gosigma.Util.*;

public class UseStream {

	public static void main(String[] args) {
		// test01();
		// peakCount();
		// peakCountParallel();
		// supplier();
		// lazyEval();
		// infinite();
		memUsage();
	}

	public static void test01() {
		System.out.println("\ntest01");

		Integer[] arr = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
		List<Integer> l01 = Arrays.asList(arr);
		List<Integer> l02 = new ArrayList<>(Arrays.asList(arr));

		System.out.println("filter even number");
		System.out.println("lambda version");
		l01.stream().filter(x -> x % 2 == 0).forEach(x -> System.out.println(x));

		System.out.println("inner class version");
		l01.stream().filter(new Predicate<Integer>() {
			@Override
			public boolean test(Integer x) {
				// TODO Auto-generated method stub
				return x % 2 == 0;
			}
		}).forEach(x -> System.out.println(x));

		System.out.println("\nmapping");
		l01.stream().filter(x -> x % 2 == 0).map(x -> new String("number : " + x)).forEach(x -> System.out.println(x));
		System.out.println("\nmapping inner class version");
		l01.stream().filter(x -> x % 2 == 0).map(new Function<Integer, String>() {
			@Override
			public String apply(Integer t) {
				// TODO Auto-generated method stub
				return new String("number " + t);
			}
		}).forEach(x -> System.out.println(x));

		System.out.println("\ncollection");
		List<String> l03 = l01.stream().filter(x -> x % 2 == 0).map(x -> new String("number : " + x))
				.collect(Collectors.toList());
		List<String> l04 = l01.stream().filter(x -> x % 2 == 0).map(x -> new String("number : " + x))
				.collect(Collectors.toCollection(ArrayList::new));
		l03.forEach(x -> System.out.println(x));
		l04.forEach(x -> System.out.println(x));

		System.out.println("\nparallel");
		List<String> l05 = l01.stream().parallel().filter(x -> x > 5).sequential().map(x -> new String(">5 " + x))
				.collect(Collectors.toList());
		l05.forEach(x -> System.out.println(x));
	}

	public static void peakCount() {
		System.out.println("\npeakCount");
		IntStream stream = IntStream.range(1, 5);
		stream = stream.peek(i -> log("starting", i)).filter(i -> {
			log("filtering", i);
			return i % 2 == 0;
		}).peek(i -> log("post filtering", i));
		log("Invoking terminal method count.");
		log("The count is", stream.count());
	}

	public static void log(Object... objects) {
		String s = LocalTime.now().toString();
		for (Object object : objects) {
			s += " - " + object.toString();
		}
		System.out.println(s);
		// putting a little delay so that we can see a clear difference
		// with parallel stream.
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void peakCountParallel() {
		System.out.println("\npeakCountParallel()");
		IntStream stream = IntStream.range(1, 5).parallel();
		stream = stream.peek(i -> log("starting", i)).filter(i -> {
			log("filtering", i);
			return i % 2 == 0;
		}).peek(i -> log("post filtering", i));
		log("Invoking terminal method count.");
		log("The count is", stream.count());
	}

	public static void supplier() {
		System.out.println("\nsupplier()");
		List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");

		myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).sorted().forEach(System.out::println);

		IntStream.range(1, 10).forEach(System.out::println);
		// IntStream.range(1, 10).toArray();

		java.util.stream.Stream<String> x = myList.stream().filter(s -> s.startsWith("a"));

		Supplier<java.util.stream.Stream<String>> streamSupplier = () -> myList.stream().filter(s -> s.startsWith("a"));

		System.out.println("anyMatch : " + streamSupplier.get().anyMatch(s -> true)); // ok
		System.out.println("nonemathc : " + streamSupplier.get().noneMatch(s -> true)); // ok
	}

	public static void lazyEval() {
		p("\nlazyEval(), lazyness of stream (infinite..)");

		String mem = memInfo();
		// IntStream.range(1, 10).forEach(System.out::println);
		IntStream.range(1, 10000);
		pmem(mem);
		IntStream.range(1, 10000).filter(x -> x % 2 == 0);
		pmem();
		IntStream.range(1, 10000).filter(x -> x % 2 == 0).map(x -> x * x);
		pmem();
		IntStream s05 = IntStream.range(1, 10010000).filter(x -> x % 2 == 0).map(x -> x * x);
		pmem();
		java.util.stream.Stream<Object> s06 = IntStream.range(1, 10010000).filter(x -> x % 2 == 0)
				.mapToObj(x -> String.format("%10d", x));
		mem = pmem();
		List<Object> l01 = Arrays.asList(s06.limit(1000).toArray());
		final Integer I = new Integer(0);
		l01.stream().limit(10).forEach(x -> o(x.toString() + "|"));
		// foreach(l01, (x,i,n) -> o(String.format("%d/%d %s", i, n, x.toString())));
		p("");
		pmem(mem);
	}

	public static void infinite() {
		p("\ninfinite() stream");
		String mem = pmem();

		p("create four stream in range");
		IntStream s01 = IntStream.range(1, 10000);
		IntStream s02 = IntStream.range(3, 5000);
		Iterator<String> i01 = s01.mapToObj(x -> String.format("%10d", x)).iterator();
		Iterator<String> i02 = s02.mapToObj(x -> String.format("%10d", x)).iterator();

		mem = pmem(mem);
		p("create two infinite stream via iterate/generate");
		Stream.iterate(new Long(2), (Long n) -> n * n).limit(6).forEach(System.out::println);
		Stream.generate(Math::random).limit(5).forEach(System.out::println);
		mem = pmem(mem);

		p("fibonacci series");
		{
			// final List<Iterator<Integer>> outerIt = new ArrayList();
			UnaryOperator<Integer> op = new UnaryOperator<Integer>() {
				public int _val = 0;

				@Override
				public Integer apply(Integer t) {
					// Iterator<Integer> it = outerIt.get(0);
					int rsl = _val + t;
					_val = t;
					p("apply : " + _val + ", " + rsl);
					return rsl;
				}
			};
			Stream<Integer> fibonacci = Stream.iterate(1, op);
			fibonacci.limit(10).forEach(x -> p(x.toString()));

		}
		mem = pmem(mem);

		p("fibonacci series2");
		{
			final int[] prev = new int[1];
			Stream<Integer> fibonacci = Stream.iterate(1, x -> {
				int y = x + prev[0];
				prev[0] = x;
				p("apply : " + x + ", " + y);
				return y;
			});
			fibonacci.limit(20).forEach(x -> p(x.toString()));

		}
		mem = pmem(mem);
	}

	public static void memUsage() {
		p("\nmemUsage() test memory usage by streams");
		System.gc();
		String mem = pmem();
		p("max/laste one : " + IntStream.range(1, 1234567890).reduce((x, y) -> y + 88));
		mem = pmem(mem);
		p("after gc");
		System.gc();
		mem = pmem(mem);

		int r01 = IntStream.range(1, 1000000).reduce(0, (x, y) -> {
			if ((x % 100000) == 0) {
				System.gc();
				o("- " + x + " - "); pmem();
			}
			return x > y ? x : y;
		});
		p("-- r01 -- " + r01);
		mem = pmem(mem);
		p("after gc");
		System.gc();
		mem = pmem(mem);
	}
}
