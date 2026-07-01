package com.stockflow.view;

import com.stockflow.model.Product;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Name", "Category", "Vendor", "Quantity", "Unit Price", "Value", "Date Added"};
    private List<Product> products = new ArrayList<>();

    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products);
        fireTableDataChanged();
    }

    public Product getProductAt(int row) {
        if (row < 0 || row >= products.size()) {
            return null;
        }
        return products.get(row);
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getId();
            case 1:
                return product.getName();
            case 2:
                return product.getCategory();
            case 3:
                return product.getVendor();
            case 4:
                return product.getQuantity();
            case 5:
                return String.format("$%.2f", product.getUnitPrice());
            case 6:
                return String.format("$%.2f", product.getInventoryValue());
            case 7:
                return product.getDateAdded();
            default:
                return "";
        }
    }
}
