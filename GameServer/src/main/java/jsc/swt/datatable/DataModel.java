package jsc.swt.datatable;

import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class DataModel
extends AbstractTableModel
{
boolean editable;
boolean changed;
DataMatrix dataMatrix;

public DataModel(DataMatrix paramDataMatrix) {
this.editable = true;
this.changed = false;
this.dataMatrix = paramDataMatrix;
addTableModelListener(new DataChangeListener(this));
}

public int addColumn() {
int i = this.dataMatrix.addColumn();
fireTableStructureChanged();
return i;
}

public void addRow() {
this.dataMatrix.addRow();
fireTableDataChanged();
}

public void copyColumn(int paramInt1, int paramInt2) {
this.dataMatrix.copyColumn(paramInt1, paramInt2);
}

public void copyRow(int paramInt1, int paramInt2) {
this.dataMatrix.copyRow(paramInt1, paramInt2);
}

public Class getColumnClass(int paramInt) {
return this.dataMatrix.getColumnClass(paramInt);
}

public int getColumnCount() {
return this.dataMatrix.getColumnCount();
}

public String getColumnName(int paramInt) {
return this.dataMatrix.getColumnName(paramInt);
}

public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
return this.dataMatrix.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
}

public Class getDefaultColumnClass() {
return this.dataMatrix.getDefaultColumnClass();
}

public String getDefaultNamePrefix() {
return this.dataMatrix.getDefaultNamePrefix();
}

public int getRowCount() {
return this.dataMatrix.getRowCount();
}

public Object getValueAt(int paramInt1, int paramInt2) {
return this.dataMatrix.getValueAt(paramInt1, paramInt2);
}

public void insertColumn(int paramInt) {
this.dataMatrix.insertColumn(paramInt); fireTableStructureChanged();
}

public int insertRow(int paramInt) {
this.dataMatrix.insertRow(paramInt);

fireTableDataChanged();
return paramInt;
}

public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
this.dataMatrix.insertValue(paramObject, paramInt1, paramInt2);

fireTableDataChanged();
}

public boolean isCellEditable(int paramInt1, int paramInt2) {
return this.editable;
}

public boolean isChanged() {
return this.changed;
}

public boolean isColumnDataDouble(int paramInt) {
return this.dataMatrix.isColumnDataDouble(paramInt);
}

public boolean isColumnDataInteger(int paramInt) {
return this.dataMatrix.isColumnDataInteger(paramInt);
}

public void removeCell(int paramInt1, int paramInt2) {
this.dataMatrix.removeCell(paramInt1, paramInt2);
}

public void removeColumn(int paramInt) {
this.dataMatrix.removeColumn(paramInt);
fireTableStructureChanged();
}

public void removeColumn(String paramString) {
this.dataMatrix.removeColumn(paramString);
fireTableStructureChanged();
}

public void removeRow(int paramInt) {
this.dataMatrix.removeRow(paramInt);
}

public void setChanged(boolean paramBoolean) {
this.changed = paramBoolean;
}

public void setColumnClass(int paramInt, Class paramClass) {
this.dataMatrix.setColumnClass(paramInt, paramClass);

fireTableDataChanged();
}

public void setColumnName(int paramInt, String paramString) {
this.dataMatrix.setColumnName(paramInt, paramString);
fireTableStructureChanged();
}

public void setDefaultColumnClass(Class paramClass) {
this.dataMatrix.setDefaultColumnClass(paramClass);
}

public void setEditable(boolean paramBoolean) {
this.editable = paramBoolean;
}

public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
this.dataMatrix.setValueAt(paramObject, paramInt1, paramInt2);
fireTableCellUpdated(paramInt1, paramInt2);
}

public void swapRow(int paramInt1, int paramInt2) {
this.dataMatrix.copyRow(paramInt1, getRowCount() - 1);
this.dataMatrix.copyRow(paramInt2, paramInt1);
this.dataMatrix.copyRow(getRowCount() - 1, paramInt2);
}

public void trimTableRows(int paramInt) {
int i = getRowCount();
int j = getColumnCount();
for (int k = i - 1; k >= i - paramInt; k--) {

for (byte b = 0; b < j; b++)
{ if (getValueAt(k, b) != null)
return;  }  removeRow(k);
} 
}
class DataChangeListener implements TableModelListener { DataChangeListener(DataModel this$0) {
this.this$0 = this$0;
} private final DataModel this$0;
public void tableChanged(TableModelEvent param1TableModelEvent) {
this.this$0.changed = true;
} }

}

