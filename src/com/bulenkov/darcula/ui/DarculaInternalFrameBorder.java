package com.bulenkov.darcula.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class DarculaInternalFrameBorder implements Border, UIResource {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(x, y);
        g2.setColor(UIManager.getColor("InternalFrame.darcula.borderColor"));
        g2.drawRect(0, 0, w - 1, h - 1);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(UIManager.getColor("InternalFrame.darcula.borderInnerColor"));
        g2.drawRect(2, 2, w - 4, h - 4);
        g2.translate(-x, -y);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new InsetsUIResource(3, 3, 3, 3);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
