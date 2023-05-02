package com.radu.dlx.algo;

import com.radu.dlx.struct.DancingArray;

public final class MinimumReachableValueHeuristic implements BranchingItemHeuristic {

    private MinimumReachableValueHeuristic() {

    }

    public static BranchingItemHeuristic create() {
        return new MinimumReachableValueHeuristic();
    }

    @Override
    public int applyAsInt(final DancingArray struct) {
        int t = Integer.MAX_VALUE;
        int p = struct.right[0];
        int i = p;
        while (p != 0) {
            int l = struct.len[p];
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
