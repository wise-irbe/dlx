package com.radu.dlx.io.storer;

import com.radu.dlx.SolverConfig;
import com.radu.dlx.io.tree.SolutionTree;

import java.util.stream.Stream;

/**
 * Dynamic service for storing solutions. When the number of solutions exceeds {@see #MAX_IN_MEMORY_SOLUTIONS},
 * storer will switch from {@see InMemoryStorer} to {@see MemoryMappedFileStorer}.
 * After the number of solutions exceeds {@see MAX_IN_FILE_MEMORY_SOLUTIONS}, only the solution count will be incremented after each storing event
 * using {@see CountingStorer}.
 */
public class DynamicStorer implements SolutionStorer {

    private SolutionStorer currentStorer;
    private int currentLimit;

    public DynamicStorer() {
        currentStorer = new InMemoryStorer();
        currentLimit = SolverConfig.MAX_IN_MEMORY_SOLUTIONS;
    }

    @Override
    public void store(SolutionTree tree) {
        if (currentStorer.getSolutionCount() == currentLimit) {
            if (currentStorer instanceof InMemoryStorer) {
                currentStorer = new CountingStorer(currentStorer);//MemoryMappedFileIntStorer(currentStorer);
                currentLimit = Integer.MAX_VALUE;//MAX_IN_FILE_MEMORY_SOLUTIONS;
            }
            //if (currentStorer instanceof MemoryMappedFileIntStorer) {
            //    currentStorer = new CountingStorer(currentStorer);
            //}
        }

        currentStorer.store(tree);
    }

    @Override
    public Stream<int[]> getSolutions() {
        return currentStorer.getSolutions();
    }

    @Override
    public int[] getFirstSolution() {
        return currentStorer.getFirstSolution();
    }

    @Override
    public int getSolutionCount() {
        return currentStorer.getSolutionCount();
    }
}
