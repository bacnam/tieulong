package jsc.swt.datatable;

import jsc.Utilities;
import jsc.mathfunction.MathFunctionException;
import jsc.mathfunction.StatisticalMathFunction;
import jsc.swt.control.IllegalCharactersField;
import jsc.swt.control.PosIntegerField;
import jsc.swt.control.RealField;
import jsc.swt.text.RealFormat;
import jsc.util.Maths;
import jsc.util.Sort;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Vector;

public class DataTable
        extends Table {
    public static final String FSDS = "\"";
    public static final String DELIMITERS = " ,\"\t\n\r";
    public static final String ROW_DELIMITERS = "\n\r";
    public static final String COL_DELIMITERS = " ,\"\t";
    public static final int SQUARE = 1;
    public static final int SQRT = 2;
    public static final int LOGE = 3;
    public static final int LOG10 = 6;
    public static final int RECIP_SQRT = 4;
    public static final int RECIP = 5;
    final IllegalCharactersField illegalCharsField = new IllegalCharactersField(0, "\"");
    final PosIntegerField integerField = new PosIntegerField(0, 5);
    final RealField realField = new RealField(0.0D, 5, (RealFormat) this.realFormat);
    JTextField columnNameEditor = new JTextField();
    JTableHeader tableHeader;
    TableColumnModel columnModel;
    JTable tableView;
    DataModel dataModel;
    int clickedColumn;
    int lastRowInserted;

    public DataTable(DataModel paramDataModel) {
        super(paramDataModel);

        this.dataModel = paramDataModel;
        this.tableView = this;
        this.tableHeader = getTableHeader();
        this.columnModel = getColumnModel();

        this.columnNameEditor.setVisible(false);
        this.tableHeader.add(this.columnNameEditor);

        this.columnNameEditor.addFocusListener(new ColumnNameFocusHandler(this));
        this.columnNameEditor.addKeyListener(new ColumnNameEditorListener(this));

        this.integerField.setHorizontalAlignment(4);
        setDefaultEditor(Integer.class, new IntegerCellEditor(this));

        this.realField.setHorizontalAlignment(4);
        setDefaultEditor(Double.class, new DoubleCellEditor(this));

        setDefaultEditor(String.class, new StringCellEditor(this));

        addMouseListenerToHeaderInTable();
        this.lastRowInserted = getRowCount() - 1;
        setAutoResizeMode(0);
    }

    public int addColumn() {
        int i = convertColumnIndexToView(this.dataModel.addColumn());

        setColumnName(i, getColumnName(i));
        return i;
    }

    void addMouseListenerToHeaderInTable() {
        MouseAdapter mouseAdapter = new MouseAdapter(this) {
            private final DataTable this$0;

            public void mouseClicked(MouseEvent param1MouseEvent) {
                TableColumnModel tableColumnModel = this.this$0.getColumnModel();
                int i = tableColumnModel.getColumnIndexAtX(param1MouseEvent.getX());

                this.this$0.clickedColumn = i;

                if (this.this$0.clickedColumn == -1) {
                    return;
                }

                if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown()) {

                    this.this$0.changeColumnType(this.this$0.clickedColumn, param1MouseEvent.getX(), param1MouseEvent.getY());
                } else if (SwingUtilities.isLeftMouseButton(param1MouseEvent) && param1MouseEvent.getClickCount() == 2) {
                    this.this$0.editColumnName(this.this$0.clickedColumn);
                }
            }
        };
        this.tableHeader.addMouseListener(mouseAdapter);
    }

    public void addRow() {
        this.dataModel.addRow();
    }

    public int calculate(StatisticalMathFunction paramStatisticalMathFunction, CalculatorVariables paramCalculatorVariables) {
        int i = addColumn();
        setColumnClass(i, Double.class);

        for (byte b = 0; b < getRowCount(); b++) {
            double d;
            paramCalculatorVariables.setRowIndex(b);
            try {
                d = paramStatisticalMathFunction.eval();
            } catch (MathFunctionException mathFunctionException) {
            }
            if (!Double.isNaN(d))
                setValueAt(new Double(d), b, i);
        }
        return i;
    }

    public void changeColumnType() {
        changeColumnType(Math.max(0, getSelectedColumn()));
    }

    public void changeColumnType(int paramInt) {
        Rectangle rectangle = this.tableHeader.getHeaderRect(paramInt);
        changeColumnType(paramInt, rectangle.x, rectangle.y);
    }

    void changeColumnType(int paramInt1, int paramInt2, int paramInt3) {
        JPopupMenu jPopupMenu = new JPopupMenu("Column type");
        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Categorical");
        jRadioButtonMenuItem1.setMnemonic('C');
        JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Continuous");
        jRadioButtonMenuItem2.setMnemonic('O');

        JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Integer");
        jRadioButtonMenuItem3.setMnemonic('I');

        jRadioButtonMenuItem1.addActionListener(new CategoricalListener(this));
        jRadioButtonMenuItem2.addActionListener(new ContinuousListener(this));

        jRadioButtonMenuItem3.addActionListener(new IntegerListener(this));

        buttonGroup.add(jRadioButtonMenuItem1);
        buttonGroup.add(jRadioButtonMenuItem2);
        buttonGroup.add(jRadioButtonMenuItem3);

        jPopupMenu.add(jRadioButtonMenuItem1);
        jPopupMenu.add(jRadioButtonMenuItem2);
        jPopupMenu.add(jRadioButtonMenuItem3);

        Class clazz = getColumnClass(paramInt1);

        if (clazz == String.class) {
            jRadioButtonMenuItem1.setSelected(true);
        } else if (clazz == Double.class) {
            jRadioButtonMenuItem2.setSelected(true);
        } else if (clazz == Integer.class) {
            jRadioButtonMenuItem3.setSelected(true);
        }
        jPopupMenu.show(this.tableView, paramInt2, paramInt3);
    }

    public void clear() {
        if (getCellSelectionEnabled()) {
            clearSelectedCells();
        } else if (getColumnSelectionAllowed()) {
            clearSelectedColumns();
        } else if (getRowSelectionAllowed()) {
            clearSelectedRows();
        }

    }

    void clearSelectedCells() {
        int i = getRowCount();
        int j = getColumnCount();
        for (int k = i - 1; k >= 0; k--) {
            for (int m = j - 1; m >= 0; m--) {
                if (isCellSelected(k, m))
                    this.dataModel.removeCell(k, convertColumnIndexToModel(m));
            }
        }
        this.dataModel.fireTableDataChanged();
    }

    void clearSelectedColumns() {
        int i = getSelectedColumnCount();
        int[] arrayOfInt = getSelectedColumns();

        String[] arrayOfString = new String[i];
        for (byte b1 = 0; b1 < i; b1++)
            arrayOfString[b1] = getColumnName(arrayOfInt[b1]);
        for (byte b2 = 0; b2 < i; b2++) {
            this.dataModel.removeColumn(arrayOfString[b2]);
        }

        this.dataModel.fireTableDataChanged();
    }

    void clearSelectedRows() {
        int[] arrayOfInt = getSelectedRows();
        int i = getSelectedRowCount();
        for (int j = i - 1; j >= 0; j--)
            this.dataModel.removeRow(arrayOfInt[j]);
        this.dataModel.fireTableDataChanged();
    }

    private void convertDoubleToInteger(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object instanceof Double) {

                Double double_ = (Double) object;
                int i = double_.intValue();
                if (i < 0) i = 0;
                setValueAt(new Integer(i), b, paramInt);
            }
        }
    }

    private void convertDoubleToString(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object instanceof Double) {

                Double double_ = (Double) object;
                String str = this.realFormat.format(double_.doubleValue());
                setValueAt(str, b, paramInt);
            }
        }
    }

    private void convertIntegerToDouble(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object instanceof Integer) {

                Integer integer = (Integer) object;
                double d = integer.doubleValue();
                setValueAt(new Double(d), b, paramInt);
            }
        }
    }

    private void convertIntegerToString(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object instanceof Integer) {

                Integer integer = (Integer) object;
                String str = this.integerFormat.format(integer.intValue());
                setValueAt(str, b, paramInt);
            }
        }
    }

    private void convertStringToDouble(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object != null) {
                Number number;
                String str = object.toString();
                try {
                    number = this.realFormat.parse(str);
                } catch (ParseException parseException) {
                    return;
                }
                double d = number.doubleValue();
                setValueAt(new Double(d), b, paramInt);
            }
        }
    }

    private void convertStringToInteger(int paramInt) {
        for (byte b = 0; b < getRowCount(); b++) {

            Object object = getValueAt(b, paramInt);
            if (object != null) {
                Number number;
                String str = object.toString();
                try {
                    number = this.integerFormat.parse(str);
                } catch (ParseException parseException) {
                    return;
                }
                int i = number.intValue();
                setValueAt(new Integer(i), b, paramInt);
            }
        }
    }

    public void editColumnName() {
        editColumnName(Math.max(0, getSelectedColumn()));
    }

    public void editColumnName(int paramInt) {
        Rectangle rectangle = this.tableHeader.getHeaderRect(paramInt);
        this.columnNameEditor.setBounds(rectangle);
        this.columnNameEditor.setText(getColumnName(paramInt));
        this.columnNameEditor.selectAll();
        this.columnNameEditor.setVisible(true);
        this.columnNameEditor.requestFocus();
    }

    public String[] getCategoricalData(String paramString) {
        int i = getColumnIndex(paramString);
        int j = getRowCount();
        Vector vector = new Vector(j);
        byte b;
        for (b = 0; b < j; b++) {

            Object object = getValueAt(b, i);
            if (object != null) vector.addElement(object.toString());
        }
        int k = vector.size();
        String[] arrayOfString = new String[k];
        for (b = 0; b < k; b++)
            arrayOfString[b] = vector.elementAt(b);
        return arrayOfString;
    }

    public Class getColumnClass(String paramString) {
        return getColumnClass(getColumnIndex(paramString));
    }

    public int getColumnIndex(String paramString) {
        return this.columnModel.getColumnIndex(paramString);
    }

    public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
        return this.dataModel.getColumnNames(paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
    }

    public Class getDefaultColumnClass() {
        return this.dataModel.getDefaultColumnClass();
    }

    public void setDefaultColumnClass(Class paramClass) {
        this.dataModel.setDefaultColumnClass(paramClass);
    }

    public String getDefaultNamePrefix() {
        return this.dataModel.getDefaultNamePrefix();
    }

    public double[] getNumericalData(String paramString) {
        int i = getColumnIndex(paramString);

        int j = getRowCount();
        Vector vector = new Vector(j);
        byte b;
        for (b = 0; b < j; b++) {

            Object object = getValueAt(b, i);
            if (object instanceof Double) {
                vector.addElement(object);
            } else if (object instanceof Integer) {

                Integer integer = (Integer) object;
                vector.addElement(new Double(integer.doubleValue()));
            }
        }
        int k = vector.size();
        double[] arrayOfDouble = new double[k];
        for (b = 0; b < k; b++)
            arrayOfDouble[b] = ((Double) vector.elementAt(b)).doubleValue();
        return arrayOfDouble;
    }

    public double[][] getNumericalData(String[] paramArrayOfString) {
        int i = paramArrayOfString.length;
        int j = getRowCount();

        Vector vector = new Vector(j);

        byte b;
        for (b = 0; b < j; b++) {

            boolean bool = true;
            double[] arrayOfDouble1 = new double[i];
            for (byte b1 = 0; b1 < i; b1++) {

                int m = getColumnIndex(paramArrayOfString[b1]);
                double d = getNumericalValueAt(b, m);
                if (Double.isNaN(d)) {
                    bool = false;
                    break;
                }
                arrayOfDouble1[b1] = d;
            }
            if (bool) vector.addElement(arrayOfDouble1);
        }
        int k = vector.size();
        double[][] arrayOfDouble = new double[k][];
        for (b = 0; b < k; b++)
            arrayOfDouble[b] = vector.elementAt(b);
        return arrayOfDouble;
    }

    public double getNumericalValueAt(int paramInt1, int paramInt2) {
        Object object = getValueAt(paramInt1, paramInt2);
        if (object instanceof Double) {

            Double double_ = (Double) object;
            return double_.doubleValue();
        }
        if (object instanceof Integer) {

            Integer integer = (Integer) object;
            return integer.doubleValue();
        }

        return Double.NaN;
    }

    public void insertColumn() {
        int i = getSelectedColumn();
        if (i < 0)
            return;
        this.dataModel.insertColumn(++i);
    }

    public void insertRow() {
        int i = getSelectedRow();
        if (i < 0) i = this.lastRowInserted;
        this.lastRowInserted = this.dataModel.insertRow(++i);
    }

    public boolean isChanged() {
        return this.dataModel.isChanged();
    }

    public void setChanged(boolean paramBoolean) {
        this.dataModel.setChanged(paramBoolean);
    }

    public boolean isColumnDataDouble(int paramInt) {
        return this.dataModel.isColumnDataDouble(convertColumnIndexToModel(paramInt));
    }

    public boolean isColumnDataInteger(int paramInt) {
        return this.dataModel.isColumnDataInteger(convertColumnIndexToModel(paramInt));
    }

    public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
        this.dataModel.insertValue(paramObject, paramInt1, convertColumnIndexToModel(paramInt2));
    }

    public boolean isNameInUse(int paramInt, String paramString) {
        for (byte b = 0; b < getColumnCount(); b++) {
            if (paramString.equals(getColumnName(b)) && b != paramInt) return true;
        }
        return false;
    }

    public void paste() {
        switch (Table.copiedData.getCopyMode()) {
            case 3:
                pasteCells(Table.copiedData);
                break;
            case 2:
                pasteColumns();
                break;
            case 1:
                pasteRows();
                break;
        }
    }

    public void pasteCells(DataMatrix paramDataMatrix) {
        int i = getSelectedRow();
        int j = getSelectedColumn();
        if (i < 0 || j < 0) {
            return;
        }
        int k = paramDataMatrix.getRowCount();
        if (k < 1)
            return;
        int m = paramDataMatrix.getColumnCount();

        int n = getColumnCount();
        int i1 = j + m;
        if (i1 > n) {
            for (byte b = 1; b <= i1 - n; ) {
                addColumn();
                b++;
            }

        }

        byte b1;

        for (b1 = 1; b1 <= k; ) {
            addRow();
            b1++;
        }

        for (byte b2 = 0; b2 < m; b2++) {

            int i2 = j + b2;

            for (b1 = 0; b1 < k; b1++) {

                int i3 = i + b1;

                insertValue(paramDataMatrix.getValueAt(b1, b2), i3, i2);
            }

            Class clazz = paramDataMatrix.getColumnClass(b2);
            if (clazz == Integer.class && isColumnDataInteger(i2)) {

                convertStringToInteger(i2);
                setColumnClass(i2, Integer.class);
            } else if (clazz == Double.class && isColumnDataDouble(i2)) {

                convertStringToDouble(i2);
                setColumnClass(i2, Double.class);
            } else {

                setColumnClass(i2, String.class);
            }
        }
        this.dataModel.trimTableRows(k);
        this.dataModel.fireTableDataChanged();
    }

    public void pasteColumns() {
        int i = Table.copiedData.getRowCount();
        if (i < 1)
            return;
        for (byte b = 0; b < Table.copiedData.getColumnCount(); b++) {

            int j = addColumn();
            setColumnClass(j, Table.copiedData.getColumnClass(b));
            setColumnName(j, Table.copiedData.getColumnName(b));
            for (byte b1 = 0; b1 < i; b1++) {
                setValueAt(Table.copiedData.getValueAt(b1, b), b1, j);
            }
        }

        this.dataModel.fireTableDataChanged();
    }

    public void pasteFromSystemClipboard() {
        Clipboard clipboard = getToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(this);

        try {
            String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            DataMatrix dataMatrix = new DataMatrix(str, false, "\n\r", " ,\"\t", "");
            pasteCells(dataMatrix);
        } catch (Exception exception) {

            getToolkit().beep();
            return;
        }
    }

    public void pasteRows() {
        int i = getSelectedRow();
        if (i < 0)
            return;
        int j = Table.copiedData.getColumnCount();

        int k = Table.copiedData.getRowCount();
        if (k < 1)
            return;
        for (byte b = 0; b < k; b++) {

            this.dataModel.insertRow(i);
            for (byte b1 = 0; b1 < j; b1++) {
                setValueAt(Table.copiedData.getValueAt(b, b1), i, b1);
            }
            i++;
        }
        this.dataModel.fireTableDataChanged();
    }

    public void promoteColumnClass(int paramInt) {
        Class clazz = getColumnClass(paramInt);
        if (clazz == String.class) {

            if (isColumnDataInteger(paramInt)) {

                convertStringToInteger(paramInt);
                setColumnClass(paramInt, Integer.class);
                return;
            }
            if (isColumnDataDouble(paramInt)) {

                convertStringToDouble(paramInt);
                setColumnClass(paramInt, Double.class);
                return;
            }
        }
        if (clazz == Integer.class && isColumnDataDouble(paramInt)) {

            convertIntegerToDouble(paramInt);
            setColumnClass(paramInt, Double.class);
        }
    }

    public int recode(String paramString, RecodeTable paramRecodeTable) {
        int i = addColumn();
        setColumnClass(i, String.class);
        int j = getRowCount();
        int k = paramRecodeTable.getRowCount();
        int m = getColumnIndex(paramString);

        for (byte b = 0; b < j; b++) {

            Object object = getValueAt(b, m);
            if (object != null) {

                for (byte b1 = 0; b1 < k; b1++) {

                    String str = object.toString();
                    Object object1 = paramRecodeTable.getValueAt(b1, 1);
                    if (object1 != null) {
                        if (str.equals(paramRecodeTable.getValueAt(b1, 0))) {
                            object = object1;
                            break;
                        }
                    }
                }
                setValueAt(object, b, i);
            }
        }
        return i;
    }

    public void setColumnClass(int paramInt, Class paramClass) {
        this.dataModel.setColumnClass(convertColumnIndexToModel(paramInt), paramClass);
    }

    public void setColumnName(int paramInt, String paramString) {
        String str1 = Utilities.deleteChars("\"", paramString);

        byte b = 0;
        String str2 = str1.trim();
        while (isNameInUse(paramInt, str2)) {
            str2 = str1 + "-" + ++b;
        }
        this.dataModel.setColumnName(convertColumnIndexToModel(paramInt), str2);
    }

    void setColumnNameEdited(boolean paramBoolean) {
        String str = this.columnNameEditor.getText();
        setColumnName(this.clickedColumn, str);
        if (!paramBoolean || this.clickedColumn >= getColumnCount() - 1) {
            this.columnNameEditor.setVisible(false);
        } else {

            this.clickedColumn++;
            editColumnName(this.clickedColumn);
        }
    }

    public void setData(double[] paramArrayOfdouble, int paramInt, String paramString) {
        setColumnClass(paramInt, Double.class);

        int i = getRowCount();
        if (paramString == null) {

            for (byte b = 0; b < paramArrayOfdouble.length; b++) {
                if (b >= i) addRow();
                setValueAt(new Double(paramArrayOfdouble[b]), b, paramInt);
            }

        } else {

            byte b2 = 0;
            int j = getColumnIndex(paramString);
            for (byte b1 = 0; b1 < i; b1++) {

                if (getValueAt(b1, j) != null) {
                    setValueAt(new Double(paramArrayOfdouble[b2++]), b1, paramInt);
                }
            }
        }
    }

    public void setEditable(boolean paramBoolean) {
        this.dataModel.setEditable(paramBoolean);
    }

    private void sort(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
        int i = paramInt2, j = paramInt3;
        Object object = getValueAt((paramInt2 + paramInt3) / 2, paramInt1);
        while (true) {
            if (paramBoolean) {

                for (; i < paramInt3 && Sort.compare(object, getValueAt(i, paramInt1), true) > 0; i++) ;
                for (; j > paramInt2 && Sort.compare(object, getValueAt(j, paramInt1), true) < 0; j--) ;
            } else {

                for (; i < paramInt3 && Sort.compare(object, getValueAt(i, paramInt1), false) < 0; i++) ;
                for (; j > paramInt2 && Sort.compare(object, getValueAt(j, paramInt1), false) > 0; j--) ;
            }
            if (i < j) this.dataModel.swapRow(i, j);
            if (i <= j) {
                i++;
                j--;
            }
            if (i > j) {
                if (paramInt2 < j) sort(paramInt1, paramInt2, j, paramBoolean);
                if (i < paramInt3) sort(paramInt1, i, paramInt3, paramBoolean);

                return;
            }
        }
    }

    public void sortRows(String paramString, boolean paramBoolean) {
        int i = getColumnIndex(paramString);
        boolean bool = false;
        int j = getRowCount() - 1;
        this.dataModel.addRow();
        sort(i, bool, j, paramBoolean);
        this.dataModel.removeRow(getRowCount() - 1);
        this.dataModel.fireTableDataChanged();
    }

    public void subset(String paramString, JList paramJList, boolean paramBoolean) {
        int i = getRowCount();
        int j = getColumnIndex(paramString);
        Object[] arrayOfObject = paramJList.getSelectedValues();
        int k = arrayOfObject.length;

        for (int m = i - 1; m >= 0; m--) {

            Object object = getValueAt(m, j);
            if (object != null) {

                boolean bool = false;
                for (byte b = 0; b < k; b++) {

                    String str = object.toString();
                    if (str.equals(arrayOfObject[b].toString())) {
                        bool = true;
                        break;
                    }
                }
                if (paramBoolean && !bool) {
                    this.dataModel.removeRow(m);
                } else if (!paramBoolean && bool) {
                    this.dataModel.removeRow(m);
                }

            }
        }
        this.dataModel.fireTableDataChanged();
    }

    public int transform(String paramString, int paramInt) {
        int i = addColumn();
        setColumnClass(i, Double.class);
        int j = getRowCount();
        int k = getColumnIndex(paramString);

        double d = 0.0D;
        for (byte b = 0; b < j; b++) {
            double d1;
            Object object = getValueAt(b, k);
            if (object instanceof Double) {
                d1 = ((Double) object).doubleValue();
            } else if (object instanceof Integer) {

                Integer integer = (Integer) object;
                d1 = integer.doubleValue();
            } else {
                continue;
            }
            switch (paramInt) {

                case 1:
                    d = d1 * d1;

                case 2:
                    if (d1 < 0.0D)
                        break;
                    d = Math.sqrt(d1);

                case 3:
                    if (d1 <= 0.0D)
                        break;
                    d = Math.log(d1);

                case 6:
                    if (d1 <= 0.0D)
                        break;
                    d = Maths.log10(d1);

                case 4:
                    if (d1 <= 0.0D)
                        break;
                    d = Math.sqrt(1.0D / d1);

                case 5:
                    if (d1 == 0.0D)
                        break;
                    d = 1.0D / d1;

                default:
                    setValueAt(new Double(d), b, i);
                    break;
            }
            continue;
        }
        return i;
    }

    public void updateData() {
        this.dataModel.fireTableDataChanged();
    }

    class CategoricalListener
            implements ActionListener {
        private final DataTable this$0;

        CategoricalListener(DataTable this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double)) {
                this.this$0.convertDoubleToString(this.this$0.clickedColumn);
            } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer)) {
                this.this$0.convertIntegerToString(this.this$0.clickedColumn);
            }
            this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String);
        }
    }

    class ColumnNameEditorListener extends KeyAdapter {
        private final DataTable this$0;

        ColumnNameEditorListener(DataTable this$0) {
            this.this$0 = this$0;
        }

        public void keyPressed(KeyEvent param1KeyEvent) {
            int i = param1KeyEvent.getKeyCode();

            if (i == 10) {
                this.this$0.setColumnNameEdited(true);
            } else if (i == 27) {
                this.this$0.setColumnNameEdited(false);
            } else if (i == 18) {
                this.this$0.columnNameEditor.setVisible(false);
            } else {
                return;
            }

        }
    }

    class ColumnNameFocusHandler extends FocusAdapter {
        private final DataTable this$0;

        ColumnNameFocusHandler(DataTable this$0) {
            this.this$0 = this$0;
        }

        public void focusLost(FocusEvent param1FocusEvent) {
            this.this$0.columnNameEditor.setVisible(false);
        }
    }

    class ContinuousListener implements ActionListener {
        private final DataTable this$0;

        ContinuousListener(DataTable this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            if (!this.this$0.isColumnDataDouble(this.this$0.clickedColumn))
                return;
            if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String)) {
                this.this$0.convertStringToDouble(this.this$0.clickedColumn);
            } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer)) {
                this.this$0.convertIntegerToDouble(this.this$0.clickedColumn);
            }
            this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double);
        }
    }

    class DoubleCellEditor extends DefaultCellEditor {
        private final DataTable this$0;

        public DoubleCellEditor(DataTable this$0) {
            super((JTextField) this$0.realField);
            this.this$0 = this$0;
        }

        public Object getCellEditorValue() {
            String str = this.this$0.realField.getText();
            if (str.length() == 0) return null;
            return new Double(this.this$0.realField.getValue());
        }

        public Component getTableCellEditorComponent(JTable param1JTable, Object param1Object, boolean param1Boolean, int param1Int1, int param1Int2) {
            String str;
            if (param1Object instanceof Double) {

                double d = ((Double) param1Object).doubleValue();

                str = this.this$0.realFormat.format(d);
            } else {

                str = "";
            }

            return super.getTableCellEditorComponent(param1JTable, str, param1Boolean, param1Int1, param1Int2);
        }
    }

    class IntegerCellEditor extends DefaultCellEditor {
        private final DataTable this$0;

        public IntegerCellEditor(DataTable this$0) {
            super((JTextField) this$0.integerField);
            this.this$0 = this$0;
        }

        public Object getCellEditorValue() {
            String str = this.this$0.integerField.getText();
            if (str.length() == 0) return null;
            return new Integer(this.this$0.integerField.getValue());
        }
    }

    class IntegerListener implements ActionListener {
        private final DataTable this$0;

        IntegerListener(DataTable this$0) {
            this.this$0 = this$0;
        }

        public void actionPerformed(ActionEvent param1ActionEvent) {
            if (!this.this$0.isColumnDataDouble(this.this$0.clickedColumn))
                return;
            if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$String == null) ? (DataTable.class$java$lang$String = DataTable.class$("java.lang.String")) : DataTable.class$java$lang$String)) {
                this.this$0.convertStringToInteger(this.this$0.clickedColumn);
            } else if (this.this$0.getColumnClass(this.this$0.clickedColumn) == ((DataTable.class$java$lang$Double == null) ? (DataTable.class$java$lang$Double = DataTable.class$("java.lang.Double")) : DataTable.class$java$lang$Double)) {
                this.this$0.convertDoubleToInteger(this.this$0.clickedColumn);
            }
            this.this$0.setColumnClass(this.this$0.clickedColumn, (DataTable.class$java$lang$Integer == null) ? (DataTable.class$java$lang$Integer = DataTable.class$("java.lang.Integer")) : DataTable.class$java$lang$Integer);
        }
    }

    class StringCellEditor extends DefaultCellEditor {
        private final DataTable this$0;

        public StringCellEditor(DataTable this$0) {
            super((JTextField) this$0.illegalCharsField);
            this.this$0 = this$0;
        }

        public Object getCellEditorValue() {
            String str = this.this$0.illegalCharsField.getText();
            if (str.length() == 0) return null;
            return str;
        }
    }

}

