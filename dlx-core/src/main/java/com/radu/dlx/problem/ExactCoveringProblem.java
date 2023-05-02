package com.radu.dlx.problem;

import java.util.List;

/**
 * Holder to contain necessary data for solving and later representing exact covering problems
 *
 * TODO: Maybe we can avoid this object and directly use the builders instead?
 */
public class ExactCoveringProblem {
    public final List<String> items;
    public final List<String> secondaryItems;
    public final List<String[]> options;
    public final List<String[]> knownOptions;

    public ExactCoveringProblem(List<String> items, List<String> secondaryItems, List<String[]> options, List<String[]> knownOptions) {
        this.items = items;
        this.secondaryItems = secondaryItems;
        this.options = options;
        this.knownOptions = knownOptions;
    }
}
