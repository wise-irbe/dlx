package com.radu.dlx.io.tree;

import com.radu.dlx.io.storer.CountingStorer;
import com.radu.dlx.io.storer.DynamicStorer;
import com.radu.dlx.io.storer.SolutionStorer;
import com.radu.dlx.struct.DancingStructure;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/**
 * TODO: This is an interesting way to provide insight whether we have already visited a solution!
 * Might provide us a way to unroll the links faster, when recovering?
 */
public final class SolutionArray extends AbstractSolutionTree {

    private final int[] elements;
    private int currentOption;

    private SolutionArray(SolutionStorer storer, DancingStructure struct, int maxSolutions) {
        super(struct, storer, maxSolutions);
        elements = new int[struct.size()];
        Arrays.fill(elements, -1);
        currentOption = -1;
    }

    public static SolutionTree createStoring(DancingStructure build, int maxSolutions) {
        return new SolutionArray(new DynamicStorer(), build, maxSolutions);
    }

    public static SolutionTree createCounting(DancingStructure build, int maxSolutions) {
        return new SolutionArray(new CountingStorer(), build, maxSolutions);
    }

    @Override
    public int currentOption() {
        return currentOption;
    }

    @Override
    public void advance(int value) {
        increment();
        elements[value] = currentOption;
        currentOption = value;
    }

    @Override
    public int backup() {
        if (level < 0) {
            throw new NoSuchElementException();
        }
        int value = currentOption;
        currentOption = elements[currentOption];
        decrement();
        return value;
    }

    @Override
    public int[] getActiveBranch() {
        if (currentOption == -1) {
            return new int[]{};
        }
        int totalLevel = level + 1;
        int[] result = new int[totalLevel];

        result[0] = currentOption;

        int p = elements[currentOption];
        int i = 1;
        while (p != -1) {
            result[i] = p;
            p = elements[p];
            i++;
        }
        //the active branch is being obtained in the reverse order and thus we need to reverse it
        //TODO: can we manage without this?
        int[] reversed = new int[level + 1];
        for (int j = 0; j < totalLevel; j++) {
            reversed[j] = result[totalLevel - j - 1];
        }
        return reversed;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SolutionArray.class.getSimpleName() + "[", "]")
                .add("activeBranch=" + Arrays.toString(getActiveBranch()))
                .toString();
    }
}
