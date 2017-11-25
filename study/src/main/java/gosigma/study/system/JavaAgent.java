package gosigma.study.system;

import java.lang.instrument.*;
import java.util.UUID;

public class JavaAgent {
	private static Instrumentation inst;

	public static Instrumentation getInstrumentation() { return inst; }

	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println(inst.getClass() + ": " + inst);
		JavaAgent.inst = inst;
	}
    
    // build jar file, then
	// usage : java -javaagent:agent.jar MyMainClass
    // MANIFEST of jar file
    // Premain-Class: gosigma.study.system.JavaAgent

	// both version works
	// eclipse setupt run/debug config, setup JVM options as : 
	// -javaagent:c:\Users\jxi.AD\Documents\agent.jar
	
//    public static void premain(String agentArgs, Instrumentation inst) {
//        if (agentArgs != null) {
//            System.getProperties().put(AGENT_ARGS_KEY, agentArgs);
//        }
//        System.getProperties().put(INSTRUMENTATION_KEY, inst);
//    }
//
//    public static Instrumentation getInstrumentation() {
//        return (Instrumentation) System.getProperties().get(INSTRUMENTATION_KEY);
//    }
//
//    // Needn't be a UUID - can be a String or any other object that
//    // implements equals().    
//    private static final Object AGENT_ARGS_KEY =
//        UUID.fromString("887b43f3-c742-4b87-978d-70d2db74e40e");
//
//    private static final Object INSTRUMENTATION_KEY =
//        UUID.fromString("214ac54a-60a5-417e-b3b8-772e80a16667");

}