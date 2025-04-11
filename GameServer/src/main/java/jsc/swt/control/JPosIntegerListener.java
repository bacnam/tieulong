package jsc.swt.control;

import java.awt.event.KeyEvent;

public class JPosIntegerListener
        extends JNumberListener {
    public void keyTyped(KeyEvent paramKeyEvent) {
        if (numberKey(paramKeyEvent)) {
            return;
        }
        paramKeyEvent.consume();
    }
}

