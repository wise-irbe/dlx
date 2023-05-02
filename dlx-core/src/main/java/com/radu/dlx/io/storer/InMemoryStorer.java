package com.radu.dlx.io.storer;

import com.radu.dlx.io.tree.SolutionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InMemoryStorer implements SolutionStorer {
    private final List<int[]> solutions;

    public InMemoryStorer() {
        solutions = new ArrayList<>();
    }

    @Override
    public void store(SolutionTree tree) {
        solutions.add(tree.getActiveBranch());
    }

    @Override
    public Stream<int[]> getSolutions() {
        return solutions.stream();
    }

    @Override
    public int[] getFirstSolution() { //TODO: maybe also provide a sublist query?
        if (solutions.isEmpty()) {
            return new int[]{};
        }
        return solutions.get(0);
    }

    @Override
    public int getSolutionCount() {
        return solutions.size();
    }
}
