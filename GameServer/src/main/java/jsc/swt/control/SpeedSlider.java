package jsc.swt.control;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class SpeedSlider
extends JSlider
{
int minDelay;
int maxDelay;
JLabel slow;
JLabel fast;

public SpeedSlider(int paramInt1, int paramInt2, int paramInt3) {
super(0, paramInt1, paramInt2, paramInt2 - paramInt3);
this.minDelay = paramInt1;
this.maxDelay = paramInt2;

int i = paramInt2 - paramInt1;
setMinorTickSpacing(i / 10);
setMajorTickSpacing(i / 2);
setPaintTicks(true);

this.slow = new JLabel("Slow");
this.fast = new JLabel("Fast");
Hashtable hashtable = new Hashtable(2);
hashtable.put(new Integer(paramInt1), this.slow);
hashtable.put(new Integer(paramInt2), this.fast);
setLabelTable(hashtable);
setPaintLabels(true);
}

public int getDelay() {
return this.maxDelay - getValue();
}

public void setDelay(int paramInt) {
setValue(this.maxDelay - paramInt);
}

public void setLabelColour(Color paramColor) {
this.slow.setForeground(paramColor); this.fast.setForeground(paramColor);
}

public void setLabelFont(Font paramFont) {
this.slow.setFont(paramFont); this.fast.setFont(paramFont);
}
}

