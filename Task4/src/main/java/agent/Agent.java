package agent;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new TPTransformer());
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> System.out.printf("Number of loaded classes: %d\n",
                inst.getAllLoadedClasses().length)));
    }

}