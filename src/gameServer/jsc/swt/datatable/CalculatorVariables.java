package jsc.swt.datatable;

import java.util.Vector;
import jsc.mathfunction.MathFunctionVariables;

public class CalculatorVariables
implements MathFunctionVariables
{
private int calcRow;
DataTable dataTable;
Vector names;

public CalculatorVariables(DataTable paramDataTable) {
this.names = paramDataTable.getColumnNames(true, true, false, 1);
this.dataTable = paramDataTable;
}

public Vector getNames() {
return this.names;
}

public int getNumberOfVariables() {
return this.names.size();
}

public String getVariableName(int paramInt) {
return "\"" + (String)this.names.elementAt(paramInt) + "\"";
}

public double getVariableValue(int paramInt) {
int i = this.dataTable.getColumnIndex(this.names.elementAt(paramInt));
return this.dataTable.getNumericalValueAt(this.calcRow, i);
}

public void setRowIndex(int paramInt) {
this.calcRow = paramInt;
}
}

