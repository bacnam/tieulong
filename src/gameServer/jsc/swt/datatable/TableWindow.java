package jsc.swt.datatable;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jsc.swt.menu.DigitsMenu;

public class TableWindow
extends JInternalFrame
{
public JMenuBar menuBar;
public Table table;
CopyAction copyAction;
JInternalFrame parent;
DataWindow dataWindow;

public TableWindow(String paramString, Table paramTable, DataWindow paramDataWindow) {
super(paramString, true, true, true, true);
this.parent = this;
this.dataWindow = paramDataWindow;
this.table = paramTable;
paramTable.setColumnSelectionAllowed(false);
paramTable.setRowSelectionAllowed(false);
paramTable.setCellSelectionEnabled(true);

ImageIcon imageIcon = null;
Class clazz = getClass();
URL uRL = clazz.getResource("images/table.gif");
if (uRL != null) imageIcon = new ImageIcon(uRL); 
setFrameIcon(imageIcon);
setDefaultCloseOperation(2);

addInternalFrameListener(new TableWindowListener(this));

this.menuBar = new JMenuBar();
this.menuBar.setBorder(new BevelBorder(0));

JMenu jMenu1 = new JMenu("Edit");
this.menuBar.add(jMenu1);
this.copyAction = new CopyAction(this, "Copy", new ImageIcon("images/copy.gif"));
SelectAllAction selectAllAction = new SelectAllAction(this, "Select all");
SelectNoneAction selectNoneAction = new SelectNoneAction(this, "Select none");
ResetColumnAction resetColumnAction = new ResetColumnAction(this, "Reset column order");

JMenuItem jMenuItem = jMenu1.add(this.copyAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2, false));
jMenu1.addSeparator();
jMenuItem = jMenu1.add(selectAllAction);
jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2, false));
jMenu1.add(selectNoneAction);
jMenu1.addSeparator();
jMenu1.add(resetColumnAction);

JMenu jMenu2 = new JMenu("Options");
this.menuBar.add(jMenu2);

JMenu jMenu3 = new JMenu("Selection");
jMenu2.add(jMenu3);
ButtonGroup buttonGroup = new ButtonGroup();
JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Columns");
JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Rows");
JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Any cells", true);

jRadioButtonMenuItem1.addActionListener(new SelectionColumnsListener(this));
jRadioButtonMenuItem2.addActionListener(new SelectionRowsListener(this));
jRadioButtonMenuItem3.addActionListener(new SelectionCellsListener(this));

buttonGroup.add(jRadioButtonMenuItem1); buttonGroup.add(jRadioButtonMenuItem2); buttonGroup.add(jRadioButtonMenuItem3);
jMenu3.add(jRadioButtonMenuItem1);
jMenu3.add(jRadioButtonMenuItem2);
jMenu3.add(jRadioButtonMenuItem3);
DigitsMenu digitsMenu = new DigitsMenu("Significant figures", 16, paramTable.getSignificantDigits(), new DigitsListener(this));

jMenu2.add((JMenuItem)digitsMenu);

this.copyAction.setEnabled(false);

setJMenuBar(this.menuBar);

Container container = getContentPane();
container.setLayout(new BorderLayout(1, 1));

ListSelectionModel listSelectionModel = paramTable.getSelectionModel();
listSelectionModel.addListSelectionListener(new SelectionDebugger(this, listSelectionModel));

JScrollPane jScrollPane = new JScrollPane(paramTable);
container.add(jScrollPane, "Center");
}
class CopyAction extends AbstractAction { private final TableWindow this$0;

public CopyAction(TableWindow this$0, String param1String, Icon param1Icon) {
super(param1String, param1Icon); this.this$0 = this$0;
}
public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.copy();
this.this$0.dataWindow.pasteAction.setEnabled(true);
this.this$0.dataWindow.pasteClipboardAction.setEnabled(true);
} }
class DigitsListener implements ActionListener { private final TableWindow this$0;
DigitsListener(TableWindow this$0) {
this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
int i = Integer.parseInt(param1ActionEvent.getActionCommand());
this.this$0.table.setSignificantDigits(i);
} }

class ResetColumnAction extends AbstractAction { private final TableWindow this$0;

public ResetColumnAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.resetColumnOrder();
} }

class SelectAllAction extends AbstractAction { private final TableWindow this$0;

public SelectAllAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.selectAll();
} }
class SelectionCellsListener implements ActionListener { SelectionCellsListener(TableWindow this$0) {
this.this$0 = this$0;
}
private final TableWindow this$0;
public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.setColumnSelectionAllowed(false);
this.this$0.table.setRowSelectionAllowed(false);
this.this$0.table.setCellSelectionEnabled(true);
this.this$0.table.clearSelection();
} }
class SelectionColumnsListener implements ActionListener { private final TableWindow this$0;
SelectionColumnsListener(TableWindow this$0) {
this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.setColumnSelectionAllowed(true);
this.this$0.table.setRowSelectionAllowed(false);

this.this$0.table.clearSelection();
} }

class SelectionDebugger implements ListSelectionListener {
ListSelectionModel model;
private final TableWindow this$0;

public SelectionDebugger(TableWindow this$0, ListSelectionModel param1ListSelectionModel) {
this.this$0 = this$0;
this.model = param1ListSelectionModel;
}

public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
boolean bool = !this.model.isSelectionEmpty() ? true : false;
this.this$0.copyAction.setEnabled(bool);
} }
class SelectionRowsListener implements ActionListener { private final TableWindow this$0;
SelectionRowsListener(TableWindow this$0) {
this.this$0 = this$0;
}

public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.setColumnSelectionAllowed(false);
this.this$0.table.setRowSelectionAllowed(true);

this.this$0.table.clearSelection();
} }

class SelectNoneAction extends AbstractAction { private final TableWindow this$0;

public SelectNoneAction(TableWindow this$0, String param1String) { super(param1String); this.this$0 = this$0; } public void actionPerformed(ActionEvent param1ActionEvent) {
this.this$0.table.clearSelection();
} }

class TableWindowListener
extends InternalFrameAdapter {
private final TableWindow this$0;

TableWindowListener(TableWindow this$0) {
this.this$0 = this$0;
}

public void internalFrameClosing(InternalFrameEvent param1InternalFrameEvent) {}
}
}

