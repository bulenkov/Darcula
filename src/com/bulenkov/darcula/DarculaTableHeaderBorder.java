package com.bulenkov.darcula;

import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaTableHeaderBorder implements Border, UIResource {

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(0, 0, 0, 0);
  }

  @Override
  public boolean isBorderOpaque() {
    return false;
  }
}
