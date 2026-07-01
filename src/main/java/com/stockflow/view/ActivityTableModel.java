package com.stockflow.view;

import com.stockflow.model.StockActivity;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityTableModel extends AbstractTableModel {
    private final String[] columns = {"Time", "Product ID", "Product", "Type", "Quantity", "Note"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private List<StockActivity> activities = new ArrayList<>();

    public void setActivities(List<StockActivity> activities) {
        this.activities = new ArrayList<>(activities);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return activities.size();
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
        StockActivity activity = activities.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return formatter.format(activity.getTimestamp());
            case 1:
                return activity.getProductId();
            case 2:
                return activity.getProductName();
            case 3:
                return activity.getType();
            case 4:
                return activity.getQuantity();
            case 5:
                return activity.getNote();
            default:
                return "";
        }
    }
}
