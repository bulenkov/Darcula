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
import com.bulenkov.iconloader.util.SystemInfo;
import com.bulenkov.iconloader.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaTreeUI extends BasicTreeUI {
  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new DarculaTreeUI();
  }

  public static final String TREE_TABLE_TREE_KEY = "TreeTableTree";

  public static final String SOURCE_LIST_CLIENT_PROPERTY = "mac.ui.source.list";
  public static final String STRIPED_CLIENT_PROPERTY = "mac.ui.striped";

  private static final Border LIST_BACKGROUND_PAINTER = UIManager.getBorder("List.sourceListBackgroundPainter");
  private static final Border LIST_SELECTION_BACKGROUND_PAINTER = UIManager.getBorder("List.sourceListSelectionBackgroundPainter");
  private static final Border LIST_FOCUSED_SELECTION_BACKGROUND_PAINTER = UIManager.getBorder("List.sourceListFocusedSelectionBackgroundPainter");

  private boolean myOldRepaintAllRowValue;
  private boolean invertLineColor;
  private boolean myForceDontPaintLines = false;


  @Override
  public int getRightChildIndent() {
    return isSkinny() ? 8 : super.getRightChildIndent();
  }

  private static boolean isSkinny() {
    return true;
  }

  private final MouseListener mySelectionListener = new MouseAdapter() {
    boolean handled = false;
    @Override
    public void mousePressed(final MouseEvent e) {
      handled = false;
      if (!isSelected(e)) {
        handled = true;
        handle(e);
      }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      if (!handled) {
        handle(e);
      }
    }

    private boolean isSelected(MouseEvent e) {
      final JTree tree = (JTree)e.getSource();
      final int selected = tree.getClosestRowForLocation(e.getX(), e.getY());
      final int[] rows = tree.getSelectionRows();
      if (rows != null) {
        for (int row : rows) {
          if (row == selected) {
            return true;
          }
        }
      }

      return false;
    }

    private void handle(MouseEvent e) {
      final JTree tree = (JTree)e.getSource();
      if (SwingUtilities.isLeftMouseButton(e) && !e.isPopupTrigger()) {
        // if we can't stop any ongoing editing, do nothing
        if (isEditing(tree) && tree.getInvokesStopCellEditing() && !stopEditing(tree)) {
          return;
        }

        final TreePath pressedPath = getClosestPathForLocation(tree, e.getX(), e.getY());
        if (pressedPath != null) {
          Rectangle bounds = getPathBounds(tree, pressedPath);

          if (e.getY() >= bounds.y + bounds.height) {
            return;
          }

          if (bounds.contains(e.getPoint()) || isLocationInExpandControl(pressedPath, e.getX(), e.getY())) {
            return;
          }

          if (tree.getDragEnabled() || !startEditing(pressedPath, e)) {
            selectPathForEvent(pressedPath, e);
          }
        }
      }
    }
  };

  @Override
  protected void completeUIInstall() {
    super.completeUIInstall();

    myOldRepaintAllRowValue = UIManager.getBoolean("Tree.repaintWholeRow");
    UIManager.put("Tree.repaintWholeRow", true);

    tree.setShowsRootHandles(true);
    tree.addMouseListener(mySelectionListener);
  }

  @Override
  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);

    UIManager.put("Tree.repaintWholeRow", myOldRepaintAllRowValue);
    c.removeMouseListener(mySelectionListener);
  }

  @Override
  protected void installKeyboardActions() {
    super.installKeyboardActions();

    if (Boolean.TRUE.equals(tree.getClientProperty("MacTreeUi.actionsInstalled"))) return;

    tree.putClientProperty("MacTreeUi.actionsInstalled", Boolean.TRUE);

    final InputMap inputMap = tree.getInputMap(JComponent.WHEN_FOCUSED);
    inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "collapse_or_move_up");
    inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "expand");

    final ActionMap actionMap = tree.getActionMap();

    final Action expandAction = actionMap.get("expand");
    if (expandAction != null) {
      actionMap.put("expand", new TreeUIAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          final Object source = e.getSource();
          if (source instanceof JTree) {
            JTree tree = (JTree)source;
            int selectionRow = tree.getLeadSelectionRow();
            if (selectionRow != -1) {
              TreePath selectionPath = tree.getPathForRow(selectionRow);
              if (selectionPath != null) {
                boolean leaf = tree.getModel().isLeaf(selectionPath.getLastPathComponent());
                int toSelect = -1;
                int toScroll = -1;
                if (!leaf && tree.isExpanded(selectionRow)) {
                  if (selectionRow + 1 < tree.getRowCount()) {
                    toSelect = selectionRow + 1;
                    toScroll = toSelect;
                  }
                } else if (leaf) {
                  toScroll = selectionRow;
                }

                if (toSelect != -1) {
                  tree.setSelectionInterval(toSelect, toSelect);
                }

                if (toScroll != -1) {
                  tree.scrollRowToVisible(toScroll);
                }

                if (toSelect != -1 || toScroll != -1) return;
              }
            }
          }


          expandAction.actionPerformed(e);
        }
      });
    }

    actionMap.put("collapse_or_move_up", new TreeUIAction() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source instanceof JTree) {
          JTree tree = (JTree)source;
          int selectionRow = tree.getLeadSelectionRow();
          if (selectionRow == -1) return;

          TreePath selectionPath = tree.getPathForRow(selectionRow);
          if (selectionPath == null) return;

          if (tree.getModel().isLeaf(selectionPath.getLastPathComponent()) || tree.isCollapsed(selectionRow)) {
            final TreePath parentPath = tree.getPathForRow(selectionRow).getParentPath();
            if (parentPath != null) {
              if (parentPath.getParentPath() != null || tree.isRootVisible()) {
                final int parentRow = tree.getRowForPath(parentPath);
                tree.scrollRowToVisible(parentRow);
                tree.setSelectionInterval(parentRow, parentRow);
              }
            }
          }
          else {
            tree.collapseRow(selectionRow);
          }
        }
      }
    });
  }

  public void setForceDontPaintLines() {
    myForceDontPaintLines = true;
  }

  private abstract static class TreeUIAction extends AbstractAction implements UIResource {
  }

  @Override
  protected int getRowX(int row, int depth) {
    return isSkinny() ? 8 * depth + 8 : super.getRowX(row, depth);
  }

  @Override
  protected void paintHorizontalPartOfLeg(final Graphics g,
                                          final Rectangle clipBounds,
                                          final Insets insets,
                                          final Rectangle bounds,
                                          final TreePath path,
                                          final int row,
                                          final boolean isExpanded,
                                          final boolean hasBeenExpanded,
                                          final boolean isLeaf) {
  }

  private boolean shouldPaintLines() {
    return myForceDontPaintLines || !"None".equals(tree.getClientProperty("JTree.lineStyle"));
  }

  @Override
  protected boolean isToggleSelectionEvent(MouseEvent e) {
    return SwingUtilities.isLeftMouseButton(e) && (SystemInfo.isMac ? e.isMetaDown() : e.isControlDown()) && !e.isPopupTrigger();
  }

  @Override
  protected void paintVerticalPartOfLeg(final Graphics g, final Rectangle clipBounds, final Insets insets, final TreePath path) {
  }

  @Override
  protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {

  }

  @Override
  protected Color getHashColor() {
    if (invertLineColor && !equalsNullable(UIUtil.getTreeSelectionForeground(), UIUtil.getTreeForeground())) {
      final Color c = UIUtil.getTreeSelectionForeground();
      if (c != null) {
        return c.darker();
      }
    }
    return super.getHashColor();
  }

  private static <T> boolean equalsNullable(T a, T b) {
    if (a == null) {
      return b == null;
    } else {
      return b != null && a.equals(b);
    }
  }


  @Override
  protected void paintRow(final Graphics g,
                          final Rectangle clipBounds,
                          final Insets insets,
                          final Rectangle bounds,
                          final TreePath path,
                          final int row,
                          final boolean isExpanded,
                          final boolean hasBeenExpanded,
                          final boolean isLeaf) {
    final int containerWidth = tree.getParent() instanceof JViewport ? tree.getParent().getWidth() : tree.getWidth();
    final int xOffset = tree.getParent() instanceof JViewport ? ((JViewport)tree.getParent()).getViewPosition().x : 0;

    if (path != null) {
      boolean selected = tree.isPathSelected(path);
      Graphics2D rowGraphics = (Graphics2D)g.create();
      rowGraphics.setClip(clipBounds);

      final Object sourceList = tree.getClientProperty(SOURCE_LIST_CLIENT_PROPERTY);
      Color background = tree.getBackground();

      if ((row % 2) == 0 && Boolean.TRUE.equals(tree.getClientProperty(STRIPED_CLIENT_PROPERTY))) {
        background = UIUtil.getDecoratedRowColor();
      }

      if (sourceList != null && (Boolean)sourceList) {
        if (selected) {
          if (tree.hasFocus()) {
            LIST_FOCUSED_SELECTION_BACKGROUND_PAINTER.paintBorder(tree, rowGraphics, xOffset, bounds.y, containerWidth, bounds.height);
          }
          else {
            LIST_SELECTION_BACKGROUND_PAINTER.paintBorder(tree, rowGraphics, xOffset, bounds.y, containerWidth, bounds.height);
          }
        }
        else {
          rowGraphics.setColor(background);
          rowGraphics.fillRect(xOffset, bounds.y, containerWidth, bounds.height);
        }
      }
      else {
        if (selected) {
          Color bg = UIUtil.getTreeSelectionBackground(tree.hasFocus() || Boolean.TRUE.equals(tree.getClientProperty(TREE_TABLE_TREE_KEY)));

          rowGraphics.setColor(bg);
          rowGraphics.fillRect(xOffset, bounds.y, containerWidth, bounds.height);
        }
      }

      if (shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
        paintExpandControl(rowGraphics, bounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
      }

      super.paintRow(rowGraphics, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
      rowGraphics.dispose();
    }
    else {
      super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
    }
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    final int containerWidth = tree.getParent() instanceof JViewport ? tree.getParent().getWidth() : tree.getWidth();
    final int xOffset = tree.getParent() instanceof JViewport ? ((JViewport) tree.getParent()).getViewPosition().x : 0;
    final Rectangle bounds = g.getClipBounds();

    // draw background for the given clip bounds
    final Object sourceList = tree.getClientProperty(SOURCE_LIST_CLIENT_PROPERTY);
    if (sourceList != null && (Boolean) sourceList) {
      Graphics2D backgroundGraphics = (Graphics2D) g.create();
      backgroundGraphics.setClip(xOffset, bounds.y, containerWidth, bounds.height);
      LIST_BACKGROUND_PAINTER.paintBorder(tree, backgroundGraphics, xOffset, bounds.y, containerWidth, bounds.height);
      backgroundGraphics.dispose();
    }

    super.paint(g, c);
  }

  protected void paintSelectedRows(Graphics g, JTree tr) {
    final Rectangle rect = tr.getVisibleRect();
    final int firstVisibleRow = tr.getClosestRowForLocation(rect.x, rect.y);
    final int lastVisibleRow = tr.getClosestRowForLocation(rect.x, rect.y + rect.height);

    for (int row = firstVisibleRow; row <= lastVisibleRow; row++) {
      if (tr.getSelectionModel().isRowSelected(row)) {
        final Rectangle bounds = tr.getRowBounds(row);
        Color color = UIUtil.getTreeSelectionBackground(tr.hasFocus());
        if (color != null) {
          g.setColor(color);
          g.fillRect(0, bounds.y, tr.getWidth(), bounds.height);
        }
      }
    }
  }

  @Override
  protected CellRendererPane createCellRendererPane() {
    return new CellRendererPane() {
      @Override
      public void paintComponent(Graphics g, Component c, Container p, int x, int y, int w, int h, boolean shouldValidate) {
        if (c instanceof JComponent) {
          ((JComponent)c).setOpaque(false);
        }

        super.paintComponent(g, c, p, x, y, w, h, shouldValidate);
      }
    };
  }

  @Override
  protected void paintExpandControl(Graphics g,
                                    Rectangle clipBounds,
                                    Insets insets,
                                    Rectangle bounds,
                                    TreePath path,
                                    int row,
                                    boolean isExpanded,
                                    boolean hasBeenExpanded,
                                    boolean isLeaf) {
    boolean isPathSelected = tree.getSelectionModel().isPathSelected(path);
    if (!isLeaf(row)) {
      setExpandedIcon(DarculaUIUtil.getTreeNodeIcon(true, isPathSelected, tree.hasFocus()));
      setCollapsedIcon(DarculaUIUtil.getTreeNodeIcon(false, isPathSelected, tree.hasFocus()));
    }

    super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
  }

}
