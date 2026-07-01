package com.stockflow.view;

import com.stockflow.model.Product;
import com.stockflow.utils.ProductValidator;
import com.stockflow.utils.ValidationException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

public class ProductDialog extends JDialog {
    private final JTextField idField = new JTextField(18);
    private final JTextField nameField = new JTextField(18);
    private final JTextField categoryField = new JTextField(18);
    private final JTextField vendorField = new JTextField(18);
    private final JTextField quantityField = new JTextField(18);
    private final JTextField priceField = new JTextField(18);
    private final LocalDate dateAdded;
    private Product product;

    public ProductDialog(Frame owner, Product existing) {
        super(owner, existing == null ? "Add Product" : "Edit Product", true);
        this.dateAdded = existing == null ? LocalDate.now() : existing.getDateAdded();
        setLayout(new BorderLayout());
        add(buildForm(), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);
        if (existing != null) {
            fill(existing);
        }
        pack();
        setLocationRelativeTo(owner);
    }

    public Product getProduct() {
        return product;
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorPalette.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addRow(panel, gbc, 0, "Product ID", idField);
        addRow(panel, gbc, 1, "Name", nameField);
        addRow(panel, gbc, 2, "Category", categoryField);
        addRow(panel, gbc, 3, "Vendor", vendorField);
        addRow(panel, gbc, 4, "Quantity", quantityField);
        addRow(panel, gbc, 5, "Unit Price", priceField);
        return panel;
    }

    private JPanel buildActions() {
        JPanel actions = new JPanel();
        actions.setBackground(ColorPalette.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 20, 18, 20));
        JButton cancel = UIStyles.secondaryButton("Cancel");
        JButton save = UIStyles.primaryButton("Save Product");
        cancel.addActionListener(event -> dispose());
        save.addActionListener(event -> saveProduct());
        actions.add(cancel);
        actions.add(save);
        return actions;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(UIStyles.BODY_FONT);
        field.setFont(UIStyles.BODY_FONT);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(jLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void fill(Product existing) {
        idField.setText(existing.getId());
        nameField.setText(existing.getName());
        categoryField.setText(existing.getCategory());
        vendorField.setText(existing.getVendor());
        quantityField.setText(String.valueOf(existing.getQuantity()));
        priceField.setText(String.valueOf(existing.getUnitPrice()));
    }

    private void saveProduct() {
        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            product = new Product(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    categoryField.getText().trim(),
                    vendorField.getText().trim(),
                    quantity,
                    price,
                    dateAdded
            );
            ProductValidator.validate(product);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity and price must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ColorPalette {
        private static final java.awt.Color WHITE = java.awt.Color.WHITE;
    }
}
