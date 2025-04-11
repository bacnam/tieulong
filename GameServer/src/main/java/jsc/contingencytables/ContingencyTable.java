package jsc.contingencytables;

import jsc.descriptive.CategoricalTally;
import jsc.util.Arrays;

import java.util.Vector;

public class ContingencyTable {
    private int N;
    private int rowCount;
    private int colCount;
    private Vector rowLabels;
    private Vector colLabels;
    private int[][] O;

    public ContingencyTable(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        this.N = paramArrayOfString1.length;
        if (this.N != paramArrayOfString2.length) {
            throw new IllegalArgumentException("Data arrays do not match.");
        }

        CategoricalTally categoricalTally1 = new CategoricalTally("", paramArrayOfString1);
        CategoricalTally categoricalTally2 = new CategoricalTally("", paramArrayOfString2);

        this.rowCount = categoricalTally1.getNumberOfBins();
        this.colCount = categoricalTally2.getNumberOfBins();

        this.rowLabels = new Vector(this.rowCount);
        this.colLabels = new Vector(this.colCount);
        byte b;
        for (b = 0; b < this.rowCount; ) {
            this.rowLabels.add(categoricalTally1.getLabel(b));
            b++;
        }
        for (b = 0; b < this.colCount; ) {
            this.colLabels.add(categoricalTally2.getLabel(b));
            b++;
        }

        this.O = new int[this.rowCount][this.colCount];

        for (b = 0; b < this.N; b++) {
            this.O[categoricalTally1.indexOf(paramArrayOfString1[b])][categoricalTally2.indexOf(paramArrayOfString2[b])] = this.O[categoricalTally1.indexOf(paramArrayOfString1[b])][categoricalTally2.indexOf(paramArrayOfString2[b])] + 1;
        }
    }

    public ContingencyTable(int[] paramArrayOfint1, int[] paramArrayOfint2) {
        this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
    }

    public ContingencyTable(String[] paramArrayOfString1, String[] paramArrayOfString2, int[][] paramArrayOfint) {
        this.rowCount = paramArrayOfString1.length;
        this.colCount = paramArrayOfString2.length;

        if (this.rowCount < 1)
            throw new IllegalArgumentException("No row labels.");
        if (this.colCount < 1)
            throw new IllegalArgumentException("No column labels.");
        if (this.rowCount != paramArrayOfint.length)
            throw new IllegalArgumentException("Number of row labels do not match frequencies.");
        byte b;
        for (b = 0; b < this.rowCount; b++) {
            if ((paramArrayOfint[b]).length != this.colCount)
                throw new IllegalArgumentException("Number of column labels do not match frequencies.");
        }
        this.rowLabels = new Vector(this.rowCount);
        this.colLabels = new Vector(this.colCount);
        for (b = 0; b < this.rowCount; ) {
            this.rowLabels.add(paramArrayOfString1[b]);
            b++;
        }
        for (b = 0; b < this.colCount; ) {
            this.colLabels.add(paramArrayOfString2[b]);
            b++;
        }

        this.O = new int[this.rowCount][this.colCount];
        this.N = 0;
        for (b = 0; b < this.rowCount; b++) {
            for (byte b1 = 0; b1 < this.colCount; b1++) {

                if (paramArrayOfint[b][b1] < 0)
                    throw new IllegalArgumentException("Negative frequency.");
                this.N += paramArrayOfint[b][b1];
                this.O[b][b1] = paramArrayOfint[b][b1];
            }
        }
    }

    public ContingencyTable(int[] paramArrayOfint1, int[] paramArrayOfint2, int[][] paramArrayOfint) {
        this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2), paramArrayOfint);
    }

    public ContingencyTable(int[][] paramArrayOfint) {
        this(Arrays.sequence(0, paramArrayOfint.length - 1), Arrays.sequence(0, (paramArrayOfint[0]).length - 1), paramArrayOfint);
    }

    public Object clone() {
        return copy();
    }

    public ContingencyTable copy() {
        String[] arrayOfString1 = new String[this.rowCount];
        String[] arrayOfString2 = new String[this.colCount];
        int[][] arrayOfInt = new int[this.rowCount][this.colCount];
        byte b;
        for (b = 0; b < this.rowCount; ) {
            arrayOfString1[b] = this.rowLabels.get(b);
            b++;
        }
        for (b = 0; b < this.colCount; ) {
            arrayOfString2[b] = this.colLabels.get(b);
            b++;
        }

        for (b = 0; b < this.rowCount; b++) {
            for (byte b1 = 0; b1 < this.colCount; b1++)
                arrayOfInt[b][b1] = this.O[b][b1];
        }
        return new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
    }

    public int[][] getFrequencies() {
        return this.O;
    }

    public int getFrequency(int paramInt1, int paramInt2) {
        return this.O[paramInt1][paramInt2];
    }

    public int getN() {
        return this.N;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int[] getRowData(String paramString) {
        int i = this.rowLabels.indexOf(paramString);
        if (i < 0) return null;
        return this.O[i];
    }

    public int[] getRowData(int paramInt) {
        return this.O[paramInt];
    }

    public String getRowLabel(int paramInt) {
        return this.rowLabels.get(paramInt);
    }

    public Vector getRowLabels() {
        return this.rowLabels;
    }

    public int[] getRowTotals() {
        int[] arrayOfInt = new int[this.rowCount];
        for (byte b = 0; b < this.rowCount; b++) {

            arrayOfInt[b] = 0;
            for (byte b1 = 0; b1 < this.colCount; ) {
                arrayOfInt[b] = arrayOfInt[b] + this.O[b][b1];
                b1++;
            }

        }
        return arrayOfInt;
    }

    public boolean hasRowLabel(String paramString) {
        return this.rowLabels.contains(paramString);
    }

    public int indexOfRow(String paramString) {
        return this.rowLabels.indexOf(paramString);
    }

    public int getColumnCount() {
        return this.colCount;
    }

    public int[] getColumnData(String paramString) {
        int i = this.colLabels.indexOf(paramString);
        if (i < 0) return null;
        return getColumnData(i);
    }

    public int[] getColumnData(int paramInt) {
        int[] arrayOfInt = new int[this.rowCount];
        for (byte b = 0; b < this.rowCount; ) {
            arrayOfInt[b] = this.O[b][paramInt];
            b++;
        }
        return arrayOfInt;
    }

    public String getColumnLabel(int paramInt) {
        return this.colLabels.get(paramInt);
    }

    public Vector getColumnLabels() {
        return this.colLabels;
    }

    public int[] getColumnTotals() {
        int[] arrayOfInt = new int[this.colCount];
        for (byte b = 0; b < this.colCount; b++) {

            arrayOfInt[b] = 0;
            for (byte b1 = 0; b1 < this.rowCount; ) {
                arrayOfInt[b] = arrayOfInt[b] + this.O[b1][b];
                b1++;
            }

        }
        return arrayOfInt;
    }

    public boolean hasColumnLabel(String paramString) {
        return this.colLabels.contains(paramString);
    }

    public int indexOfColumn(String paramString) {
        return this.colLabels.indexOf(paramString);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nContingency table\n");
        byte b1;
        for (b1 = 0; b1 < this.colCount; ) {
            stringBuffer.append("\t" + getColumnLabel(b1));
            b1++;
        }
        for (byte b2 = 0; b2 < this.rowCount; b2++) {

            stringBuffer.append("\n" + getRowLabel(b2));
            for (b1 = 0; b1 < this.colCount; ) {
                stringBuffer.append("\t" + this.O[b2][b1]);
                b1++;
            }

        }
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString1 = {"Improved", "Same or worse"};
            String[] arrayOfString2 = {"Placebo", "Drug 1", "Drug 2", "Drug 3", "Drug 4", "Drug 5"};
            int[][] arrayOfInt = {{8, 12, 21, 15, 14, 19}, {22, 18, 9, 15, 16, 11}};
            ContingencyTable contingencyTable1 = new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
            System.out.println(contingencyTable1.toString());

            int[] arrayOfInt1 = {2, 1, 2, 2, 2, 3, 3, 2, 1, 1, 2, 3, 2, 3, 2, 3, 1, 2, 3, 2};
            int[] arrayOfInt2 = {8, 8, 9, 8, 9, 9, 9, 8, 8, 9, 8, 8, 9, 8, 8, 9, 9, 8, 8, 9};
            contingencyTable1 = new ContingencyTable(arrayOfInt1, arrayOfInt2);
            System.out.println(contingencyTable1.toString());
            ContingencyTable contingencyTable2 = contingencyTable1.copy();
            System.out.println(contingencyTable2.toString());
        }
    }
}

