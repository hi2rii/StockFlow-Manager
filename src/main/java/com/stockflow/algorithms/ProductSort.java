package com.stockflow.algorithms;

import com.stockflow.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sorting algorithms implemented explicitly for learning and portfolio review.
 */
public final class ProductSort {
    private ProductSort() {
    }

    /**
     * Insertion Sort - O(n^2): efficient for nearly sorted small lists.
     */
    public static List<Product> insertionSort(List<Product> products, Comparator<Product> comparator) {
        List<Product> sorted = new ArrayList<>(products);
        for (int i = 1; i < sorted.size(); i++) {
            Product key = sorted.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(sorted.get(j), key) > 0) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, key);
        }
        return sorted;
    }

    /**
     * Selection Sort - O(n^2): repeatedly selects the smallest remaining item.
     */
    public static List<Product> selectionSort(List<Product> products, Comparator<Product> comparator) {
        List<Product> sorted = new ArrayList<>(products);
        for (int i = 0; i < sorted.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < sorted.size(); j++) {
                if (comparator.compare(sorted.get(j), sorted.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            Product temp = sorted.get(i);
            sorted.set(i, sorted.get(minIndex));
            sorted.set(minIndex, temp);
        }
        return sorted;
    }

    /**
     * Merge Sort - O(n log n): divide-and-conquer sort used for larger lists.
     */
    public static List<Product> mergeSort(List<Product> products, Comparator<Product> comparator) {
        if (products.size() <= 1) {
            return new ArrayList<>(products);
        }
        int middle = products.size() / 2;
        List<Product> left = mergeSort(products.subList(0, middle), comparator);
        List<Product> right = mergeSort(products.subList(middle, products.size()), comparator);
        return merge(left, right, comparator);
    }

    private static List<Product> merge(List<Product> left, List<Product> right, Comparator<Product> comparator) {
        List<Product> result = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }
}
