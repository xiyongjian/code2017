package gosigma.study;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

public class BestJavaSnippets {

	public static void main(String[] args) {
		System.out.println("-- template --");
		{
		}

		System.out.println("-- string <> int --");
		{
			String a = String.valueOf(2); // integer to numeric string
			int i = Integer.parseInt(a); // numeric string to an int
		}

		System.out.println("-- append to file --");
		{
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter("c:\\tmp\\tem.txt", true));
				// out = new BufferedWriter(new FileWriter("c:\\tmxxp\\tem.txt", true));
				out.write("aString");
			} catch (IOException e) {
				System.out.println(e.toString());
				e.printStackTrace(System.out);
				// error processing code
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						System.out.println(e.toString());
					}
				}
			}
		}

		System.out.println("-- get current method and location --");
		{
			// String methodName =
			// Thread.currentThread().getStackTrace()[1].getMethodName();
			System.out.println("current method(0) : " + Thread.currentThread().getStackTrace()[0].getMethodName());
			System.out.println("current method(1) : " + Thread.currentThread().getStackTrace()[1].getMethodName());
			System.out.println("current file name(1) : " + Thread.currentThread().getStackTrace()[1].getFileName());
			System.out.println("current line number(1) : " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			System.out.println("current class name(1) : " + Thread.currentThread().getStackTrace()[1].getClassName());
		}

		System.out.println("-- convert date string --");
		try {
			Date date = java.text.DateFormat.getDateInstance().parse("2017-10-28 23:45:56");
			System.out.println("converted date : " + date.toString());

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
			date = format.parse("28.10.2017 12:34:56");
			System.out.println("converted date : " + date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("-- nio copy file --");
		{
			try {
				long startTime = Calendar.getInstance().getTimeInMillis();

				File src = new File("e:\\tmp\\Jack Erjavec Automotive Technology- A Systems Approach  .pdf");
				File dst = new File("e:\\tmp\\tmp.pdf");
				System.out.println("file copy from " + src.getAbsolutePath() + " to " + dst.getAbsolutePath());
				System.out.println("file size : " + src.length());
				fileCopy(src, dst);

				long endTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("time using : " + (endTime - startTime) + " ms");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("-- initialize http proxy --");
		{
			System.getProperties().put("http.proxyHost", "someProxyURL");
			System.getProperties().put("http.proxyPort", "someProxyPort");
			System.getProperties().put("http.proxyUser", "someUserName");
			System.getProperties().put("http.proxyPassword", "somePassword");
		}

		System.out.println("-- get screen shot --");
		{
			try {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle screenRectangle = new Rectangle(screenSize);
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(screenRectangle);
				ImageIO.write(image, "png", new File("c:\\tmp\\tmp.png"));
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void fileCopy(File in, File out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			// inChannel.transferTo(0, inChannel.size(), outChannel); // original --
			// apparently has trouble copying large files on Windows

			// magic number for Windows, 64Mb - 32Kb)
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = inChannel.size();
			long position = 0;
			while (position < size) {
				position += inChannel.transferTo(position, maxCount, outChannel);
			}
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}
}
