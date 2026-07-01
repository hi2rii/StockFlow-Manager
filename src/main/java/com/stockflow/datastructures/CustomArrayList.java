package com.stockflow.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A compact generic dynamic array implementation used to demonstrate how
 * ArrayList-like storage works internally.
 */
public class CustomArrayList<T> implements Iterable<T> {
    private Object[] elements;
    private int size;

    public CustomArrayList() {
        this.elements = new Object[10];
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        elements[size++] = value;
    }

    public T get(int index) {
        checkIndex(index);
        return elementAt(index);
    }

    public void set(int index, T value) {
        checkIndex(index);
        elements[index] = value;
    }

    public T removeAt(int index) {
        checkIndex(index);
        T removed = elementAt(index);
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elements, index + 1, elements, index, moved);
        }
        elements[--size] = null;
        return removed;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        Object[] copy = new Object[size];
        System.arraycopy(elements, 0, copy, 0, size);
        return copy;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        while (newCapacity < minCapacity) {
            newCapacity *= 2;
        }
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) elements[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elementAt(cursor++);
            }
        };
    }
}
