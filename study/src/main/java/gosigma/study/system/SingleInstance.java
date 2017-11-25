package gosigma.study.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SingleInstance {
	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}

	public static File _lockFile;
	public static FileChannel _lockChannel;
	public static FileLock _appLock;

	public static void check() {
		try {
			String tmpDir = System.getProperty("java.io.tmpdir");
			p("tmp dir : " + tmpDir);
			String lockFileName = tmpDir + File.separator + "SingleInstance.lock";
			p("lock file name : " + lockFileName);

			_lockFile = new File(lockFileName);
			p("Check if the _appLock exist");
			if (_lockFile.exists()) {
				p("if exist try to delete it");
				_lockFile.delete();
			}

			p("Try to get the _appLock");
			_lockChannel = new RandomAccessFile(_lockFile, "rw").getChannel();
			_appLock = _lockChannel.tryLock();
			if (_appLock == null) {
				p("File is lock by other application");
				_lockChannel.close();
				throw new RuntimeException("Only 1 instance of MyApp can run.");
			}

			p("Add shutdown hook to release appLock when application shutdown");
			ShutdownHook shutdownHook = new ShutdownHook();
			Runtime.getRuntime().addShutdownHook(shutdownHook);

			p("Your application tasks here..");
			System.out.println("Running");
			Thread.sleep(1000);
			p("done");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException("Could not start process.", e);
		}
	}

	public static void unlockFile() {
		// release and delete file lock
		try {
			if (_appLock != null)
				_appLock.release();
			_lockChannel.close();
			_lockFile.delete();
			p("shutdown -> unlockFile()");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ShutdownHook extends Thread {
		public void run() {
			unlockFile();
		}
	}

	public static void main(String[] args) {
		check();

	}

}
