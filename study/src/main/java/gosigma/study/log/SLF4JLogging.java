package gosigma.study.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * 
 * @author srccodes.com
 * @version 1.0
 */
public class SLF4JLogging {
    public static Logger logger = LoggerFactory.getLogger(SLF4JLogging.class);
 
    /**
     * Print hello in log.
     * 
     * @param name
     */
    public void sayHello(String name) {
        logger.info("Hi, {}", name);
        logger.info("Welcome to the HelloWorld example of SLF4J");
    }
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        SLF4JLogging slf4jHello = new SLF4JLogging();
        slf4jHello.sayHello("srccodes.com");
    }
}