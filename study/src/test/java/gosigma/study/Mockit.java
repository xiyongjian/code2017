package gosigma.study;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.TestCase;

public class Mockit extends TestCase {
	@Test
	public void testLinkedListSpyWrong() {
		// Lets mock a LinkedList
		List<String> list = new LinkedList<>();
		List<String> spy = Mockito.spy(list);
		spy.add("hello");
		spy.add("hello");

		System.out.println("get(0): " + spy.get(0));
		System.out.println("get(1): " + spy.get(1));

		// this does not work
		// real method is called so spy.get(0)
		// throws IndexOutOfBoundsException (list is still empty)
		when(spy.get(0)).thenReturn("foo");

		System.out.println("get(0): " + spy.get(0));
		System.out.println("get(1): " + spy.get(1));

		assertEquals("foo", spy.get(0));
	}

	@Test
	public void testWhenSpyingOnList_thenCorrect() {
		List<String> list = new ArrayList<String>();
		List<String> spyList = Mockito.spy(list);

		spyList.add("one");
		spyList.add("two");

		Mockito.verify(spyList).add("one");
		Mockito.verify(spyList).add("two");

		assertEquals(2, spyList.size());
	}

	@Test
	public void testMockAndSpy() {
		{
			// mock creation
			List mockedList = mock(List.class);

			// using mock object - it does not throw any "unexpected interaction" exception
			mockedList.add("one");
			mockedList.clear();

			// selective, explicit, highly readable verification
			verify(mockedList).add("one");
			verify(mockedList).clear();
		}

		{
			// you can mock concrete classes, not only interfaces
			LinkedList mockedList = mock(LinkedList.class);

			// stubbing appears before the actual execution
			when(mockedList.get(0)).thenReturn("first");

			// the following prints "first"
			System.out.println(mockedList.get(0));

			// the following prints "null" because get(999) was not stubbed
			System.out.println(mockedList.get(999));
		}

	}
}
