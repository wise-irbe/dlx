package com.radu.dlx.io.storer;

import com.radu.dlx.io.tree.SolutionTree;

import java.util.stream.Stream;

public class CountingStorer implements SolutionStorer {
    private int count = 0;

    public CountingStorer(SolutionStorer storer) {
        count += storer.getSolutionCount();
    }

    public CountingStorer() {
    }

    @Override
    public void store(SolutionTree tree) {
        count++;
    }

    @Override
    public Stream<int[]> getSolutions() {
        return Stream.empty();
    }

    @Override
    public int[] getFirstSolution() {
        return new int[]{};
    }

    @Override
    public int getSolutionCount() {
        return count;
    }

}
