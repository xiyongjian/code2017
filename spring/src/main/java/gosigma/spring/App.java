package gosigma.spring;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Bean.xml");

		System.out.println("list all defined bean names");
		printList(Arrays.asList(context.getBeanDefinitionNames()));

		HelloWorld obj = (HelloWorld) context.getBean("helloBean");
		obj.printHello();

		obj = (HelloWorld) context.getBean("hello2");
		obj.printHello();
		
		System.out.println("list all defined bean names");
		printList(Arrays.asList(context.getBeanDefinitionNames()));

		listClassPath();

		useConfigClass();
	}

	public static void useConfigClass() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

		HelloWorld helloWorld = (HelloWorld)ctx.getBean("helloBean");
		helloWorld.printHello();

		HelloWorld obj = (HelloWorld) ctx.getBean("hello");
		obj.printHello();

		System.out.println("list all defined bean names");
		printList(Arrays.asList(ctx.getBeanDefinitionNames()));
	}

	public static void listClassPath() {
		System.out.println("listClassPath()");
		ClassLoader cl = ClassLoader.getSystemClassLoader();

		URL[] urls = ((URLClassLoader) cl).getURLs();

		for (URL url : urls) {
			System.out.println(url.getFile());
		}
	}
	
	public static void printList(List<?> list) {
		for (Object o : list) {
			System.out.println(o.toString());
		}
	}
}
