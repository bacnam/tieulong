package jsc.swt.control;

import java.awt.event.KeyEvent;

public class IntegerListener
extends NumberListener
{
public void keyTyped(KeyEvent paramKeyEvent) {
if (minusKey(paramKeyEvent))
return;  if (numberKey(paramKeyEvent)) {
return;
}
paramKeyEvent.consume();
}
}

