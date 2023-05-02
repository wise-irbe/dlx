package com.radu.dlx.io.tree;

import com.radu.dlx.io.storer.SolutionStorer;
import com.radu.dlx.struct.DancingStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractSolutionTree implements SolutionTree {
    private final SolutionStorer storer;
    private final int maxSolutions;
    private final DancingStructure struct;

    private int maxLevel;
    protected int level;
    protected int increments;
    private int decrements;

    AbstractSolutionTree(DancingStructure struct, SolutionStorer storer, int maxSolutions) {
        this.struct = struct;
        this.storer = storer;
        this.maxSolutions = maxSolutions;
        maxLevel = Integer.MIN_VALUE;
        level = -1;
        increments = 0;
        decrements = 0;
    }

    protected final void increment() {
        level++;
        if (level > maxLevel) {
            maxLevel = level;
        }
        increments++;
    }

    protected final void decrement() {
        level--;
        decrements++;
    }

    @Override
    public int currentItem() {
        return struct.getOptionItem(currentOption());
    }

    @Override
    public int level() {
        return level;
    }


    @Override
    public final boolean isEmpty() {
        return increments <= 0;
    }

    @Override
    public final int size() {
        return increments + decrements;
    }

    @Override
    public final void storeSolution() {
        storer.store(this);
    }

    @Override
    public final int getSolutionCount() {
        return storer.getSolutionCount();
    }

    @Override
    public final int[] getFirstSolution() {
        return storer.getFirstSolution();
    }

    @Override
    public final int maxLevel() {
        return maxLevel;
    }

    @Override
    public final int maxSolutionNum() {
        return maxSolutions;
    }

    @Override
    public final boolean isDone() {
        boolean foundAllSolutions = maxSolutionNum() <= getSolutionCount();
        return !isEmpty() && (level < 0 || foundAllSolutions);
    }

    @Override
    public final Stream<String> printCurrentSolutions(DancingStructure struct) {
        return storer.getSolutions().map(struct.printer()::printCurrentSolution);
    }

    @Override
    public Stream<List<String>> printCurrentSolutionsAsList(DancingStructure struct) {
        return storer.getSolutions()
                .parallel()
                .map(opts -> Arrays.stream(opts)
                        .mapToObj(struct.printer()::printOption).collect(Collectors.toList()));
    }

    @Override
    public final double completionScore() {
        double score = 0.0d;
        if (level < 0) {
            return score;
        }

        int total = 1;
        int[] current = getActiveBranch();
        for (int i = 0; i < level + 1; i++) {
            int opt = current[i];
            if (opt > -1) {
                total *= struct.getOptionRowNum(opt);
                int choice = struct.getOptionPositionInRow(opt);
                if (choice > 1) {
                    score += 1.0d * (choice - 1) / total;
                }
            }
        }

        score += 1.0d / (2 * total);//TODO: investigate case when list is empty
        return score;
    }
}
