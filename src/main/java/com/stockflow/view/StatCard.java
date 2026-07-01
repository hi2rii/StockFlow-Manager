package com.stockflow.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class StatCard extends JPanel {
    private final JLabel valueLabel = new JLabel("0");

    public StatCard(String title, Color accent) {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyles.BODY_FONT);
        titleLabel.setForeground(new Color(100, 116, 139));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(UIStyles.TEXT);
        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(int value) {
        valueLabel.setText(String.valueOf(value));
    }
}
