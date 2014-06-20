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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import com.bulenkov.iconloader.IconLoader;
import com.bulenkov.iconloader.util.DoubleColor;
import com.bulenkov.iconloader.util.Gray;
import com.bulenkov.iconloader.util.UIUtil;

public class DarculaSplitPaneDivider extends BasicSplitPaneDivider {
  private Icon splitGlueV = IconLoader.findIcon("/com/bulenkov/darcula/icons/splitGlueV.png", DarculaSplitPaneDivider.class, true);
  private Icon splitGlueH = IconLoader.findIcon("/com/bulenkov/darcula/icons/splitGlueH.png", DarculaSplitPaneDivider.class, true);

 /**
  * Creates an instance of DarculaSplitPaneDivider. Registers this
  * instance for mouse events and mouse dragged events.
  */
  public DarculaSplitPaneDivider(DarculaSplitPaneUI ui) {
    super(ui);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

      if(splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT)
          splitGlueV.paintIcon(this, g, getWidth() / 2, (getHeight() - splitGlueV.getIconHeight()) / 2);
      else
          splitGlueH.paintIcon(this, g, (getWidth() - splitGlueH.getIconWidth()) / 2, getHeight() / 2);
  }

  @Override
  protected JButton createLeftOneTouchButton() {
    JButton b = new JButton() {
      public void setBorder(Border b) {
      }

      public void paint(Graphics g) {
        if (splitPane != null) {
          int[]   xs = new int[3];
                    int[]   ys = new int[3];
                    int     blockSize;

                    // Fill the background first ...
                    g.setColor(this.getBackground());
                    g.fillRect(0, 0, this.getWidth(),
                            this.getHeight());

                    // ... then draw the arrow.
                    g.setColor(new DoubleColor(Gray._255, UIUtil.getLabelForeground()));
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        blockSize = Math.min(getHeight(), ONE_TOUCH_SIZE);
                        xs[0] = blockSize;
                        xs[1] = 0;
                        xs[2] = blockSize << 1;
                        ys[0] = 0;
                        ys[1] = ys[2] = blockSize;
                        g.drawPolygon(xs, ys, 3); // Little trick to make the
                        // arrows of equal size
                    }
                    else {
                        blockSize = Math.min(getWidth(), ONE_TOUCH_SIZE);
                        xs[0] = xs[2] = blockSize;
                        xs[1] = 0;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.fillPolygon(xs, ys, 3);
                }
            }
            // Don't want the button to participate in focus traversable.
            public boolean isFocusTraversable() {
                return false;
            }
        };
        b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        return b;
    }

    @Override
    protected JButton createRightOneTouchButton() {
        JButton b = new JButton() {
            public void setBorder(Border border) {
            }
            public void paint(Graphics g) {
                if (splitPane != null) {
                    int[]          xs = new int[3];
                    int[]          ys = new int[3];
                    int            blockSize;

                    // Fill the background first ...
                    g.setColor(this.getBackground());
                    g.fillRect(0, 0, this.getWidth(),
                            this.getHeight());

                    // ... then draw the arrow.
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        blockSize = Math.min(getHeight(), ONE_TOUCH_SIZE);
                        xs[0] = blockSize;
                        xs[1] = blockSize << 1;
                        xs[2] = 0;
                        ys[0] = blockSize;
                        ys[1] = ys[2] = 0;
                    }
                    else {
                        blockSize = Math.min(getWidth(), ONE_TOUCH_SIZE);
                        xs[0] = xs[2] = 0;
                        xs[1] = blockSize;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.setColor(new DoubleColor(Gray._255, UIUtil.getLabelForeground()));
                    g.fillPolygon(xs, ys, 3);
                }
            }
            // Don't want the button to participate in focus traversable.
            public boolean isFocusTraversable() {
                return false;
            }
        };
        b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);
        return b;
    }
}
