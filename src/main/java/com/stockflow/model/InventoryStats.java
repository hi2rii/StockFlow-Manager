package com.stockflow.model;

/**
 * Immutable dashboard statistics.
 */
public class InventoryStats {
    private final int totalProducts;
    private final int totalStockQuantity;
    private final int lowStockItems;
    private final int categoryCount;

    public InventoryStats(int totalProducts, int totalStockQuantity, int lowStockItems, int categoryCount) {
        this.totalProducts = totalProducts;
        this.totalStockQuantity = totalStockQuantity;
        this.lowStockItems = lowStockItems;
        this.categoryCount = categoryCount;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public int getLowStockItems() {
        return lowStockItems;
    }

    public int getCategoryCount() {
        return categoryCount;
    }
}
