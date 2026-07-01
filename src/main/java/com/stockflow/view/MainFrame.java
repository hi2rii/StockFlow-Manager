package com.stockflow.view;

import com.stockflow.config.AppConfig;
import com.stockflow.controller.InventoryController;
import com.stockflow.model.InventoryStats;
import com.stockflow.model.Product;
import com.stockflow.service.SortMode;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.util.List;

public class MainFrame extends JFrame {
    private final InventoryController controller;
    private final ProductTableModel productTableModel = new ProductTableModel();
    private final ActivityTableModel activityTableModel = new ActivityTableModel();
    private final JTable productTable = new JTable(productTableModel);
    private final JTable activityTable = new JTable(activityTableModel);
    private final JTextField searchField = new JTextField(22);
    private final JComboBox<String> categoryFilter = new JComboBox<>();
    private final JComboBox<SortMode> sortCombo = new JComboBox<>(SortMode.values());
    private final StatCard totalProductsCard = new StatCard("Total Products", UIStyles.PRIMARY);
    private final StatCard totalStockCard = new StatCard("Total Stock Quantity", UIStyles.SUCCESS);
    private final StatCard lowStockCard = new StatCard("Low Stock Items", UIStyles.WARNING);
    private final StatCard categoriesCard = new StatCard("Categories", UIStyles.SIDEBAR_ACCENT);
    private final JTextArea alertArea = new JTextArea(6, 28);

    public MainFrame(InventoryController controller) {
        super(AppConfig.APP_NAME);
        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainContent(), BorderLayout.CENTER);
        applyTableStyle(productTable);
        applyTableStyle(activityTable);
    }

    public void updateDashboard(InventoryStats stats, List<Product> lowStockProducts) {
        totalProductsCard.setValue(stats.getTotalProducts());
        totalStockCard.setValue(stats.getTotalStockQuantity());
        lowStockCard.setValue(stats.getLowStockItems());
        categoriesCard.setValue(stats.getCategoryCount());
        StringBuilder alerts = new StringBuilder();
        if (lowStockProducts.isEmpty()) {
            alerts.append("No low stock alerts. Inventory levels look healthy.");
        } else {
            for (Product product : lowStockProducts) {
                alerts.append(product.getId())
                        .append(" - ")
                        .append(product.getName())
                        .append(" has ")
                        .append(product.getQuantity())
                        .append(" units remaining.")
                        .append(System.lineSeparator());
            }
        }
        alertArea.setText(alerts.toString());
    }

    public void updateCategories(List<String> categories) {
        Object selected = categoryFilter.getSelectedItem();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All Categories");
        for (String category : categories) {
            model.addElement(category);
        }
        categoryFilter.setModel(model);
        if (selected != null) {
            categoryFilter.setSelectedItem(selected);
        }
    }

    public void updateProducts(List<Product> products) {
        productTableModel.setProducts(products);
    }

    public void updateActivities(List<com.stockflow.model.StockActivity> activities) {
        activityTableModel.setActivities(activities);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(UIStyles.SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 18, 24, 18));

        JLabel title = new JLabel("<html><b>StockFlow</b><br>Manager</html>");
        title.setForeground(Color.WHITE);
        title.setFont(UIStyles.TITLE_FONT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(24));
        sidebar.add(sidebarLabel("Dashboard"));
        sidebar.add(sidebarLabel("Inventory"));
        sidebar.add(sidebarLabel("Stock Activity"));
        sidebar.add(sidebarLabel("Reports"));
        sidebar.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("<html>Small business<br>inventory control</html>");
        footer.setForeground(new Color(203, 213, 225));
        footer.setFont(UIStyles.BODY_FONT);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(footer);
        return sidebar;
    }

    private JLabel sidebarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(UIStyles.SIDEBAR_ACCENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        label.setFont(UIStyles.BODY_FONT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JPanel buildMainContent() {
        JPanel main = new JPanel(new BorderLayout(16, 16));
        main.setBackground(UIStyles.BACKGROUND);
        main.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JLabel header = new JLabel("Inventory Dashboard");
        header.setFont(UIStyles.TITLE_FONT);
        header.setForeground(UIStyles.TEXT);
        main.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIStyles.BODY_FONT);
        tabs.addTab("Dashboard", buildDashboardPanel());
        tabs.addTab("Inventory", buildInventoryPanel());
        tabs.addTab("Stock Activity", new JScrollPane(activityTable));
        tabs.addTab("Reports", buildReportsPanel());
        main.add(tabs, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UIStyles.BACKGROUND);
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 14));
        cards.setBackground(UIStyles.BACKGROUND);
        cards.add(totalProductsCard);
        cards.add(totalStockCard);
        cards.add(lowStockCard);
        cards.add(categoriesCard);
        panel.add(cards, BorderLayout.NORTH);

        JPanel alerts = new JPanel(new BorderLayout(8, 8));
        alerts.setBackground(Color.WHITE);
        alerts.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel title = new JLabel("Low Stock Alerts");
        title.setFont(UIStyles.SECTION_FONT);
        alertArea.setEditable(false);
        alertArea.setFont(UIStyles.BODY_FONT);
        alertArea.setForeground(UIStyles.TEXT);
        alertArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        alerts.add(title, BorderLayout.NORTH);
        alerts.add(new JScrollPane(alertArea), BorderLayout.CENTER);
        panel.add(alerts, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(UIStyles.BACKGROUND);
        panel.add(buildToolbar(), BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
        toolbar.setBackground(UIStyles.BACKGROUND);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setBackground(UIStyles.BACKGROUND);
        searchField.setFont(UIStyles.BODY_FONT);
        categoryFilter.setFont(UIStyles.BODY_FONT);
        sortCombo.setFont(UIStyles.BODY_FONT);
        JButton search = UIStyles.secondaryButton("Search");
        JButton clear = UIStyles.secondaryButton("Clear");
        search.addActionListener(event -> applyFilters());
        clear.addActionListener(event -> {
            searchField.setText("");
            categoryFilter.setSelectedItem("All Categories");
            applyFilters();
        });
        categoryFilter.addActionListener(event -> applyFilters());
        sortCombo.addActionListener(event -> applyFilters());
        filters.add(new JLabel("Search"));
        filters.add(searchField);
        filters.add(new JLabel("Category"));
        filters.add(categoryFilter);
        filters.add(new JLabel("Sort"));
        filters.add(sortCombo);
        filters.add(search);
        filters.add(clear);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(UIStyles.BACKGROUND);
        JButton add = UIStyles.primaryButton("Add Product");
        JButton edit = UIStyles.secondaryButton("Edit");
        JButton delete = UIStyles.dangerButton("Delete");
        JButton stock = UIStyles.secondaryButton("Adjust Stock");
        add.addActionListener(event -> openAddDialog());
        edit.addActionListener(event -> openEditDialog());
        delete.addActionListener(event -> deleteSelectedProduct());
        stock.addActionListener(event -> openStockDialog());
        actions.add(add);
        actions.add(edit);
        actions.add(stock);
        actions.add(delete);

        toolbar.add(filters, BorderLayout.CENTER);
        toolbar.add(actions, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel buildReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(UIStyles.BACKGROUND);
        JPanel content = new JPanel(new GridLayout(2, 1, 12, 12));
        content.setBackground(UIStyles.BACKGROUND);
        content.add(reportCard("Vendor/Product Report", "Export vendor totals, stock quantities, and inventory value.", true));
        content.add(reportCard("Stock Activity History", "Export add/remove stock activity as a CSV audit trail.", false));
        panel.add(content, BorderLayout.NORTH);
        return panel;
    }

    private JPanel reportCard(String title, String description, boolean vendor) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyles.SECTION_FONT);
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(UIStyles.BODY_FONT);
        JButton export = UIStyles.primaryButton("Export CSV");
        export.addActionListener(event -> exportReport(vendor));
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descriptionLabel, BorderLayout.CENTER);
        card.add(export, BorderLayout.EAST);
        return card;
    }

    private void applyTableStyle(JTable table) {
        table.setRowHeight(30);
        table.setFont(UIStyles.BODY_FONT);
        table.getTableHeader().setFont(UIStyles.BODY_FONT.deriveFont(java.awt.Font.BOLD));
        table.getTableHeader().setBackground(new Color(226, 232, 240));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
    }

    private void applyFilters() {
        controller.applyTableQuery(
                searchField.getText(),
                (String) categoryFilter.getSelectedItem(),
                (SortMode) sortCombo.getSelectedItem()
        );
    }

    private void openAddDialog() {
        ProductDialog dialog = new ProductDialog(this, null);
        dialog.setVisible(true);
        Product product = dialog.getProduct();
        if (product != null) {
            runAction(() -> controller.addProduct(product));
        }
    }

    private void openEditDialog() {
        Product selected = selectedProduct();
        if (selected == null) {
            showSelectProductMessage();
            return;
        }
        ProductDialog dialog = new ProductDialog(this, selected);
        dialog.setVisible(true);
        Product product = dialog.getProduct();
        if (product != null) {
            product.setDateAdded(selected.getDateAdded());
            runAction(() -> controller.updateProduct(selected.getId(), product));
        }
    }

    private void deleteSelectedProduct() {
        Product selected = selectedProduct();
        if (selected == null) {
            showSelectProductMessage();
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete " + selected.getName() + "? This cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            runAction(() -> controller.deleteProduct(selected.getId()));
        }
    }

    private void openStockDialog() {
        Product selected = selectedProduct();
        if (selected == null) {
            showSelectProductMessage();
            return;
        }
        StockAdjustmentDialog dialog = new StockAdjustmentDialog(this, selected);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            runAction(() -> controller.adjustStock(selected.getId(), dialog.getAmount(), dialog.getAdjustmentType(), dialog.getNote()));
        }
    }

    private void exportReport(boolean vendor) {
        JFileChooser chooser = new JFileChooser(AppConfig.REPORTS_DIRECTORY.toFile());
        chooser.setDialogTitle("Export CSV Report");
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            Path path = chooser.getSelectedFile().toPath();
            boolean exported = runAction(() -> {
                if (vendor) {
                    controller.exportVendorReport(path);
                } else {
                    controller.exportActivityReport(path);
                }
            });
            if (exported) {
                JOptionPane.showMessageDialog(this, "Report exported to " + path, "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private Product selectedProduct() {
        int row = productTable.getSelectedRow();
        return row < 0 ? null : productTableModel.getProductAt(row);
    }

    private void showSelectProductMessage() {
        JOptionPane.showMessageDialog(this, "Select a product first.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
    }

    private boolean runAction(Runnable action) {
        try {
            action.run();
            return true;
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Action Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
