package gosigma.study;

import java.util.stream.Stream;

public class InfiniteStream {
	public static void main(String args[]) {
		Stream.iterate(2, (Integer n) -> n * n).limit(5).forEach(System.out::println);
	}
}
