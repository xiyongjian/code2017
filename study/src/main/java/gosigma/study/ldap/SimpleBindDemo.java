package gosigma.study.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class SimpleBindDemo {

	public static void main(String[] args) throws NamingException {

		if (args.length < 2) {
			System.err.println("Usage: java SimpleBindDemo <userDN> <password>");
			System.exit(1);
		}

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/ou=system");

		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, args[0]);
		env.put(Context.SECURITY_CREDENTIALS, args[1]);

		try {
			Context ctx = new InitialContext(env);
			NamingEnumeration enm = ctx.list("");

			while (enm.hasMore()) {
				System.out.println(enm.next());
			}

			enm.close();
			ctx.close();
		} catch (NamingException e) {
			System.out.println(e.getMessage());
		}
	}
}