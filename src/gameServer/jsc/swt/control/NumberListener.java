package jsc.swt.control;

import java.awt.TextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumberListener
extends KeyAdapter
{
public boolean dpKey(KeyEvent paramKeyEvent) {
if (paramKeyEvent.getKeyChar() == '.') {

TextComponent textComponent = (TextComponent)paramKeyEvent.getComponent();
String str = textComponent.getText();
if (str.length() == 0 || str.indexOf('.') < 0) {
return true;
}
return false;
} 

return false;
}

public boolean minusKey(KeyEvent paramKeyEvent) {
if (paramKeyEvent.getKeyChar() == '-') {

TextComponent textComponent = (TextComponent)paramKeyEvent.getComponent();
String str = textComponent.getText();
if (str.length() == 0 || (textComponent.getCaretPosition() == 0 && str.charAt(0) != '-')) {
return true;
}
return false;
} 

return false;
}

public boolean numberKey(KeyEvent paramKeyEvent) {
char c = paramKeyEvent.getKeyChar();
int i = paramKeyEvent.getKeyCode();
if (Character.isDigit(c) || c == '\b' || i == 8 || i == 127 || i == 35 || i == 10 || i == 27 || i == 36 || i == 155 || i == 37 || i == 39 || i == 9)
{

return true;
}
return false;
}
}

