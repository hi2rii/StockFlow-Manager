package com.stockflow.storage;

import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;
import com.stockflow.utils.CsvUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV persistence for products and stock activities.
 */
public class CsvInventoryStorage implements InventoryStorage {
    private final Path productsFile;
    private final Path activityFile;

    public CsvInventoryStorage(Path productsFile, Path activityFile) {
        this.productsFile = productsFile;
        this.activityFile = activityFile;
    }

    @Override
    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        if (!Files.exists(productsFile)) {
            return products;
        }
        try (BufferedReader reader = Files.newBufferedReader(productsFile)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                List<String> row = CsvUtil.parseLine(line);
                if (row.size() >= 7) {
                    products.add(new Product(
                            row.get(0),
                            row.get(1),
                            row.get(2),
                            row.get(3),
                            Integer.parseInt(row.get(4)),
                            Double.parseDouble(row.get(5)),
                            LocalDate.parse(row.get(6))
                    ));
                }
            }
        } catch (IOException | RuntimeException ex) {
            throw new StorageException("Unable to load products from " + productsFile + ": " + ex.getMessage(), ex);
        }
        return products;
    }

    @Override
    public List<StockActivity> loadActivities() {
        List<StockActivity> activities = new ArrayList<>();
        if (!Files.exists(activityFile)) {
            return activities;
        }
        try (BufferedReader reader = Files.newBufferedReader(activityFile)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                List<String> row = CsvUtil.parseLine(line);
                if (row.size() >= 6) {
                    activities.add(new StockActivity(
                            row.get(0),
                            row.get(1),
                            StockActivity.Type.valueOf(row.get(2)),
                            Integer.parseInt(row.get(3)),
                            LocalDateTime.parse(row.get(4)),
                            row.get(5)
                    ));
                }
            }
        } catch (IOException | RuntimeException ex) {
            throw new StorageException("Unable to load stock activity from " + activityFile + ": " + ex.getMessage(), ex);
        }
        return activities;
    }

    @Override
    public void saveProducts(List<Product> products) {
        ensureParent(productsFile);
        try (BufferedWriter writer = Files.newBufferedWriter(productsFile)) {
            writer.write("id,name,category,vendor,quantity,unitPrice,dateAdded");
            writer.newLine();
            for (Product product : products) {
                writer.write(String.join(",",
                        CsvUtil.escape(product.getId()),
                        CsvUtil.escape(product.getName()),
                        CsvUtil.escape(product.getCategory()),
                        CsvUtil.escape(product.getVendor()),
                        String.valueOf(product.getQuantity()),
                        String.valueOf(product.getUnitPrice()),
                        String.valueOf(product.getDateAdded())
                ));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new StorageException("Unable to save products to " + productsFile + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public void saveActivities(List<StockActivity> activities) {
        ensureParent(activityFile);
        try (BufferedWriter writer = Files.newBufferedWriter(activityFile)) {
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
            throw new StorageException("Unable to save activity to " + activityFile + ": " + ex.getMessage(), ex);
        }
    }

    private void ensureParent(Path file) {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException ex) {
            throw new StorageException("Unable to create data directory: " + ex.getMessage(), ex);
        }
    }
}
