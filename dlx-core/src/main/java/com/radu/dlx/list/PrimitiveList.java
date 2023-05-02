package com.radu.dlx.list;

/**
 * Class for list containing only primitive data types.
 *
 * @param <ARRAY> We are using as a parameter the primitive array,
 *                as Java generics do not support primitive values.
 */
public interface PrimitiveList<ARRAY> {
    int DEFAULT_CAPACITY = 10;
    /**
     * The maximum size of array to allocate (unless necessary).
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    int size();

    boolean isEmpty();

    /**
     * @return internal array representation.
     * @implNote NB! is mutable and not thread-safe! But we need this when we will anyway discard the list and just need the created array.
     * This avoids array copying that for example java.util.ArrayList#toArray forces upon us
     */
    ARRAY internalArray();

    /**
     * TODO: Rename to toArray
     * @return a new array. This array is usable by other threads.
     */
    ARRAY toArray();

    ARRAY toArray(int fromInclusive, int toExclusive);

    ARRAY toArray(int toExclusive);

    default int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE)
                ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }

    interface OfInt extends PrimitiveList<int[]> {
        static int[] empty() {
            return new int[0];
        }

        boolean add(int val);

        int get(int index);

        /**
         * Replace element at index position with specified value.
         *
         * @param index position where the value will be replaced
         * @param value value that will be stored at the specified position
         * @throws IndexOutOfBoundsException for when index is out of range bounds
         * @implNote Differs from java.util.List by not having a return value.
         * As we do not need that yet, did not advance it.
         */
        void set(int index, int value);

        int pop();

        int peek();
    }

    interface OfObject<T> extends PrimitiveList<T[]> {
        static int[] empty() {
            return new int[0];
        }

        boolean add(T val);

        T get(int index);

        /**
         * Replace element at index position with specified value.
         *
         * @param index position where the value will be replaced
         * @param value value that will be stored at the specified position
         * @throws IndexOutOfBoundsException for when index is out of range bounds
         * @implNote Differs from java.util.List by not having a return value.
         * As we do not need that yet, did not advance it.
         */
        void set(int index, T value);

        T pop();

        T peek();
    }
}
