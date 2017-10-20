package gosigma.study;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class ListClass {

    private static Iterator list(ClassLoader CL)
        throws NoSuchFieldException, SecurityException,
        IllegalArgumentException, IllegalAccessException {
        Class CL_class = CL.getClass();
        while (CL_class != java.lang.ClassLoader.class) {
            CL_class = CL_class.getSuperclass();
        }
        java.lang.reflect.Field ClassLoader_classes_field = CL_class
                .getDeclaredField("classes");
        ClassLoader_classes_field.setAccessible(true);
        Vector classes = (Vector) ClassLoader_classes_field.get(CL);
        return classes.iterator();
    }

    public static void main(String args[]) throws Exception {
        ClassLoader myCL = Thread.currentThread().getContextClassLoader();
        while (myCL != null) {
            System.out.println("ClassLoader: " + myCL);
            for (Iterator iter = list(myCL); iter.hasNext();) {
                System.out.println("\t" + iter.next());
            }
            myCL = myCL.getParent();
        }
        
        main01(args);
    }
    
    public static void main01(String[] args) {
        Field f;
        try {
            f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Vector<Class> classes =  (Vector<Class>) f.get(classLoader);


            for(Class cls : classes){
                java.net.URL location = cls.getResource('/' + cls.getName().replace('.',
                '/') + ".class");
                System.out.println("<p>"+location +"<p/>");

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}