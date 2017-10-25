package gosigma.study.ldap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class DigestDemo {
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	System.out.println("{SHA}" + digest("pass", "SHA"));
    	System.out.println("{SHA}" + digest("password", "SHA"));
    	System.out.println("{SHA}" + digest("secret", "SHA"));
    	System.out.println("{SHA-512}" + digest("secret", "SHA-512"));
    	System.out.println("{MD5}" + digest("secret", "MD5"));
    }

    public static String digest(String password, String algorithm) throws NoSuchAlgorithmException {
    	//        String password = "pass";
    	//        String algorithm = "SHA";

        // Calculate hash value
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(password.getBytes());
        byte[] bytes = md.digest();

        // Print out value in Base64 encoding
        BASE64Encoder base64encoder = new BASE64Encoder();
        String hash = base64encoder.encode(bytes);        
        return hash;
        // System.out.println('{'+algorithm+'}'+hash);
    }
}