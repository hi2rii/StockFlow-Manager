package com.stockflow.algorithms;

import com.stockflow.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Search algorithms used by the inventory service.
 */
public final class ProductSearch {
    private ProductSearch() {
    }

    /**
     * Linear Search - O(n): scans every product until matching text is found.
     */
    public static List<Product> linearSearch(List<Product> products, String query) {
        List<Product> matches = new ArrayList<>();
        String normalized = normalize(query);
        for (Product product : products) {
            if (normalize(product.getId()).contains(normalized)
                    || normalize(product.getName()).contains(normalized)
                    || normalize(product.getCategory()).contains(normalized)) {
                matches.add(product);
            }
        }
        return matches;
    }

    /**
     * Binary Search - O(log n): requires products sorted by ID before searching.
     */
    public static Product binarySearchById(List<Product> sortedProducts, String productId) {
        int left = 0;
        int right = sortedProducts.size() - 1;
        String target = normalize(productId);
        while (left <= right) {
            int middle = left + (right - left) / 2;
            Product product = sortedProducts.get(middle);
            int comparison = normalize(product.getId()).compareTo(target);
            if (comparison == 0) {
                return product;
            }
            if (comparison < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return null;
    }

    public static List<Product> sortedByIdCopy(List<Product> products) {
        List<Product> copy = new ArrayList<>(products);
        copy.sort(Comparator.comparing(Product::getId, String.CASE_INSENSITIVE_ORDER));
        return copy;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
