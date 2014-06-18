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

import com.bulenkov.iconloader.util.GraphicsConfig;
import com.bulenkov.iconloader.util.GraphicsUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaInternalFrameUI extends BasicInternalFrameUI {
    public DarculaInternalFrameUI(JInternalFrame b) {
        super(b);
    }


    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new DarculaInternalFrameUI((JInternalFrame) c);
    }

    @Override
    protected JComponent createNorthPane(JInternalFrame w) {
        this.titlePane = new BasicInternalFrameTitlePane(w) {
            @Override
            protected void installDefaults() {
                super.installDefaults();

                closeIcon = new CloseIcon();
                maxIcon = new MaximizeIcon();
                minIcon = new MinimizeIcon();
                iconIcon = new IconifyIcon();

                selectedTitleColor = UIManager.getColor("InternalFrameTitlePane.darcula.selected.backgroundColor");
                selectedTextColor = UIManager.getColor("darcula.textForeground");
                notSelectedTitleColor = UIManager.getColor("InternalFrameTitlePane.darcula.backgroundColor");
                notSelectedTextColor = UIManager.getColor("darcula.textForeground");
            }

            @Override
            protected void createButtons() {
                super.createButtons();

                MouseListener listener = new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Icon icon = ((JButton) e.getComponent()).getIcon();
                        if (icon instanceof FrameIcon) {
                            Color c = ((FrameIcon) icon).getColor();
                            ((FrameIcon) icon).setColor(c.brighter());
                            e.getComponent().repaint();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        Icon icon = ((JButton) e.getComponent()).getIcon();
                        if (icon instanceof FrameIcon) {
                            ((FrameIcon) icon).setColor(UIManager.getColor("InternalFrameTitlePane.darcula.buttonColor"));
                            e.getComponent().repaint();
                        }
                    }
                };

                closeButton.setBorder(null);
                closeButton.setOpaque(false);
                closeButton.addMouseListener(listener);

                maxButton.setBorder(null);
                maxButton.setOpaque(false);
                maxButton.addMouseListener(listener);

                iconButton.setBorder(null);
                iconButton.setOpaque(false);
                iconButton.addMouseListener(listener);
            }

            @Override
            protected void paintBorder(Graphics g) {
                int w = getWidth();
                int h = getHeight();

                Color top = UIManager.getColor("InternalFrameTitlePane.darcula.borderColorTop");
                Color left = UIManager.getColor("InternalFrameTitlePane.darcula.borderColorLeft");
                Color bottom = UIManager.getColor("InternalFrameTitlePane.darcula.borderColorBottom");
                if (frame.isSelected()) {
                    top = UIManager.getColor("InternalFrameTitlePane.darcula.selected.borderColorTop");
                    left = UIManager.getColor("InternalFrameTitlePane.darcula.selected.borderColorLeft");
                    bottom = UIManager.getColor("InternalFrameTitlePane.darcula.selected.borderColorBottom");
                }

                g.setColor(top);
                g.drawLine(2, 0, w, 0);
                g.setColor(left);
                g.drawLine(0, 1, 0, h);
                g.setColor(bottom);
                g.drawLine(2, h, w, h);
            }
        };

        this.titlePane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 0));
        return this.titlePane;
    }

    private static abstract class FrameIcon implements Icon {
        private Color mColor;

        public FrameIcon(Color c) {
            mColor = c;
        }

        public Color getColor() {
            return mColor;
        }

        public void setColor(Color c) {
            mColor = c;
        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }

    private static class CloseIcon extends FrameIcon {
        public CloseIcon() {
            this(UIManager.getColor("InternalFrameTitlePane.darcula.buttonColor"));
        }

        public CloseIcon(Color c) {
            super(c);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            GraphicsConfig config = GraphicsUtil.setupAAPainting(g2);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setPaint(getColor());
            g.drawLine(5, 4, 11, 10);
            g.drawLine(11, 4, 5, 10);
            config.restore();
        }
    }

    private static class MaximizeIcon extends FrameIcon {
        public MaximizeIcon() {
            this(UIManager.getColor("InternalFrameTitlePane.darcula.buttonColor"));
        }

        public MaximizeIcon(Color c) {
            super(c);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            GraphicsConfig config = GraphicsUtil.setupAAPainting(g2);
            g2.setStroke(new BasicStroke(2f));
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setPaint(getColor());
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(3, 3, 10, 9);
            config.restore();
        }
    }

  @Override
  protected void installDefaults() {
    super.installDefaults();

  }

  private static class MinimizeIcon extends FrameIcon {
        public MinimizeIcon() {
            this(UIManager.getColor("InternalFrameTitlePane.darcula.buttonColor"));
        }

        public MinimizeIcon(Color c) {
            super(c);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            GraphicsConfig config = GraphicsUtil.setupAAPainting(g2);
            g2.setStroke(new BasicStroke(2f));
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setPaint(getColor());
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(1, 5, 8, 8);
            g2.drawLine(4, 2, 12, 2);
            g2.drawLine(12, 3, 12, 10);
            config.restore();
        }
    }

    private static class IconifyIcon extends FrameIcon {
        public IconifyIcon() {
            this(UIManager.getColor("InternalFrameTitlePane.darcula.buttonColor"));
        }

        public IconifyIcon(Color c) {
            super(c);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            GraphicsConfig config = GraphicsUtil.setupAAPainting(g2);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setPaint(getColor());
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(4, 12, 12, 12);
            config.restore();
        }
    }
}
