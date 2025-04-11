package jsc.swt.control;

import java.awt.event.KeyEvent;

public class JIntegerListener
        extends JNumberListener {
    public void keyTyped(KeyEvent paramKeyEvent) {
        if (minusKey(paramKeyEvent))
            return;
        if (numberKey(paramKeyEvent)) {
            return;
        }
        paramKeyEvent.consume();
    }
}

