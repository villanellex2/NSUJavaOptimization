import java.util.HashMap;
import java.util.Map;

public class HelloJNI {
    static {
        System.loadLibrary("hello");
    }

    static native HashMap<String, String> getCpuInfo();

    public static void main(String[] args) {
        getCpuInfo().forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
