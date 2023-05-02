package com.radu.dlx.algo;

/**
 * We will loop over the actions because we do not want to use the stack for the recursive function calls
 * and we can easily blow it up.
 * The use of return codes for control flow is also a very old-school and classic programming pattern.
 * In a way, similar to using goto-s. As actually assembler is all about jumps, it is not as bad as it sounds.
 * Our aim is to avoid increasing stack.
 */
public enum Action {
    /**
     * Choose branch
     */
    FORWARD,
    /**
     * Advance the branch
     */
    ADVANCE,
    /**
     * Uncover all items related to the current option
     */
    RECOVER,
    /**
     * Calculation process has finished
     */
    DONE
}
