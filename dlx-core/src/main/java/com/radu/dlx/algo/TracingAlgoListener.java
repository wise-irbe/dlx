package com.radu.dlx.algo;

import com.radu.dlx.io.progress.TrackingStream;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.DancingStructure;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class TracingAlgoListener implements AlgorithmListener {
    private final TrackingStream stream;
    private BigInteger mems;

    public TracingAlgoListener(TrackingStream tracking) {
        stream = tracking;
        mems = BigInteger.ZERO;
    }

    @Override
    public void beforeStart(DancingStructure struct, SolutionTree tree) {
        stream.write(() -> String.format("Dancing links algorithm X:\n"
                        + "number of items %d, number of options %d,\n"
                        + "memory state is:\n%s\n",
                struct.getItemNum(), struct.getOptionRowNum(), struct.toString()));
    }

    @Override
    public void afterAdvance(DancingStructure struct, SolutionTree tree) {
        if (stream.isWritable(tree.getSolutionCount())) {
            stream.write(() ->
                    String.format("Current (state ADVANCE x %d (%s), level %d, branch %s, completion score %,.4f, search node count %d, max search depth %d)\n %s"
                            , tree.currentOption()
                            , struct.printer().printOption(tree.currentOption())
                            , tree.level()
                            , struct.printer().printItem(tree.currentItem())
                            , tree.completionScore()
                            , tree.size()
                            , tree.maxLevel()
                            , struct.printer().describe(tree.currentItem(), true))
            );
        }
    }

    @Override
    public void beforeStore(DancingStructure struct, SolutionTree tree) {
        stream.write(() ->
                "Found solution (completed: " + tree.completionScore() + "):\n"
                        + Arrays.stream(tree.getActiveBranch())
                        .mapToObj(v -> struct.printer().describe(v, false))
                        .collect(Collectors.joining("\n")));
    }

    @Override
    public void afterEnd(DancingStructure struct, SolutionTree tree) {
        stream.write(() -> "Found " + tree.getSolutionCount() + " solutions!\n"
                + tree.toString());
    }

    //TODO: Make use of this. Move to Solver.
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
            min = Double.min(min, delta / 1_000.0d);
            max = Double.max(max, delta / 1_000.0d);
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
