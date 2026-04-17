package com.library.ui.panel;

import com.library.service.StatService;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StatPanel extends JPanel {
    private final StatService statService;
    private JPanel cardsPanel;

    private final Color BACKGROUND_COLOR = new Color(30, 39, 46);
    private final Color CARD_BG = new Color(47, 53, 66);
    private final Color TEXT_COLOR = Color.WHITE;

    public StatPanel() {
        this.statService = new StatService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JLabel titleLabel = new JLabel("BÁO CÁO THỐNG KÊ THƯ VIỆN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel: Grid of Cards
        cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(BACKGROUND_COLOR);

        refreshStats();

        add(cardsPanel, BorderLayout.CENTER);

        // Footer: Refresh button
        JButton btnRefresh = new JButton("Cập nhật số liệu");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> refreshStats());
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.add(btnRefresh);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void refreshStats() {
        cardsPanel.removeAll();
        Map<String, Object> stats = statService.getOverviewStats();

        cardsPanel.add(createStatCard("TỔNG ĐỘC GIẢ", stats.get("totalReaders").toString(), new Color(52, 152, 219)));
        cardsPanel.add(createStatCard("TỔNG ĐẦU SÁCH", stats.get("totalBookTypes").toString(), new Color(46, 204, 113)));
        cardsPanel.add(createStatCard("ĐANG MƯỢN", stats.get("activeLoans").toString(), new Color(241, 196, 15)));
        cardsPanel.add(createStatCard("QUÁ HẠN", stats.get("overdueCount").toString(), new Color(231, 76, 60)));

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(accentColor);
                g2.fillRect(0, 0, 10, getHeight()); // Left accent bar
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(189, 195, 199));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(TEXT_COLOR);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
