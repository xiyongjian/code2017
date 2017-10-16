package gosigma.study;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public interface Service {
    	  void get(String s);
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        
    	Service service = Mockito.mock(Service.class);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
            	System.out.println("Answer called");
               return null;
            }
        }).when(service).get(anyObject());
        
        service.get("hello");
        service.get("world");
    }
}
