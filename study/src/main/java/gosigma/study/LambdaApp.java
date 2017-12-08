package gosigma.study;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaApp {

	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}

	public static void main(String[] args) {
		useAsCtor();
	}

	public static class Person {
		public String _name;

		public Person() {
			_name = "John Doe";
		}

		public Person(String n) {
			_name = n;
		}

		@Override
		public String toString() {
			return "Person [_name=" + _name + "]";
		}
	}

	public static void useAsCtor() {
		p("useAsCtor");

		List<String> names = Arrays.asList("James Xi", "John Xi", "NONE");
		names.stream().forEach(x -> p("name : " + x));

		{
			p("via stream collector");
			Stream<Person> stream = names.stream().map(Person::new);
			List<Person> people = stream.collect(Collectors.toList());
			people.forEach(x -> p("people : " + x.toString()));
		}

		{
			p("via stream.toArray with ctor");
			Stream<Person> stream = names.stream().map(Person::new);
			Person[] people = stream.toArray(Person[]::new);
			Arrays.asList(people).forEach(x -> p("people : " + x.toString()));
		}

		{
			p("via stream.toArray with lambda");
			Stream<Person> stream = names.stream().map(Person::new);
			Person[] people = stream.toArray(n -> new Person[n]);
			Arrays.asList(people).forEach(x -> p("people : " + x.toString()));
		}
	}
}
