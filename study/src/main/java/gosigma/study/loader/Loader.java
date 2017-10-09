package gosigma.study.loader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Loader {
	public static void loadJar(String name) {
		System.out.println("loadJar : " + name);
		try {
			File myJar = new File(name);
			URL url = myJar.toURI().toURL();

			Class[] parameters = new Class[] { URL.class };

			URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class sysClass = URLClassLoader.class;
			Method method = sysClass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { url });

			// Constructor cs =
			// ClassLoader.getSystemClassLoader().loadClass("com.example.MyClass").getConstructor();
			// MyInterface instance = (MyInterface) cs.newInstance();
			// instance.someFunction();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void loadDir(String dirName) {
		System.out.println("loadDir : " + dirName);
		try {
			List<Object> urls = new ArrayList<Object>();

			// get all the files from a directory
			File directory = new File(dirName);
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.getName().endsWith(".jar")) {
					System.out.println("load jar file : " + file.getName());
					urls.add(file.toURI().toURL());
				}
				else
					System.out.println("skip file : " + file.getName());
			}

			URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class sysClass = URLClassLoader.class;

			Class[] parameters = new Class[] { URL.class };
			Method method = sysClass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);

			method.invoke(sysLoader, urls.toArray());

			// Constructor cs =
			// ClassLoader.getSystemClassLoader().loadClass("com.example.MyClass").getConstructor();
			// MyInterface instance = (MyInterface) cs.newInstance();
			// instance.someFunction();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			loadDir("e:\\code\\tmp\\");
			Constructor cs = ClassLoader.getSystemClassLoader().loadClass("gosigma.study.DerbyClient").getConstructor();
			IClient client = (IClient) cs.newInstance();
			client.process("hello, world");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
