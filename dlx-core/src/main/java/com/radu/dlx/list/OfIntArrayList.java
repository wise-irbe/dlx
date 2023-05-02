package com.radu.dlx.list;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * List for int primitive.
 * <p>
 * java.util.ArrayList was used as reference
 */
public class OfIntArrayList implements PrimitiveList.OfInt {
    private static final int[] EMPTY_LIST = {};
    private static final int DEFAULT_VALUE = 0;

    private int[] elements;
    private int size;

    public OfIntArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public OfIntArrayList(int capacity) {
        if (capacity > 0) {
            elements = new int[capacity];
        } else if (capacity == 0) {
            elements = EMPTY_LIST;
        } else {
            throw new IllegalArgumentException("List capacity can only be >= 0");
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(int e) {
        add(e, size);
        return true;
    }

    @Override
    public void set(int index, int value) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index);
        }
        if (index >= size) {
            add(value, index);
        } else {
            elements[index] = value;
        }
    }

    @Override
    public int pop() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        int val = elements[size - 1];
        size--;
        return val;
    }

    @Override
    public int peek() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        return elements[size - 1];
    }

    @Override
    public int get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        if (index >= size) {
            return DEFAULT_VALUE;
        }
        return elements[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int[] internalArray() {
        return elements;
    }

    @Override
    public int[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public int[] toArray(int fromInclusive, int toExclusive) {
        return Arrays.copyOfRange(elements, fromInclusive, toExclusive);
    }

    @Override
    public int[] toArray(int toExclusive) {
        return Arrays.copyOfRange(elements, 0, toExclusive);
    }

    private void add(int e, int s) {
        if (s >= elements.length)
            elements = grow(s + 1);
        elements[s] = e;
        size = s + 1;
    }

    private int[] grow(int minCapacity) {
        return elements = Arrays.copyOf(elements,
                newCapacity(minCapacity));
    }

    /**
     * Returns a capacity at least as large as the given minimum capacity.
     * Returns the current capacity increased by 50% if that suffices.
     * Will not return a capacity greater than MAX_ARRAY_SIZE unless
     * the given minimum capacity is greater than MAX_ARRAY_SIZE.
     *
     * @param minCapacity the desired minimum capacity
     * @throws OutOfMemoryError if minCapacity is less than zero
     */
    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity <= 0) {
            if (elements == EMPTY_LIST)
                return Math.max(DEFAULT_CAPACITY, minCapacity);
            if (minCapacity < 0) // overflow
                throw new OutOfMemoryError();
            return minCapacity;
        }
        return (newCapacity - MAX_ARRAY_SIZE <= 0)
                ? newCapacity
                : hugeCapacity(minCapacity);
    }
}
