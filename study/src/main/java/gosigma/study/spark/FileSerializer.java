package gosigma.study.spark;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FileSerializer {

	public static void writeObjectToFile(Object obj, String file) {
		try {
			OutputStream fileStream = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fileStream);
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Object readObjectFromFile(String file) {
		try {
			InputStream fileStream = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fileStream);
			Object obj = ((ObjectInput) ois).readObject();
			ois.close();
			return obj;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
