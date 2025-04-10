package jsc.swt.control;

import java.awt.event.KeyEvent;

public class JRealListener
extends JNumberListener
{
public void keyTyped(KeyEvent paramKeyEvent) {
if (minusKey(paramKeyEvent))
return;  if (dpKey(paramKeyEvent))
return;  if (numberKey(paramKeyEvent)) {
return;
}
paramKeyEvent.consume();
}
}

