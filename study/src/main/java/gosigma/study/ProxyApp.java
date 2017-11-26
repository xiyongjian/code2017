package gosigma.study;

import java.lang.reflect.*;
import java.util.*;

/**
 * This program demonstrates the use of proxies.
 * 
 * @version 1.00 2000-04-13
 * @author Cay Horstmann
 */
public class ProxyApp {

	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}

	public static void main(String[] args) {
		proxyComparable();

		listInterfaces(TraceHandler.class);
		listInterfaces(ProxyApp.class);
		listInterfaces(ArrayList.class);

		listInterfacesRecur(TraceHandler.class);
		listInterfacesRecur(ProxyApp.class);
		listInterfacesRecur(ArrayList.class);
	}

	public static void proxyComparable() {
		p("proxyComparable");
		Object[] elements = new Object[1000];

		// fill elements with proxies for the integers 1 ... 1000
		for (int i = 0; i < elements.length; i++) {
			Integer value = i + 1;
			InvocationHandler handler = new TraceHandler(value);
			Object proxy = Proxy.newProxyInstance(null, new Class[] { Comparable.class }, handler);
			elements[i] = proxy;
		}

		// construct a random integer
		Integer key = new Random().nextInt(elements.length) + 1;
		// search for the key
		int result = Arrays.binarySearch(elements, key);

		// print match if found
		if (result >= 0)
			System.out.println(elements[result]);
	}

	public static void listInterfaces(Class clazz) {
		p("\nlist interfaces&super class of  : " + clazz.getName());
		// `Class clazz = TraceHandler.class; //someObject.getClass();
		for (Class x : clazz.getInterfaces()) {
			p("interface : " + x.getName());
		}
		p("super class : " + clazz.getSuperclass().getName());
	}

	public static void listInterfacesRecur(Class clazz) {
		p("\nrecursively list interfaces&super class of  : " + clazz.getName());
		// `Class clazz = TraceHandler.class; //someObject.getClass();
		for (Object x : getSuperInterfaces(clazz)) {
			p("interface : " + ((Class)x).getName());
		}
		for (Object x : getSuperClasses(clazz)) {
			p("class : " + ((Class)x).getName());
		}
	}

	public static <T> Set<Class<? super T>> getSuperClasses(Class<? super T> clazz) {
		Set<Class<? super T>> superclasses = new HashSet<>();
		if (clazz.getSuperclass() != null) {
			superclasses.add(clazz.getSuperclass());
			superclasses.addAll(getSuperClasses(clazz.getSuperclass()));
		}
		return superclasses;
	}

	public static <T> Set<Class<? super T>> getSuperInterfaces(Class<T> clazz) {
		Set<Class<? super T>> superInterfaces = new HashSet<>();
		if (clazz.getInterfaces().length != 0) {
			// Only keep the one you use
			// Java 7:
			for (Class<?> superInterface : clazz.getInterfaces()) {
				// noinspection unchecked
				superInterfaces.add((Class<? super T>) superInterface);
				for (Class x : getSuperInterfaces(superInterface))
					superInterfaces.add(x);
			}
			// // Java 8:
			// // noinspection unchecked
			// Arrays.stream(clazz.getInterfaces()).map(c -> (Class<? super T>)
			// c).forEach(superInterfaces::add);
		}
		return superInterfaces;
	}

}

/**
 * An invocation handler that prints out the method name and parameters, then
 * invokes the original method
 */
class TraceHandler implements InvocationHandler {
	private Object target;

	/**
	 * Constructs a TraceHandler
	 * 
	 * @param t
	 *            the implicit parameter of the method call
	 */
	public TraceHandler(Object t) {
		target = t;
	}

	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		// print implicit argument
		System.out.print(target);
		// print method name
		System.out.print("." + m.getName() + "(");
		// print explicit arguments
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				System.out.print(args[i]);
				if (i < args.length - 1)
					System.out.print(", ");
			}
		}
		System.out.println(")");

		// invoke actual method
		return m.invoke(target, args);
	}
}
