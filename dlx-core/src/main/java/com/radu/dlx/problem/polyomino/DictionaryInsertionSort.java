package com.radu.dlx.problem.polyomino;

import java.util.function.BiConsumer;

public class DictionaryInsertionSort implements BiConsumer<int[], int[]> {
    public void accept(int[] keys, int[] values) {
        for (int i = 1, len = keys.length; i < len; i++) {
            for (int j = i; j > 0 && keys[j] <= keys[j - 1]; j--) {
                if (keys[j] < keys[j - 1]) {
                    swap(keys, j, j - 1);
                    swap(values, j, j - 1);
                } else {
                    if (values[j] < values[j - 1]) {
                        swap(values, j, j - 1);
                    }
                }
            }
        }
    }

    private void swap(int[] arr, int from, int to) {
        int temp = arr[to];
        arr[to] = arr[from];
        arr[from] = temp;
    }
}
