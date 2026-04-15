package com.library;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Không thể thiết lập FlatLaf Look and Feel");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new com.library.ui.LoginFrame().setVisible(true);
        });
    }
}
