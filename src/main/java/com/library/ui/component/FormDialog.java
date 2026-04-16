package com.library.ui.component;

import javax.swing.*;
import java.awt.*;

public class FormDialog extends JDialog {
    private boolean confirmed = false;
    private final JPanel contentPanel;
    private final StyledButton btnSave;
    private final StyledButton btnCancel;

    public FormDialog(Frame owner, String title, JPanel fieldsPanel) {
        super(owner, title, true);
        this.contentPanel = fieldsPanel;

        setLayout(new BorderLayout());
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnSave = new StyledButton("Lưu", new Color(0, 122, 255), Color.WHITE);
        btnCancel = new StyledButton("Hủy");

        btnSave.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
