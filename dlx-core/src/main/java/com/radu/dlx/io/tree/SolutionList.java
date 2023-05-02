package com.radu.dlx.io.tree;

import com.radu.dlx.io.storer.CountingStorer;
import com.radu.dlx.io.storer.DynamicStorer;
import com.radu.dlx.io.storer.SolutionStorer;
import com.radu.dlx.list.OfIntArrayList;
import com.radu.dlx.list.PrimitiveList;
import com.radu.dlx.struct.DancingStructure;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public final class SolutionList extends AbstractSolutionTree {

    private final PrimitiveList.OfInt activeBranch;

    private SolutionList(DancingStructure struct, SolutionStorer storer, int maxSolutions) {
        super(struct, storer, maxSolutions);
        activeBranch = new OfIntArrayList(struct.getItemNum());
    }

    public static SolutionTree createCounting(DancingStructure struct, int maxSolutions) {
        return new SolutionList(struct, new CountingStorer(), maxSolutions);
    }

    public static SolutionTree createStoring(DancingStructure struct, int maxSolutions) {
        return new SolutionList(struct, new DynamicStorer(), maxSolutions);
    }

    public static SolutionTree create(DancingStructure struct, SolutionStorer strategy, int maxSolutions) {
        return new SolutionList(struct, strategy, maxSolutions);
    }

    @Override
    public int currentOption() {
        return activeBranch.peek();
    }

    @Override
    public void advance(int val) {
        increment();
        activeBranch.add(val);
    }

    @Override
    public int backup() {
        if (activeBranch.isEmpty()) {
            throw new NoSuchElementException();
        }
        decrement();
        return activeBranch.pop();
    }

    @Override
    public int[] getActiveBranch() {
        int activeBranchLen = activeBranch.size();
        if (activeBranchLen <= 0) {
            return PrimitiveList.OfInt.empty();
        } else {
            return activeBranch.toArray();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SolutionList.class.getSimpleName() + "[", "]")
                .add("activeBranch=" + Arrays.toString(getActiveBranch()))
                .toString();
    }
}
