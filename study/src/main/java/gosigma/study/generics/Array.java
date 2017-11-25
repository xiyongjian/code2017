package gosigma.study.generics;

public class Array {
	static public void main(String[] args) {
		test01();
	}

	
	public static class Test<T> {
		public T[] array;
		public Test(T[] arr) { array = arr; }
		public T get(int i) { return array[i]; }
	}
	static public void test01() {
		System.out.println("test01()");

		Integer[] array = new Integer[] {1,2,3,4,5,6,7,8};
		Test<Integer> test = new Test(array);
		System.out.println("Test, get(2): " + test.get(2));
	}

}
