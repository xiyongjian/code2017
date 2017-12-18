package gosigma.study;

public class StringApp {
	public static void p(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		String s = "hello, 		, world 	and here no		matt";
		p("string : " + s);
		p("remove space \\\\s: " + s.replaceAll("\\s",  ""));
		p("remove space ' ': " + s.replaceAll(" ",  ""));
		
		p("match Worl : " + s.matches(".*Worl.*"));
		p("match (?i)Worl : " + s.matches(".*(?i)Worl.*"));
	}

}
