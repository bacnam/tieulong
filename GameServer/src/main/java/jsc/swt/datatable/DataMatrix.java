package jsc.swt.datatable;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

public class DataMatrix
        implements Serializable {
    int columnCount;
    int rowCount;
    Class defaultClass;
    String defaultNamePrefix;
    Vector columnClasses;
    Vector columnData;
    Vector columnNames;

    public DataMatrix(int paramInt1, int paramInt2, String paramString, Class paramClass) {
        if (paramInt1 < 0 || paramInt2 < 1)
            throw new IllegalArgumentException("Invalid data matrix");
        this.rowCount = paramInt1;
        this.columnCount = paramInt2;
        this.defaultNamePrefix = paramString;
        this.defaultClass = paramClass;
        this.columnNames = new Vector(paramInt2);
        this.columnClasses = new Vector(paramInt2);
        this.columnData = new Vector(paramInt2);

        for (byte b = 1; b <= paramInt2; b++) {

            this.columnNames.addElement(paramString + b);
            this.columnClasses.addElement(paramClass);
            Vector vector;
            this.columnData.addElement(vector = new Vector(paramInt1));
            for (byte b1 = 0; b1 < paramInt1; ) {
                vector.addElement(null);
                b1++;
            }

        }
    }

    public DataMatrix(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, "", Double.class);
    }

    public DataMatrix(String paramString1, boolean paramBoolean, String paramString2, String paramString3, String paramString4) {
        this(0, 1, paramString4, String.class);

        StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2);
        int i = stringTokenizer.countTokens();
        int j = 0;
        byte b1;
        for (b1 = 0; b1 < i; b1++) {

            StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), paramString3);
            int k = stringTokenizer1.countTokens();
            if (k > j) j = k;

        }
        byte b2;
        for (b2 = 2; b2 <= j; ) {
            addColumn();
            b2++;
        }

        stringTokenizer = new StringTokenizer(paramString1, paramString2);

        byte b3 = 0;
        for (b1 = 0; b1 < i; b1++) {

            StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), paramString3);
            int k = stringTokenizer1.countTokens();
            if (b1 == 0 && paramBoolean) {

                b3 = 1;
                for (b2 = 0; b2 < k; b2++) {
                    String str = stringTokenizer1.nextToken();
                    setColumnName(b2, str);
                }

            } else {

                addRow();
                for (b2 = 0; b2 < k; b2++) {

                    String str = stringTokenizer1.nextToken();
                    setValueAt(str, b1 - b3, b2);
                }
            }
        }

        for (b2 = 0; b2 < this.columnCount; b2++) {

            if (isColumnDataInteger(b2)) {

                convertStringToInteger(b2);
                setColumnClass(b2, Integer.class);
            } else if (isColumnDataDouble(b2)) {

                convertStringToDouble(b2);
                setColumnClass(b2, Double.class);
            }
        }
    }

    public int addColumn() {
        this.columnCount++;
        this.columnNames.addElement(this.defaultNamePrefix + this.columnCount);
        this.columnClasses.addElement(this.defaultClass);
        Vector vector;
        this.columnData.addElement(vector = new Vector(this.rowCount));
        for (byte b = 0; b < this.rowCount; ) {
            vector.addElement(null);
            b++;
        }
        return this.columnCount - 1;
    }

    public void addRow() {
        this.rowCount++;

        for (byte b = 0; b < this.columnCount; b++) {

            Vector vector = this.columnData.elementAt(b);
            vector.addElement(null);
        }
    }

    private void convertDoubleToInteger(int paramInt) {
        Vector vector = this.columnData.elementAt(paramInt);
        for (byte b = 0; b < this.rowCount; b++) {

            Double double_ = (Double) vector.elementAt(b);
            if (double_ != null) {

                Double double_1 = double_;
                int i = double_1.intValue();
                if (i < 0) i = 0;
                vector.setElementAt(new Integer(i), b);
            }
        }
    }

    private void convertStringToDouble(int paramInt) {
        Vector vector = this.columnData.elementAt(paramInt);
        for (byte b = 0; b < this.rowCount; b++) {

            Object object = vector.elementAt(b);
            if (object != null) {

                String str = object.toString();

                try {
                    vector.setElementAt(new Double(str), b);
                } catch (Exception exception) {

                    vector.setElementAt(new Double(0.0D), b);
                }
            }
        }
    }

    private void convertStringToInteger(int paramInt) {
        convertStringToDouble(paramInt);
        convertDoubleToInteger(paramInt);
    }

    public void copyColumn(int paramInt1, int paramInt2) {
        setColumnName(paramInt2, getColumnName(paramInt1));
        setColumnClass(paramInt2, getColumnClass(paramInt1));
        Vector vector1 = this.columnData.elementAt(paramInt1);
        Vector vector2 = this.columnData.elementAt(paramInt2);
        for (byte b = 0; b < this.rowCount; b++) {
            vector2.setElementAt(vector1.elementAt(b), b);
        }
    }

    public void copyRow(int paramInt1, int paramInt2) {
        for (byte b = 0; b < this.columnCount; b++) {

            Vector vector = this.columnData.elementAt(b);
            vector.setElementAt(vector.elementAt(paramInt1), paramInt2);
        }
    }

    public Class getColumnClass(int paramInt) {
        return this.columnClasses.elementAt(paramInt);
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public Vector getColumnData(int paramInt) {
        return this.columnData.elementAt(paramInt);
    }

    public String getColumnName(int paramInt) {
        return this.columnNames.elementAt(paramInt);
    }

    public Vector getColumnNames(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt) {
        Vector vector = new Vector(this.columnCount);
        for (byte b = 0; b < this.columnCount; b++) {

            if (getColumnValueCount(b) >= paramInt) {

                Class clazz = getColumnClass(b);
                if ((paramBoolean1 && clazz == Double.class) || (paramBoolean2 && clazz == Integer.class) || (paramBoolean3 && clazz == String.class)) {

                    vector.addElement(getColumnName(b));
                }
            }
        }
        return vector;
    }

    public int getColumnValueCount(int paramInt) {
        Vector vector = this.columnData.elementAt(paramInt);
        byte b1 = 0;
        for (byte b2 = 0; b2 < this.rowCount; b2++) {
            if (vector.elementAt(b2) != null) b1++;
        }
        return b1;
    }

    public Class getDefaultColumnClass() {
        return this.defaultClass;
    }

    public void setDefaultColumnClass(Class paramClass) {
        this.defaultClass = paramClass;
    }

    public String getDefaultNamePrefix() {
        return this.defaultNamePrefix;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public Object getValueAt(int paramInt1, int paramInt2) {
        Vector vector = this.columnData.elementAt(paramInt2);

        return vector.elementAt(paramInt1);
    }

    public void insertColumn(int paramInt) {
        this.columnCount++;

        if (paramInt > this.columnNames.size()) paramInt = this.columnNames.size();
        this.columnNames.insertElementAt(this.defaultNamePrefix + this.columnCount, paramInt);
        this.columnClasses.insertElementAt(this.defaultClass, paramInt);
        Vector vector;
        this.columnData.insertElementAt(vector = new Vector(this.rowCount), paramInt);
        for (byte b = 0; b < this.rowCount; ) {
            vector.addElement(null);
            b++;
        }

    }

    public void insertRow(int paramInt) {
        this.rowCount++;

        for (byte b = 0; b < this.columnCount; b++) {

            Vector vector = this.columnData.elementAt(b);
            vector.insertElementAt(null, paramInt);
        }
    }

    public void insertValue(Object paramObject, int paramInt1, int paramInt2) {
        Vector vector = this.columnData.elementAt(paramInt2);
        vector.insertElementAt(paramObject, paramInt1);
        vector.removeElementAt(vector.size() - 1);
    }

    public boolean isColumnDataDouble(int paramInt) {
        Vector vector = this.columnData.elementAt(paramInt);
        for (byte b = 0; b < this.rowCount; b++) {

            Object object = vector.elementAt(b);
            if (object != null) {

                String str = object.toString();
                try {
                    Double double_ = new Double(str);
                } catch (Exception exception) {

                    return false;
                }
            }
        }

        return true;
    }

    public boolean isColumnDataInteger(int paramInt) {
        Vector vector = this.columnData.elementAt(paramInt);
        for (byte b = 0; b < this.rowCount; b++) {

            Object object = vector.elementAt(b);
            if (object != null) {

                String str = object.toString();
                try {
                    Integer integer = new Integer(str);
                    if (integer.intValue() < 0) return false;

                } catch (Exception exception) {
                    return false;
                }
            }
        }
        return true;
    }

    public void removeCell(int paramInt1, int paramInt2) {
        Vector vector = this.columnData.elementAt(paramInt2);
        vector.removeElementAt(paramInt1);
        vector.addElement(null);
    }

    public void removeColumn(int paramInt) {
        this.columnCount--;
        this.columnNames.removeElementAt(paramInt);
        this.columnClasses.removeElementAt(paramInt);
        this.columnData.removeElementAt(paramInt);
    }

    public void removeColumn(String paramString) {
        int i = this.columnNames.indexOf(paramString);
        removeColumn(i);
    }

    public void removeRow(int paramInt) {
        this.rowCount--;

        for (byte b = 0; b < this.columnCount; b++) {

            Vector vector = this.columnData.elementAt(b);
            vector.removeElementAt(paramInt);
        }
    }

    public void setColumnClass(int paramInt, Class paramClass) {
        this.columnClasses.setElementAt(paramClass, paramInt);
    }

    public void setColumnName(int paramInt, String paramString) {
        this.columnNames.setElementAt(paramString, paramInt);
    }

    public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
        Vector vector = this.columnData.elementAt(paramInt2);

        vector.setElementAt(paramObject, paramInt1);
    }
}

