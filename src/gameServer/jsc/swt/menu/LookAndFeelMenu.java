package jsc.swt.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LookAndFeelMenu
extends JMenu
{
static final String WINDOWS_LAF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
static final String MAC_LAF = "javax.swing.plaf.mac.MacLookAndFeel";
static final String JAVA_LAF = UIManager.getCrossPlatformLookAndFeelClassName();

static final String MOTIF_LAF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

Component parent;

public LookAndFeelMenu(Component paramComponent) {
super("Look & feel");
this.parent = paramComponent;
ButtonGroup buttonGroup = new ButtonGroup();
JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Windows");
JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Java");
JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Motif");
JRadioButtonMenuItem jRadioButtonMenuItem4 = new JRadioButtonMenuItem("Mac");
LookAndFeelListener lookAndFeelListener = new LookAndFeelListener(this);
jRadioButtonMenuItem1.addActionListener(lookAndFeelListener);
jRadioButtonMenuItem2.addActionListener(lookAndFeelListener);
jRadioButtonMenuItem3.addActionListener(lookAndFeelListener);
jRadioButtonMenuItem4.addActionListener(lookAndFeelListener);
String str = UIManager.getSystemLookAndFeelClassName();
if (str.equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) { jRadioButtonMenuItem1.setSelected(true); jRadioButtonMenuItem4.setEnabled(false); }
else if (str.equals(JAVA_LAF)) { jRadioButtonMenuItem2.setSelected(true); }
else if (str.equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) { jRadioButtonMenuItem3.setSelected(true); }
else if (str.equals("javax.swing.plaf.mac.MacLookAndFeel")) { jRadioButtonMenuItem4.setSelected(true); }
buttonGroup.add(jRadioButtonMenuItem1); buttonGroup.add(jRadioButtonMenuItem2); buttonGroup.add(jRadioButtonMenuItem3); buttonGroup.add(jRadioButtonMenuItem4);
add(jRadioButtonMenuItem1);
add(jRadioButtonMenuItem2);
add(jRadioButtonMenuItem3);
add(jRadioButtonMenuItem4);
}
class LookAndFeelListener implements ActionListener { LookAndFeelListener(LookAndFeelMenu this$0) {
this.this$0 = this$0;
}
private final LookAndFeelMenu this$0;
public void actionPerformed(ActionEvent param1ActionEvent) {
String str = param1ActionEvent.getActionCommand();

try {
String str1;
if (str.equals("Java"))
{ str1 = LookAndFeelMenu.JAVA_LAF; }
else if (str.equals("Windows"))
{ str1 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; }
else if (str.equals("Mac"))
{ str1 = "javax.swing.plaf.mac.MacLookAndFeel"; }
else if (str.equals("Motif"))
{ str1 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; }
else { return; }
UIManager.setLookAndFeel(str1);
} catch (Exception exception) {

JOptionPane.showMessageDialog(this.this$0.parent, str + " look and feel unavailable on your system.", "Error", 0);
} 

SwingUtilities.updateComponentTreeUI(this.this$0.parent);
} }

}

