/*
 * Copyright 2000-2016 JetBrains s.r.o.
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
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

/**
 * JToggleButton support.
 * Created by Alessandro Falappa on 25/01/16.
 */
public class DarculaToggleButtonUI extends BasicToggleButtonUI {

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new DarculaToggleButtonUI();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        final Border border = c.getBorder();
        final GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        if (border != null) {
            final Insets ins = border.getBorderInsets(c);
            final int yOff = (ins.top + ins.bottom) / 4;
            if (c.isEnabled()) {
                if (model.isSelected()) {
                    ((Graphics2D) g).setPaint(new GradientPaint(0, 0, getSelectedButtonColor1(), 0, c.getHeight(), getSelectedButtonColor2()));
                } else {
                    ((Graphics2D) g).setPaint(new GradientPaint(0, 0, getButtonColor1(), 0, c.getHeight(), getButtonColor2()));
                }
            } else {
                if (model.isSelected())
                    ((Graphics2D) g).setPaint(getSelectedButtonColor2());
            }
            g.fillRoundRect(4, yOff, c.getWidth() - 2 * 4, c.getHeight() - 2 * yOff, 5, 5);
        }
        config.restore();
        super.paint(g, c);
    }

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        FontMetrics metrics = SwingUtilities2.getFontMetrics(c, g);
        int mnemonicIndex = button.getDisplayedMnemonicIndex();
        Color fg = button.getForeground();
        g.setColor(fg);
        if (model.isEnabled()) {
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + metrics.getAscent() + getTextShiftOffset());
        } else {
            g.setColor(UIManager.getColor("ToggleButton.darcula.disabledText.shadow"));
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, -1,
                    textRect.x + getTextShiftOffset() + 1,
                    textRect.y + metrics.getAscent() + getTextShiftOffset() + 1);
            g.setColor(UIManager.getColor("ToggleButton.disabledText"));
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, -1,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + metrics.getAscent() + getTextShiftOffset());
        }
    }


    protected Color getButtonColor1() {
        return UIManager.getColor("ToggleButton.darcula.color1");
    }

    protected Color getButtonColor2() {
        return UIManager.getColor("ToggleButton.darcula.color2");
    }

    private Color getSelectedButtonColor1() {
        return UIManager.getColor("ToggleButton.darcula.selected.color1");
    }

    private Color getSelectedButtonColor2() {
        return UIManager.getColor("ToggleButton.darcula.selected.color2");
    }

}
