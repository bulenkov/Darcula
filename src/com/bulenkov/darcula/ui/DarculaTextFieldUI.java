/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bulenkov.darcula.ui;

import com.bulenkov.darcula.DarculaUIUtil;
import com.bulenkov.iconloader.util.GraphicsConfig;
import com.bulenkov.iconloader.util.Gray;
import com.bulenkov.iconloader.IconLoader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaTextFieldUI extends BasicTextFieldUI {

  private final FocusListener myFocusListener = new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
      getComponent().repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
      getComponent().repaint();
    }
  };

  private final MouseMotionListener myMouseMotionListener = new MouseMotionAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {
      if (getComponent() != null && isSearchField(getComponent())) {
        if (getActionUnder(e) != null) {
          getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
          getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
      }
    }
  };

  private final MouseListener myMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
      if (isSearchField(getComponent())) {
        final SearchAction action = getActionUnder(e);
        if (action != null) {
          switch (action) {
            case POPUP:
              showSearchPopup();
              break;
            case CLEAR:
              getComponent().setText("");
              break;
          }
          e.consume();
        }
      }
    }
  };

  private enum SearchAction {POPUP, CLEAR}

  protected JLabel myClearIcon;
  protected JLabel myRecentIcon;

  @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
  public static ComponentUI createUI(final JComponent c) {
    return new DarculaTextFieldUI();
  }

  @Override
  protected void installListeners() {
    super.installListeners();
    final JTextComponent c = getComponent();
    c.addFocusListener(myFocusListener);
    c.addMouseMotionListener(myMouseMotionListener);
    c.addMouseListener(myMouseListener);
  }

  @Override
  protected void uninstallListeners() {
    final JTextComponent c = getComponent();
    c.removeMouseListener(myMouseListener);
    c.removeMouseMotionListener(myMouseMotionListener);
    c.removeFocusListener(myFocusListener);
    super.uninstallListeners();
  }

  protected void showSearchPopup() {
    final Object value = getComponent().getClientProperty("JTextField.Search.FindPopup");
    if (value instanceof JPopupMenu) {
      final JPopupMenu popup = (JPopupMenu)value;
      popup.show(getComponent(), getSearchIconCoord().x, getComponent().getHeight());
    }
  }

  private SearchAction getActionUnder(MouseEvent e) {
    final Point cPoint = getClearIconCoord();
    final Point sPoint = getSearchIconCoord();
    cPoint.x+=8;
    cPoint.y+=8;
    sPoint.x+=8;
    sPoint.y+=8;
    final Point ePoint = e.getPoint();
    return cPoint.distance(ePoint) <= 8 ? SearchAction.CLEAR : sPoint.distance(ePoint) <= 8 ? SearchAction.POPUP : null;
  }

  protected Rectangle getDrawingRect() {
    final JTextComponent c = getComponent();
    final Insets i = c.getInsets();
    final int x = i.right - 4 - 16;
    final int y = i.top - 3;
    final int w = c.getWidth() - i.left - i.right + 16*2 +7*2  - 5;
    int h = c.getBounds().height - i.top - i.bottom + 4*2 - 3;
    if (h%2==1) h++;
    return new Rectangle(x, y, w, h);
  }

  protected Point getSearchIconCoord() {
    final Rectangle r = getDrawingRect();
    return new Point(r.x + 3, r.y + (r.height - 16) / 2 + 1);
  }

  protected Point getClearIconCoord() {
    final Rectangle r = getDrawingRect();
    return new Point(r.x + r.width - 16 - 1, r.y + (r.height - 16) / 2);
  }

  @Override
  protected void paintBackground(Graphics graphics) {
    Graphics2D g = (Graphics2D)graphics;
    final JTextComponent c = getComponent();
    final Container parent = c.getParent();
    final Rectangle r = getDrawingRect();
    if (c.isOpaque() && parent != null) {
      g.setColor(parent.getBackground());
      g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }
    final GraphicsConfig config = new GraphicsConfig(g);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

    final Border border = c.getBorder();
    if (isSearchField(c)) {
      g.setColor(c.getBackground());

      int radius = r.height-1;
      g.fillRoundRect(r.x, r.y, r.width, r.height-1, radius, radius);
      g.setColor(c.isEnabled() ? Gray._100 : new Color(0x535353));
      if (c.getClientProperty("JTextField.Search.noBorderRing") != Boolean.TRUE) {
        if (c.hasFocus()) {
            DarculaUIUtil.paintSearchFocusRing(g, r);
        } else {
          g.drawRoundRect(r.x, r.y, r.width, r.height-1, radius, radius);
        }
      }
      Point p = getSearchIconCoord();
      Icon searchIcon = getComponent().getClientProperty("JTextField.Search.FindPopup") instanceof JPopupMenu ? UIManager.getIcon("TextField.darcula.searchWithHistory.icon") : UIManager.getIcon("TextField.darcula.search.icon");
      if (searchIcon == null) {
        searchIcon = IconLoader.findIcon("/com/bulenkov/darcula/icons/search.png", DarculaTextFieldUI.class, true);
      }
      searchIcon.paintIcon(null, g, p.x, p.y);
      if (getComponent().hasFocus() && getComponent().getText().length() > 0) {
        p = getClearIconCoord();
        Icon clearIcon = UIManager.getIcon("TextField.darcula.clear.icon");
        if (clearIcon == null) {
          clearIcon = IconLoader.findIcon("/com/bulenkov/darcula/icons/clear.png", DarculaTextFieldUI.class, true);
        }
        clearIcon.paintIcon(null, g, p.x, p.y);
      }
    } else if (border instanceof DarculaTextBorder) {
      if (c.isEnabled() && c.isEditable()) {
        g.setColor(c.getBackground());
      }
      final int width = c.getWidth();
      final int height = c.getHeight();
      final Insets i = border.getBorderInsets(c);
      if (c.hasFocus()) {
        g.fillRoundRect(i.left - 5, i.top - 2, width - i.right - i.left + 10, height - i.top - i.bottom + 6, 5, 5);
      } else {
        g.fillRect(i.left - 5, i.top - 2, width - i.right - i.left + 12, height - i.top - i.bottom + 6);
      }
    } else {
      super.paintBackground(g);
    }
    config.restore();
  }

  @Override
  protected void paintSafely(Graphics g) {
    paintBackground(g);
    super.paintSafely(g);
  }

  public static boolean isSearchField(Component c) {
    return c instanceof JTextField && "search".equals(((JTextField)c).getClientProperty("JTextField.variant"));
  }

  public static boolean isSearchFieldWithHistoryPopup(Component c) {
    return isSearchField(c) && ((JTextField)c).getClientProperty("JTextField.Search.FindPopup") instanceof JPopupMenu;
  }
}
