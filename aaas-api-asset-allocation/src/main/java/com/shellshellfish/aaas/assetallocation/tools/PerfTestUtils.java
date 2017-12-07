package com.shellshellfish.aaas.assetallocation.tools;

import java.util.Random;
import java.util.concurrent.Callable;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class PerfTestUtils {
    public static final double NANO_TO_MILLI = 1.0E-6D;
    private static final int DEFAULT_REPEAT_CHUNK = 1000;
    private static final int DEFAULT_REPEAT_STAT = 10000;
    private static Random rng = new Random();

    public PerfTestUtils() {
    }

    public static StatisticalSummary[] time(int repeatChunk, int repeatStat, boolean runGC, Callable... methods) {
        double[][][] times = timesAndResults(repeatChunk, repeatStat, runGC, methods);
        int len = methods.length;
        StatisticalSummary[] stats = new StatisticalSummary[len];

        for(int j = 0; j < len; ++j) {
            SummaryStatistics s = new SummaryStatistics();

            for(int k = 0; k < repeatStat; ++k) {
                s.addValue(times[j][k][0]);
            }

            stats[j] = s.getSummary();
        }

        return stats;
    }

    public static double[][][] timesAndResults(int repeatChunk, int repeatStat, boolean runGC, Callable... methods) {
        int numMethods = methods.length;
        double[][][] timesAndResults = new double[numMethods][repeatStat][2];

        try {
            for(int k = 0; k < repeatStat; ++k) {
                for(int j = 0; j < numMethods; ++j) {
                    if (runGC) {
                        System.gc();
                    }

                    Callable<Double> r = methods[j];
                    double[] result = new double[repeatChunk];
                    long start = System.nanoTime();

                    for(int i = 0; i < repeatChunk; ++i) {
                        result[i] = (Double)r.call();
                    }

                    long stop = System.nanoTime();
                    timesAndResults[j][k][0] = (double)(stop - start) * 1.0E-6D;
                    timesAndResults[j][k][1] = result[rng.nextInt(repeatChunk)];
                }
            }
        } catch (Exception var14) {
            throw new MathIllegalStateException(LocalizedFormats.SIMPLE_MESSAGE, new Object[]{var14.getMessage()});
        }

        double normFactor = 1.0D / (double)repeatChunk;

        for(int j = 0; j < numMethods; ++j) {
            for(int k = 0; k < repeatStat; ++k) {
                timesAndResults[j][k][0] *= normFactor;
            }
        }

        return timesAndResults;
    }

    public static StatisticalSummary[] timeAndReport(String title, int repeatChunk, int repeatStat, boolean runGC, PerfTestUtils.RunTest... methods) {
        String hFormat = "%s (calls per timed block: %d, timed blocks: %d, time unit: ms)";
        int nameLength = 0;
        PerfTestUtils.RunTest[] arr$ = methods;
        int len$ = methods.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            PerfTestUtils.RunTest m = arr$[i$];
            int len = m.getName().length();
            if (len > nameLength) {
                nameLength = len;
            }
        }

        String nameLengthFormat = "%" + nameLength + "s";
        String cFormat = nameLengthFormat + " %14s %14s %10s %10s %15s";
        String format = nameLengthFormat + " %.8e %.8e %.4e %.4e % .8e";
        System.out.println(String.format("%s (calls per timed block: %d, timed blocks: %d, time unit: ms)", title, repeatChunk, repeatStat));
        System.out.println(String.format(cFormat, "name", "time/call", "std error", "total time", "ratio", "difference"));
        StatisticalSummary[] time = time(repeatChunk, repeatStat, runGC, methods);
        double refSum = time[0].getSum() * (double)repeatChunk;
        int i = 0;

        for(int max = time.length; i < max; ++i) {
            StatisticalSummary s = time[i];
            double sum = s.getSum() * (double)repeatChunk;
            System.out.println(String.format(format, methods[i].getName(), s.getMean(), s.getStandardDeviation(), sum, sum / refSum, sum - refSum));
        }

        return time;
    }

    public static StatisticalSummary[] timeAndReport(String title, PerfTestUtils.RunTest... methods) {
        return timeAndReport(title, 1000, 10000, false, methods);
    }

    public abstract static class RunTest implements Callable<Double> {
        private final String name;

        public RunTest(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public abstract Double call() throws Exception;
    }
}

