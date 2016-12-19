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

import com.bulenkov.iconloader.util.ColorUtil;
import com.bulenkov.iconloader.util.GraphicsConfig;
import com.bulenkov.iconloader.util.GraphicsUtil;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaSliderUI extends BasicSliderUI {
  public DarculaSliderUI(JSlider b) {
    super(b);
  }


  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new DarculaSliderUI((JSlider) c);
  }

  @Override
  public void paintFocus(Graphics g) {
    super.paintFocus(g);
  }

  @Override
  public void paintTrack(Graphics g2d) {
    Graphics2D g = (Graphics2D) g2d;
    Rectangle trackBounds = trackRect;
    final int arc = JBUI.scale(6);
    int trackSize = JBUI.scale(6);
    final GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
    final Color bg = getTrackBackground();
    final Color selection = slider.isEnabled() ? getSelectedTrackColor() : getDisabledTickColor();
    if (slider.getOrientation() == JSlider.HORIZONTAL) {
      int cy = (trackBounds.height / 2) - trackSize / 2;
      int cw = trackBounds.width;
      final Area shape = new Area(new RoundRectangle2D.Double(0, 0, cw, trackSize, arc, arc));
      g.translate(trackBounds.x, trackBounds.y + cy);
      g.setColor(bg);
      g.fill(shape);
      int x = thumbRect.x;
      shape.intersect(new Area(new Rectangle2D.Double(0, 0, x, trackSize)));
      g.setColor(selection);
      g.fill(shape);
      g.translate(-trackBounds.x, -(trackBounds.y + cy));
    } else {
      int cx = (trackBounds.width / 2) - trackSize / 2;
      int ch = trackBounds.height;
      g.translate(trackBounds.x + cx, trackBounds.y);
      g.setColor(bg);
      g.fillRoundRect(0, 0, trackSize, ch, arc, arc);
      g.translate(-(trackBounds.x + cx), -trackBounds.y);
    }
    config.restore();
  }

  @Override
  protected Dimension getThumbSize() {
    if (isPlainThumb()) {
      return new Dimension(JBUI.scale(20), JBUI.scale(20));
    }
    return super.getThumbSize();
  }

  @NotNull
  protected Color getTrackBackground() {
    return ColorUtil.fromHex("2b2b2b");
  }

  @NotNull
  protected Color getSelectedTrackColor() {
    return ColorUtil.fromHex("888888");
  }

  @NotNull
  protected Color getDisabledTickColor() {
    return ColorUtil.fromHex("555555");
  }

  @Override
  public void paintTicks(Graphics g) {
    super.paintTicks(g);
  }

  @Override
  protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
    checkDisabled(g);
    super.paintMinorTickForHorizSlider(g, tickBounds, x);
  }

  private void checkDisabled(Graphics g) {
    if (!slider.isEnabled()) {
      g.setColor(getDisabledTickColor());
    }
  }

  @Override
  protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
    checkDisabled(g);
    super.paintMajorTickForHorizSlider(g, tickBounds, x);
  }

  @Override
  protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
    checkDisabled(g);
    super.paintMinorTickForVertSlider(g, tickBounds, y);
  }

  @Override
  protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
    checkDisabled(g);
    super.paintMajorTickForVertSlider(g, tickBounds, y);
  }

  @Override
  public void paintLabels(Graphics g) {
    checkDisabled(g);
    super.paintLabels(g);
  }

  @Override
  protected void paintHorizontalLabel(Graphics g, int value, Component label) {
    checkDisabled(g);
    super.paintHorizontalLabel(g, value, label);
  }

  @Override
  protected void paintVerticalLabel(Graphics g, int value, Component label) {
    checkDisabled(g);
    super.paintVerticalLabel(g, value, label);
  }

  @Override
  public void paintThumb(Graphics g) {
    final GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
    Rectangle knobBounds = thumbRect;
    int w = knobBounds.width;
    int h = knobBounds.height;

    g.translate(knobBounds.x, knobBounds.y);

    if ( slider.isEnabled() ) {
        g.setColor(slider.getBackground());
    }
    else {
        g.setColor(slider.getBackground().darker());
    }


    if (isPlainThumb()) {
      double r = slider.getOrientation() == JSlider.HORIZONTAL ? h : w;
      final Ellipse2D.Double thumb = new Ellipse2D.Double(0, 0, r, r);
      final Ellipse2D.Double innerThumb = new Ellipse2D.Double(1, 1, r-2, r-2);
      g.setColor(ColorUtil.fromHex("555555"));
      ((Graphics2D) g).fill(thumb);
      g.setColor(slider.isEnabled() ? getSelectedTrackColor() : getDisabledTickColor());
      ((Graphics2D) g).fill(innerThumb);
    }
    else if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
        int cw = w / 2;
        g.fillRect(1, 1, w-3, h-1-cw);
        Polygon p = new Polygon();
        p.addPoint(1, h-cw);
        p.addPoint(cw-1, h-1);
        p.addPoint(w-2, h-1-cw);
        g.fillPolygon(p);

        g.setColor(getHighlightColor());
        g.drawLine(0, 0, w-2, 0);
        g.drawLine(0, 1, 0, h-1-cw);
        g.drawLine(0, h-cw, cw-1, h-1);

        g.setColor(Color.black);
        g.drawLine(w-1, 0, w-1, h-2-cw);
        g.drawLine(w-1, h-1-cw, w-1-cw, h-1);

        g.setColor(getShadowColor());
        g.drawLine(w-2, 1, w-2, h-2-cw);
        g.drawLine(w-2, h-1-cw, w-1-cw, h-2);
    }
    else {  // vertical
        int cw = h / 2;
        if(slider.getComponentOrientation().isLeftToRight()) {
              g.fillRect(1, 1, w-1-cw, h-3);
              Polygon p = new Polygon();
              p.addPoint(w-cw-1, 0);
              p.addPoint(w-1, cw);
              p.addPoint(w-1-cw, h-2);
              g.fillPolygon(p);

              g.setColor(getHighlightColor());
              g.drawLine(0, 0, 0, h - 2);                  // left
              g.drawLine(1, 0, w-1-cw, 0);                 // top
              g.drawLine(w-cw-1, 0, w-1, cw);              // top slant

              g.setColor(Color.black);
              g.drawLine(0, h-1, w-2-cw, h-1);             // bottom
              g.drawLine(w-1-cw, h-1, w-1, h-1-cw);        // bottom slant

              g.setColor(getShadowColor());
              g.drawLine(1, h-2, w-2-cw,  h-2 );         // bottom
              g.drawLine(w-1-cw, h-2, w-2, h-cw-1 );     // bottom slant
        }
        else {
              g.fillRect(5, 1, w-1-cw, h-3);
              Polygon p = new Polygon();
              p.addPoint(cw, 0);
              p.addPoint(0, cw);
              p.addPoint(cw, h-2);
              g.fillPolygon(p);

              g.setColor(getHighlightColor());
              g.drawLine(cw-1, 0, w-2, 0);             // top
              g.drawLine(0, cw, cw, 0);                // top slant

              g.setColor(Color.black);
              g.drawLine(0, h-1-cw, cw, h-1 );         // bottom slant
              g.drawLine(cw, h-1, w-1, h-1);           // bottom

              g.setColor(getShadowColor());
              g.drawLine(cw, h-2, w-2,  h-2 );         // bottom
              g.drawLine(w-1, 1, w-1,  h-2 );          // right
        }
    }

    g.translate(-knobBounds.x, -knobBounds.y);
    config.restore();
  }

  protected boolean isPlainThumb() {
    Boolean paintThumbArrowShape = (Boolean)slider.getClientProperty("Slider.paintThumbArrowShape");
    return (!slider.getPaintTicks() && paintThumbArrowShape == null) ||
        paintThumbArrowShape == Boolean.FALSE;
  }
}
