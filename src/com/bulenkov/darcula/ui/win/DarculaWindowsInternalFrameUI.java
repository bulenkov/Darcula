package com.bulenkov.darcula.ui.win;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaWindowsInternalFrameUI extends BasicInternalFrameUI {
  public DarculaWindowsInternalFrameUI(JInternalFrame b) {
    super(b);
  }


  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new DarculaWindowsInternalFrameUI(((JInternalFrame) c));
  }


}
