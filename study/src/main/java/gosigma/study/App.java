package gosigma.study;

import java.util.Random;

/**
 * Hello world!
 *
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
    }
}
