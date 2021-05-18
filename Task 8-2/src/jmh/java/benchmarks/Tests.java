package benchmarks;

import calculator.DigitsChecker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
public class Tests {

    @Param({"13472956", "134729zz"})
    private String test_string;


    @Benchmark
    public void testException(Blackhole bh) {
        bh.consume(DigitsChecker.exceptionCheck(test_string));
    }

    @Benchmark
    public void testCharacters(Blackhole bh) {
        bh.consume(DigitsChecker.characterCheck(test_string));
    }

    @Benchmark
    public void testRegex(Blackhole bh) {
        bh.consume(DigitsChecker.regexCheck(test_string));
    }
}
