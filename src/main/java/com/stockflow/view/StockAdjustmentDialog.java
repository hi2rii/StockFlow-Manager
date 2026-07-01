package com.stockflow.view;

import com.stockflow.model.Product;
import com.stockflow.model.StockActivity;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class StockAdjustmentDialog extends JDialog {
    private final JTextField amountField = new JTextField(12);
    private final JTextField noteField = new JTextField(20);
    private final JComboBox<StockActivity.Type> typeCombo = new JComboBox<>(StockActivity.Type.values());
    private boolean confirmed;
    private int amount;
    private StockActivity.Type type;
    private String note;

    public StockAdjustmentDialog(Frame owner, Product product) {
        super(owner, "Adjust Stock - " + product.getName(), true);
        setLayout(new BorderLayout());
        add(buildForm(product), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getAmount() {
        return amount;
    }

    public StockActivity.Type getAdjustmentType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    private JPanel buildForm(Product product) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addLabel(panel, gbc, 0, "Product", product.getId() + " - " + product.getName());
        addLabel(panel, gbc, 1, "Current Quantity", String.valueOf(product.getQuantity()));
        addField(panel, gbc, 2, "Action", typeCombo);
        addField(panel, gbc, 3, "Quantity", amountField);
        addField(panel, gbc, 4, "Note", noteField);
        return panel;
    }

    private JPanel buildActions() {
        JPanel actions = new JPanel();
        actions.setBackground(java.awt.Color.WHITE);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 20, 18, 20));
        JButton cancel = UIStyles.secondaryButton("Cancel");
        JButton save = UIStyles.primaryButton("Apply");
        cancel.addActionListener(event -> dispose());
        save.addActionListener(event -> confirm());
        actions.add(cancel);
        actions.add(save);
        return actions;
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        addField(panel, gbc, row, label, new JLabel(value));
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
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

    private void confirm() {
        try {
            amount = Integer.parseInt(amountField.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException();
            }
            type = (StockActivity.Type) typeCombo.getSelectedItem();
            note = noteField.getText().trim();
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a positive whole number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
