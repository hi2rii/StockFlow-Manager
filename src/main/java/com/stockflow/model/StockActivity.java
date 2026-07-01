package com.stockflow.model;

import java.time.LocalDateTime;

/**
 * Records stock movements for audit and reporting.
 */
public class StockActivity {
    public enum Type {
        ADD,
        REMOVE
    }

    private final String productId;
    private final String productName;
    private final Type type;
    private final int quantity;
    private final LocalDateTime timestamp;
    private final String note;

    public StockActivity(String productId, String productName, Type type, int quantity, LocalDateTime timestamp, String note) {
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Type getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNote() {
        return note;
    }
}
