package com.library.ui.component;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private final Consumer<String> onMenuSelect;
    private JPanel menuPanel;

    public Sidebar(Consumer<String> onMenuSelect) {
        this.onMenuSelect = onMenuSelect;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBackground(new Color(45, 52, 54));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 52, 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel("LIB MANAGER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Menu items
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(45, 52, 54));

        addMenuItem("Dashboard", "DASHBOARD");
        addMenuItem("Quản lý Độc giả", "READER");
        addMenuItem("Quản lý Sách", "BOOK");
        addMenuItem("Mượn sách", "BORROW");
        addMenuItem("Trả sách", "RETURN");
        addMenuItem("Thống kê", "STAT");
        
        add(menuPanel, BorderLayout.CENTER);

        // Logout at bottom
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(45, 52, 54));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StyledButton btnLogout = new StyledButton("Đăng xuất", new Color(214, 48, 49), Color.WHITE);
        btnLogout.addActionListener(e -> onMenuSelect.accept("LOGOUT"));
        footerPanel.add(btnLogout, BorderLayout.SOUTH);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addMenuItem(String text, String action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setForeground(new Color(223, 230, 233));
        btn.setBackground(new Color(45, 52, 54));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(99, 110, 114));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 52, 54));
            }
        });

        btn.addActionListener(e -> onMenuSelect.accept(action));
        menuPanel.add(btn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
}
