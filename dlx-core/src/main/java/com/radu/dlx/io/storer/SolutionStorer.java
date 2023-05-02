package com.radu.dlx.io.storer;

import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.DancingStructure;

import java.util.stream.Stream;

/**
 * Solution storing solution
 * <p>
 * Ensures that in case of problems where we can have even trillions of solutions, our IO is optimal
 * and we at the minimum retain the solution count number.
 *
 * @implNote As the service manages the different specialized storers, it is also acting as a delegate to the storers and ensures that
 * the client does not need to know anything about the storers.
 */
public interface SolutionStorer  {

    /**
     * Get solutions.
     * <p>
     * Stream can be extremely large! Thus check the solution count before collecting this into a collection!
     *
     * @return stream of solutions, where the integers are the option indexes and can be printed via {@link DancingStructure#printOption(int)}.
     */
    Stream<int[]> getSolutions();

    int[] getFirstSolution();

    int getSolutionCount();

    void store(SolutionTree tree);

}
