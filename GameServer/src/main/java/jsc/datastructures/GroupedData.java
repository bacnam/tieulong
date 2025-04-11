package jsc.datastructures;

import jsc.util.Arrays;
import jsc.util.Sort;

import java.util.Vector;

public class GroupedData {
    private int groupCount;
    private int n;
    private Vector groupLabels;
    private double minValue;
    private double maxValue;
    private double[][] groupedData;

    public GroupedData(double[] paramArrayOfdouble, String[] paramArrayOfString) {
        this.n = paramArrayOfdouble.length;
        if (this.n < 1)
            throw new IllegalArgumentException("No data.");
        if (this.n != paramArrayOfString.length) {
            throw new IllegalArgumentException("Arrays not equal length.");
        }

        this.minValue = paramArrayOfdouble[0];
        this.maxValue = paramArrayOfdouble[0];
        for (byte b1 = 1; b1 < paramArrayOfdouble.length; b1++) {

            if (paramArrayOfdouble[b1] > this.maxValue) this.maxValue = paramArrayOfdouble[b1];
            if (paramArrayOfdouble[b1] < this.minValue) this.minValue = paramArrayOfdouble[b1];

        }

        this.groupLabels = Sort.getLabels(paramArrayOfString);

        this.groupCount = this.groupLabels.size();
        this.groupedData = new double[this.groupCount][];
        Vector vector = new Vector(this.n);

        for (byte b2 = 0; b2 < this.groupCount; b2++) {

            vector.clear();
            String str = this.groupLabels.get(b2);
            for (byte b3 = 0; b3 < this.n; b3++) {
                if (str.equals(paramArrayOfString[b3])) vector.add(new Double(paramArrayOfdouble[b3]));
            }
            int i = vector.size();
            this.groupedData[b2] = new double[i];
            for (byte b4 = 0; b4 < i; b4++) {
                this.groupedData[b2][b4] = ((Double) vector.get(b4)).doubleValue();
            }
        }
    }

    public GroupedData(double[] paramArrayOfdouble, int[] paramArrayOfint) {
        this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint));
    }

    public Object clone() {
        return copy();
    }

    public GroupedData copy() {
        byte b2 = 0;

        String[] arrayOfString = new String[this.n];
        double[] arrayOfDouble = new double[this.n];

        for (byte b1 = 0; b1 < this.groupCount; b1++) {

            String str = getLabel(b1);
            double[] arrayOfDouble1 = getData(b1);
            for (byte b = 0; b < getSize(b1); b++) {

                arrayOfDouble[b2] = arrayOfDouble1[b];
                arrayOfString[b2] = str;
                b2++;
            }
        }

        return new GroupedData(arrayOfDouble, arrayOfString);
    }

    public double[] getData(String paramString) {
        int i = this.groupLabels.indexOf(paramString);
        if (i < 0) return null;
        return this.groupedData[i];
    }

    public double[] getData() {
        double[] arrayOfDouble = new double[this.n];
        byte b1 = 0;
        for (byte b2 = 0; b2 < this.groupCount; b2++) {

            double[] arrayOfDouble1 = this.groupedData[b2];
            for (byte b = 0; b < arrayOfDouble1.length; b++) {
                arrayOfDouble[b1] = arrayOfDouble1[b];
                b1++;
            }
        }
        return arrayOfDouble;
    }

    public double[] getData(int paramInt) {
        return this.groupedData[paramInt];
    }

    public int getGroupCount() {
        return this.groupCount;
    }

    public String getLabel(int paramInt) {
        return this.groupLabels.get(paramInt);
    }

    public Vector getLabels() {
        return this.groupLabels;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public int getMaxSize() {
        int i = (this.groupedData[0]).length;
        for (byte b = 1; b < this.groupCount; b++) {
            if ((this.groupedData[b]).length > i) i = (this.groupedData[b]).length;
        }
        return i;
    }

    public int getMinSize() {
        int i = (this.groupedData[0]).length;
        for (byte b = 1; b < this.groupCount; b++) {
            if ((this.groupedData[b]).length < i) i = (this.groupedData[b]).length;
        }
        return i;
    }

    public int getN() {
        return this.n;
    }

    public int getSize(int paramInt) {
        return (this.groupedData[paramInt]).length;
    }

    public int getSize(String paramString) {
        int i = this.groupLabels.indexOf(paramString);
        if (i < 0) return 0;
        return (this.groupedData[i]).length;
    }

    public int[] getSizes() {
        int[] arrayOfInt = new int[this.groupCount];
        for (byte b = 0; b < this.groupCount; b++)
            arrayOfInt[b] = (this.groupedData[b]).length;
        return arrayOfInt;
    }

    public boolean hasLabel(String paramString) {
        return this.groupLabels.contains(paramString);
    }

    public int indexOf(String paramString) {
        return this.groupLabels.indexOf(paramString);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\nGrouped data");
        for (byte b = 0; b < getGroupCount(); b++) {

            String str = getLabel(b);
            stringBuffer.append("\n" + str);
            double[] arrayOfDouble = getData(b);
            for (byte b1 = 0; b1 < getSize(str); ) {
                stringBuffer.append(" " + arrayOfDouble[b1]);
                b1++;
            }

        }
        return stringBuffer.toString();
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String[] arrayOfString = {"Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Britain", "Japan", "West Germany", "Britain", "Japan", "West Germany", "Britain", "West Germany", "Britain", "Britain", "Britain", "Britain", "Britain"};

            double[] arrayOfDouble = {21.0D, 9.0D, 18.0D, 41.0D, 33.0D, 13.0D, 35.0D, 41.0D, 12.0D, 6.0D, 8.0D, 48.0D, 28.0D, 3.0D, 17.0D, 34.0D, 41.0D, 5.0D, 22.0D, 39.0D, 10.0D, 20.0D, 24.0D, 4.0D, 37.0D, 29.0D, 11.0D, 30.0D, 19.0D, 27.0D, 38.0D, 23.0D};
            GroupedData groupedData1 = new GroupedData(arrayOfDouble, arrayOfString);
            System.out.println(groupedData1.getN() + " values, " + groupedData1.getGroupCount() + " groups" + " min size = " + groupedData1.getMinSize() + " max size = " + groupedData1.getMaxSize());

            System.out.println(groupedData1.toString());
            GroupedData groupedData2 = groupedData1.copy();
            System.out.println(groupedData2.toString());
        }
    }
}

