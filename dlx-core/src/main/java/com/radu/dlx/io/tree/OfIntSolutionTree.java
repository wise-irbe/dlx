package com.radu.dlx.io.tree;

import com.radu.dlx.io.storer.CountingStorer;
import com.radu.dlx.io.storer.DynamicStorer;
import com.radu.dlx.io.storer.SolutionStorer;
import com.radu.dlx.list.OfIntArrayList;
import com.radu.dlx.list.PrimitiveList;
import com.radu.dlx.struct.DancingStructure;
import com.radu.dlx.struct.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unbalanced n-ary tree for keeping track of solution paths
 * and to enable debugging of complex backtracking
 * algorithms.
 * <p>
 * It maintains a map of active nodes that forms a solution.
 * Also the solution constraints are kept as node values.
 * <p>
 * When a new value is added at the same node depth to the current active path,
 * the subtree reference is moved to a inactive branch.
 * <p>
 * At the same time when branching is done, the depth value is kept.
 * This way we can easily represent the solution tree as a k-way tree, where
 * each node has one active node and a set of inactive branches,
 * while at the same time, internally we are maintaining a binary tree.
 */
public final class OfIntSolutionTree extends AbstractSolutionTree {
    private final TreePrinter printer;
    private final PrimitiveNode rootNode;

    private PrimitiveNode activeNode;


    private OfIntSolutionTree(DancingStructure struct, SolutionStorer storer, int maxSolutions) {
        this(struct, storer, maxSolutions, SolutionTreePrinter.create(struct, true, false));
    }

    private OfIntSolutionTree(DancingStructure struct, SolutionStorer storer, int maxSolutions, TreePrinter printer) {
        super(struct, storer, maxSolutions);
        rootNode = new PrimitiveNode(increments);
        activeNode = rootNode;
        this.printer = printer;
    }


    public static SolutionTree createStoring(DancingStructure struct, int maxSolutions) {
        return new OfIntSolutionTree(struct, new DynamicStorer(), maxSolutions);
    }

    public static SolutionTree createCounting(DancingStructure struct, int maxSolutions) {
        return new OfIntSolutionTree(struct, new CountingStorer(), maxSolutions);
    }

    public static SolutionTree create(DancingStructure struct, SolutionStorer storer, int maxSolutions) {
        return new OfIntSolutionTree(struct, storer, maxSolutions);
    }

    public static SolutionTree create(DancingStructure struct, SolutionStorer storer, int maxSolutions, TreePrinter printer) {
        return new OfIntSolutionTree(struct, storer, maxSolutions, printer);
    }

    @Override
    public void advance(int value) {
        increment();
        activeNode = activeNode.activeNode = new PrimitiveNode(activeNode, increments, value);
    }

    public PrimitiveNode getRoot() {
        return rootNode;
    }

    @Override
    public int backup() {
        if (level < 0) {
            throw new NoSuchElementException();
        }
        int value = activeNode.value;
        activeNode.parent.archive(activeNode);
        activeNode = activeNode.parent;
        decrement();
        return value;
    }

    @Override
    public int currentOption() {
        return activeNode.value;
    }

    @Override
    public int[] getActiveBranch() {
        if (level < 0) {
            return PrimitiveList.OfInt.empty();
        }
        PrimitiveList.OfInt result = new OfIntArrayList(level + 1);
        PrimitiveNode currentNode = rootNode;
        while (currentNode != null && currentNode.activeNode != null) {
            currentNode = currentNode.activeNode;
            result.add(currentNode.value);//no value in first node, thus it needs to be here
        }
        return result.internalArray();
    }

    @Override
    public String toString() {
        return printer.print(this);
    }

    public static final class PrimitiveNode implements Node<PrimitiveNode> {
        final int idx;
        PrimitiveNode activeNode;
        PrimitiveNode parent;
        List<PrimitiveNode> inactiveNodes;
        int value;

        private PrimitiveNode(int idx) {
            this.idx = idx;
        }

        private PrimitiveNode(PrimitiveNode parent, int idx, int value) {
            this.idx = idx;
            this.value = value;
            this.parent = parent;
        }

        public List<PrimitiveNode> getInactiveNodes() {
            if (inactiveNodes == null) {
                inactiveNodes = new ArrayList<>();
            }
            return inactiveNodes;
        }

        public void archive(PrimitiveNode oldNode) {
            List<PrimitiveNode> nodes = getInactiveNodes();
            if (nodes.size() > 0) {
                nodes.add(0, oldNode);
            } else {
                nodes.add(oldNode);
            }
            activeNode = null;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public PrimitiveNode getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return Integer.toString(idx);
        }
    }

}
