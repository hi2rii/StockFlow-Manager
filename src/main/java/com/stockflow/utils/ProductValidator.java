package com.stockflow.utils;

import com.stockflow.model.Product;

/**
 * Shared input validation for product forms and services.
 */
public final class ProductValidator {
    private ProductValidator() {
    }

    public static void validate(Product product) {
        if (isBlank(product.getId())) {
            throw new ValidationException("Product ID is required.");
        }
        if (isBlank(product.getName())) {
            throw new ValidationException("Product name is required.");
        }
        if (isBlank(product.getCategory())) {
            throw new ValidationException("Category is required.");
        }
        if (isBlank(product.getVendor())) {
            throw new ValidationException("Vendor is required.");
        }
        if (product.getQuantity() < 0) {
            throw new ValidationException("Quantity cannot be negative.");
        }
        if (product.getUnitPrice() < 0) {
            throw new ValidationException("Unit price cannot be negative.");
        }
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
