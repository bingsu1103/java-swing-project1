package com.library.ui;

import com.library.ui.component.Sidebar;
import com.library.ui.panel.BookPanel;
import com.library.ui.panel.BorrowPanel;
import com.library.ui.panel.ReturnPanel;
import com.library.ui.panel.StatPanel;
import com.library.ui.panel.DashboardPanel;
import com.library.ui.panel.ReaderPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Hệ thống quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        Sidebar sidebar = new Sidebar(this::handleMenuSelection);
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(30, 39, 46));

        contentArea.add(new DashboardPanel(), "DASHBOARD");
        contentArea.add(new ReaderPanel(), "READER");
        contentArea.add(new BookPanel(), "BOOK");
        contentArea.add(new BorrowPanel(), "BORROW");
        contentArea.add(new ReturnPanel(), "RETURN");
        contentArea.add(new StatPanel(), "STAT");

        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 39, 46));
        JLabel label = new JLabel(title + " (Sắp ra mắt)", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void handleMenuSelection(String action) {
        if ("LOGOUT".equals(action)) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                this.dispose();
            }
            return;
        }

        cardLayout.show(contentArea, action);
    }
}
