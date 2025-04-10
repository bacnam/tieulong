package jsc.swt.control;

import java.awt.event.KeyEvent;

public class RealListener
extends NumberListener
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

