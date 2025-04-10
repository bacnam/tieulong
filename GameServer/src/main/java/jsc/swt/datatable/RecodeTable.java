package jsc.swt.datatable;

import java.awt.Dimension;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RecodeTable
extends JTable
{
public RecodeTable(RecodeModel paramRecodeModel) {
super(paramRecodeModel);
createDefaultColumnsFromModel();
createDefaultRenderers();

DefaultCellEditor defaultCellEditor = new DefaultCellEditor(new JTextField());
defaultCellEditor.setClickCountToStart(1);

setDefaultEditor(String.class, defaultCellEditor);

ListSelectionModel listSelectionModel = getSelectionModel();
listSelectionModel.addListSelectionListener(new SelectionDebugger(this, listSelectionModel));

setIntercellSpacing(new Dimension(0, 0));
}

class SelectionDebugger
implements ListSelectionListener
{
ListSelectionModel model;

private final RecodeTable this$0;

public SelectionDebugger(RecodeTable this$0, ListSelectionModel param1ListSelectionModel) {
this.this$0 = this$0; this.model = param1ListSelectionModel;
}
public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
int i = this.model.getMinSelectionIndex();
int j = this.model.getMaxSelectionIndex();

Object object = this.this$0.getValueAt(i, 1);
for (int k = i; k <= j; ) { this.this$0.setValueAt(object, k, 1); k++; }

}
}
}

