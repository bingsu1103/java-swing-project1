package com.library.ui;

import com.library.ui.component.Sidebar;
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

        // Sidebar
        Sidebar sidebar = new Sidebar(this::handleMenuSelection);
        add(sidebar, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Color.WHITE);

        contentArea.add(new DashboardPanel(), "DASHBOARD");
        contentArea.add(new ReaderPanel(), "READER");

        add(contentArea, BorderLayout.CENTER);
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
