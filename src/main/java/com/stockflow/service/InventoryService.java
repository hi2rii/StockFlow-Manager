package com.stockflow.service;

import com.stockflow.algorithms.ProductComparators;
import com.stockflow.algorithms.ProductSearch;
import com.stockflow.algorithms.ProductSort;
import com.stockflow.config.AppConfig;
import com.stockflow.model.InventoryStats;
import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;
import com.stockflow.storage.InventoryStorage;
import com.stockflow.storage.ReportExporter;
import com.stockflow.utils.ProductValidator;
import com.stockflow.utils.ValidationException;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection-backed inventory service. This is the main production service used
 * by the Swing UI.
 */
public class InventoryService {
    private final InventoryStorage storage;
    private final ReportExporter reportExporter = new ReportExporter();
    private final List<Product> products = new ArrayList<>();
    private final List<StockActivity> activities = new ArrayList<>();

    public InventoryService(InventoryStorage storage) {
        this.storage = storage;
    }

    public void load() {
        products.clear();
        products.addAll(storage.loadProducts());
        activities.clear();
        activities.addAll(storage.loadActivities());
    }

    public void save() {
        storage.saveProducts(products);
        storage.saveActivities(activities);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public List<StockActivity> getActivities() {
        return new ArrayList<>(activities);
    }

    public void addProduct(Product product) {
        ProductValidator.validate(product);
        if (findById(product.getId()) != null) {
            throw new ValidationException("A product with this ID already exists.");
        }
        products.add(product);
        save();
    }

    public void updateProduct(String originalId, Product updatedProduct) {
        ProductValidator.validate(updatedProduct);
        Product existingWithNewId = findById(updatedProduct.getId());
        if (existingWithNewId != null && !existingWithNewId.getId().equals(originalId)) {
            throw new ValidationException("Another product already uses this ID.");
        }
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(originalId)) {
                products.set(i, updatedProduct);
                save();
                return;
            }
        }
        throw new ValidationException("Product was not found.");
    }

    public void deleteProduct(String productId) {
        products.removeIf(product -> product.getId().equals(productId));
        save();
    }

    public void adjustStock(String productId, int amount, StockActivity.Type type, String note) {
        if (amount <= 0) {
            throw new ValidationException("Stock amount must be greater than zero.");
        }
        Product product = findById(productId);
        if (product == null) {
            throw new ValidationException("Product was not found.");
        }
        int newQuantity = type == StockActivity.Type.ADD
                ? product.getQuantity() + amount
                : product.getQuantity() - amount;
        if (newQuantity < 0) {
            throw new ValidationException("Cannot remove more stock than currently available.");
        }
        product.setQuantity(newQuantity);
        activities.add(0, new StockActivity(product.getId(), product.getName(), type, amount, LocalDateTime.now(), note));
        save();
    }

    public List<Product> search(String query) {
        if (ProductValidator.isBlank(query)) {
            return getProducts();
        }
        Product exact = ProductSearch.binarySearchById(ProductSearch.sortedByIdCopy(products), query);
        if (exact != null) {
            List<Product> result = new ArrayList<>();
            result.add(exact);
            return result;
        }
        return ProductSearch.linearSearch(products, query);
    }

    public List<Product> filterByCategory(String category) {
        if (ProductValidator.isBlank(category) || "All Categories".equals(category)) {
            return getProducts();
        }
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    public List<Product> sortProducts(List<Product> source, SortMode sortMode) {
        if (sortMode == SortMode.NAME_INSERTION) {
            return ProductSort.insertionSort(source, ProductComparators.byName());
        }
        if (sortMode == SortMode.CATEGORY_SELECTION) {
            return ProductSort.selectionSort(source, ProductComparators.byCategory());
        }
        if (sortMode == SortMode.QUANTITY_MERGE) {
            return ProductSort.mergeSort(source, ProductComparators.byQuantity());
        }
        return ProductSort.mergeSort(source, ProductComparators.byDate());
    }

    public InventoryStats getStats() {
        int totalQuantity = 0;
        int lowStock = 0;
        Set<String> categories = new LinkedHashSet<>();
        for (Product product : products) {
            totalQuantity += product.getQuantity();
            categories.add(product.getCategory());
            if (product.getQuantity() <= AppConfig.LOW_STOCK_THRESHOLD) {
                lowStock++;
            }
        }
        return new InventoryStats(products.size(), totalQuantity, lowStock, categories.size());
    }

    public List<String> getCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (Product product : ProductSort.mergeSort(products, ProductComparators.byCategory())) {
            categories.add(product.getCategory());
        }
        return new ArrayList<>(categories);
    }

    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() <= AppConfig.LOW_STOCK_THRESHOLD) {
                lowStock.add(product);
            }
        }
        return lowStock;
    }

    public void exportVendorReport(Path path) {
        reportExporter.exportVendorReport(path, getProducts());
    }

    public void exportActivityReport(Path path) {
        reportExporter.exportActivityReport(path, getActivities());
    }

    private Product findById(String id) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }
        return null;
    }
}
