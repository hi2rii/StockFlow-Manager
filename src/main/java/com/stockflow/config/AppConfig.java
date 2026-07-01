package com.stockflow.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Central application configuration.
 */
public final class AppConfig {
    public static final String APP_NAME = "StockFlow Manager";
    public static final int LOW_STOCK_THRESHOLD = 10;
    public static final Path DATA_DIRECTORY = Paths.get("data");
    public static final Path PRODUCTS_FILE = DATA_DIRECTORY.resolve("products.csv");
    public static final Path ACTIVITY_FILE = DATA_DIRECTORY.resolve("stock_activity.csv");
    public static final Path REPORTS_DIRECTORY = Paths.get("reports");

    private AppConfig() {
    }
}
