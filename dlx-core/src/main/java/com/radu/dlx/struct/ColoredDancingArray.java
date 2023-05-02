package com.radu.dlx.struct;

/**
 * This class will contain global preallocated arrays and lookup tables for
 * {@see DancingTorus}, {@see Option}, {@see Item}.
 * <p>
 * Array values are shift-encoded to save space and internally we are looking
 * at both options and items as 1D structures. Structure is created by lookups.
 */
public class ColoredDancingArray extends DancingArray {

    private static final int NO_COLOR = 0;
    private static final int SKIP_COLOR = -1;

    //TODO: maybe we should use int for colors and convert char to int? Will save memory and when equals is being executed!
    public final int[] colors;

    protected ColoredDancingArray(int itemCount, int optionRowCount, int optionCount) {
        super(itemCount, optionRowCount, optionCount);
        colors = new int[optionTotal];
    }

    protected ColoredDancingArray(int optionRowNum, int[] left, int[] right, int[] up, int[] down, int[] len, int[] top, String[] items, int[] colors) {
        super(optionRowNum, left, right, up, down, len, top, items);
        this.colors = colors;
    }

    public void commit(int p, int j) {
        int c = colors[p];
        if (NO_COLOR == c) {
            cover(j);
        } else if (SKIP_COLOR != c) {
            purify(p);
        }
    }

    private void purify(int p) {
        int c = colors[p];
        int i = top[p];
        int q = down[i];
        while (q != i) {
            if (c == colors[q]) {
                colors[q] = SKIP_COLOR;
            } else {
                hide(q);
            }
            q = down[q];
        }
    }

    public void uncommit(int p, int j) {
        int c = colors[p];
        if (NO_COLOR == c) {
            uncover(j);
        } else if (SKIP_COLOR != c) {
            unpurify(p);
        }
    }

    private void unpurify(int p) {
        int c = colors[p];
        int i = top[p];
        int q = up[i];
        while (q != i) {
            if (SKIP_COLOR == colors[q]) {
                colors[q] = c;
            } else {
                unhide(q);
            }
            q = up[q];
        }
    }

    @Override
    public void hide(int p) {
        int q = p + 1;
        while (q != p) {

            int x = top[q];
            if (x <= 0 || SKIP_COLOR == colors[q]) {//is spacer
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
            if (x <= 0 || SKIP_COLOR == colors[q]) {
                q = down[q];
            } else {
                unhideOption(q);
                len[x] = len[x] + 1;
                q--;
            }
        }
    }

    @Override
    public boolean isSpacer(int q) {
        return top[q] <= 0 || SKIP_COLOR == colors[q];
    }
}
