/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)SplitPaneDemo.java	1.8 03/01/23
 */


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

/**
 * Split Pane demo
 *
 * @version 1.8 01/23/03
 * @author Scott Violet
 * @author Jeff Dinkins
 */
public class SplitPaneDemo extends DemoModule {

    JSplitPane splitPane = null;
    JLabel earth = null;
    JLabel moon = null;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
	SplitPaneDemo demo = new SplitPaneDemo(null);
	demo.mainImpl();
    }
    
    /**
     * SplitPaneDemo Constructor
     */
    public SplitPaneDemo(SwingSet2 swingset) {
	super(swingset, "SplitPaneDemo", "toolbar/JSplitPane.gif");

	earth = new JLabel(createImageIcon("splitpane/earth.jpg", getString("SplitPaneDemo.earth")));
	earth.setMinimumSize(new Dimension(20, 20));

	moon = new JLabel(createImageIcon("splitpane/moon.jpg", getString("SplitPaneDemo.moon")));
	moon.setMinimumSize(new Dimension(20, 20));
	
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, earth, moon);
        splitPane.setContinuousLayout(true);
	splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(200);

	getDemoPanel().add(splitPane, BorderLayout.CENTER);
	getDemoPanel().setBackground(Color.black);

	getDemoPanel().add(createSplitPaneControls(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates controls to alter the JSplitPane.
     */
    protected JPanel createSplitPaneControls() {
        JPanel wrapper = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton button;

        Box buttonWrapper = new Box(BoxLayout.X_AXIS);
	
        wrapper.setLayout(new GridLayout(0, 1));
	
        /* Create a radio button to vertically split the split pane. */
        button = new JRadioButton(getString("SplitPaneDemo.vert_split"));
        button.setMnemonic(getMnemonic("SplitPaneDemo.vert_split_mnemonic"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);

        /* Create a radio button the horizontally split the split pane. */
        button = new JRadioButton(getString("SplitPaneDemo.horz_split"));
        button.setMnemonic(getMnemonic("SplitPaneDemo.horz_split_mnemonic"));
        button.setSelected(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);
	
        /* Create a check box as to whether or not the split pane continually
           lays out the component when dragging. */
        JCheckBox checkBox = new JCheckBox(getString("SplitPaneDemo.cont_layout"));
        checkBox.setMnemonic(getMnemonic("SplitPaneDemo.cont_layout_mnemonic"));
        checkBox.setSelected(true);
	
        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setContinuousLayout(
		    ((JCheckBox)e.getSource()).isSelected());
            }
        });
        buttonWrapper.add(checkBox);
	
        /* Create a check box as to whether or not the split pane divider
           contains the oneTouchExpandable buttons. */
        checkBox = new JCheckBox(getString("SplitPaneDemo.one_touch_expandable"));
        checkBox.setMnemonic(getMnemonic("SplitPaneDemo.one_touch_expandable_mnemonic"));
        checkBox.setSelected(true);
	
        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setOneTouchExpandable(
		    ((JCheckBox) e.getSource()).isSelected());
	    }
	});
	buttonWrapper.add(checkBox);
	wrapper.add(buttonWrapper);
	
	/* Create a text field to change the divider size. */
	JPanel                   tfWrapper;
	JTextField               tf;
	JLabel                   label;
	
	tf = new JTextField();
	tf.setText(new Integer(splitPane.getDividerSize()).toString());
	tf.setColumns(5);
	tf.getAccessibleContext().setAccessibleName(getString("SplitPaneDemo.divider_size"));
	tf.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String  value = ((JTextField)e.getSource()).getText();
		int newSize;
		
		try {
		    newSize = Integer.parseInt(value);
		} catch (Exception ex) {
		    newSize = -1;
		}
		if(newSize > 0) {
		    splitPane.setDividerSize(newSize);
		} else {
		    JOptionPane.showMessageDialog(splitPane,
						  getString("SplitPaneDemo.invalid_divider_size"),
						  getString("SplitPaneDemo.error"),
						  JOptionPane.ERROR_MESSAGE);
		}
	    }
	});
	label = new JLabel(getString("SplitPaneDemo.divider_size"));
	tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	tfWrapper.add(label);
	tfWrapper.add(tf);
	label.setLabelFor(tf);
	label.setDisplayedMnemonic(getMnemonic("SplitPaneDemo.divider_size_mnemonic"));
	wrapper.add(tfWrapper);
	
	/* Create a text field that will change the preferred/minimum size
	   of the earth component. */
	tf = new JTextField(String.valueOf(earth.getMinimumSize().width));
	tf.setColumns(5);
	tf.getAccessibleContext().setAccessibleName(getString("SplitPaneDemo.first_component_min_size"));
	tf.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String           value = ((JTextField)e.getSource()).getText();
		int              newSize;
		
		try {
		    newSize = Integer.parseInt(value);
		} catch (Exception ex) {
		    newSize = -1;
		}
		if(newSize > 10) {
		    earth.setMinimumSize(new Dimension(newSize, newSize));
		} else {
		    JOptionPane.showMessageDialog(splitPane,
						  getString("SplitPaneDemo.invalid_min_size") +
						  getString("SplitPaneDemo.must_be_greater_than") + 10,
						  getString("SplitPaneDemo.error"),
						  JOptionPane.ERROR_MESSAGE);
		}
	    }
	});
	label = new JLabel(getString("SplitPaneDemo.first_component_min_size"));
	tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	tfWrapper.add(label);
	tfWrapper.add(tf);
	label.setLabelFor(tf);
	label.setDisplayedMnemonic(getMnemonic("SplitPaneDemo.first_component_min_size_mnemonic"));
	wrapper.add(tfWrapper);
	
	/* Create a text field that will change the preferred/minimum size
	   of the moon component. */
	tf = new JTextField(String.valueOf(moon.getMinimumSize().width));
	tf.setColumns(5);
	tf.getAccessibleContext().setAccessibleName(getString("SplitPaneDemo.second_component_min_size"));
	tf.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String           value = ((JTextField)e.getSource()).getText();
		int              newSize;
		
		try {
		    newSize = Integer.parseInt(value);
		} catch (Exception ex) {
		    newSize = -1;
		}
		if(newSize > 10) {
		    moon.setMinimumSize(new Dimension(newSize, newSize));
		} else {
		    JOptionPane.showMessageDialog(splitPane,
						  getString("SplitPaneDemo.invalid_min_size") +
						  getString("SplitPaneDemo.must_be_greater_than") + 10,
						  getString("SplitPaneDemo.error"),
						  JOptionPane.ERROR_MESSAGE);
		}
	    }
	});
	label = new JLabel(getString("SplitPaneDemo.second_component_min_size"));
	tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	tfWrapper.add(label);
	tfWrapper.add(tf);
	label.setLabelFor(tf);
	label.setDisplayedMnemonic(getMnemonic("SplitPaneDemo.second_component_min_size_mnemonic"));
	wrapper.add(tfWrapper);
	
	return wrapper;
    }
    
}
