package gosigma.study.system;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderApp {
	public static void p(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		testClassPath();
	}

	public static void testClassPath() {
		p("current class loader ; " + ClassLoaderApp.class.getClassLoader().toString());

		p("getSystemClassLoader(): " + ClassLoader.getSystemClassLoader());

		ClassLoader cl = ClassLoaderApp.class.getClassLoader();
		p("Classloader of ClassloaderApp: " + cl);

		Class type_of_cl = cl.getClass();
		p("..and its type: " + type_of_cl);

		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		p("Classloader of thread context: " + tcl);

		type_of_cl = cl.getClass();
		p("..and its type: " + type_of_cl);

		ClassLoader cl_of_cl = cl.getClass().getClassLoader();
		p("Classloader of (Classloader of ClassloaderApp): " + cl_of_cl);
		if (cl_of_cl != null) {
			type_of_cl = cl.getClass();
			p("..and its type: " + type_of_cl);
		}
		
		p("tcl is URL class loader : " + (tcl instanceof URLClassLoader));
		p("is URL class loader : " + (ClassLoaderApp.class.getClassLoader() instanceof URLClassLoader));
		p("is URL class loader : " +  (URLClassLoader)ClassLoaderApp.class.getClassLoader());
		
		URLClassLoader ucl = new URLClassLoader(new URL[0]);
		p("null url class loader : " + ucl);
		
	}

}
