package com.library.ui.panel;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Thư viện", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }
}
