package jsc.swt.control;

import java.awt.event.KeyEvent;

public class PosIntegerListener
        extends NumberListener {
    public void keyTyped(KeyEvent paramKeyEvent) {
        if (numberKey(paramKeyEvent)) {
            return;
        }
        paramKeyEvent.consume();
    }
}

