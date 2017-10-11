package gosigma.study;

public class AnonymousClass {

	public interface HelloWorld {
		public void greet();
	}
	
	public class Inner {
		public Inner(String s) {}
		public void greet() {}
	}

	public String msg = "hello, world";

	static public void main(String[] args) {
		new AnonymousClass().test01();
	}

	public void test01() {
		final String msg = "hello, world";

		HelloWorld hw = new HelloWorld() {
			public void greet() {
				System.out.println(AnonymousClass.this.msg);
				System.out.println(msg);
			}
		};
		
		hw.greet();
		
		Inner in = new Inner("inner") {
			public void greet() {
				System.out.println("inner");
			}
		};
		
		in.greet();
	}
}
