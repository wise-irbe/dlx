package com.radu.dlx;

import com.radu.dlx.algo.DancingAlgorithm;
import com.radu.dlx.algo.DancingLinks;
import com.radu.dlx.io.progress.PrintableEvent;
import com.radu.dlx.io.progress.StreamListener;
import com.radu.dlx.io.progress.TeeTrackingStream;
import com.radu.dlx.io.progress.TrackingStream;
import com.radu.dlx.io.storer.CountingStorer;
import com.radu.dlx.io.storer.DynamicStorer;
import com.radu.dlx.io.storer.SolutionStorer;
import com.radu.dlx.io.tree.DotTreePrinter;
import com.radu.dlx.io.tree.OfIntSolutionTree;
import com.radu.dlx.io.tree.SolutionList;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.problem.ExactCoveringProblem;
import com.radu.dlx.problem.LangfordPairBuilder;
import com.radu.dlx.problem.NQueenProblem;
import com.radu.dlx.problem.polyomino.GenericProblemBuilder;
import com.radu.dlx.problem.polyomino.PentominoGenerator;
import com.radu.dlx.problem.sudoku.SudokuBoardBuilder;
import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrays;
import com.radu.dlx.struct.DancingStructure;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class Solver {
    private final static Logger LOGGER = Logger.getLogger(Solver.class.getName());

    private final SolverConfig config = new SolverConfig();
    private TrackingStream progress = TrackingStream.NULL_STREAM;

    private ExactCoveringProblem problem;
    private DancingAlgorithm algo;

    private Solver() {
    }

    public boolean isValid() {
        return problem != null && algo != null;
    }

    public Stream<String> printSolution() {
        return algo.printCurrentSolutions();
    }

    public boolean isDebug() {
        return config.debug;
    }

    public int maxSolutionCount() {
        return config.maxSolutionCount;
    }

    public void writeProgress(PrintableEvent event) {
        if (isDebug()) {
            progress.write(event);
        }
    }

    public static Solver.Builder builder() {
        return new Solver().new Builder();
    }

    public SolutionTree solve() {
        return algo.solve();
    }

    public class ProgressBuilder {
        private final Builder parent;

        private ProgressBuilder(Builder parent) {
            this.parent = parent;
            progress = new TeeTrackingStream();
            //executor = Executors.newSingleThreadExecutor();
        }

        public ProgressBuilder writeInfoToLogger() {
            progress.register(e -> LOGGER.log(Level.INFO, e.print()));
            return this;
        }

        public ProgressBuilder writeToStdOut() {
            progress.register(e -> System.out.println(e.print()));
            return this;
        }

        public Builder forSolver() {
            return parent;
        }

        public ProgressBuilder writeTo(StreamListener listener) {
            progress.register(listener);
            return this;
        }

        public LangfordPairBuilder.SolverBuilder forLangfordPair(int n) {
            return parent.forLangFordPair(n);
        }

        public GenericProblemBuilder.SolverBuilder forExactCover() {
            return parent.forExactCover();
        }

        public SudokuBoardBuilder.SolverBuilder forSudoku() {
            return parent.forSudoku();
        }

        public GenericProblemBuilder.SolverBuilder forPentomino() {
            return parent.forPentomino();
        }
    }

    public class ProblemAlgoBuilder {
        private final Builder parent;
        private boolean dotPrinter;

        public ProblemAlgoBuilder(Builder builder) {
            parent = builder;
        }

        public ProblemAlgoBuilder withProblem(ExactCoveringProblem newProblem) {
            problem = newProblem;
            return this;
        }

        public ProblemAlgoBuilder withDotTreePrinter() {
            dotPrinter = true;
            return this;
        }

        public Builder withStacklessAlgo() {
            DancingArray array = DancingArrays.create(problem);
            algo = createAlgo(array);
            return parent;
        }

        private SolutionStorer createStorer() {
            return parent.withoutSolutionStore ? new CountingStorer() : new DynamicStorer();
        }

        private DancingAlgorithm createAlgo(DancingArray array) {
            SolutionStorer storer = createStorer();
            SolutionTree tree = createSolutionTree(storer, array, maxSolutionCount());
            if (isDebug()) {
                return DancingLinks.createVebose(tree, array, progress);
            } else {
                return DancingLinks.create(tree, array);
            }
        }

        private SolutionTree createSolutionTree(SolutionStorer storer, DancingStructure struct, int maxSolutionNUm) {
            if (isDebug()) {
                if (dotPrinter) {
                    return OfIntSolutionTree.create(struct, storer, maxSolutionNUm, DotTreePrinter.create(struct));
                } else {
                    return OfIntSolutionTree.create(struct, storer, maxSolutionNUm);
                }
            } else {
                return SolutionList.create(struct, storer, maxSolutionNUm);
            }
        }

    }

    public class Builder {
        private boolean withoutSolutionStore;

        public Builder withoutSolutionStore() {
            withoutSolutionStore = true;
            return this;
        }

        public ProgressBuilder withDebug() {
            config.debug = true;
            return new ProgressBuilder(this);
        }

        private void throwIfInvalid() {
            if (algo == null || problem == null) {
                throw new IllegalStateException();
            }
        }

        public Builder maxSolutionCount(int count) {
            config.maxSolutionCount = count;
            return this;
        }


        public Solver build() {
            throwIfInvalid();
            return Solver.this;
        }

        public LangfordPairBuilder.SolverBuilder forLangFordPair(int n) {
            return LangfordPairBuilder.builder(n, new ProblemAlgoBuilder(this));
        }

        public GenericProblemBuilder.SolverBuilder forExactCover() {
            return GenericProblemBuilder.builder(new ProblemAlgoBuilder(this));
        }

        public SudokuBoardBuilder.SolverBuilder forSudoku() {
            return SudokuBoardBuilder.builder(new ProblemAlgoBuilder(this));
        }

        public NQueenProblem.SolverBuilder forNQueen(int n) {
            return NQueenProblem.builder(n, new ProblemAlgoBuilder(this));
        }


        public GenericProblemBuilder.SolverBuilder forPentomino() {
            return PentominoGenerator.build(new ProblemAlgoBuilder(this));
        }
    }

}
