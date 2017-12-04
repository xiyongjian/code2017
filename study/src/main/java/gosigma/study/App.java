package gosigma.study;

import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Random;

/**
 * Hello world!
 * also dump all system properties and information
 */
public class App 
{
	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println("home : " + System.getProperty("user.home"));
        
        Random rand = new Random();
        p("random : " + String.format("OAUTH-%08d",  rand.nextInt(1000 * 1000)));
        
        //        doTest();
        //        showSystemInfo();
        
        stringFormat();
    }
    
    static class AppI {
    	public int i;
    	public AppI() {}
    }
    
    public static void doTest() {
    	final AppI o = new AppI();
    	System.out.println("o.i is : " + o.i);
    }
    
    static public void showSystemInfo() {
		System.out.println("\nshowSystemInfo()");

		final String dir = System.getProperty("user.dir");
		System.out.println("[current dir] - " + dir);
		String classpathStr = System.getProperty("java.class.path");
		System.out.println("[classpath] - " + classpathStr.replaceAll(";",  "\n"));
		
		System.out.println("system properties : ");
		Properties props = System.getProperties();
		for (Object k : props.keySet()) {
			System.out.println("[" + k.toString() + "] - " + props.getProperty((String) k));
		}
		
		System.out.println("get current PID : " + ManagementFactory.getRuntimeMXBean().getName());
    }
    
    static public void stringFormat() {
    	p("stringFormat");
    	
    	p("%02d, %8s   : " + String.format("%02d:%8s",  1, "hello"));
    	
    }
}
