package gosigma.study.mock;

import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

public class MockIt {

	public static void main(String[] args) {
		test01();
	}

	public static void test01() {
	    // Lets mock a LinkedList
	    List<String> list = new LinkedList<>();
	    List<String> spy = spy(list);

	    // this does not work
	    // real method is called so spy.get(0)
	    // throws IndexOutOfBoundsException (list is still empty)
	    when(spy.get(0)).thenReturn("foo");

	    String x = spy.get(0);
	    System.out.println("spy.get(0) : " + x);

	    x = spy.get(1);
	    System.out.println("spy.get(1) : " + x);

	}


}
