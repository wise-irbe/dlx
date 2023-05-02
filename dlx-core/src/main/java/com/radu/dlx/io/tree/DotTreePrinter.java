package com.radu.dlx.io.tree;

import com.radu.dlx.struct.DancingStructure;
import com.radu.dlx.struct.StructurePrinter;

import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import static com.radu.dlx.io.tree.DotTreePrinter.MergeMode.BY_ITEM;
import static com.radu.dlx.io.tree.DotTreePrinter.MergeMode.BY_NODE;
import static com.radu.dlx.io.tree.DotTreePrinter.MergeMode.BY_OPTION;

/**
 * Prints solution tree in GraphViz DOT format
 * TODO: create a streamable solution tree printer?
 * TODO: mark nodes that are part of the solution
 */
public final class DotTreePrinter implements TreePrinter {
    private final GenericDotTreePrinter byOptionPrinter;
    private final GenericDotTreePrinter byItemPrinter;
    private final GenericDotTreePrinter byNodePrinter;

    public static DotTreePrinter create(DancingStructure struct) {
        return new DotTreePrinter(struct);
    }

    private DotTreePrinter(DancingStructure struct) {
        this.byOptionPrinter = new GenericDotTreePrinter(BY_OPTION, struct);
        this.byItemPrinter = new GenericDotTreePrinter(BY_ITEM, struct);
        this.byNodePrinter = new GenericDotTreePrinter(BY_NODE, struct);
    }

    @Override
    public String print(OfIntSolutionTree tree) {
        if (tree.isEmpty()) {
            return "digraph solutiontree {}";
        }
        return byNodePrinter.print(tree) + byOptionPrinter.print(tree) + byItemPrinter.print(tree);
    }

    enum MergeMode {
        BY_NODE,
        BY_OPTION,
        BY_ITEM
    }

    private static class GenericDotTreePrinter implements TreePrinter {
        private final DancingStructure struct;
        private final MergeMode mode;
        private final StructurePrinter printer;

        GenericDotTreePrinter(MergeMode mode, DancingStructure struct) {
            this.struct = struct;
            this.mode = mode;
            printer = struct.printer();
        }

        @Override
        public String print(OfIntSolutionTree tree) {
            if (tree.isEmpty()) {
                return "digraph " + mode.name() + " {}\n";
            }
            return printUniqueNodes(tree);
        }


        private String printUniqueNodes(OfIntSolutionTree tree) {
            OfIntSolutionTree.PrimitiveNode root = tree.getRoot();
            StringBuilder result = new StringBuilder("digraph " + mode.name() + " {\n");
            Set<String> nodes = new TreeSet<>();
            Set<String> edges = new TreeSet<>();

            Stack<OfIntSolutionTree.PrimitiveNode> stack = new Stack<>();
            stack.push(root);

            OfIntSolutionTree.PrimitiveNode currentNode;
            while (!stack.isEmpty()) {
                currentNode = stack.pop();
                nodes.add(printNode(currentNode));
                if (currentNode.parent != null) {
                    edges.add(printEdge(currentNode));
                }

                if (currentNode.inactiveNodes != null && !currentNode.inactiveNodes.isEmpty()) {
                    for (OfIntSolutionTree.PrimitiveNode node : currentNode.inactiveNodes) {
                        stack.push(node);
                    }
                }
                if (currentNode.activeNode != null) {
                    stack.push(currentNode.activeNode);
                }
            }

            for (String node : nodes) {
                result.append(node).append(";\n");
            }

            for (String edge : edges) {
                result.append(edge).append(";\n");
            }

            result.append("}\n");


            return result.toString();
        }

        private String printEdge(OfIntSolutionTree.PrimitiveNode node) {
            return printNodeId(node.parent) + "->" + printNodeId(node) + " [label=\"(" + printOption(node.parent) + ")->(" + printOption(node) + ")\"]";
        }

        private String printNode(OfIntSolutionTree.PrimitiveNode node) {
            return node.parent == null
                    ? printNodeId(node) + " [label=\"root\"]"
                    : printNodeId(node) + " [label=\"" + printShortOption(node) + "\"]";
        }

        private String printNodeId(OfIntSolutionTree.PrimitiveNode node) {
            if (mode == BY_OPTION) {
                String val = printer.printOptionItemList(node.value);
                return val.length() == 0 ? "root" : "\"" + val + "\"";
            } else if (mode == BY_ITEM) {
                return printShortOption(node);
            } else if (mode == BY_NODE) {
                return "n" + node.idx;
            } else {
                throw new IllegalStateException();
            }
        }


        private String printOption(OfIntSolutionTree.PrimitiveNode node) {
            int val = node.getValue();
            return printer.printOptionItemList(val);
        }

        private String printShortOption(OfIntSolutionTree.PrimitiveNode node) {
            int val = node.getValue();
            if (struct.getItemList(val).isEmpty()) {
                return "root";
            }
            return struct.getItemList(val).get(0);
        }
    }
}
