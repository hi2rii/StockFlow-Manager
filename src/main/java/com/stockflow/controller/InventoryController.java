package com.stockflow.controller;

import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;
import com.stockflow.service.InventoryService;
import com.stockflow.service.SortMode;
import com.stockflow.view.MainFrame;

import java.nio.file.Path;
import java.util.List;

/**
 * MVC controller connecting Swing views to inventory services.
 */
public class InventoryController {
    private final InventoryService service;
    private MainFrame view;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    public void setView(MainFrame view) {
        this.view = view;
        refreshView();
    }

    public void refreshView() {
        if (view != null) {
            view.updateDashboard(service.getStats(), service.getLowStockProducts());
            view.updateCategories(service.getCategories());
            view.updateProducts(service.getProducts());
            view.updateActivities(service.getActivities());
        }
    }

    public void addProduct(Product product) {
        service.addProduct(product);
        refreshView();
    }

    public void updateProduct(String originalId, Product product) {
        service.updateProduct(originalId, product);
        refreshView();
    }

    public void deleteProduct(String productId) {
        service.deleteProduct(productId);
        refreshView();
    }

    public void adjustStock(String productId, int amount, StockActivity.Type type, String note) {
        service.adjustStock(productId, amount, type, note);
        refreshView();
    }

    public void applyTableQuery(String searchText, String category, SortMode sortMode) {
        List<Product> products = searchText == null || searchText.trim().isEmpty()
                ? service.filterByCategory(category)
                : service.search(searchText);
        if (category != null && !"All Categories".equals(category) && (searchText == null || searchText.trim().isEmpty())) {
            products = service.filterByCategory(category);
        } else if (category != null && !"All Categories".equals(category)) {
            products.removeIf(product -> !product.getCategory().equalsIgnoreCase(category));
        }
        view.updateProducts(service.sortProducts(products, sortMode));
    }

    public void exportVendorReport(Path path) {
        service.exportVendorReport(path);
    }

    public void exportActivityReport(Path path) {
        service.exportActivityReport(path);
    }
}
