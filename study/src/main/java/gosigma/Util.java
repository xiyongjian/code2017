package gosigma;

public class Util {
	// o/p for output shortcut, save typing
	static public void o(String msg) {
		System.out.print(msg);
	}

	static public void p(String msg) {
		System.out.println(msg);
	}
	
	static public String memInfo() {
		return String.format("total/free/used : %10d %10d %10d", 
				Runtime.getRuntime().totalMemory(),
				Runtime.getRuntime().freeMemory(),
				Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	static public String pmem() {
		String t = memInfo();
		p(t);
		return t;
	}

	static public String pmem(String s) {
		p(s);
		return pmem();
	}

	public static void main(String[] args) {
		o("hello, world\n");
		p("hello, world");
		pmem();
		p(memInfo());
	}
}
