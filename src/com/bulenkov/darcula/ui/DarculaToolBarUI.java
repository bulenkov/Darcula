package com.bulenkov.darcula.ui;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaToolBarUI extends MetalToolBarUI {
  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new DarculaToolBarUI();
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    g.setColor(UIManager.getColor("ToolBar.background"));
    g.fillRect(0, 0, c.getWidth(), c.getHeight());
  }
}
