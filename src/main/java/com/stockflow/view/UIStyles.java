package com.stockflow.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public final class UIStyles {
    public static final Color BACKGROUND = new Color(245, 247, 251);
    public static final Color SIDEBAR = new Color(27, 38, 59);
    public static final Color SIDEBAR_ACCENT = new Color(65, 90, 119);
    public static final Color PRIMARY = new Color(31, 111, 235);
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color WARNING = new Color(217, 119, 6);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color TEXT = new Color(30, 41, 59);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private UIStyles() {
    }

    public static JButton primaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(226, 232, 240));
        button.setForeground(TEXT);
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(DANGER);
        button.setForeground(Color.WHITE);
        return button;
    }

    public static void pad(JComponent component, int top, int left, int bottom, int right) {
        component.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    private static JButton baseButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
