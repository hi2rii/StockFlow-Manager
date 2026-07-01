package com.stockflow;

import com.stockflow.config.AppConfig;
import com.stockflow.controller.InventoryController;
import com.stockflow.service.InventoryService;
import com.stockflow.storage.CsvInventoryStorage;
import com.stockflow.view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point for StockFlow Manager.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing can continue with the default look and feel.
            }

            CsvInventoryStorage storage = new CsvInventoryStorage(
                    AppConfig.PRODUCTS_FILE,
                    AppConfig.ACTIVITY_FILE
            );
            InventoryService service = new InventoryService(storage);
            service.load();

            InventoryController controller = new InventoryController(service);
            MainFrame frame = new MainFrame(controller);
            controller.setView(frame);
            frame.setVisible(true);
        });
    }
}
