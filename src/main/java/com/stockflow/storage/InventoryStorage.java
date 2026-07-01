package com.stockflow.storage;

import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;

import java.util.List;

public interface InventoryStorage {
    List<Product> loadProducts();

    List<StockActivity> loadActivities();

    void saveProducts(List<Product> products);

    void saveActivities(List<StockActivity> activities);
}
