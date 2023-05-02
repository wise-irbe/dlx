package com.radu.dlx.io.tree;

import com.radu.dlx.struct.DancingStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TODO: create a streamable solution tree printer?
 */
public final class SolutionTreePrinter implements TreePrinter {
    private static final int DEFAULT_NODE_PRINT_WIDTH = 10;
    private final Map<Integer, SortedMap<Integer, List<String>>> grid = new HashMap<>();
    private final DancingStructure struct;
    private final Integer defaultPrintWidth;
    private final boolean justify;
    private final boolean abbreviate;
    private int minColumn = 0;

    public static SolutionTreePrinter create(DancingStructure struct, boolean justify, boolean abbreviate) {
        return new SolutionTreePrinter(struct, justify, abbreviate);
    }

    private SolutionTreePrinter(DancingStructure struct, boolean justify, boolean abbreviate) {
        defaultPrintWidth = DEFAULT_NODE_PRINT_WIDTH;
        this.struct = struct;
        this.justify = justify;
        this.abbreviate = abbreviate;
    }

    @Override
    public String print(OfIntSolutionTree tree) {
        clear();
        if (tree.isEmpty()) {
            return "";
        }
        layoutTree(tree.getRoot());
        if (abbreviate) {
            return printFixed();
        } else {
            return printFlex();
        }
    }

    private String printFixed() {
        return print(defaultPrintWidth);
    }

    private String printFlex() {
        int maxNodeWidth = defaultPrintWidth;
        for (SortedMap.Entry<Integer, SortedMap<Integer, List<String>>> entry : grid.entrySet()) {
            SortedMap<Integer, List<String>> colToNodes = entry.getValue();
            for (SortedMap.Entry<Integer, List<String>> colToNode : colToNodes.entrySet()) {
                for (String leaf : colToNode.getValue()) {
                    maxNodeWidth = Integer.max(maxNodeWidth, leaf.length());
                }
            }
        }
        return print(maxNodeWidth);
    }

    private String print(int width) {
        StringBuilder builder = new StringBuilder();
        String space = " ".repeat(width);

        for (SortedMap.Entry<Integer, SortedMap<Integer, List<String>>> entry : grid.entrySet()) {
            SortedMap<Integer, List<String>> colToNodes = entry.getValue();
            int firstColumn = colToNodes.firstKey() - minColumn;
            if (justify) {
                builder.append(space.repeat(firstColumn));
            }
            for (SortedMap.Entry<Integer, List<String>> colToNode : colToNodes.entrySet()) {
                List<String> leaves = colToNode.getValue();
                if (!leaves.isEmpty()) {
                    for (int j = 0, count = leaves.size(); j < count; j++) {
                        builder.append(padOrTrim(leaves.get(j), width));
                        if (j + 1 < count) {
                            builder.append('-');
                        }
                    }
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    private void clear() {
        grid.clear();
        minColumn = 0;
    }

    private void updateGrid(OfIntSolutionTree.PrimitiveNode node, int column, int level, boolean isLeft) {
        if (grid.containsKey(level)) {
            updateColToNode(grid.get(level), node, column, isLeft);
        } else {
            SortedMap<Integer, List<String>> colToNode = new TreeMap<>();
            grid.put(level, colToNode);
            updateColToNode(colToNode, node, column, isLeft);
        }
    }

    private void updateColToNode(SortedMap<Integer, List<String>> colToNode, OfIntSolutionTree.PrimitiveNode node, int column, boolean isLeft) {
        String val = printNode(node, isLeft);

        if (colToNode.containsKey(column)) {
            colToNode.get(column).add(val);
        } else {
            List<String> nodes = new ArrayList<>();
            nodes.add(val);
            colToNode.put(column, nodes);
        }
    }

    private String padOrTrim(String str, int width) {
        int strLength = str.length();

        if (strLength < width) {
            int diff = width - str.length();
            String prefix = " ".repeat(diff / 2);
            String suffix = " ".repeat(diff - diff / 2);
            return prefix + str + suffix;
        }
        if (strLength > width) {
            return str.substring(0, width);
        }
        return str;
    }

    private String padWidthDirection(String val, boolean isLeft) {
        String valStr = "[" + val + "]";
        return isLeft ? valStr + "<-" : "->" + valStr;
    }

    private String printNode(OfIntSolutionTree.PrimitiveNode node, boolean isLeft) {
        if (node.getParent() == null) {
            return "[root]";
        }
        return padWidthDirection(
                printOption(node), isLeft);
    }

    private String printOption(OfIntSolutionTree.PrimitiveNode node) {
        int val = node.getValue();
        if (abbreviate) {
            return struct.printer().printOption(val);
        }
        return struct.printer().printOptionItemList(val);
    }

    private void layoutTree(OfIntSolutionTree.PrimitiveNode rootNode) {
        layoutTree(rootNode, 0, 0, true);
    }

    private void layoutTree(OfIntSolutionTree.PrimitiveNode node, int column, int level, boolean isLeft) {
        minColumn = Integer.min(minColumn, column);

        if (node.activeNode != null) {
            layoutTree(node.activeNode, column - 1, level + 1, true);
        }

        updateGrid(node, column, level, isLeft);

        if (node.inactiveNodes != null) {

            Iterator<OfIntSolutionTree.PrimitiveNode> it = node.inactiveNodes.iterator();
            int i = 1;
            while (it.hasNext()) {
                OfIntSolutionTree.PrimitiveNode child = it.next();
                layoutTree(child, column + i, level + 1, false);
                i++;
            }
        }
    }
}
