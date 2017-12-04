package gosigma.etl_web;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	public static Logger log = LoggerFactory.getLogger(Utils.class);

	public static void main(String[] args) {
		System.out.println("Utils hierarchy : \n" + getSupers(Utils.class));
		log.info("Utils hierarchy : \n" + getSupers(Utils.class));

	}

	public static String getSupers(Class c) {
		StringBuilder sb = new StringBuilder();
		sb.append(c.getName()).append("\n");
		getSupers(sb, c, "    ");
		return sb.toString();
	}

	public static String annotationToString(Annotation x) {
		// return x.getClass().getName() + ":" + x.annotationType().getName() + ":" + x.toString();
		return x.toString();
	}

	public static void getAnnotations(StringBuilder sb, Annotation a, String prefix, Set<String> done) {
		for (Annotation x : a.annotationType().getDeclaredAnnotations()) {
			if (done.contains(x.toString()))
				sb.append(prefix + "AR ").append(annotationToString(x)).append("\n");
			else {
				sb.append(prefix + "A  ").append(annotationToString(x)).append("\n");
				done.add(x.toString());
				getAnnotations(sb, x, prefix + "    ", done);
				// done.remove(x.toString());
			}
		}
	}

	public static void getSupers(StringBuilder sb, Class c, String prefix) {
		for (Annotation a : c.getAnnotations()) {
				sb.append(prefix + "A  ").append(annotationToString(a)).append("\n");
			Set<String> done = new HashSet<>();
			getAnnotations(sb, a, prefix + "    ", done);
		}
		for (Class i : c.getInterfaces()) {
			sb.append(prefix + "I  " + i.getName()).append("\n");
			getSupers(sb, i, prefix + "    ");
		}
		Class s = c.getSuperclass();
		if (s != null) {
			sb.append(prefix + "C  " + s.getName()).append("\n");
			getSupers(sb, s, prefix + "    ");
		}
	}

}
