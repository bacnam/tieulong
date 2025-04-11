package jsc.swt.control;

import java.awt.event.KeyEvent;

public class PosRealListener
        extends NumberListener {
    public void keyTyped(KeyEvent paramKeyEvent) {
        if (dpKey(paramKeyEvent))
            return;
        if (numberKey(paramKeyEvent)) {
            return;
        }
        paramKeyEvent.consume();
    }
}

