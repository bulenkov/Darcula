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
 * @(#)SliderDemo.java	1.6 03/01/23
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
 * JSlider Demo
 *
 * @version 1.6 01/23/03
 * @author Dave Kloba
 * @author Jeff Dinkins
 */
public class SliderDemo extends DemoModule {

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
	SliderDemo demo = new SliderDemo(null);
	demo.mainImpl();
    }

    /**
     * SliderDemo Constructor
     */
    public SliderDemo(SwingSet2 swingset) {
	// Set the title for this demo, and an icon used to represent this
	// demo inside the SwingSet2 app.
	super(swingset, "SliderDemo", "toolbar/JSlider.gif");

	createSliderDemo();
    }

    public void createSliderDemo() {
        JSlider s;
	JPanel hp;
	JPanel vp;
	GridLayout g;
	JPanel tp;
	JLabel tf;
	ChangeListener listener;

	getDemoPanel().setLayout(new BorderLayout());

	tf = new JLabel(getString("SliderDemo.slidervalue"));
	getDemoPanel().add(tf, BorderLayout.SOUTH);
	
	tp = new JPanel();
	g = new GridLayout(1, 2);
	g.setHgap(5);
	g.setVgap(5);
	tp.setLayout(g);
	getDemoPanel().add(tp, BorderLayout.CENTER);
		
	listener = new SliderListener(tf);

	BevelBorder border = new BevelBorder(BevelBorder.LOWERED);

	hp = new JPanel();
	hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
	hp.setBorder(new TitledBorder( 
			border,
			getString("SliderDemo.horizontal"),
			TitledBorder.LEFT,
			TitledBorder.ABOVE_TOP));
	tp.add(hp);

	vp = new JPanel();
	vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
	vp.setBorder(new TitledBorder( 
			border,
			getString("SliderDemo.vertical"),
			TitledBorder.LEFT,
			TitledBorder.ABOVE_TOP));
	tp.add(vp);

	// Horizontal Slider 1
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.plain")));
	s = new JSlider(-10, 100, 20);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.plain"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.a_plain_slider"));
	s.addChangeListener(listener);

	p.add(Box.createRigidArea(VGAP5));
	p.add(s);
	p.add(Box.createRigidArea(VGAP5));
	hp.add(p);
	hp.add(Box.createRigidArea(VGAP10));

	// Horizontal Slider 2
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.majorticks")));
	s = new JSlider(100, 1000, 400);
	s.setPaintTicks(true);
	s.setMajorTickSpacing(100);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.majorticks"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.majorticksdescription"));
	s.addChangeListener(listener);

	p.add(Box.createRigidArea(VGAP5));
	p.add(s);
	p.add(Box.createRigidArea(VGAP5));
	hp.add(p);
	hp.add(Box.createRigidArea(VGAP10));

	// Horizontal Slider 3
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.ticks")));
	s = new JSlider(0, 11, 6);

	s.putClientProperty("JSlider.isFilled", Boolean.TRUE );

	s.setPaintTicks(true);
	s.setMajorTickSpacing(5);
	s.setMinorTickSpacing(1);

	s.setPaintLabels( true );
	s.setSnapToTicks( true );

	s.getLabelTable().put(new Integer(11), new JLabel(new Integer(11).toString(), JLabel.CENTER));
	s.setLabelTable( s.getLabelTable() );

	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.minorticks"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.minorticksdescription"));

	s.addChangeListener(listener);

	p.add(Box.createRigidArea(VGAP5));
	p.add(s);
	p.add(Box.createRigidArea(VGAP5));
	hp.add(p);
	hp.add(Box.createRigidArea(VGAP10));

	// Horizontal Slider 4
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.disabled")));
	BoundedRangeModel brm = new DefaultBoundedRangeModel(80, 0, 0, 100);
	s = new JSlider(brm);
	s.setPaintTicks(true);
	s.setMajorTickSpacing(20);
	s.setMinorTickSpacing(5);
	s.setEnabled(false);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.disabled"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.disableddescription"));
	s.addChangeListener(listener);

	p.add(Box.createRigidArea(VGAP5));
	p.add(s);
	p.add(Box.createRigidArea(VGAP5));
	hp.add(p);
	
        //////////////////////////////////////////////////////////////////////////////

	// Vertical Slider 1
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.plain")));
	s = new JSlider(JSlider.VERTICAL, -10, 100, 20);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.plain"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.a_plain_slider"));
	s.addChangeListener(listener);
	p.add(Box.createRigidArea(HGAP10));
	p.add(s);
	p.add(Box.createRigidArea(HGAP10));
	vp.add(p);
	vp.add(Box.createRigidArea(HGAP10));

	// Vertical Slider 2
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.majorticks")));
	s = new JSlider(JSlider.VERTICAL, 100, 1000, 400);

	s.putClientProperty( "JSlider.isFilled", Boolean.TRUE );

	s.setPaintTicks(true);
	s.setMajorTickSpacing(100);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.majorticks"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.majorticksdescription"));
	s.addChangeListener(listener);
	p.add(Box.createRigidArea(HGAP25));
	p.add(s);
	p.add(Box.createRigidArea(HGAP25));
	vp.add(p);
	vp.add(Box.createRigidArea(HGAP5));

	// Vertical Slider 3
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.minorticks")));
	s = new JSlider(JSlider.VERTICAL, 0, 100, 60);
	s.setPaintTicks(true);
	s.setMajorTickSpacing(20);
	s.setMinorTickSpacing(5);

	s.setPaintLabels( true );

	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.minorticks"));
	s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.minorticksdescription"));

	s.addChangeListener(listener);
	p.add(Box.createRigidArea(HGAP10));
	p.add(s);
	p.add(Box.createRigidArea(HGAP10));
	vp.add(p);
	vp.add(Box.createRigidArea(HGAP5));

	// Vertical Slider 4
	p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
	p.setBorder(new TitledBorder(getString("SliderDemo.disabled")));
	s = new JSlider(JSlider.VERTICAL, 0, 100, 80);
	s.setPaintTicks(true);
	s.setMajorTickSpacing(20);
	s.setMinorTickSpacing(5);
	s.setEnabled(false);
	s.getAccessibleContext().setAccessibleName(getString("SliderDemo.disabled"));
        s.getAccessibleContext().setAccessibleDescription(getString("SliderDemo.disableddescription"));
	s.addChangeListener(listener);
	p.add(Box.createRigidArea(HGAP20));
	p.add(s);
	p.add(Box.createRigidArea(HGAP20));
	vp.add(p);
    }

    class SliderListener implements ChangeListener {
	JLabel tf;
	public SliderListener(JLabel f) {
	    tf = f;
	}
	public void stateChanged(ChangeEvent e) {
	    JSlider s1 = (JSlider)e.getSource();
	    tf.setText(getString("SliderDemo.slidervalue") + s1.getValue());
	}
    }
}

