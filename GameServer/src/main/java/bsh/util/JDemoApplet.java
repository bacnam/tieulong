package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

import javax.swing.*;
import java.awt.*;

public class JDemoApplet
        extends JApplet {
    public void init() {
        String debug = getParameter("debug");
        if (debug != null && debug.equals("true")) {
            Interpreter.DEBUG = true;
        }
        String type = getParameter("type");
        if (type != null && type.equals("desktop")) {

            try {
                (new Interpreter()).eval("desktop()");
            } catch (TargetError te) {
                te.printStackTrace();
                System.out.println(te.getTarget());
                te.getTarget().printStackTrace();
            } catch (EvalError evalError) {
                System.out.println(evalError);
                evalError.printStackTrace();
            }
        } else {

            getContentPane().setLayout(new BorderLayout());
            JConsole console = new JConsole();
            getContentPane().add("Center", console);
            Interpreter interpreter = new Interpreter(console);
            (new Thread((Runnable) interpreter)).start();
        }
    }
}

