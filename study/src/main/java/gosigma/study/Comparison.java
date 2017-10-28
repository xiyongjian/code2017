package gosigma.study;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Comparison {
	public static class RevString implements Comparable<RevString> {
		private String str;

		public String getStr() {
			return str;
		}

		public RevString(String s) {
			str = new String(s);
		}

		@Override
		public int compareTo(RevString o) {
			// TODO Auto-generated method stub
			return (new StringBuilder(getStr()).reverse().toString())
					.compareTo(new StringBuilder(o.getStr()).reverse().toString());
		}

		@Override
		public String toString() {
			return str;
		}
	}
	
	public static void main(String[] args) {
		List<String> list = Arrays.asList("apple", "orange", "hello", "world");
		System.out.println("original list : ");
		list.forEach(x -> System.out.println(x));
		Collections.sort(list);
		System.out.println("sorted list : ");
		list.forEach(x -> System.out.println(x));
		
		List<RevString> listRev = list.stream().map(x -> new RevString(x)).collect(Collectors.toList());
		System.out.println("original listRev : ");
		listRev.forEach(x -> System.out.println(x));
		System.out.println(listRev.stream().map(x -> x.toString()).collect(Collectors.joining(",")));
		Collections.sort(listRev);
		System.out.println("sorted listRev : ");
		listRev.forEach(x -> System.out.println(x));
		System.out.println(listRev.stream().map(x -> x.toString()).collect(Collectors.joining(",")));
	}

}
