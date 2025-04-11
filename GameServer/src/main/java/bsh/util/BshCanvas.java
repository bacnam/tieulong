package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.This;

import javax.swing.*;
import java.awt.*;

public class BshCanvas
        extends JComponent {
    This ths;
    Image imageBuffer;

    public BshCanvas() {
    }

    public BshCanvas(This ths) {
        this.ths = ths;
    }

    public void paintComponent(Graphics g) {
        if (this.imageBuffer != null) {
            g.drawImage(this.imageBuffer, 0, 0, this);
        }

        if (this.ths != null) {
            try {
                this.ths.invokeMethod("paint", new Object[]{g});
            } catch (EvalError e) {
                if (Interpreter.DEBUG) Interpreter.debug("BshCanvas: method invocation error:" + e);

            }
        }
    }

    public Graphics getBufferedGraphics() {
        Dimension dim = getSize();
        this.imageBuffer = createImage(dim.width, dim.height);
        return this.imageBuffer.getGraphics();
    }

    public void setBounds(int x, int y, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        super.setBounds(x, y, width, height);
    }
}

