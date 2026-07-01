package com.stockflow.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * A separate-chaining hash map for demonstrating key-based inventory lookup.
 */
public class CustomHashMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        buckets = (Entry<K, V>[]) new Entry[16];
    }

    public void put(K key, V value) {
        if ((size + 1.0) / buckets.length > LOAD_FACTOR) {
            resize();
        }
        int index = bucketIndex(key);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
    }

    public V get(K key) {
        Entry<K, V> current = buckets[bucketIndex(key)];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = bucketIndex(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;
        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }
        return values;
    }

    public int size() {
        return size;
    }

    private int bucketIndex(K key) {
        return Math.abs(key == null ? 0 : key.hashCode()) % buckets.length;
    }

    private boolean keysEqual(K left, K right) {
        return left == null ? right == null : left.equals(right);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];
        size = 0;
        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
