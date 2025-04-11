package jsc.swt.datatable;

import jsc.swt.text.SigFigFormat;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

public class Table
        extends JTable {
    public static DataClipboard copiedData = new DataClipboard();
    AbstractTableModel tableModel;
    SigFigFormat realFormat;
    NumberFormat integerFormat;

    public Table(AbstractTableModel paramAbstractTableModel) {
        super(paramAbstractTableModel);

        this.tableModel = paramAbstractTableModel;
        createDefaultColumnsFromModel();

        createDefaultRenderers();

        this.realFormat = new SigFigFormat(6);

        RealRenderer realRenderer = new RealRenderer(this);
        setDefaultRenderer(Double.class, realRenderer);

        this.integerFormat = NumberFormat.getNumberInstance();
        IntegerRenderer integerRenderer = new IntegerRenderer(this);
        setDefaultRenderer(Integer.class, integerRenderer);

        setIntercellSpacing(new Dimension(0, 0));
    }

    public void copy() {
        copyToSystemClipboard();
        if (getCellSelectionEnabled()) {
            copyCells();
        } else if (getColumnSelectionAllowed()) {
            copyColumns();
        } else if (getRowSelectionAllowed()) {
            copyRows();
        }
    }

    public void copyCells() {
        int i = getSelectedColumnCount();
        int j = getSelectedRowCount();

        if (i < 1 && j < 1)
            return;
        copiedData = new DataClipboard(3, j, i);

        int[] arrayOfInt1 = getSelectedColumns();
        int[] arrayOfInt2 = getSelectedRows();
        for (byte b = 0; b < i; b++) {

            int k = arrayOfInt1[b];
            copiedData.setColumnClass(b, getColumnClass(k));
            for (byte b1 = 0; b1 < j; b1++) {

                int m = arrayOfInt2[b1];
                copiedData.setValueAt(getValueAt(m, k), b1, b);
            }
        }
    }

    public void copyColumns() {
        int i = getRowCount();
        int j = getSelectedColumnCount();
        if (j < 1)
            return;
        copiedData = new DataClipboard(2, i, j);

        int[] arrayOfInt = getSelectedColumns();
        for (byte b = 0; b < j; b++) {

            int k = arrayOfInt[b];
            copiedData.setColumnClass(b, getColumnClass(k));
            copiedData.setColumnName(b, getColumnName(k));
            for (byte b1 = 0; b1 < i; b1++) {
                copiedData.setValueAt(getValueAt(b1, k), b1, b);
            }
        }
    }

    public void copyRows() {
        int i = getColumnCount();
        int j = getSelectedRowCount();
        if (j < 1)
            return;
        copiedData = new DataClipboard(1, j, i);

        int[] arrayOfInt = getSelectedRows();
        for (byte b = 0; b < j; b++) {

            int k = arrayOfInt[b];
            for (byte b1 = 0; b1 < i; b1++) {
                copiedData.setValueAt(getValueAt(k, b1), b, b1);
            }
        }
    }

    public void copyToSystemClipboard() {
        StringBuffer stringBuffer = getDataAsStringBuffer(false, false, "\t");
        StringSelection stringSelection = new StringSelection(stringBuffer.toString());
        getToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
    }

    public StringBuffer getDataAsStringBuffer(boolean paramBoolean1, boolean paramBoolean2, String paramString) {
        StringBuffer stringBuffer = new StringBuffer();

        int i = getColumnCount();

        if (paramBoolean2) {

            for (byte b1 = 0; b1 < i; b1++) {
                if (paramBoolean1 || isColumnSelected(b1))
                    stringBuffer.append(getColumnName(b1) + paramString);
            }
            stringBuffer.append("\n");
        }

        for (byte b = 0; b < getRowCount(); b++) {

            boolean bool = false;
            for (byte b1 = 0; b1 < i; b1++) {

                if (paramBoolean1 || isCellSelected(b, b1)) {

                    bool = true;
                    stringBuffer.append(getValueAt(b, b1) + paramString);
                }
            }
            if (bool) stringBuffer.append("\n");
        }
        return stringBuffer;
    }

    public int getSignificantDigits() {
        return this.realFormat.getSignificantDigits();
    }

    public void setSignificantDigits(int paramInt) {
        this.realFormat.setSignificantDigits(paramInt);
        this.tableModel.fireTableDataChanged();
    }

    public SigFigFormat getRealFormat() {
        return this.realFormat;
    }

    protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean) {
        if (paramKeyStroke.equals(KeyStroke.getKeyStroke(67, 2, false)) || paramKeyStroke.equals(KeyStroke.getKeyStroke(88, 2, false)) || paramKeyStroke.equals(KeyStroke.getKeyStroke(86, 2, false))) {

            return false;
        }

        return super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
    }

    public void resetColumnOrder() {
        this.tableModel.fireTableStructureChanged();
    }

    class IntegerRenderer extends DefaultTableCellRenderer {
        private final Table this$0;

        IntegerRenderer(Table this$0) {
            this.this$0 = this$0;
        }

        public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
            String str;
            setHorizontalAlignment(4);

            if (param1Object instanceof Integer) {

                int i = ((Integer) param1Object).intValue();
                str = this.this$0.integerFormat.format(i);
            } else {

                str = "";
            }

            return super.getTableCellRendererComponent(param1JTable, str, param1Boolean1, param1Boolean2, param1Int1, param1Int2);
        }
    }

    class RealRenderer extends DefaultTableCellRenderer {
        private final Table this$0;

        RealRenderer(Table this$0) {
            this.this$0 = this$0;
        }

        public void RealRenderer() {
        }

        public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
            String str;
            setHorizontalAlignment(4);

            if (param1Object instanceof Double) {

                double d = ((Double) param1Object).doubleValue();

                str = this.this$0.realFormat.format(d);
            } else {

                str = "";
            }

            return super.getTableCellRendererComponent(param1JTable, str, param1Boolean1, param1Boolean2, param1Int1, param1Int2);
        }
    }

}

