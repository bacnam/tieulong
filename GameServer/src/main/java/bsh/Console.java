package bsh;

import bsh.util.Util;

public class Console
{
public static void main(String[] args) {
if (!Capabilities.classExists("bsh.util.Util")) {
System.out.println("Can't find the BeanShell utilities...");
}
if (Capabilities.haveSwing()) {

Util.startSplashScreen();
try {
(new Interpreter()).eval("desktop()");
} catch (EvalError e) {
System.err.println("Couldn't start desktop: " + e);
} 
} else {
System.err.println("Can't find javax.swing package:  An AWT based Console is available but not built by default.");
} 
}
}

