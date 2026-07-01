package com.stockflow.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an inventory item managed by the application.
 */
public class Product {
    private String id;
    private String name;
    private String category;
    private String vendor;
    private int quantity;
    private double unitPrice;
    private LocalDate dateAdded;

    public Product(String id, String name, String category, String vendor, int quantity, double unitPrice, LocalDate dateAdded) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.vendor = vendor;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.dateAdded = dateAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public double getInventoryValue() {
        return quantity * unitPrice;
    }

    public Product copy() {
        return new Product(id, name, category, vendor, quantity, unitPrice, dateAdded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
