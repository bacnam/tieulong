package jsc.swt.menu;

import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class DigitsMenu
extends JMenu
{
int lo;
int hi;
JRadioButtonMenuItem[] rb;

public DigitsMenu(String paramString, int paramInt1, int paramInt2, ActionListener paramActionListener) {
this(paramString, 1, paramInt1, paramInt2, paramActionListener);
}

public DigitsMenu(String paramString, int paramInt1, int paramInt2, int paramInt3, ActionListener paramActionListener) {
super(paramString);
this.lo = paramInt1;
this.hi = paramInt2;
ButtonGroup buttonGroup = new ButtonGroup();
int i = paramInt2 - paramInt1 + 1;
if (i < 2) throw new IllegalArgumentException("Invalid menu.");

this.rb = new JRadioButtonMenuItem[i];

for (byte b = 0; b < i; b++) {

int j = paramInt1 + b;
String str = String.valueOf(j);
this.rb[b] = new JRadioButtonMenuItem(str);
this.rb[b].addActionListener(paramActionListener);
buttonGroup.add(this.rb[b]);
JMenuItem jMenuItem = add(this.rb[b]);
if (b < 10) {

int k = j % 10;
char[] arrayOfChar = String.valueOf(k).toCharArray();
jMenuItem.setMnemonic(arrayOfChar[0]);
} 
} 

this.rb[paramInt3 - paramInt1].setSelected(true);
}

public int getSelectedValue() {
for (byte b = 0; b < this.hi - this.lo + 1; b++) {
if (this.rb[b].isSelected()) return this.lo + b; 
}  return -1;
}

public void setSelectedValue(int paramInt) {
if (paramInt < this.lo || paramInt > this.hi)
throw new IllegalArgumentException("Invalid value."); 
this.rb[paramInt - this.lo].setSelected(true);
}
}

