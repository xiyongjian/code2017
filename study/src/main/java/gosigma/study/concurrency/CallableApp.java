package gosigma.study.concurrency;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class CallableApp {

	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) {
		try {
			// useFuture();
			useFutureTask();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void useFutureTask() throws InterruptedException, ExecutionException {
		FutureTask<Integer> task = new FutureTask<Integer>(() -> {
			Thread.sleep(1000);
			return 888;
		});
		Thread t = new Thread(task);
		t.start();
		p("task isDone() : " + task.isDone());
		p("sleep 1500ms");
		Thread.sleep(1050);
		p("task isDone() : " + task.isDone());
		p("task return : " + task.get());
	}

	public static AtomicInteger _totalThreads = new AtomicInteger(0);

	public static int getThreadSeqNumber() {
		int i = _totalThreads.incrementAndGet();
		return i;
	}

	public static void useFuture() {
		try (Scanner in = new Scanner(System.in)) {
			System.out.print("Enter base directory (e.g. /usr/local/jdk5.0/src): ");
			String directory = in.nextLine();
			System.out.print("Enter keyword (e.g. volatile): ");
			String keyword = in.nextLine();

			MatchCounter counter = new MatchCounter(new File(directory), keyword);
			FutureTask<Integer> task = new FutureTask<>(counter);
			Thread t = new Thread(task);
			t.start();
			try {
				System.out.println(task.get() + " matching files.");
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
			}
		}
	}

	public static class MatchCounter implements Callable<Integer> {
		private File directory;
		private String keyword;

		/**
		 * Constructs a MatchCounter.
		 * 
		 * @param directory
		 *            the directory in which to start the search
		 * @param keyword
		 *            the keyword to look for
		 */
		public MatchCounter(File directory, String keyword) {
			this.directory = directory;
			this.keyword = keyword;
		}

		public Integer call() {
			int count = 0;
			int seq = getThreadSeqNumber();
			p("thread " + seq + " : call() entering... : " + directory.getName());
			try {
				File[] files = directory.listFiles();
				List<Future<Integer>> results = new ArrayList<>();

				for (File file : files)
					if (file.isDirectory()) {
						MatchCounter counter = new MatchCounter(file, keyword);
						FutureTask<Integer> task = new FutureTask<>(counter);
						results.add(task);
						Thread t = new Thread(task);
						t.start();
					} else {
						if (search(file))
							count++;
					}

				for (Future<Integer> result : results)
					try {
						count += result.get();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
			} catch (InterruptedException e) {
			}
			p("thread " + seq + " : call() leaving...  " + count);
			return count;
		}

		/**
		 * Searches a file for a given keyword.
		 * 
		 * @param file
		 *            the file to search
		 * @return true if the keyword is contained in the file
		 */
		public boolean search(File file) {
			try {
				try (Scanner in = new Scanner(file, "UTF-8")) {
					boolean found = false;
					while (!found && in.hasNextLine()) {
						String line = in.nextLine();
						if (line.contains(keyword))
							found = true;
					}
					return found;
				}
			} catch (IOException e) {
				return false;
			}
		}
	}
}
