package com.stockflow.storage;

import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;
import com.stockflow.utils.CsvUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportExporter {
    public void exportVendorReport(Path path, List<Product> products) {
        ensureParent(path);
        Map<String, Integer> stockByVendor = new LinkedHashMap<>();
        Map<String, Double> valueByVendor = new LinkedHashMap<>();
        for (Product product : products) {
            stockByVendor.merge(product.getVendor(), product.getQuantity(), Integer::sum);
            valueByVendor.merge(product.getVendor(), product.getInventoryValue(), Double::sum);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("vendor,totalQuantity,totalInventoryValue");
            writer.newLine();
            for (String vendor : stockByVendor.keySet()) {
                writer.write(CsvUtil.escape(vendor) + "," + stockByVendor.get(vendor) + "," + String.format("%.2f", valueByVendor.get(vendor)));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new StorageException("Unable to export vendor report: " + ex.getMessage(), ex);
        }
    }

    public void exportActivityReport(Path path, List<StockActivity> activities) {
        ensureParent(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("productId,productName,type,quantity,timestamp,note");
            writer.newLine();
            for (StockActivity activity : activities) {
                writer.write(String.join(",",
                        CsvUtil.escape(activity.getProductId()),
                        CsvUtil.escape(activity.getProductName()),
                        activity.getType().name(),
                        String.valueOf(activity.getQuantity()),
                        String.valueOf(activity.getTimestamp()),
                        CsvUtil.escape(activity.getNote())
                ));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new StorageException("Unable to export activity report: " + ex.getMessage(), ex);
        }
    }

    private void ensureParent(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException ex) {
            throw new StorageException("Unable to create report directory: " + ex.getMessage(), ex);
        }
    }
}
