package bsh.util;

import bsh.ConsoleInterface;
import bsh.Interpreter;

import java.applet.Applet;
import java.awt.*;

public class AWTDemoApplet
        extends Applet {
    public void init() {
        setLayout(new BorderLayout());
        ConsoleInterface console = new AWTConsole();
        add("Center", (Component) console);
        Interpreter interpreter = new Interpreter(console);
        (new Thread((Runnable) interpreter)).start();
    }
}

