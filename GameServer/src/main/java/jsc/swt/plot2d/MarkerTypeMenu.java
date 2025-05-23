package jsc.swt.plot2d;

import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class MarkerTypeMenu
extends JMenu
{
public MarkerTypeMenu(String paramString, int paramInt, ActionListener paramActionListener) {
super(paramString);
ButtonGroup buttonGroup = new ButtonGroup();
int i = StandardMarker.getTypeCount();
JRadioButtonMenuItem[] arrayOfJRadioButtonMenuItem = new JRadioButtonMenuItem[i];
for (byte b = 0; b < i; b++) {

arrayOfJRadioButtonMenuItem[b] = new JRadioButtonMenuItem(StandardMarker.getTypeName(b));
arrayOfJRadioButtonMenuItem[b].addActionListener(paramActionListener);
buttonGroup.add(arrayOfJRadioButtonMenuItem[b]);
add(arrayOfJRadioButtonMenuItem[b]);
} 
if (paramInt >= 0 && paramInt < i) arrayOfJRadioButtonMenuItem[paramInt].setSelected(true); 
}
}

