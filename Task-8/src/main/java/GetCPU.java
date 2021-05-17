import java.io.File;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Context;

public class GetCPU {
    public static void main(String[] args) {
        val s = Source.newBuilder("llvm", new File("./yourTestProgram.bc")).build
        val c = Context.newBuilder().allowNativeAccess(true).build()
        val lib = c.eval(s)
        val fn = lib.getMember("printHello")
        fn.executeVoid()
    }
}
