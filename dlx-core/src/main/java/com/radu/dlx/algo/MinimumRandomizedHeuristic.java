package com.radu.dlx.algo;

import com.radu.dlx.struct.DancingArray;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public final class MinimumRandomizedHeuristic implements BranchingItemHeuristic {

    @Override
    public int applyAsInt(DancingArray struct) {
        int t = Integer.MAX_VALUE;
        int p = struct.right[0];
        int i = p;
        int mins = 1;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (p != 0) {
            int l = struct.len[p];
            if (l < t) {
                t = l;
                i = p;
                mins = 1;
            } else if (l == t) {
                mins++;
                if (random.nextInt(mins) != 0) {
                    i = p;
                }
            }
            p = struct.right[p];
        }
        return i;
    }
}
