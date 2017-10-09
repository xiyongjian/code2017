package gosigma.study.loader;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class LoaderList {
	public static void main(String[] args) {
		list01();
		list02();
		list03();
	}

	public static void list01() {
		Field f;
		try {
			f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Vector<Class> classes = (Vector<Class>) f.get(classLoader);

			System.out.println("current loader : " + classLoader.toString());
			for (Class c : classes) {
				System.out.println("class : " + c.getCanonicalName());
			}

			ClassLoader sysLoader = classLoader.getSystemClassLoader();
			classes = (Vector<Class>) f.get(sysLoader);
			System.out.println("sysloader : " + sysLoader.toString());
			for (Class c : classes) {
				System.out.println("class : " + c.getCanonicalName());
			}

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void list02() {
		Object obj = new LoaderList();
		ClassLoader classLoader = obj.getClass().getClassLoader();
		if (classLoader != null) { // to escape from system classes that are loaded by bootstrap class-loader such
									// as String.
			do {
				try {
					Class clClass = classLoader.getClass();
					System.out.println("loader name : " + clClass.getName() + ", " + clClass.getTypeName());
					while (clClass != ClassLoader.class) {
						clClass = clClass.getSuperclass();
					}
					java.lang.reflect.Field domainField = clClass.getDeclaredField("domains");
					java.lang.reflect.Field classesField = clClass.getDeclaredField("classes");
					domainField.setAccessible(true);
					classesField.setAccessible(true);
					// System.out.println("domains : " +
					// domainField.get(classLoader).getClass().getCanonicalName());
					// System.out.println("classes : " +
					// classesField.get(classLoader).getClass().getCanonicalName());
					Set<String> domains = (Set<String>) domainField.get(classLoader);
					Vector classes = (Vector) classesField.get(classLoader);
					System.out.println("\n******************** " + classLoader.toString() + "\n");
					System.out.println(Arrays.toString(classes.toArray()).getBytes());
					for (Object d : domains) {
						System.out.println("domain: " + d.getClass().getName());
					}
					for (Object o : classes) {
						System.out.println("class : " + o.getClass().getName());
					}
					System.out.println();
					classLoader = classLoader.getParent();
				} catch (Exception exception) {
					exception.printStackTrace();
					// TODO
				}
			} while (classLoader.getParent() != null);
		}
	}

	public static void list03() {
		Field f;
		try {
			f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Vector<Class> classes = (Vector<Class>) f.get(classLoader);
			
			System.out.println("list loaded classes");
			for (Class c : classes) {
				System.out.println(c.getName());
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
