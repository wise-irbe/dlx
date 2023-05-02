package com.radu.dlx.struct;

import java.util.Collection;
import java.util.Iterator;

/**
 * Issue is that we are creating a class, that heavily uses Dancingarray internals.
 * At the same time we do not want to pollute Dancingarray implementation with this printing functionality.
 * Thus, we are making the factory method an optional to relate this semantic, while at the same time making use of interfaces
 * Maybe we should then return a default printer, which does nothing?
 */
public final class StructurePrinters implements StructurePrinter {
    private static final int NOT_FOUND = -1;

    private final DancingArray array;
    private final DancingArrayMemoryPrinter memoryPrinter;

    private StructurePrinters(DancingArray array) {
        this.array = array;
        memoryPrinter = DancingArrayMemoryPrinter.create();
    }

    public static StructurePrinter create(DancingStructure array) {
        if (!(array instanceof DancingArray)) {
            throw new IllegalArgumentException("Not implemented for class " + array.getClass().getName());
        }
        return new StructurePrinters((DancingArray) array);
    }

    @Override
    public String printMemory() {
        return memoryPrinter.print(array);
    }

    @Override
    public String printItem(int item) {
        return array.items[item];
    }

    @Override
    public String printOption(int option) {
        if (array.top[option] <= 0) {
            return "";//create spacer annotation
        }
        String id = array.items[array.top[option]];//option ID
        int p = option - 1;
        while (!array.isSpacer(p)) {
            p--;
        }
        return -array.top[p] + ":" + id;
    }

    @Override
    public String describe(int element, boolean verbose) {//ex 12
        String result = verbose ? "x=" + element : "";
        if (element < array.itemTotal) {
            return (verbose ? result + " is item " : "") + array.items[element] + " (item)";
        } else if (element > array.optionTotal - 1) {
            return verbose ? result + " out of range" : "";
        } else if (array.isSpacer(element)) {
            return (verbose ? result + " is spacer " : "") + array.items[-array.top[element]] + " (spacer)";
        } else {
            result += verbose ? " is option\n" : "";
            result += printOptionList(element);
            return result + " " + printOptionPosition(element);
        }
    }

    private String printOptionPosition(int x) {
        int pos = array.getOptionPositionInRow(x);
        if (pos > NOT_FOUND) {
            return "(" + pos + " of " + array.len[array.top[x]] + ")";
        }
        return "(not found)";
    }

    private String printOptionList(int x) {
        StringBuilder result = new StringBuilder();
        int q = x;
        do {
            result.append(" ").append(array.items[array.top[q]]);
            q = q + 1;
            if (array.isSpacer(q)) {
                q = array.up[q];
            }
        } while (q != x);
        return result.toString();
    }

    @Override
    public String printOptionItemList(int optionIdx) {
        Collection<String> options = array.getItemList(optionIdx);
        if (options.isEmpty()) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            String row = printOptionListRow(optionIdx);
            result.append(row).append(":");
            Iterator<String> it = options.iterator();
            while (it.hasNext()) {
                result.append(it.next());
                if (it.hasNext()) {
                    result.append("->");
                }
            }
            return result.toString();
        }
    }

    private String printOptionListRow(int x) {
        if (array.top[x] <= 0) {
            return "";//create spacer annotation
        }

        int p = x - 1;
        while (!array.isSpacer(p)) {
            p--;
        }

        return Integer.toString(-array.top[p]);
    }


    /**
     * @param solution solution array
     * @return solution description
     */
    public String printCurrentSolution(int[] solution) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, len = solution.length; i < len; i++) {
            int x = solution[i];
            result.append(printOptionItemList(x));
            if (i < len - 1) {
                result.append(',');
            }
        }
        return result.toString();
    }
}
