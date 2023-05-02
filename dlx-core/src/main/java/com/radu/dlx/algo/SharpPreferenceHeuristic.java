package com.radu.dlx.algo;

import com.radu.dlx.struct.DancingArray;

/**
 * If item name begins with '#', then that item is preferred over other items.
 * TODO: Make use of this heuristic
 */
public final class SharpPreferenceHeuristic implements BranchingItemHeuristic {

    public static BranchingItemHeuristic create() {
        return new SharpPreferenceHeuristic();
    }

    private SharpPreferenceHeuristic() {
    }

    @Override
    public int applyAsInt(DancingArray struct) {
        int t = Integer.MAX_VALUE;
        int p = struct.right[0];
        int l = struct.len[p];
        int i = p;
        while (p != 0) {
            if (l > 1 && struct.items[p].charAt(0) != '#')
                l = 1 + l;
            if (l < t) {
                t = l;
                if (t == 0) return p;
                i = p;
            }
            p = struct.right[p];
        }
        return i;
    }
}
