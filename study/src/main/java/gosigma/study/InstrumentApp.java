package gosigma.study;

import java.lang.instrument.Instrumentation;

import javax.print.DocFlavor.URL;

import gosigma.study.system.JavaAgent;

public class InstrumentApp {
	public static void main(String[] args) {
		Instrumentation inst = JavaAgent.getInstrumentation();
		Class[] x = inst.getAllLoadedClasses();

		System.out.println("All loaded classes : ");
		for (Class c : x) {
			System.out.println(c.getName());
		}

		{
			Class klass = String.class;
			String loc = '/' + klass.getName().replace('.', '/') + ".class";
			java.net.URL location = klass.getResource('/' + klass.getName().replace('.', '/') + ".class");
			System.out.println("loc :  " + loc);
			System.out.println("string from : " + location.toString());
			System.out.println("Class from : " + klass.getResource("/java/lang/Class.class"));
			System.out.println("Dictionary from : " + klass.getResource("/java/util/Dictionary.class"));
		}

		{
			//			ClassLoader loader = java.lang.Error.class.getClassLoader();
			//			System.out.println(loader.getResource("c:\\Users\\jxi.AD\\git\\code2017\\dns\\target\\classes\\gosigma\\dns\\App.class"));
		}

	}
	// NOT WORK!!

	// usage : java -javaagent:agent.jar MyMainClass
	// -javaagent:c:\Users\jxi.AD\Documents\agent.jar

}
