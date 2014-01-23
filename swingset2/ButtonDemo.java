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
 * @(#)ButtonDemo.java	1.10 03/01/23
 */


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
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
 * JButton, JRadioButton, JToggleButton, JCheckBox Demos
 *
 * @version 1.10 01/23/03
 * @author Jeff Dinkins
 */
public class ButtonDemo extends DemoModule implements ChangeListener {

    JTabbedPane tab;

    JPanel buttonPanel = new JPanel();
    JPanel checkboxPanel = new JPanel();
    JPanel radioButtonPanel = new JPanel();
    JPanel toggleButtonPanel = new JPanel();

    Vector buttons = new Vector();
    Vector checkboxes = new Vector();
    Vector radiobuttons = new Vector();
    Vector togglebuttons = new Vector();

    Vector currentControls = buttons;

    JButton button;
    JCheckBox check;
    JRadioButton radio;
    JToggleButton toggle;

    EmptyBorder border5 = new EmptyBorder(5,5,5,5);
    EmptyBorder border10 = new EmptyBorder(10,10,10,10);

    ItemListener buttonDisplayListener = null;
    ItemListener buttonPadListener = null;

    Insets insets0 = new Insets(0,0,0,0);
    Insets insets10 = new Insets(10,10,10,10);

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
	ButtonDemo demo = new ButtonDemo(null);
	demo.mainImpl();
    }

    /**
     * ButtonDemo Constructor
     */
    public ButtonDemo(SwingSet2 swingset) {
	// Set the title for this demo, and an icon used to represent this
	// demo inside the SwingSet2 app.
	super(swingset, "ButtonDemo", "toolbar/JButton.gif");

	tab = new JTabbedPane();
	tab.getModel().addChangeListener(this);

	JPanel demo = getDemoPanel();
	demo.setLayout(new BoxLayout(demo, BoxLayout.Y_AXIS));
	demo.add(tab);

	addButtons();
	addRadioButtons();
	addCheckBoxes();
	// addToggleButtons();
	currentControls = buttons;
    }

    public void addButtons() {
	tab.addTab(getString("ButtonDemo.buttons"), buttonPanel);
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(border5);

	JPanel p1 = createVerticalPanel(true);
	p1.setAlignmentY(TOP_ALIGNMENT);
	buttonPanel.add(p1);

	// Text Buttons
	JPanel p2 = createHorizontalPanel(false);
	p1.add(p2);
	p2.setBorder(new CompoundBorder(new TitledBorder(null, getString("ButtonDemo.textbuttons"),
							  TitledBorder.LEFT, TitledBorder.TOP), border5));

	buttons.add(p2.add(new JButton(getString("ButtonDemo.button1"))));
	p2.add(Box.createRigidArea(HGAP10));

	buttons.add(p2.add(new JButton(getString("ButtonDemo.button2"))));
	p2.add(Box.createRigidArea(HGAP10));

	buttons.add(p2.add(new JButton(getString("ButtonDemo.button3"))));


	// Image Buttons
	p1.add(Box.createRigidArea(VGAP30));
	JPanel p3 = createHorizontalPanel(false);
	p1.add(p3);
	p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
	p3.setBorder(new TitledBorder(null, getString("ButtonDemo.imagebuttons"),
					 TitledBorder.LEFT, TitledBorder.TOP));

	// home image button
	String description = getString("ButtonDemo.phone");
	button = new JButton(createImageIcon("buttons/b1.gif", description));
	button.setPressedIcon(createImageIcon("buttons/b1p.gif", description));
	button.setRolloverIcon(createImageIcon("buttons/b1r.gif", description));
	button.setDisabledIcon(createImageIcon("buttons/b1d.gif", description));
	button.setMargin(new Insets(0,0,0,0));
	p3.add(button);
	buttons.add(button);
	p3.add(Box.createRigidArea(HGAP10));

	// write image button
	description = getString("ButtonDemo.write");
	button = new JButton(createImageIcon("buttons/b2.gif", description));
	button.setPressedIcon(createImageIcon("buttons/b2p.gif", description));
	button.setRolloverIcon(createImageIcon("buttons/b2r.gif", description));
	button.setDisabledIcon(createImageIcon("buttons/b2d.gif", description));
	button.setMargin(new Insets(0,0,0,0));
	p3.add(button);
	buttons.add(button);
	p3.add(Box.createRigidArea(HGAP10));

	// write image button
	description = getString("ButtonDemo.peace");
	button = new JButton(createImageIcon("buttons/b3.gif", description));
	button.setPressedIcon(createImageIcon("buttons/b3p.gif", description));
	button.setRolloverIcon(createImageIcon("buttons/b3r.gif", description));
	button.setDisabledIcon(createImageIcon("buttons/b3d.gif", description));
	button.setMargin(new Insets(0,0,0,0));
	p3.add(button);
	buttons.add(button);

	p1.add(Box.createVerticalGlue());

	buttonPanel.add(Box.createRigidArea(HGAP10));
	currentControls = buttons;
	buttonPanel.add(createControls());
    }

    public void addRadioButtons() {
	ButtonGroup group = new ButtonGroup();

	tab.addTab(getString("ButtonDemo.radiobuttons"), radioButtonPanel);
	radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.X_AXIS));
        radioButtonPanel.setBorder(border5);

	JPanel p1 = createVerticalPanel(true);
	p1.setAlignmentY(TOP_ALIGNMENT);
	radioButtonPanel.add(p1);

	// Text Radio Buttons
	JPanel p2 = createHorizontalPanel(false);
	p1.add(p2);
	p2.setBorder(new CompoundBorder(
                      new TitledBorder(
			null, getString("ButtonDemo.textradiobuttons"),
			TitledBorder.LEFT, TitledBorder.TOP), border5)
	);

        radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio1")));
        group.add(radio);
	radiobuttons.add(radio);
	p2.add(Box.createRigidArea(HGAP10));

	radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio2")));
        group.add(radio);
	radiobuttons.add(radio);
	p2.add(Box.createRigidArea(HGAP10));

	radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio3")));
        group.add(radio);
	radiobuttons.add(radio);

	// Image Radio Buttons
        group = new ButtonGroup();
	p1.add(Box.createRigidArea(VGAP30));
	JPanel p3 = createHorizontalPanel(false);
	p1.add(p3);
	p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
	p3.setBorder(new TitledBorder(null, getString("ButtonDemo.imageradiobuttons"),
					 TitledBorder.LEFT, TitledBorder.TOP));

	// image radio button 1
	String description = getString("ButtonDemo.customradio");
	String text = getString("ButtonDemo.radio1");
	radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
	radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
	radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
	radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
	radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
	radio.setMargin(new Insets(0,0,0,0));
	group.add(radio);
	p3.add(radio);
	radiobuttons.add(radio);
	p3.add(Box.createRigidArea(HGAP20));

	// image radio button 2
	text = getString("ButtonDemo.radio2");
	radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
	radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
	radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
	radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
	radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
	radio.setMargin(new Insets(0,0,0,0));
	group.add(radio);
	p3.add(radio);
	radiobuttons.add(radio);
	p3.add(Box.createRigidArea(HGAP20));

	// image radio button 3
	text = getString("ButtonDemo.radio3");
	radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
	radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
	radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
	radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
	radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
	radio.setMargin(new Insets(0,0,0,0));
	group.add(radio);
	radiobuttons.add(radio);
	p3.add(radio);

	// verticaly glue fills out the rest of the box
	p1.add(Box.createVerticalGlue());

	radioButtonPanel.add(Box.createRigidArea(HGAP10));
	currentControls = radiobuttons;
	radioButtonPanel.add(createControls());
    }


    public void addCheckBoxes() {
	tab.addTab(getString("ButtonDemo.checkboxes"), checkboxPanel);
	checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.X_AXIS));
        checkboxPanel.setBorder(border5);

	JPanel p1 = createVerticalPanel(true);
	p1.setAlignmentY(TOP_ALIGNMENT);
	checkboxPanel.add(p1);

	// Text Radio Buttons
	JPanel p2 = createHorizontalPanel(false);
	p1.add(p2);
	p2.setBorder(new CompoundBorder(
                      new TitledBorder(
			null, getString("ButtonDemo.textcheckboxes"),
			TitledBorder.LEFT, TitledBorder.TOP), border5)
	);

	checkboxes.add(p2.add(new JCheckBox(getString("ButtonDemo.check1"))));
	p2.add(Box.createRigidArea(HGAP10));

	checkboxes.add(p2.add(new JCheckBox(getString("ButtonDemo.check2"))));
	p2.add(Box.createRigidArea(HGAP10));

	checkboxes.add(p2.add(new JCheckBox(getString("ButtonDemo.check3"))));

	// Image Radio Buttons
	p1.add(Box.createRigidArea(VGAP30));
	JPanel p3 = createHorizontalPanel(false);
	p1.add(p3);
	p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
	p3.setBorder(new TitledBorder(null, getString("ButtonDemo.imagecheckboxes"),
					 TitledBorder.LEFT, TitledBorder.TOP));

	// image checkbox 1
	String description = getString("ButtonDemo.customcheck");
	String text = getString("ButtonDemo.check1");
	check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
	check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
	check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
	check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
	check.setMargin(new Insets(0,0,0,0));
	p3.add(check);
	checkboxes.add(check);
	p3.add(Box.createRigidArea(HGAP20));

	// image checkbox 2
	text = getString("ButtonDemo.check2");
	check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
	check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
	check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
	check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
	check.setMargin(new Insets(0,0,0,0));
	p3.add(check);
	checkboxes.add(check);
	p3.add(Box.createRigidArea(HGAP20));

	// image checkbox 3
	text = getString("ButtonDemo.check3");
	check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
	check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
	check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
	check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
	check.setMargin(new Insets(0,0,0,0));
	p3.add(check);
	checkboxes.add(check);

	// verticaly glue fills out the rest of the box
	p1.add(Box.createVerticalGlue());

	checkboxPanel.add(Box.createRigidArea(HGAP10));
	currentControls = checkboxes;
	checkboxPanel.add(createControls());
    }

    public void addToggleButtons() {
	tab.addTab(getString("ButtonDemo.togglebuttons"), toggleButtonPanel);
    }

    public JPanel createControls() {
        JPanel controls = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(300, super.getMaximumSize().height);
            }
        };
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setAlignmentY(TOP_ALIGNMENT);
        controls.setAlignmentX(LEFT_ALIGNMENT);

        JPanel buttonControls = createHorizontalPanel(true);
        buttonControls.setAlignmentY(TOP_ALIGNMENT);
        buttonControls.setAlignmentX(LEFT_ALIGNMENT);

        JPanel leftColumn = createVerticalPanel(false);
        leftColumn.setAlignmentX(LEFT_ALIGNMENT);
        leftColumn.setAlignmentY(TOP_ALIGNMENT);

        JPanel rightColumn = new LayoutControlPanel(this);

        buttonControls.add(leftColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));
        buttonControls.add(rightColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));

        controls.add(buttonControls);

	createListeners();

        // Display Options
        JLabel l = new JLabel("Display Options:");
        leftColumn.add(l);

        JCheckBox bordered = new JCheckBox("Paint Border");
        bordered.setToolTipText("Click here to turn border painting on or off.");
        bordered.setMnemonic('b');
	if (currentControls == buttons) {
	        bordered.setSelected(true);
	}
        bordered.addItemListener(buttonDisplayListener);
        leftColumn.add(bordered);

        JCheckBox focused = new JCheckBox("Paint Focus");
        focused.setToolTipText("Click here to turn focus painting on or off.");
        focused.setMnemonic('f');
        focused.setSelected(true);
        focused.addItemListener(buttonDisplayListener);
        leftColumn.add(focused);

        JCheckBox enabled = new JCheckBox("Enabled");
        enabled.setToolTipText("Click here to enable or disable the buttons.");
        enabled.setSelected(true);
        enabled.addItemListener(buttonDisplayListener);
        enabled.setMnemonic('e');
        leftColumn.add(enabled);

        JCheckBox filled = new JCheckBox("Content Filled");
        filled.setToolTipText("Click here to control the filling of the content area.");
        filled.setSelected(true);
        filled.addItemListener(buttonDisplayListener);
        filled.setMnemonic('i');
        leftColumn.add(filled);

        leftColumn.add(Box.createRigidArea(VGAP20));

        l = new JLabel("Pad Amount:");
        leftColumn.add(l);
        ButtonGroup group = new ButtonGroup();
        JRadioButton defaultPad = new JRadioButton("Default");
        defaultPad.setToolTipText("Uses the default padding between the border and label.");
        defaultPad.setMnemonic('d');
        defaultPad.addItemListener(buttonPadListener);
        group.add(defaultPad);
        defaultPad.setSelected(true);
        leftColumn.add(defaultPad);

        JRadioButton zeroPad = new JRadioButton("0");
        zeroPad.setToolTipText("Uses no padding between the border and label.");
        zeroPad.addItemListener(buttonPadListener);
        zeroPad.setMnemonic('0');
        group.add(zeroPad);
        leftColumn.add(zeroPad);

        JRadioButton tenPad = new JRadioButton("10");
        tenPad.setMnemonic('1');
        tenPad.setToolTipText("Uses a 10 pixel pad between the border and label.");
        tenPad.addItemListener(buttonPadListener);
        group.add(tenPad);
        leftColumn.add(tenPad);

        leftColumn.add(Box.createRigidArea(VGAP20));
	return controls;
    }
    
    public void createListeners() {
	buttonDisplayListener = new ItemListener() {
		Component c;
		AbstractButton b;
		
		public void itemStateChanged(ItemEvent e) {
		    JCheckBox cb = (JCheckBox) e.getSource();
		    if(cb.getText().equals("Enabled")) {
			for(int i = 0; i < currentControls.size(); i++) {
			    c = (Component) currentControls.elementAt(i);
			    c.setEnabled(cb.isSelected());
			    c.invalidate();
			}
		    } else if(cb.getText().equals("Paint Border")) {
			c = (Component) currentControls.elementAt(0);
			if(c instanceof AbstractButton) {
			    for(int i = 0; i < currentControls.size(); i++) {
				b = (AbstractButton) currentControls.elementAt(i);
				b.setBorderPainted(cb.isSelected());
				b.invalidate();
			    }
			}
		    } else if(cb.getText().equals("Paint Focus")) {
			c = (Component) currentControls.elementAt(0);
			if(c instanceof AbstractButton) {
			    for(int i = 0; i < currentControls.size(); i++) {
				b = (AbstractButton) currentControls.elementAt(i);
				b.setFocusPainted(cb.isSelected());
				b.invalidate();
			    }
			}
		    } else if(cb.getText().equals("Content Filled")) {
			c = (Component) currentControls.elementAt(0);
			if(c instanceof AbstractButton) {
			    for(int i = 0; i < currentControls.size(); i++) {
				b = (AbstractButton) currentControls.elementAt(i);
				b.setContentAreaFilled(cb.isSelected());
				b.invalidate();
			    }
			}
		    }
		    invalidate();
		    validate();
		    repaint();
		}
	};

	buttonPadListener = new ItemListener() {
		Component c;
		AbstractButton b;
		
		public void itemStateChanged(ItemEvent e) {
		    // *** pad = 0
		    int pad = -1;
		    JRadioButton rb = (JRadioButton) e.getSource();
		    if(rb.getText().equals("0") && rb.isSelected()) {
			pad = 0;
		    } else if(rb.getText().equals("10") && rb.isSelected()) {
			pad = 10;
		    }
		    
		    for(int i = 0; i < currentControls.size(); i++) {
			b = (AbstractButton) currentControls.elementAt(i);
			if(pad == -1) {
			    b.setMargin(null);
			} else if(pad == 0) {
			    b.setMargin(insets0);
			} else {
			    b.setMargin(insets10);
			}
		    }
		    invalidate();
		    validate();
		    repaint();
		}
	};
    }
	
    public void stateChanged(ChangeEvent e) {
	SingleSelectionModel model = (SingleSelectionModel) e.getSource();
	if(model.getSelectedIndex() == 0) {
	    currentControls = buttons;
	} else if(model.getSelectedIndex() == 1) {
	    currentControls = radiobuttons;
	} else if(model.getSelectedIndex() == 2) {
	    currentControls = checkboxes;
	} else {
	    currentControls = togglebuttons;
	}
    }

    public Vector getCurrentControls() {
	return currentControls;
    }
}
