package gosigma.study.system;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Properties;

public class Info {
	public static String getSystemInfo() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nEnvironment\n");
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
		    sb.append(String.format("%s=%s%n", envName, env.get(envName)));
		}
		
		sb.append("\nSystem Properties\n");
		Properties props = System.getProperties();
		for (Object k : props.keySet()) {
			sb.append(String.format("%s=%s%n", k.toString(), props.getProperty((String) k)));
		}
		
		sb.append("\nCurrent PID : " + ManagementFactory.getRuntimeMXBean().getName() + "\n");
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getSystemInfo());
	}

}
