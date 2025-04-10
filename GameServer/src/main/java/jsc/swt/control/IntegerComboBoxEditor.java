package jsc.swt.control;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ComboBoxEditor;

class IntegerComboBoxEditor
implements ComboBoxEditor, FocusListener
{
protected IntegerField editor;

public IntegerComboBoxEditor(int paramInt) {
this.editor = new IntegerField(0, paramInt);

this.editor.setBorder(null);
}
public Component getEditorComponent() {
return this.editor;
}
public void setItem(Object paramObject) {
if (paramObject != null) {
this.editor.setText(paramObject.toString());
} else {
this.editor.setText("");
} 
}

public Object getItem() {
return new Integer(this.editor.getValue());
}

public void selectAll() {
this.editor.selectAll();
this.editor.requestFocus();
}

public void focusGained(FocusEvent paramFocusEvent) {}

public void focusLost(FocusEvent paramFocusEvent) {}

public void addActionListener(ActionListener paramActionListener) {
this.editor.addActionListener(paramActionListener);
}

public void removeActionListener(ActionListener paramActionListener) {
this.editor.removeActionListener(paramActionListener);
}
}

