package com.stockflow.service;

public enum SortMode {
    NAME_INSERTION("Name - Insertion Sort"),
    CATEGORY_SELECTION("Category - Selection Sort"),
    QUANTITY_MERGE("Quantity - Merge Sort"),
    DATE_MERGE("Date Added - Merge Sort");

    private final String label;

    SortMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
