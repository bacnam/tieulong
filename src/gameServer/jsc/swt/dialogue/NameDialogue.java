package jsc.swt.dialogue;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NameDialogue
extends Dialogue
{
JList nameList;

public NameDialogue(Component paramComponent, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt) {
super(paramComponent, paramString1, paramString2, -1, 2);

this.nameList = new JList(paramArrayOfString);
addList(paramInt);
}

public NameDialogue(Component paramComponent, String paramString1, String paramString2, Vector paramVector, int paramInt) {
super(paramComponent, paramString1, paramString2, -1, 2);

this.nameList = new JList(paramVector);
addList(paramInt);
}

void addList(int paramInt) {
setDefaultButtonEnabled(false);
this.nameList.setSelectionMode(paramInt);
this.nameList.addListSelectionListener(new ListListener(this));
JScrollPane jScrollPane = new JScrollPane(this.nameList);
add(jScrollPane, "Center");
}

public int[] getIndices() {
return this.nameList.getSelectedIndices();
}

public String[] showNames() {
if (show() == null) return null; 
if (this.nameList.isSelectionEmpty()) return null; 
Object[] arrayOfObject = this.nameList.getSelectedValues();
String[] arrayOfString = new String[arrayOfObject.length];
for (byte b = 0; b < arrayOfObject.length; ) { arrayOfString[b] = (String)arrayOfObject[b]; b++; }
return arrayOfString;
}
class ListListener implements ListSelectionListener { ListListener(NameDialogue this$0) {
this.this$0 = this$0;
}
private final NameDialogue this$0;
public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
this.this$0.setDefaultButtonEnabled(!this.this$0.nameList.isSelectionEmpty());
} }

}

