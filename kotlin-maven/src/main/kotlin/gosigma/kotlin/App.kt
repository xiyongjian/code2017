package gosigma.kotlin

import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.core.util.StatusPrinter;

object Env {
	internal var logger = LoggerFactory.getLogger(Env.javaClass);

	@JvmStatic fun main(args: Array<String>) {
		logger.info("Enetering...")

		showSystemInfo();

		clazz();

		var name: String = this.javaClass.name;
		logger.info("reflection name : " + name);

		val t = Thread.currentThread();
		System.out.println("class name : " + t.javaClass.name)
		System.out.println("thread name : " + t.name)
		System.out.println("thread name : " + t.getName())
		logger.info("file name   : " + java.lang.Thread.currentThread().stackTrace[1].fileName)
		logger.info("line number : " + java.lang.Thread.currentThread().stackTrace[1].lineNumber)
		logger.info("class       : " + java.lang.Thread.currentThread().stackTrace[1].className)

		logger.info("Leaving...")
	}

	fun showSystemInfo() {
		logger.info("Entering...")
		System.out.println("\nshowSystemInfo()")

		var dir = System.getProperty("user.dir")
		logger.info("[current dir] - " + dir)
		var classpathStr: String = System.getProperty("java.class.path")
		logger.info("[classpath] - " + classpathStr.replace(";".toRegex(), "\n"))

		logger.info("system properties : ")
		var props: Properties = System.getProperties()
		for (e in props) {
			logger.info("[" + e.key.toString() + "] - " + e.value.toString());
		}

		logger.info("get current PID : " + ManagementFactory.getRuntimeMXBean().getName())
		logger.info("Leaving...")
	}

	fun clazz() {
		logger.info("Entering...")
		logger.info("ENV clas : " + Env.javaClass.toString())
		logger.info("Leaving...")
	}

}
