package com.radu.dlx.algo;

import com.radu.dlx.io.tree.SolutionTree;

import java.util.stream.Stream;

/**
 * TODO: Can we partition the DancingStructure data structure in such a way,
 * TODO: that we could parallelise covering/uncovering and search several of them at the same time?
 */
public interface DancingAlgorithm {
    SolutionTree solve();

    Stream<String> printCurrentSolutions();

}
