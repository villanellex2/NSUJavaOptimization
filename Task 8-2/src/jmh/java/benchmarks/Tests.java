package benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 5)
public class Tests {

    @Param({ "134729012456", "1347290124zz"})
    private String test_string;


    @Benchmark
    public void testTrueException(Blackhole bh) {
        bh.consume(Calculator.isDigit1(test_string));
    }

}
