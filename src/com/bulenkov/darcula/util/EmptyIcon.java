package com.bulenkov.darcula.util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Bulenkov
 */
public class EmptyIcon implements Icon {
  private static final Map<Integer, Icon> cache = new HashMap<Integer, Icon>();
  private final int width;
  private final int height;

  public static Icon create(int size) {
    Icon icon = cache.get(size);
    if (icon == null && size < 129) {
      cache.put(size, icon = new EmptyIcon(size, size));
    }
    return icon == null ? new EmptyIcon(size, size) : icon;
  }

  public static Icon create(int width, int height) {
    return width == height ? create(width) : new EmptyIcon(width, height);
  }

  public static Icon create(Icon base) {
    return create(base.getIconWidth(), base.getIconHeight());
  }

  /**
   * @deprecated use {@linkplain #create(int)} for caching.
   */
  public EmptyIcon(int size) {
    this(size, size);
  }

  public EmptyIcon(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * @deprecated use {@linkplain #create(javax.swing.Icon)} for caching.
   */
  public EmptyIcon(Icon base) {
    this(base.getIconWidth(), base.getIconHeight());
  }

  public int getIconWidth() {
    return width;
  }

  public int getIconHeight() {
    return height;
  }

  public void paintIcon(Component component, Graphics g, int i, int j) {
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EmptyIcon)) return false;

    final EmptyIcon icon = (EmptyIcon)o;

    if (height != icon.height) return false;
    if (width != icon.width) return false;

    return true;
  }

  public int hashCode() {
    int sum = width + height;
    return sum * (sum + 1)/2 + width;
  }
}
