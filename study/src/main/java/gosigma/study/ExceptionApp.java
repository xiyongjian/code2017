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
			for (int i=0; i<frames.length; ++i) {
				p("thread " + t.toString() + ", frame " + i + " : " + frames[i].toString());
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		listAllStackTraces();

	}

}
