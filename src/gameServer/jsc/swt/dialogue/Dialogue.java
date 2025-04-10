package jsc.swt.dialogue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

public class Dialogue
{
protected JOptionPane pane;
protected JDialog dialog;
JPanel panel;
Component parentComponent;

public Dialogue(Component paramComponent, String paramString1, String paramString2, int paramInt1, int paramInt2) {
this.parentComponent = paramComponent;

this.panel = new JPanel(new BorderLayout(0, 2));

if (paramString2.length() > 0) {

JLabel jLabel = new JLabel(paramString2, 0);
this.panel.add(jLabel, "North");
} 

this.pane = new JOptionPane(this.panel, paramInt1, paramInt2);
this.dialog = this.pane.createDialog(paramComponent, paramString1);
this.dialog.setModal(true);
}

public void add(Component paramComponent, String paramString) {
this.panel.add(paramComponent, paramString); this.dialog.pack();
}

public void setDefaultButtonEnabled(boolean paramBoolean) {
JRootPane jRootPane = this.dialog.getRootPane();
JButton jButton = jRootPane.getDefaultButton();
jButton.setEnabled(paramBoolean);
}

public void setLocation(int paramInt1, int paramInt2) {
this.dialog.setLocation(paramInt1, paramInt2);
}

public void setLocation(Point paramPoint) {
this.dialog.setLocation(paramPoint);
}

public void setLocationRelativeTo(Component paramComponent) {
this.dialog.setLocationRelativeTo(paramComponent);
this.parentComponent = paramComponent;
}

public void setSize(int paramInt1, int paramInt2) {
this.dialog.setSize(paramInt1, paramInt2);
this.dialog.setLocationRelativeTo(this.parentComponent);
}

public Object show() {
this.dialog.show();

Object object = this.pane.getValue();
if (object == null) {
return null;
}

if (object instanceof Integer) {

int i = ((Integer)object).intValue();
if (i != 0) {
return null;
}
} 

return object;
}
}

