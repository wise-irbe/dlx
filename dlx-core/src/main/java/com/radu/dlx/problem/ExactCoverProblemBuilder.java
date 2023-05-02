package com.radu.dlx.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: We should later optimize this builder for the case where we have a lot of test data for the same problem
 */
public abstract class ExactCoverProblemBuilder {

    protected final List<String> items;
    protected final List<String> secondaryItems;
    protected final List<String[]> unsolvedOptions;
    protected final List<String[]> solvedOptions;


    public ExactCoverProblemBuilder() {
        items = new ArrayList<>();
        secondaryItems = new ArrayList<>();
        solvedOptions = new ArrayList<>();
        unsolvedOptions = new ArrayList<>();
    }

    public ExactCoveringProblem build() {
        if (!isValid()) {
            throw new IllegalStateException(invalidMessage());
        }
        calculateUnknownOptions(unsolvedOptions, items, secondaryItems);
        return new ExactCoveringProblem(items, secondaryItems, unsolvedOptions, solvedOptions);
    }

    protected abstract String invalidMessage();

    protected abstract void calculateUnknownOptions(List<String[]> options, List<String> items, List<String> secondaryItems);

    protected abstract boolean isValid();
}
