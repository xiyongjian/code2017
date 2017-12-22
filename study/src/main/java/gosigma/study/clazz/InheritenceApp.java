package gosigma.study.clazz;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.Set;

import com.sun.tools.javac.code.TypeAnnotations.AnnotationType;

public class InheritenceApp {
	public static void p(String... s) {
		// System.out.println(s.getClass().getName());
		// System.out.println(s);
		// System.out.println((Object)s);
		for (String t : s) {
			System.out.println(t);
		}
		System.out.println("");
	}

	@Documented
	@Inherited // for descenders of the annotation to have the @Documented feature
				// automatically
	@Retention(RetentionPolicy.RUNTIME) // must be there
	static public @interface InWork {
		String value();
	}

	@InWork("hello")
	public static class Inner extends InheritenceApp implements Log<Inner> {
		@Override
		public void info() {
			p("Inner::info()");
		}
	}

	public static String getSupers(Class c) {
		StringBuilder sb = new StringBuilder();
		sb.append(c.getName()).append("\n");
		getSupers(sb, c, "    ");
		return sb.toString();
	}

	public static String annotationToString(Annotation x) {
		// return x.getClass().getName() + ":" + x.annotationType().getName() + ":" +
		// x.toString();
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
		AnnotatedType as = c.getAnnotatedSuperclass();
		if (as != null)
			sb.append(prefix + "AS ").append(as).append("\n");
		for (AnnotatedType ai : c.getAnnotatedInterfaces()) {
			sb.append(prefix + "AI ").append(ai.toString()).append("\n");
		}
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

	public static class Base {
		public void p() { System.out.println("it's Base"); }
	}
	public static class Derived extends Base {
		public void p() { System.out.println("it's Derived"); }
	}
	public static void test01() {
		Derived d = new Derived();
		d.p();
		((Base)d).p();
	}

	public static void main(String[] args) {
		p("InheritenceApp class base classes & interface : \n", getSupers(InheritenceApp.class));
		p("Inner class base classes & interface : \n", getSupers(Inner.class));
		test01();
	}

}
