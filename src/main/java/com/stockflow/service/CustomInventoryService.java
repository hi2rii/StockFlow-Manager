package com.stockflow.service;

import com.stockflow.datastructures.CustomArrayList;
import com.stockflow.datastructures.CustomHashMap;
import com.stockflow.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Alternative inventory module backed by custom data structures instead of Java
 * Collections. Kept separate so reviewers can compare both approaches.
 */
public class CustomInventoryService {
    private final CustomArrayList<Product> productList = new CustomArrayList<>();
    private final CustomHashMap<String, Product> productById = new CustomHashMap<>();

    public void add(Product product) {
        productList.add(product);
        productById.put(product.getId(), product);
    }

    public Product getById(String productId) {
        return productById.get(productId);
    }

    public Product remove(String productId) {
        Product removed = productById.remove(productId);
        if (removed == null) {
            return null;
        }
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(productId)) {
                productList.removeAt(i);
                break;
            }
        }
        return removed;
    }

    public List<Product> toList() {
        List<Product> products = new ArrayList<>();
        for (Product product : productList) {
            products.add(product);
        }
        return products;
    }
}
