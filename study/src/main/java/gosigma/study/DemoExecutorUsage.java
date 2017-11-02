package gosigma.study;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
 
public class DemoExecutorUsage {
 
    private static ExecutorService executor = null;
    private static volatile Future taskOneResults = null;
    private static volatile Future taskTwoResults = null;
 
    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(2);
        while (true)
        {
            try
            {
                checkTasks();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("Caught exception: " + e.getMessage());
            }
        }
    }
 
    private static void checkTasks() throws Exception {
        if (taskOneResults == null
                || taskOneResults.isDone()
                || taskOneResults.isCancelled())
        {
        	System.out.println("restart TestOne");
            taskOneResults = executor.submit(new TestOne());
        }
 
        if (taskTwoResults == null
                || taskTwoResults.isDone()
                || taskTwoResults.isCancelled())
        {
        	System.out.println("restart TestTwo");
            taskTwoResults = executor.submit(new TestTwo());
        }
    }
}
 
class TestOne implements Runnable {
    public void run() {
    	int i = 0;
        while (true)
        {
            System.out.println("Executing task one");
            try
            {
                Thread.sleep(888);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (++i > 5)
            	break;
        }
 
    }
}
 
class TestTwo implements Runnable {
    public void run() {
    	int i = 0;
        while (true)
        {
            System.out.println("Executing task two");
            try
            {
                Thread.sleep(333);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (++i > 6)
            	break;
        }
    }
}