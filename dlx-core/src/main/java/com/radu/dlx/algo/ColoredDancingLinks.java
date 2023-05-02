package com.radu.dlx.algo;

import com.radu.dlx.io.progress.TrackingStream;
import com.radu.dlx.io.tree.SolutionTree;
import com.radu.dlx.struct.ColoredDancingArray;
import com.radu.dlx.struct.StructurePrinter;
import com.radu.dlx.struct.StructurePrinters;

import java.util.stream.Stream;

import static com.radu.dlx.algo.Action.ADVANCE;
import static com.radu.dlx.algo.Action.DONE;
import static com.radu.dlx.algo.Action.FORWARD;
import static com.radu.dlx.algo.Action.RECOVER;

/**
 * TODO: A lot of duplication with DancingLinks algorithm
 */
public final class ColoredDancingLinks implements DancingAlgorithm {

    private final ColoredDancingArray struct;
    private final SolutionTree solutions;
    private final AlgorithmListener listener;
    private final BranchingItemHeuristic itemFinder;
    private final StructurePrinter printer;

    private int branch;
    private int xl;

    public static ColoredDancingLinks create(SolutionTree solutionTree, ColoredDancingArray struct) {
        return new ColoredDancingLinks(solutionTree, struct, AlgorithmListener.SILENT);
    }

    public static ColoredDancingLinks createVebose(SolutionTree solutionTree, ColoredDancingArray struct, TrackingStream stream) {
        return new ColoredDancingLinks(solutionTree, struct, new TracingAlgoListener(stream));
    }

    private ColoredDancingLinks(SolutionTree solutions, ColoredDancingArray struct, AlgorithmListener listener) {
        this.struct = struct;
        this.solutions = solutions;
        this.listener = listener;
        itemFinder = MinimumReachableValueHeuristic.create();
        printer = StructurePrinters.create(struct);//TODO: how?
    }

    @Override
    public SolutionTree solve() {
        listener.beforeStart(struct, solutions);
        Action state = FORWARD;
        while (state != DONE) {
            state = step(state);
        }
        listener.afterEnd(struct, solutions);
        return solutions;
    }

    @Override
    public Stream<String> printCurrentSolutions() {
        return solutions.printCurrentSolutions(struct);
    }

    private Action step(Action state) {
        switch (state) {
            case FORWARD:
                return forward();
            case RECOVER:
                return recover();
            case ADVANCE:
                return advance();
        }
        return state;
    }

    public Action forward() {

        if (struct.isEmpty()) {//x2
            listener.beforeStore(struct, solutions);
            solutions.storeSolution();
            return doneOrRecover();//go to x8
        }

        branch = itemFinder.applyAsInt(struct);//x3

        struct.cover(branch);//x4
        xl = struct.down[branch];
        return ADVANCE;//x5
    }

    public Action advance() {//x5
        if (xl == branch) {
            return backtrack(); //x7
        } else {
            solutions.advance(xl);
            listener.afterAdvance(struct, solutions);
            int p = xl + 1;
            while (p != xl) {
                int j = struct.top[p];
                if (struct.isSpacer(p)) {
                    p = struct.up[p];
                } else {
                    struct.commit(p, j);//TODO: move to method cover and override from DancingLinks?
                    p++;
                }
            }
            return FORWARD;//x2
        }
    }

    public Action recover() {
        int p = xl - 1;//x6
        while (p != xl) {
            int j = struct.top[p];
            if (j <= 0) {
                p = struct.down[p];
            } else {
                struct.uncommit(p, j);
                p--;
            }
        }
        branch = struct.top[xl];
        xl = struct.down[xl];

        return ADVANCE; // got to x5
    }

    private Action backtrack() {
        struct.uncover(branch);
        return doneOrRecover();
    }


    private Action doneOrRecover() {
        //X8
        if (solutions.isDone()) {
            return DONE;//terminate
        }
        xl = solutions.backup();
        return RECOVER;//X6
    }

    @Override
    public String toString() {
        return "Dancing links algorithm X:\n"
                + "number of items is " + struct.getItemNum() + " number of options is " + struct.getOptionAndHeaderNum() + "\n"
                + "search level " + solutions.level() + " number of option lists is " + struct.getOptionRowNum() + "\n"
                + "current branch is " + struct.items[branch] + "\n"
                + "current option is " + printer.describe(xl, true) + "\n"
                + "current solution branch is " + printer.printCurrentSolution(solutions.getActiveBranch()) + "\n"
                + "memory state is:\n" + struct.toString()
                + "solution tree is:\n" + solutions.toString();
    }
}
