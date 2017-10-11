package gosigma.study;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Wait {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Future<Integer> x = executor.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				Thread.sleep(2000);
				// TODO Auto-generated method stub
				return new Integer(99);
			}
		});
		while (!x.isDone() && !x.isCancelled()) {
			System.out.println("submit job, sleep 500 ms.");
			Thread.sleep(500);
		}
		System.out.println("submit job, return result : " + x.get());

		final StringBuilder sb = new StringBuilder();
		executor.execute(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				try {
					System.out.println("run, sleep");
					Thread.sleep(2000);
					System.out.println("run, sleep done");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized (sb) {
					System.out.println("run, start");
					sb.append("hello, world");
					System.out.println("run, notifyAll");
					sb.notifyAll();
					System.out.println("run, notifyAll done");
				}
			}
		});
		synchronized (sb) {
			while (sb.length() < 1) {
				System.out.println("wait 100 ms for string builder, " + sb.length());
				sb.wait(100);
				System.out.println("wait 100 ms for string builder, timeout, " + sb.length());
			}
		}

		executor.shutdown();
	}

}
