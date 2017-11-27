package gosigma.study;

import java.util.Map;
import java.util.Map.Entry;

public class ExceptionApp {
	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}

	public static void listAllStackTraces() {
		p("listAllStackTraces");
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		for (Entry<Thread, StackTraceElement[]> e : map.entrySet()) {
			Thread t = e.getKey();
			StackTraceElement[] frames = e.getValue();
			for (int i = 0; i < frames.length; ++i) {
				p("thread " + t.toString() + ", frame " + i + " : " + frames[i].toString());
			}
		}
	}

	/**
	 * Computes the factorial of a number
	 * 
	 * @param n
	 *            a non-negative integer
	 * @return n! = 1 * 2 * . . . * n
	 */
	public static int factorial(int n) {
		System.out.println("factorial(" + n + "):");
		Throwable t = new Throwable();
		StackTraceElement[] frames = t.getStackTrace();
		for (StackTraceElement f : frames)
			System.out.println(f);
		int r;
		if (n <= 1)
			r = 1;
		else
			r = n * factorial(n - 1);
		System.out.println("return " + r);
		return r;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		listAllStackTraces();
		
		factorial(5);

	}

}
