package jsc.swt.datatable;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class RecodeModel
extends AbstractTableModel
{
Vector labels;
String[] newValues;

public RecodeModel(Vector paramVector) {
this.labels = paramVector;
this.newValues = new String[paramVector.size()];
}

public Class getColumnClass(int paramInt) {
return String.class;
}

public int getColumnCount() {
return 2;
}

public String getColumnName(int paramInt) {
switch (paramInt) {
case 0:
return "Value";
case 1: return "New value";
} 
return null;
}
public int getRowCount() {
return this.labels.size();
}

public Object getValueAt(int paramInt1, int paramInt2) {
switch (paramInt2) {
case 0:
return this.labels.elementAt(paramInt1);
case 1: return this.newValues[paramInt1];
} 
return null;
}
public boolean isCellEditable(int paramInt1, int paramInt2) {
return (paramInt2 == 1);
}
public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
if (paramInt2 == 1) this.newValues[paramInt1] = (String)paramObject; 
}
}

