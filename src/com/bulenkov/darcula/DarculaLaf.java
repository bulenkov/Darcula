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

package com.bulenkov.darcula;

import com.bulenkov.iconloader.IconLoader;
import com.bulenkov.iconloader.util.ColorUtil;
import com.bulenkov.iconloader.util.EmptyIcon;
import com.bulenkov.iconloader.util.StringUtil;
import com.bulenkov.iconloader.util.SystemInfo;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author Konstantin Bulenkov
 */
public final class DarculaLaf extends BasicLookAndFeel {
  public static final String NAME = "Darcula";
  BasicLookAndFeel base;
  public DarculaLaf() {
    try {
      if (SystemInfo.isWindows || SystemInfo.isLinux) {
        base = new MetalLookAndFeel();
        MetalLookAndFeel.setCurrentTheme(new DarculaMetalTheme());
      } else {
        final String name = UIManager.getSystemLookAndFeelClassName();
        base = (BasicLookAndFeel)Class.forName(name).newInstance();
      }
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }

  private void callInit(String method, UIDefaults defaults) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod(method, UIDefaults.class);
      superMethod.setAccessible(true);
      superMethod.invoke(base, defaults);
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }

  @SuppressWarnings("UnusedParameters")
  private static void log(Exception e) {
    //everything is gonna be alright
    //e.printStackTrace();
  }

  @Override
  public UIDefaults getDefaults() {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod("getDefaults");
      superMethod.setAccessible(true);
      final UIDefaults metalDefaults =
          (UIDefaults)superMethod.invoke(new MetalLookAndFeel());
      final UIDefaults defaults = (UIDefaults)superMethod.invoke(base);
      initInputMapDefaults(defaults);
      initIdeaDefaults(defaults);
      patchStyledEditorKit();
      patchComboBox(metalDefaults, defaults);
      defaults.remove("Spinner.arrowButtonBorder");
      defaults.put("Spinner.arrowButtonSize", new Dimension(16, 5));
      defaults.put("Tree.collapsedIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/treeNodeCollapsed.png")));
      defaults.put("Tree.expandedIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/treeNodeExpanded.png")));
      defaults.put("CheckBoxMenuItem.checkIcon", EmptyIcon.create(16));
      defaults.put("RadioButtonMenuItem.checkIcon", EmptyIcon.create(16));
      defaults.put("InternalFrame.icon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/internalFrame.png")));
      defaults.put("OptionPane.informationIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/option_pane_info.png")));
      defaults.put("OptionPane.questionIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/option_pane_question.png")));
      defaults.put("OptionPane.warningIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/option_pane_warning.png")));
      defaults.put("OptionPane.errorIcon", new IconUIResource(IconLoader.getIcon("/com/bulenkov/darcula/icons/option_pane_error.png")));
      return defaults;
    }
    catch (Exception ignore) {
      log(ignore);
    }
    return super.getDefaults();
  }

  private static void patchComboBox(UIDefaults metalDefaults, UIDefaults defaults) {
    defaults.remove("ComboBox.ancestorInputMap");
    defaults.remove("ComboBox.actionMap");
    defaults.put("ComboBox.ancestorInputMap", metalDefaults.get("ComboBox.ancestorInputMap"));
    defaults.put("ComboBox.actionMap", metalDefaults.get("ComboBox.actionMap"));
  }

  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  private static void patchStyledEditorKit() {
    try {
      StyleSheet defaultStyles = new StyleSheet();
      InputStream is = DarculaLaf.class.getResourceAsStream("darcula.css");
      Reader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      defaultStyles.loadRules(r, null);
      r.close();
      final Field keyField = HTMLEditorKit.class.getDeclaredField("DEFAULT_STYLES_KEY");
      keyField.setAccessible(true);
      final Object key = keyField.get(null);
      AppContext.getAppContext().put(key, defaultStyles);
    } catch (Exception e) {
      log(e);
    }
  }

  private void call(String method) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod(method);
      superMethod.setAccessible(true);
      superMethod.invoke(base);
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }

  public void initComponentDefaults(UIDefaults defaults) {
    callInit("initComponentDefaults", defaults);
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  static void initIdeaDefaults(UIDefaults defaults) {
    loadDefaults(defaults);
    defaults.put("Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] {
        "ctrl C", "copy",
        "ctrl V", "paste",
        "ctrl X", "cut",
        "COPY", "copy",
        "PASTE", "paste",
        "CUT", "cut",
        "control INSERT", "copy",
        "shift INSERT", "paste",
        "shift DELETE", "cut",
        "RIGHT", "selectNextColumn",
        "KP_RIGHT", "selectNextColumn",
        "LEFT", "selectPreviousColumn",
        "KP_LEFT", "selectPreviousColumn",
        "DOWN", "selectNextRow",
        "KP_DOWN", "selectNextRow",
        "UP", "selectPreviousRow",
        "KP_UP", "selectPreviousRow",
        "shift RIGHT", "selectNextColumnExtendSelection",
        "shift KP_RIGHT", "selectNextColumnExtendSelection",
        "shift LEFT", "selectPreviousColumnExtendSelection",
        "shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "shift DOWN", "selectNextRowExtendSelection",
        "shift KP_DOWN", "selectNextRowExtendSelection",
        "shift UP", "selectPreviousRowExtendSelection",
        "shift KP_UP", "selectPreviousRowExtendSelection",
        "PAGE_UP", "scrollUpChangeSelection",
        "PAGE_DOWN", "scrollDownChangeSelection",
        "HOME", "selectFirstColumn",
        "END", "selectLastColumn",
        "shift PAGE_UP", "scrollUpExtendSelection",
        "shift PAGE_DOWN", "scrollDownExtendSelection",
        "shift HOME", "selectFirstColumnExtendSelection",
        "shift END", "selectLastColumnExtendSelection",
        "ctrl PAGE_UP", "scrollLeftChangeSelection",
        "ctrl PAGE_DOWN", "scrollRightChangeSelection",
        "ctrl HOME", "selectFirstRow",
        "ctrl END", "selectLastRow",
        "ctrl shift PAGE_UP", "scrollRightExtendSelection",
        "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection",
        "ctrl shift HOME", "selectFirstRowExtendSelection",
        "ctrl shift END", "selectLastRowExtendSelection",
        "TAB", "selectNextColumnCell",
        "shift TAB", "selectPreviousColumnCell",
        //"ENTER", "selectNextRowCell",
        "shift ENTER", "selectPreviousRowCell",
        "ctrl A", "selectAll",
        "meta A", "selectAll",
        //"ESCAPE", "cancel",
        "F2", "startEditing"
    }));
  }

  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  private static void loadDefaults(UIDefaults defaults) {
    final Properties properties = new Properties();
    final String osSuffix = SystemInfo.isMac ? "mac" : SystemInfo.isWindows ? "windows" : "linux";
    try {
      InputStream stream = DarculaLaf.class.getResourceAsStream("darcula.properties");
      properties.load(stream);
      stream.close();

      stream = DarculaLaf.class.getResourceAsStream("darcula_" + osSuffix + ".properties");
      properties.load(stream);
      stream.close();

      HashMap<String, Object> darculaGlobalSettings = new HashMap<String, Object>();
      final String prefix = "darcula.";
      for (String key : properties.stringPropertyNames()) {
        if (key.startsWith(prefix)) {
          darculaGlobalSettings.put(key.substring(prefix.length()), parseValue(key, properties.getProperty(key)));
        }
      }

      for (Object key : defaults.keySet()) {
        if (key instanceof String && ((String)key).contains(".")) {
          final String s = (String)key;
          final String darculaKey = s.substring(s.lastIndexOf('.') + 1);
          if (darculaGlobalSettings.containsKey(darculaKey)) {
            defaults.put(key, darculaGlobalSettings.get(darculaKey));
          }
        }
      }

      for (String key : properties.stringPropertyNames()) {
        final String value = properties.getProperty(key);
        defaults.put(key, parseValue(key, value));
      }
    }
    catch (IOException e) {log(e);}
  }

  private static Object parseValue(String key, String value) {
    if ("null".equals(value)) {
      return null;
    }
    if (key.endsWith("Insets")) {
      final List<String> numbers = StringUtil.split(value, ",");
      return new InsetsUIResource(Integer.parseInt(numbers.get(0)),
          Integer.parseInt(numbers.get(1)),
          Integer.parseInt(numbers.get(2)),
          Integer.parseInt(numbers.get(3)));
    } else if (key.endsWith(".border")) {
      try {
        return Class.forName(value).newInstance();
      } catch (Exception e) {log(e);}
    } else {
      final Color color = ColorUtil.fromHex(value, null);
      final Integer invVal = getInteger(value);
      final Boolean boolVal = "true".equals(value) ? Boolean.TRUE : "false".equals(value) ? Boolean.FALSE : null;
      Icon icon = key.toLowerCase().endsWith("icon") ? null : null; //TODO: copy image loading
      if (color != null) {
        return  new ColorUIResource(color);
      } else if (invVal != null) {
        return invVal;
      } else if (icon != null) {
        return new IconUIResource(icon);
      } else if (boolVal != null) {
        return boolVal;
      }
    }
    return value;
  }

  private static Integer getInteger(String value) {
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      return null;
    }
  }


  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getID() {
    return getName();
  }

  @Override
  public String getDescription() {
    return "IntelliJ Dark Look and Feel";
  }

  @Override
  public boolean isNativeLookAndFeel() {
    return true;
  }

  @Override
  public boolean isSupportedLookAndFeel() {
    return true;
  }

  @Override
  protected void initSystemColorDefaults(UIDefaults defaults) {
    callInit("initSystemColorDefaults", defaults);
  }

  @Override
  protected void initClassDefaults(UIDefaults defaults) {
    callInit("initClassDefaults", defaults);
  }

  @Override
  public void initialize() {
    call("initialize");
  }

  @Override
  public void uninitialize() {
    call("uninitialize");
  }

  @Override
  protected void loadSystemColors(UIDefaults defaults, String[] systemColors, boolean useNative) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod("loadSystemColors",
          UIDefaults.class,
          String[].class,
          boolean.class);
      superMethod.setAccessible(true);
      superMethod.invoke(base, defaults, systemColors, useNative);
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }


  @Override
  public boolean getSupportsWindowDecorations() {
    return true;
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  public static void initInputMapDefaults(UIDefaults defaults){
    // Make ENTER work in JTrees
    InputMap treeInputMap = (InputMap)defaults.get("Tree.focusInputMap");
    if(treeInputMap!=null){ // it's really possible. For example,  GTK+ doesn't have such map
      treeInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"toggle");
    }
    // Cut/Copy/Paste in JTextAreas
    InputMap textAreaInputMap=(InputMap)defaults.get("TextArea.focusInputMap");
    if(textAreaInputMap!=null){ // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
      installCutCopyPasteShortcuts(textAreaInputMap, false);
    }
    // Cut/Copy/Paste in JTextFields
    InputMap textFieldInputMap=(InputMap)defaults.get("TextField.focusInputMap");
    if(textFieldInputMap!=null){ // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
      installCutCopyPasteShortcuts(textFieldInputMap, false);
    }
    // Cut/Copy/Paste in JPasswordField
    InputMap passwordFieldInputMap=(InputMap)defaults.get("PasswordField.focusInputMap");
    if(passwordFieldInputMap!=null){ // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
      installCutCopyPasteShortcuts(passwordFieldInputMap, false);
    }
    // Cut/Copy/Paste in JTables
    InputMap tableInputMap=(InputMap)defaults.get("Table.ancestorInputMap");
    if(tableInputMap!=null){ // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
      installCutCopyPasteShortcuts(tableInputMap, true);
    }
  }

  private static void installCutCopyPasteShortcuts(InputMap inputMap, boolean useSimpleActionKeys) {
    String copyActionKey = useSimpleActionKeys ? "copy" : DefaultEditorKit.copyAction;
    String pasteActionKey = useSimpleActionKeys ? "paste" : DefaultEditorKit.pasteAction;
    String cutActionKey = useSimpleActionKeys ? "cut" : DefaultEditorKit.cutAction;
    // Ctrl+Ins, Shift+Ins, Shift+Del
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), copyActionKey);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK | InputEvent.SHIFT_DOWN_MASK), pasteActionKey);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK | InputEvent.SHIFT_DOWN_MASK), cutActionKey);
    // Ctrl+C, Ctrl+V, Ctrl+X
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), copyActionKey);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), pasteActionKey);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), DefaultEditorKit.cutAction);
  }
}
