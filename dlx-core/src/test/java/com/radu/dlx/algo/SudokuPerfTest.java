package com.radu.dlx.algo;

import com.radu.dlx.io.tree.SolutionList;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.problem.ExactCoveringProblem;
import com.radu.dlx.problem.LangfordPairBuilder;
import com.radu.dlx.problem.NQueenProblem;
import com.radu.dlx.problem.sudoku.SudokuBoardReader;
import com.radu.dlx.problem.sudoku.SudokuBoardWriter;
import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;

public class SudokuPerfTest {

    @Test
    @Disabled
    /**
     * 50 000 sudokus
     *
     * For solutionList
     * Profile[problem=15.436 us total=758,707.721 us
     * , links=32.692 us total=1,606,865.996 us
     * , solve=28.825 us total=1,416,800.846 us
     * , write=10.177 us total=500,203.669 us
     * ]
     *
     * Profile[problem=15.519 us total=762,770.802 us min=11.486 us max=932.380 us
     * , links=69.506 us total=3,416,266.887 us min=56.558 us max=1,964.601 us
     * , solve=28.793 us total=1,415,217.384 us min=16.112 us max=5,584.797 us
     * , write=7.913 us total=388,938.385 us min=6.338 us max=116.646 us
     * ]
     *
     * For solutionArray
     * Profile[problem=15.517 us total=762,688.377 us
     * , links=33.337 us total=1,638,529.892 us
     * , solve=29.304 us total=1,440,332.169 us
     * , write=9.722 us total=477,834.139 us
     * ]
     * For OfIntSolutionTree
     * Profile[problem=15.410 us total=757,416.432 us
     * , links=32.218 us total=1,583,546.002 us
     * , solve=32.420 us total=1,593,469.734 us
     * , write=9.563 us total=470,018.181 us
     * ]
     * Profile[problem=15.225 us total=748,334.673 us min=11.182 us max=93.942 us
     * , links=32.091 us total=1,577,294.987 us min=24.818 us max=1,056.816 us
     * , solve=31.889 us total=1,567,367.502 us min=17.566 us max=6,634.714 us -> notice the heavily backtracked max value
     * , write=9.626 us total=473,146.122 us min=8.138 us max=1,057.859 us
     * ]
     * Profile[problem=15.679 us total=770,648.309 us min=11.874 us max=840.231 us
     * , links=32.999 us total=1,621,952.423 us min=25.560 us max=856.775 us
     * , solve=29.481 us total=1,449,030.666 us min=16.163 us max=5,971.449 us
     * , write=9.960 us total=489,547.041 us min=8.225 us max=1,001.760 us
     * ]
     */
    public void shouldGordonRoyle17ClueSudokusStackless() throws IOException {
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/sudoku17.txt", '0');
    }

    @Test
    @Disabled
    /**
     * 1465 sudokus
     *
     * For solutionList
     * Profile[problem=14.996 us total=21,968.774 us
     * , links=29.843 us total=43,720.727 us
     * , solve=147.167 us total=215,599.951 us
     * , write=9.039 us total=13,242.760 us
     * ]
     *
     * For solutionArray
     * Profile[problem=14.897 us total=21,824.575 us
     * , links=31.151 us total=45,636.002 us
     * , solve=144.283 us total=211,374.102 us
     * , write=8.992 us total=13,172.586 us
     * ]
     *
     * For OfIntSolutionTree
     * Profile[problem=14.922 us total=21,860.386 us
     * , links=28.800 us total=42,191.766 us
     * , solve=157.879 us total=231,292.315 us
     * , write=9.163 us total=13,424.229 us
     * ]
     * Profile[problem=15.194 us total=22,259.399 us min=12.948 us max=58.352 us
     * , links=27.442 us total=40,202.406 us min=21.546 us max=132.701 us
     * , solve=151.872 us total=222,492.990 us min=13.791 us max=3,865.154 us
     * , write=9.505 us total=13,924.462 us min=7.773 us max=97.636 us
     * ]
     * w/ solution array
     * Profile[problem=16.054 us total=23,519.250 us min=14.125 us max=80.612 us
     * , links=30.348 us total=44,459.986 us min=22.234 us max=4,518.630 us
     * , solve=153.699 us total=225,169.547 us min=14.178 us max=3,888.114 us
     * , write=9.730 us total=14,254.386 us min=8.016 us max=55.564 us
     * ]
     * w/ solution list
     * Profile[problem=15.031 us total=22,020.065 us min=12.924 us max=102.151 us
     * , links=30.402 us total=44,539.436 us min=21.836 us max=4,388.684 us
     * , solve=145.247 us total=212,786.259 us min=13.436 us max=3,596.931 us
     * , write=8.926 us total=13,077.301 us min=7.428 us max=56.958 us
     * ]
     * Profile[problem=16.557 us total=24,255.930 us min=13.047 us max=715.028 us
     * , links=61.565 us total=90,192.451 us min=42.076 us max=678.467 us
     * , solve=144.596 us total=211,833.226 us min=12.830 us max=3,335.704 us
     * , write=8.958 us total=13,123.907 us min=5.750 us max=128.971 us
     * ]
     */
    public void shouldGuenterStertenbrinkSudokusStackless() throws IOException { //http://magictour.free.fr
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');

        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
        runSudokuFileStackless("/Users/radu/Documents/personal/sudoku/data/top1465.txt", '.');
    }

    @Test//
    //79619280/2=39809640  3us/solution --openjdk jdk11
    //after optimization of solution tree with new primitive array list 2.7us/solution
    // 3us --> oracle jdk12
    // 241996--> -XX:-UseBiasedLocking
    //-XX:MaxGCPauseMillis=time
    //-XX:+UseCMoveUnconditionally
    //-XX:+UnlockDiagnosticVMOptions -XX:+UseAESIntrinsics -XX:+UseAES    -XX:+UseFMA ???
    //-XX:+UseRTMLocking -XX:+UseRTMDeopt
    //-XX:+UseStringDeduplication -Xlog:stringdedup*=debug -XX:+UseG1GC
    //-ea -XX:+UnlockDiagnosticVMOptions -XX:+UseAESIntrinsics -XX:+UseAES    -XX:+UseFMA -XX:+UseRTMLocking -XX:+UseRTMDeopt
    //-XX:+UnlockExperimentalVMOptions -XX:+UseZGC
    //-XX:+UseNUMA
    // 214039 ms
    // 218312 ms -> worse, a bit
    // -> -XX:FreqInlineSize=4048 -XX:MaxInlineSize=400 -XX:CompileThreshold=1000
    //  218312
    // 207578 -> if array solution tree and backup() used in algo
    // 222081 -> if for array solution tree getActiveNodeValue is used
    // 215430 --> if for list solution getActiveNodeValue is used
    // 212071 --> if for list solution backup is used
    // 115569.0 -> 39809640 solutions if we lose the symmetrical solutions
    // 110503.0 -> 39809640 solutions if we lose the symmetrical solutions using the most optimal solution by ignoring options with: i = n - [n even] and j > n/2
    // 107450.0 -> we replace advance(i,val) with advance(val) in stackless dancing links algorithm
    //29752 -> we are now handling tracking in the solution list itself
    /**
     * Solution num 39809640
     * SolutionArray
     * Profile[problem=6,197.089 us total=6,197.089 us
     * , links=4,423.882 us total=4,423.882 us
     * , solve=108,115,107.077 us total=108,115,107.077 us
     * , write=0.000 us total=0.000 us
     * ] -> 108115107/39809640 = 2.7 us
     * SolutionList
     * Profile[problem=6,259.001 us total=6,259.001 us min=6,259.001 us max=6,259.001 us
     * , links=11,355.710 us total=11,355.710 us min=11,355.710 us max=11,355.710 us
     * , solve=114,033,163.305 us total=114,033,163.305 us min=114,033,163.305 us max=114,033,163.305 us
     * , write=0.000 us total=0.000 us min=9,223,372,036,854,776,000.000 us max=-9,223,372,036,854,776,000.000 us
     * ]
     * Profile[problem=8,541.016 us total=8,541.016 us min=8,541.016 us max=8,541.016 us
     * , links=10,113.738 us total=10,113.738 us min=10,113.738 us max=10,113.738 us
     * , solve=110,901,022.075 us total=110,901,022.075 us min=110,901,022.075 us max=110,901,022.075 us
     * , write=0.000 us total=0.000 us min=9,223,372,036,854,776,000.000 us max=-9,223,372,036,854,776,000.000 us
     * ]
     */
    @Disabled
    public void shouldSolveLangfordPairN15() {
        Profile profile = new Profile();
        long start = profile.currentUsTime();
        ExactCoveringProblem problem = LangfordPairBuilder.build(15);
        profile.problem.update(start);
        int count = runStacklessCounter(problem, profile, 1_000_000_000);
        System.out.println(count);
        System.out.println(profile);
    }

    @Test//solution: 653 443 600/2,  2120 728ms -> 3.25us per solution
    /**
     * Profile[problem=7,509.410 us total=7,509.410 us min=7,509.410 us max=7,509.410 us
     * , links=7,780.812 us total=7,780.812 us min=7,780.812 us max=7,780.812 us
     * , solve=940,600,047.706 us total=940,600,047.706 us min=940,600,047.706 us max=940,600,047.706 us
     * , write=0.000 us total=0.000 us min=9,223,372,036,854,776,000.000 us max=-9,223,372,036,854,776,000.000 us
     * ]
     * 2.87 us per solution
     */
    @Disabled
    public void shouldSolveLangfordPairN16() {
        Profile profile = new Profile();
        long start = profile.currentUsTime();
        ExactCoveringProblem problem = LangfordPairBuilder.build(16);
        profile.problem.update(start);
        int count = runStacklessCounter(problem, profile, 1_000_000_000);
        System.out.println(count);
        System.out.println(profile);
    }

    @Test
    @Disabled
    /**
     * Solution num 14 772 512 (with symmetries)
     * SolutionArray
     * Profile[problem=8,519.925 us total=8,519.925 us
     * , links=3,262.869 us total=3,262.869 us
     * , solve=30,262,918.016 us total=30,262,918.016 us
     * , write=0.000 us total=0.000 us
     * ] -> 30262918.016/14772512 = 2.04 us
     *
     * Profile[problem=6,423.929 us total=6,423.929 us min=6,423.929 us max=6,423.929 us
     * , links=6,179.009 us total=6,179.009 us min=6,179.009 us max=6,179.009 us
     * , solve=31,925,596.011 us total=31,925,596.011 us min=31,925,596.011 us max=31,925,596.011 us
     * , write=0.000 us total=0.000 us min=9,223,372,036,854,776,000.000 us max=-9,223,372,036,854,776,000.000 us
     * ] -> 2.16 us
     */
    public void shouldSolveQueensN16() {
        Profile profile = new Profile();
        long start = profile.currentUsTime();
        ExactCoveringProblem problem = NQueenProblem.build(16);
        profile.problem.update(start);
        int count = runStacklessCounter(problem, profile, 1_000_000_000);
        System.out.println(count);
        System.out.println(profile);
    }

    private void runSudokuFileStackless(String path, char zeroCell) throws IOException {
        Profile profile = new Profile();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            final SudokuBoardWriter printer = SudokuBoardWriter.create();
            final SudokuBoardReader boardReader = SudokuBoardReader.create(zeroCell);
            reader.lines()
                    .map(l -> {
                        long start = profile.currentUsTime();
                        ExactCoveringProblem problem = boardReader.read(l);
                        profile.problem.update(start);
                        return problem;
                    })
                    .map(p -> runStackless(p, profile, printer))
                    .forEach(a -> {
                    });
            System.out.println(profile);
        }
    }

    private String runStackless(ExactCoveringProblem p, Profile profile, SudokuBoardWriter printer) {
        long start = profile.currentUsTime();
        DancingArray t = DancingArrays.create(p);
        SolutionTree tree = SolutionList.createStoring(t, 1);
        start = profile.links.update(start);
        SolutionTree solution = DancingLinks.create(tree, t).solve();
        start = profile.solve.update(start);
        String answer = printer.print(p, solution.getFirstSolution(), t) + "\n";
        profile.write.update(start);
        return answer;
    }

    private int runStacklessCounter(ExactCoveringProblem p, Profile profile, int maxSolutionNum) {
        long start = profile.currentUsTime();
        DancingArray t = DancingArrays.create(p);
        SolutionTree tree = SolutionList.createCounting(t, maxSolutionNum);
        start = profile.links.update(start);
        SolutionTree solution = DancingLinks.create(tree, t).solve();
        profile.solve.update(start);
        return solution.getSolutionCount();
    }

    private static class Profile {
        private final Timing problem = new Timing();
        private final Timing links = new Timing();
        private final Timing solve = new Timing();
        private final Timing write = new Timing();

        public long currentUsTime() {
            return System.nanoTime();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Profile.class.getSimpleName() + "[", "]\n")
                    .add(String.format("problem=%,.3f us total=%,.3f us min=%,.3f us max=%,.3f us\n", problem.getAverage(), problem.sum, problem.min, problem.max))
                    .add(String.format("links=%,.3f us total=%,.3f us min=%,.3f us max=%,.3f us\n", links.getAverage(), links.sum, links.min, links.max))
                    .add(String.format("solve=%,.3f us total=%,.3f us min=%,.3f us max=%,.3f us\n", solve.getAverage(), solve.sum, solve.min, solve.max))
                    .add(String.format("write=%,.3f us total=%,.3f us min=%,.3f us max=%,.3f us\n", write.getAverage(), write.sum, write.min, write.max))
                    .toString();
        }
    }

    private static class Timing {
        private double sum = 0;
        private double average = 0;
        private long count = 0;
        private double min = Long.MAX_VALUE;
        private double max = Long.MIN_VALUE;

        public long update(long prevTime) {
            long last = currentUsTime();
            long delta = last - prevTime;
            min = Double.min(min, delta/1_000.0d);
            max = Double.max(max, delta/1_000.0d);
            sum += delta / 1_000.0d;
            average += (delta - average) / (count + 1);
            count++;
            return last;
        }

        public double getAverage() {
            return count == 0 ? 0 : average / 1_000.0d;
        }

        private long currentUsTime() {
            return System.nanoTime();
        }
    }
}