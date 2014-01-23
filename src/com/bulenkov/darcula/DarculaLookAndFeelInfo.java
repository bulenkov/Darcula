package com.bulenkov.darcula;

import javax.swing.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaLookAndFeelInfo extends UIManager.LookAndFeelInfo {
    public static final String CLASS_NAME = "idea.dark.laf.classname";
    public DarculaLookAndFeelInfo(){
      super("Darcula", CLASS_NAME);
    }

    public boolean equals(Object obj){
      return (obj instanceof DarculaLookAndFeelInfo);
    }

    public int hashCode(){
      return getName().hashCode();
    }
}
