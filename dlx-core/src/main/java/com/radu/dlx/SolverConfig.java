package com.radu.dlx;

//TODO: move all configuration here
//TODO: create builder for this object
public class SolverConfig {
    public static final int MAX_IN_MEMORY_SOLUTIONS = 1_000_000;
    public static final int MAX_IN_FILE_MEMORY_SOLUTIONS = 1_000_000_000;
    public static final int DEFAULT_SOLUTION_COUNT = 1;

    public boolean logProgress = false;//0x7 fff fff fff fff fffL
    public boolean debug = false;
    //TODO: calculate mems
    public long memReportingPeriod = 10_000_000_000L;//0x7fffffffffffffffL
    public long memTimeout = 0x1FFF_FFF_FFF_FFF_FFFL; //  6 + 1
    public int maxSolutionCount = DEFAULT_SOLUTION_COUNT;
}
