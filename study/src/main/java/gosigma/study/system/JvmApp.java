package gosigma.study.system;

import java.lang.management.ManagementFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

public class JvmApp {

	public static Logger log = (Logger) LoggerFactory.getLogger(JvmApp.class);

	public static void p(String s) {
		System.out.println(s);
	}

	public static void findJvmApps() {
		int counter = 0;
		/*
		 * http://chaoticjava.com/posts/retrieving-a-vms-pid-and-more-info-through-java/
		 * adds about 3.5MB to the application, but small enough overall that I think
		 * it's worth it to verify we only have one running instance
		 */
		System.out.println("Verify if another running instance is active...");
		try {
			// get monitored host for local computer
			MonitoredHost host = MonitoredHost.getMonitoredHost(new String("localhost"));
			// System.out.println(host.getHostIdentifier());
			// System.out.println(host.activeVms());
			int pid;
			MonitoredVm vm;
			String vmClass;
			String vmCmdLine;

			// iterate over pids of running applications on this host.
			// seems every application is considered an instance of the 'activeVM'
			for (Object activeVMPid : host.activeVms()) {
				pid = (Integer) activeVMPid;
				System.out.println("-- Looking at pid: " + pid);

				// get specific vm instance for given pid
				vm = host.getMonitoredVm(new VmIdentifier("//" + pid));
				p("vm : " + vm.toString());
				p("vm identifier : " + vm.getVmIdentifier().toString());

				// get class of given vm instance's main class
				vmCmdLine = MonitoredVmUtil.commandLine(vm);
				vmClass = MonitoredVmUtil.mainClass(vm, true);
				p("vm version : " + MonitoredVmUtil.vmVersion(vm).toString());

				// is class in vm same as class you're comparing?
				System.out.println("class to examine: [" + vmClass + "]");
				System.out.println("cmd line to examine: [" + vmCmdLine + "]");

				if (vmClass.equals(JvmApp.class.getName()) || vmClass.equals("myJar.jar")
						|| (vmCmdLine.contains("myJar.jar") || vmCmdLine.contains(JvmApp.class.getSimpleName()))) {
					counter++;
					System.out.println("Match to current class");
				}
			}
			System.out.println("Found running instances of this " + "program (including this one): " + counter);
			System.out.println("Runtime info of this class: " + ManagementFactory.getRuntimeMXBean().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (counter > 1) {
			System.out.println("Attempting to run more than " + "one instance. Exiting.");
			// System.exit(-1);
		} else {
			// new myClass(args).run();
		}
	}

	public static void main(String[] args) {
		JvmApp.findJvmApps();
	}

}
