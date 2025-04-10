package jsc.swt.control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class JNumberListener
extends KeyAdapter
{
boolean dpKey(KeyEvent paramKeyEvent) {
if (paramKeyEvent.getKeyChar() == '.') {

JTextField jTextField = (JTextField)paramKeyEvent.getComponent();
String str = jTextField.getText();
if (str.length() == 0 || str.indexOf('.') < 0) {
return true;
}
return false;
} 

return false;
}

boolean minusKey(KeyEvent paramKeyEvent) {
if (paramKeyEvent.getKeyChar() == '-') {

JTextField jTextField = (JTextField)paramKeyEvent.getComponent();
String str = jTextField.getText();
if (str.length() == 0 || (jTextField.getCaretPosition() == 0 && str.charAt(0) != '-')) {
return true;
}
return false;
} 

return false;
}

boolean numberKey(KeyEvent paramKeyEvent) {
char c = paramKeyEvent.getKeyChar();
int i = paramKeyEvent.getKeyCode();
if (Character.isDigit(c) || c == '\b' || i == 8 || i == 127 || i == 35 || i == 10 || i == 27 || i == 36 || i == 155 || i == 37 || i == 39 || i == 9)
{

return true;
}
return false;
}
}

