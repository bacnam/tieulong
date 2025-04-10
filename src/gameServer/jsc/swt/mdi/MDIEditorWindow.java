package jsc.swt.mdi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MDIEditorWindow
extends MDIWindow
{
protected JTextArea ta;

public MDIEditorWindow(File paramFile, String paramString) {
super(paramFile);

this.ta = new JTextArea(paramString);
this.ta.setEditable(true);
this.ta.setLineWrap(true);

this.ta.getDocument().addDocumentListener(new TextListener(this));
this.ta.addCaretListener(new SelectionListener(this));

Container container = getContentPane();
container.setLayout(new BorderLayout(1, 1));

JScrollPane jScrollPane = new JScrollPane(this.ta);
container.add(jScrollPane, "Center");
}

public void clear() {
this.ta.replaceRange("", this.ta.getSelectionStart(), this.ta.getSelectionEnd());
}

public void copy() {
this.ta.copy();
}

public void cut() {
this.ta.cut();
}

public void paste() {
this.ta.paste();
}

public void selectAll() {
setSelection(true);
this.ta.selectAll();
}

public void selectNone() {
setSelection(false);
this.ta.select(0, 0);
}

public boolean write(File paramFile) {
FileWriter fileWriter = null;

try { fileWriter = new FileWriter(paramFile);
String str = this.ta.getText();

fileWriter.write(str, 0, str.length());

return true; }

catch (IOException iOException) {  }
finally { try { if (fileWriter != null) fileWriter.close();  } catch (IOException iOException) {} }
return false;
}

class SelectionListener implements CaretListener {
private final MDIEditorWindow this$0;

SelectionListener(MDIEditorWindow this$0) {
this.this$0 = this$0;
}

public void caretUpdate(CaretEvent param1CaretEvent) {
this.this$0.getParentApp().setEditEnabled((param1CaretEvent.getDot() != param1CaretEvent.getMark()));
} }
class TextListener implements DocumentListener { private final MDIEditorWindow this$0;

TextListener(MDIEditorWindow this$0) {
this.this$0 = this$0;
}
public void insertUpdate(DocumentEvent param1DocumentEvent) { this.this$0.setChanged(true); }
public void removeUpdate(DocumentEvent param1DocumentEvent) { this.this$0.setChanged(true); } public void changedUpdate(DocumentEvent param1DocumentEvent) {
this.this$0.setChanged(true);
} }

}

