package jsc.swt.datatable;

public class DataClipboard
extends DataMatrix
{
public static final int ROWS = 1;
public static final int COLUMNS = 2;
public static final int CELLS = 3;
int copyMode;

public DataClipboard() {
super(0, 1);
}

public DataClipboard(int paramInt1, int paramInt2, int paramInt3) {
super(paramInt2, paramInt3); this.copyMode = paramInt1;
}

public int getCopyMode() {
return this.copyMode;
}

public boolean hasData() {
return (this.rowCount > 0);
}

public void setCopyMode(int paramInt) {
this.copyMode = paramInt;
}
}

