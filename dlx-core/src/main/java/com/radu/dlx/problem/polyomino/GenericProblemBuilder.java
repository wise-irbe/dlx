package com.radu.dlx.problem.polyomino;

import com.radu.dlx.Solver;
import com.radu.dlx.problem.ExactCoverProblemBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GenericProblemBuilder extends ExactCoverProblemBuilder {
    private int n;

    private GenericProblemBuilder() {
        n = 0;
    }

    private GenericProblemBuilder addOptionRow(String... optionRow) {
        unsolvedOptions.add(optionRow);
        n++;
        return this;
    }

    private GenericProblemBuilder addSecondaryItem(String item) {
        secondaryItems.add(item);
        return this;
    }

    public static SolverBuilder builder(Solver.ProblemAlgoBuilder parent) {
        return new SolverBuilder(parent);
    }

    @Override
    protected String invalidMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void calculateUnknownOptions(List<String[]> options, List<String> items, List<String> secondaryItems) {
        Set<String> set = new HashSet<>();
        Set<String> secondaryItemSet = new HashSet<>(secondaryItems);
        for (String[] option : options) {
            for (String item : option) {
                if (!set.contains(item) && !secondaryItemSet.contains(item)) {
                    items.add(item);
                    set.add(item);
                }
            }
        }
    }

    @Override
    protected boolean isValid() {
        return n > 0;
    }

    public static final class SolverBuilder {
        private final Solver.ProblemAlgoBuilder parent;
        private final GenericProblemBuilder problemBuilder;

        private SolverBuilder(Solver.ProblemAlgoBuilder parent) {
            this.parent = parent;
            problemBuilder = new GenericProblemBuilder();
        }

        public SolverBuilder addOptionRow(String... optionRow) {
            problemBuilder.addOptionRow(optionRow);
            return this;
        }

        public Solver.ProblemAlgoBuilder forAlgorithm() {
            return parent.withProblem(problemBuilder.build());
        }
    }
}
