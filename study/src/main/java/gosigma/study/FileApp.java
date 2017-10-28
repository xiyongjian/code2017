package gosigma.study;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileApp {
	public static void main(String[] args) {
		File file  = new File("c:\\tmp\\NgLog4j.prop.log");
		System.out.println("file modified at : "  + file.lastModified());
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("to date : " + sdf.format(file.lastModified()));
	}
}
