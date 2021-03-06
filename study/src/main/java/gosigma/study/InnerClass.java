package gosigma.study;

import java.util.concurrent.Callable;

public class InnerClass {

	public interface HelloWorld {
		public void greet();
	}
	
	public class Inner {
		public Inner(String s) {}
		public void greet() {}
	}

	public String msg = "hello, world";

	static public void main(String[] args) {
		new InnerClass().test01();
	}

	public void test01() {
		final String msg = "hello, world";

		HelloWorld hw = new HelloWorld() {
			public void greet() {
				System.out.println(InnerClass.this.msg);
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
		
		Runnable lambda = () -> {
			System.out.println("lambda");
			System.out.println(InnerClass.this.msg);
		};
		
		lambda.run();
		
		Callable<String> call = () -> {
			return "Callable again";
		};
		try {
			System.out.println("callable : " + call.call());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
