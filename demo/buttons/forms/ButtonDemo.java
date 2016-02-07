package forms;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;

/**
 * Small demo app to work on JToggleButton and compare to JButton and JCheckBox
 * Created by sasha on 06/02/16.
 */
public class ButtonDemo {
    private JPanel rootPane;
    private JButton button1;
    private JButton button2;
    private JToggleButton toggleButton1;
    private JToggleButton toggleButton2;
    private JToggleButton toggleButton3;
    private JToggleButton toggleButton4;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JToggleButton oneToggleButton;
    private JToggleButton twoToggleButton;
    private JToggleButton threeToggleButton;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // will go with metal
        }
        JFrame frame = new JFrame("ButtonDemo");
        frame.setContentPane(new ButtonDemo().rootPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
