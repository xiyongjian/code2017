package gosigma.kotlin

import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

object Env {
	internal var logger = LoggerFactory.getLogger(Env.javaClass);

	@JvmStatic fun main(args: Array<String>) {
		logger.info("Enetering...")
		
		showSystemInfo();

		logger.info("Leaving...")
	}

    fun showSystemInfo() {
		logger.info("Entering...")
		System.out.println("\nshowSystemInfo()")

		var dir = System.getProperty("user.dir")
		logger.info("[current dir] - " + dir)
		var classpathStr : String = System.getProperty("java.class.path")
		logger.info("[classpath] - " + classpathStr.replace(";".toRegex(), "\n"))
		
		logger.info("system properties : ")
		var props : Properties = System.getProperties()
		for (e in props) {
			logger.info("[" + e.key.toString() + "] - " + e.value.toString());
		}
		
		logger.info("get current PID : " + ManagementFactory.getRuntimeMXBean().getName())
		logger.info("Leaving...")
    }
}
