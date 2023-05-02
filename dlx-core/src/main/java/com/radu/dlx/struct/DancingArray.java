package com.radu.dlx.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class will contain global preallocated arrays and lookup tables.
 * <p>
 * Array values are shift-encoded to save space and internally we are looking
 * at both options and items as 1D structures. Structure is created by lookups.
 * <p>
 * Spacers are equivalent to row numbers
 * <p>
 */
public class DancingArray implements DancingStructure {
    private static final int SPACER_BUFFER = 2;
    private static final int HEADER_BUFFER = 2; //1 for root node for primary items and 1 root for secondary items
    private static final int NOT_FOUND = -1;

    public final int[] left;
    public final int[] right;
    public final int[] up;
    public final int[] down;
    public final int[] len;
    public final int[] top;

    public final String[] items;

    public final int itemTotal;
    public final int optionTotal;
    private final StructurePrinter printer;

    private int optionRowNum = 0;

    protected DancingArray(int itemCount, int optionRowCount, int optionCount) {
        itemTotal = itemCount + HEADER_BUFFER;
        optionTotal = SPACER_BUFFER + itemCount + optionRowCount + optionCount;

        left = new int[itemTotal];
        right = new int[itemTotal];
        len = new int[optionTotal];

        up = new int[optionTotal];
        down = new int[optionTotal];
        top = new int[optionTotal];

        items = new String[itemTotal];

        printer = StructurePrinters.create(this);

    }

    protected DancingArray(
            int optionRowNum,
            int[] left,
            int[] right,
            int[] up,
            int[] down,
            int[] len,
            int[] top,
            String[] items
    ) {
        this.optionRowNum = optionRowNum;
        this.itemTotal = left.length;
        this.optionTotal = up.length;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.len = len;
        this.top = top;
        this.items = items;

        printer = StructurePrinters.create(this);
    }

    @Override
    public int size() {
        return optionTotal;
    }

    @Override
    public int getItemNum() {
        return itemTotal - HEADER_BUFFER;
    }

    @Override
    public int getOptionAndHeaderNum() {
        return optionTotal - itemTotal + HEADER_BUFFER - SPACER_BUFFER;
    }


    @Override
    public List<String> getItemList(int x) {
        if (top[x] <= 0) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        result.add(items[top[x]]);

        int p = x - 1;
        while (top[p] > 0) {
            result.add(0, items[top[p]]);
            p--;
        }

        p = x + 1;
        while (top[p] > 0) {
            result.add(items[top[p]]);
            p++;
        }
        return result;
    }

    @Override
    public int getOptionItem(int option) {
        return top[option];
    }

    @Override
    public int getOptionRowNum(int element) {
        return len[top[element]];
    }

    @Override
    public boolean isEmpty() {
        return right[0] == 0;
    }

    @Override
    public StructurePrinter printer() {
        return printer;
    }

    private List<Integer> findOptionRow(int x) {
        if (top[x] <= 0) {
            return Collections.emptyList();
        }
        List<Integer> result = new ArrayList<>();
        result.add(x);

        int p = x - 1;
        while (top[p] > 0) {
            result.add(0, p);
            p--;
        }

        p = x + 1;
        while (top[p] > 0) {
            result.add(p);
            p++;
        }
        return result;
    }

    private void hideItem(int x) {
        int l = left[x];
        int r = right[x];
        right[l] = r;
        left[r] = l;
    }

    private void unhideItem(int x) {
        int l = left[x];
        int r = right[x];
        right[l] = x;
        left[r] = x;
    }

    public void cover(int i) {
        int p = down[i];
        while (p != i && p != 0) { //for some cases p == 0 ? TODO:
            hide(p);
            p = down[p];
        }

        hideItem(i);
    }

    public void uncover(int i) {
        unhideItem(i);

        int p = up[i];
        while (p != i) {
            unhide(p);
            p = up[p];
        }
    }

    @Override
    public void hide(int p) {
        int q = p + 1;
        while (q != p) {

            int x = top[q];
            if (x <= 0) {//is spacer
                q = up[q];
            } else {
                hideOption(q);
                len[x] = len[x] - 1;
                q++;
            }
        }
    }

    @Override
    public void unhide(int p) {
        int q = p - 1;
        while (q != p) {

            int x = top[q];
            if (x <= 0) {
                q = down[q];
            } else {
                unhideOption(q);
                len[x] = len[x] + 1;
                q--;
            }
        }
    }

    @Override
    public boolean isSpacer(int x) {
        return top[x] <= 0;
    }

    protected void unhideOption(int q) {
        int u = up[q];
        int d = down[q];
        down[u] = q;
        up[d] = q;
    }

    protected void hideOption(int q) {
        int d = down[q];
        int u = up[q];
        down[u] = d;
        up[d] = u;
    }

    public int getOptionRowNum() {
        return optionRowNum;
    }


    @Override
    public int getOptionPositionInRow(int element) {
        int i = top[element];
        int q = down[i];
        if (q == i) {
            return NOT_FOUND;
        }
        int k = 1;
        while (q != element && q != i) {
            q = down[q];
            k++;
        }
        return k;
    }

    @Override
    public String toString() {
        return printer.printMemory();
    }

}
