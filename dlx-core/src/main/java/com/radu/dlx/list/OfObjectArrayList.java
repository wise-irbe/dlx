package com.radu.dlx.list;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OfObjectArrayList<T> implements PrimitiveList.OfObject<T> {
    private final T[] emptyList;

    private T[] elements;
    private int size;

    public OfObjectArrayList(Class<T> clazz) {
        this(clazz, DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private OfObjectArrayList(Class<T> clazz, int capacity) {
        emptyList = (T[]) Array.newInstance(clazz, 0);
        if(capacity < 0){
            throw new IllegalArgumentException("List capacity can only be >= 0");
        } else if (capacity > 0) {
            elements = (T[]) Array.newInstance(clazz, capacity);
        } else {
            elements = emptyList;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T e) {
        add(e, size);
        return true;
    }

    @Override
    public void set(int index, T value) {
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
    public T pop() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        T val = elements[size - 1];
        size--;
        return val;
    }

    @Override
    public T peek() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        return elements[size - 1];
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return elements[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T[] internalArray() {
        return elements;
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public T[] toArray(int fromInclusive, int toExclusive) {
        return Arrays.copyOfRange(elements, fromInclusive, toExclusive);
    }

    @Override
    public T[] toArray(int toExclusive) {
        return Arrays.copyOfRange(elements, 0, toExclusive);
    }

    private void add(T e, int s) {
        if (s >= elements.length)
            elements = grow(s + 1);
        elements[s] = e;
        size = s + 1;
    }

    private T[] grow(int minCapacity) {
        return elements = Arrays.copyOf(elements, newCapacity(minCapacity));
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
            if (elements == emptyList)
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
