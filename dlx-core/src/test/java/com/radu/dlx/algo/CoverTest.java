package com.radu.dlx.algo;

import com.radu.dlx.io.progress.TeeTrackingStream;
import com.radu.dlx.io.progress.TrackingStream;
import com.radu.dlx.io.tree.SolutionArray;
import com.radu.dlx.io.tree.SolutionList;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.DancingArray;
import com.radu.dlx.struct.DancingArrayBuilder;
import com.radu.dlx.struct.DancingStructure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverTest {

    @Test
    public void whenStacklessExactCover_Solve() {
        validateCoverStackless(DancingArrayBuilder.buildExactCoverProblem(),
                "3:A->D->F,4:B->G,0:C->E");
    }

    @Test
    public void whenStacklessSolutionArrayExactCover_Solve() {
        validateCoverStacklessForArraySolutionList(DancingArrayBuilder.buildExactCoverProblem(),
                "3:A->D->F,4:B->G,0:C->E");
    }

    @Test
    public void whenStacklessExactCover_SolveDebug() {
        validateCoverStackless(DancingArrayBuilder.buildExactCoverProblem(),
                "3:A->D->F,4:B->G,0:C->E");
    }


    @Test
    public void whenStacklessExactCover_Solve2() {
        validateCoverStackless(DancingArrayBuilder.buildExactCoverProblem2(),
                "3:A->D,0:C->E->F,4:B->G");
    }

    @Test
    public void whenStacklessExactCoverWithOptions_Solve() {
        DancingArray problem = DancingArrayBuilder.buildExactCoverProblemWithOptions();
        TrackingStream stream = new TeeTrackingStream();
        stream.register(val -> System.out.println(val.print()));
        DancingLinks algo = DancingLinks.createVebose(SolutionArray.createStoring(problem, 2), problem, stream);
        SolutionTree tree = algo.solve();
        List<String> solutions = tree.printCurrentSolutions(problem).collect(Collectors.toList());
        assertEquals("1:A->D->G,2:B->C->F", solutions.get(0));
        assertEquals("3:A->D->F,4:B->G,0:C->E", solutions.get(1));
        assertEquals(2, tree.getSolutionCount());
        assertEquals(2, tree.getFirstSolution().length);
        assertEquals("Dancing links algorithm X:\n" +
                "number of items is 7 number of options is 22\n" +
                "search level 2 number of option lists is 6\n" +
                "current branch is C\n" +
                "current option is x=9 is option\n" +
                " C E (1 of 1)\n" +
                "current solution branch is 3:A->D->F,4:B->G,0:C->E\n" +
                "memory state is:\n" +
                "    i      0     1     2     3     4     5     6     7     8   \n" +
                " name(i)         A     B     C     D     E     F     G         \n" +
                "llink(i)   0     0     0     0     3     8     5     5     8   \n" +
                "rlink(i)   0     2     3     0     0     8     7     8     8   \n" +
                "    x      0     1     2     3     4     5     6     7     8   \n" +
                " len(x)    0     2     1     1     1     0     1     0     0   \n" +
                "ulink(x)   0     20    24    9     27    5     18    7     0   \n" +
                "dlink(x)   0     12    24    9     27    5     18    7     10  \n" +
                "    x      9     10    11    12    13    14    15    16    17  \n" +
                " top(x)    3     5     -1    1     4     7     -2    2     3   \n" +
                "ulink(x)   3     5     9     1     4     7     12    2     9   \n" +
                "dlink(x)   3     5     14    20    21    25    18    24    3   \n" +
                "    x      18    19    20    21    22    23    24    25    26  \n" +
                " top(x)    6     -3    1     4     6     -4    2     7     -5  \n" +
                "ulink(x)   6     16    12    4     18    20    2     7     24  \n" +
                "dlink(x)   6     22    1     27    6     25    2     7     29  \n" +
                "    x      27    28    29    30  \n" +
                " top(x)    4     5     7     -6  \n" +
                "ulink(x)   4     10    25    27  \n" +
                "dlink(x)   4     5     7     0   \n" +
                "solution tree is:\n" +
                "SolutionArray[activeBranch=[20, 24, 9]]", algo.toString());
    }

    private void validateCoverStackless(DancingArray problem, String expectedSolution) {
        SolutionTree solutions = DancingLinks.create(SolutionList.createStoring(problem, 2), problem).solve();
        validateCoverSolution(problem, solutions, expectedSolution);
    }

    private void validateCoverStacklessForArraySolutionList(DancingArray problem, String expectedSolution) {
        SolutionTree solutions = DancingLinks.create(SolutionArray.createStoring(problem, 2), problem).solve();
        validateCoverSolution(problem, solutions, expectedSolution);
    }

    private void validateCoverSolution(DancingStructure struct, SolutionTree tree, String expectedSolution) {
        List<String> solutions = tree.printCurrentSolutions(struct).collect(Collectors.toList());
        assertEquals(expectedSolution, solutions.get(0));
        assertEquals(1, tree.getSolutionCount());
        assertEquals(3, tree.getFirstSolution().length);
    }
}
