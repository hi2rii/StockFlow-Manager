package com.stockflow.algorithms;

import com.stockflow.model.Product;

import java.util.Comparator;

public final class ProductComparators {
    private ProductComparators() {
    }

    public static Comparator<Product> byName() {
        return Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Product> byCategory() {
        return Comparator.comparing(Product::getCategory, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Product> byQuantity() {
        return Comparator.comparingInt(Product::getQuantity).thenComparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Product> byDate() {
        return Comparator.comparing(Product::getDateAdded).thenComparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Product> byId() {
        return Comparator.comparing(Product::getId, String.CASE_INSENSITIVE_ORDER);
    }
}
