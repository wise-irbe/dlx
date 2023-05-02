package com.radu.dlx.io;

/**
 * We can annotate interesting classes with memory access counter that
 * counts using a convention, how many times memory is being accessed.
 *
 * The measurement unit is called mems.
 *
 * load -> 1 mem
 * swap -> 2 mem
 * store -> 1 mem
 *
 * TODO:
 */
public interface MemoryAccessCounter {
    long getMems();
}
