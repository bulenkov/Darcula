package com.bulenkov.darcula.ui;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalMenuBarUI;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaMenuBarUI extends MetalMenuBarUI {
  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new DarculaMenuBarUI();
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    g.setColor(UIManager.getColor("MenuItem.background"));
    g.fillRect(0, 0, c.getWidth(), c.getHeight());
  }
}
