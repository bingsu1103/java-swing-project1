package com.library.ui.component;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {
    public StyledButton(String text) {
        super(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
    }

    public StyledButton(String text, Color background, Color foreground) {
        this(text);
        setBackground(background);
        setForeground(foreground);
    }
}
