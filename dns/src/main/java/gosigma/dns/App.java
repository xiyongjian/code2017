package gosigma.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.URLClassLoader;

import com.alibaba.dcm.DnsCacheManipulator;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void o(String s)
	{
		System.out.print(s);
	}

	public static void p(String s)
	{
		System.out.println(s);
	}

	public static void showEnv()
	{
		p("class path : ");
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
	}

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {
        	showEnv();
			test01();
			test02();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void test01() throws UnknownHostException
    {
    	DnsCacheManipulator.setDnsCache("www.hello.com", "192.168.1.1");
    	DnsCacheManipulator.setDnsCache("www.world.com", "1234:5678:0:0:0:0:0:200e"); // 支持IPv6地址

    	// 上面设置全局生效，之后Java中的所有的域名解析逻辑都会是上面设定的IP。
    	// 下面用一个简单获取域名对应的IP，来演示一下：

    	String ip = InetAddress.getByName("www.hello.com").getHostAddress();
    	// ip = "192.168.1.1"
    	String ipv6 = InetAddress.getByName("www.world.com").getHostAddress();
    	// ipv6 = "1234:5678:0:0:0:0:0:200e"
    	p("ip : " + ip + ", ipv6 : " + ipv6);

    	// 可以设置多个IP
    	DnsCacheManipulator.setDnsCache("www.hello-world.com", "192.168.2.1", "192.168.2.2");

    	String ipHw = InetAddress.getByName("www.hello-world.com").getHostAddress();
    	// ipHw = 192.168.2.1 ，读到第一个IP
    	InetAddress[] allIps = InetAddress.getAllByName("www.hello-world.com");
    	// 上面读到设置的多个IP
    	p("allIps : " + allIps.toString());

    	// 设置失效时间，单元毫秒
    	DnsCacheManipulator.setDnsCache(3600 * 1000, "www.hello-hell.com", "192.168.1.1", "192.168.1.2");
    }

    public static void test02() throws UnknownHostException
    {
    	DnsCacheManipulator.loadDnsCacheConfig();
    	String ip = InetAddress.getByName("www.hello-world.com").getHostAddress();
    	p("hello-world : " + ip);
    	
    	ip = InetAddress.getByName("www.foo.com").getHostAddress();
    	p("foo: " + ip);
    }
}
