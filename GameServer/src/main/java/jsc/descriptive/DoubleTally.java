package jsc.descriptive;

import java.util.Arrays;
import java.util.Vector;

public class DoubleTally
        implements Cloneable {
    private int N;
    private Vector freq;
    private Vector x;
    private double tolerance;

    public DoubleTally(double[] paramArrayOfdouble) {
        this(paramArrayOfdouble, 0.0D);
    }

    public DoubleTally(double[] paramArrayOfdouble, double paramDouble) {
        this.tolerance = paramDouble;

        this.N = paramArrayOfdouble.length;

        Arrays.sort(paramArrayOfdouble);

        byte b2 = 1;
        double d1 = paramArrayOfdouble[0];
        byte b1;
        for (b1 = 1; b1 < this.N; b1++) {

            if (Math.abs(paramArrayOfdouble[b1] - d1) > paramDouble) {
                b2++;
                d1 = paramArrayOfdouble[b1];
            }
        }
        this.x = new Vector(b2);

        this.freq = new Vector(b2);

        for (b1 = 0; b1 < b2; ) {
            this.freq.add(new Integer(0));
            b1++;
        }

        double d2 = paramArrayOfdouble[0];
        this.x.add(new Double(d2));

        this.freq.add(0, new Integer(1));
        byte b3 = 0;

        for (b1 = 1; b1 < this.N; b1++) {

            if (Math.abs(paramArrayOfdouble[b1] - d2) > paramDouble) {

                b3++;
                d2 = paramArrayOfdouble[b1];

                this.x.add(new Double(d2));
            }

            int i = ((Integer) this.freq.get(b3)).intValue();
            this.freq.set(b3, new Integer(++i));
        }
    }

    public DoubleTally() {
        this(0.0D);
    }

    public DoubleTally(double paramDouble) {
        this(10, 0, paramDouble);
    }

    public DoubleTally(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0.0D);
    }

    public DoubleTally(int paramInt1, int paramInt2, double paramDouble) {
        this.tolerance = paramDouble;
        this.N = 0;
        this.x = new Vector(paramInt1, paramInt2);
        this.freq = new Vector(paramInt1, paramInt2);
    }

    public int addValue(double paramDouble) {
        this.N++;

        int i = this.x.size();
        if (i == 0) {

            addEnd(paramDouble);
            return 0;
        }

        double d1 = ((Double) this.x.firstElement()).doubleValue();
        double d2 = ((Double) this.x.lastElement()).doubleValue();
        if (Math.abs(paramDouble - d1) <= this.tolerance) {
            addExisting(0);
            return 0;
        }
        if (paramDouble < d1) {
            addNew(0, paramDouble);
            return -1;
        }
        if (Math.abs(paramDouble - d2) <= this.tolerance) {
            addExisting(i - 1);
            return i - 1;
        }
        if (paramDouble > d2) {
            addEnd(paramDouble);
            return -1;
        }

        int j = 0, k = i - 1;
        while (k - j > 1) {

            int m = k + j >> 1;
            double d = ((Double) this.x.get(m)).doubleValue();
            if (Math.abs(paramDouble - d) <= this.tolerance) {
                addExisting(m);
                return m;
            }
            if (paramDouble < d) {
                k = m;
                continue;
            }
            j = m;
        }
        double d3 = ((Double) this.x.get(k)).doubleValue();
        if (Math.abs(paramDouble - d3) <= this.tolerance) {
            addExisting(k);
            return k;
        }
        addNew(k, paramDouble);
        return k;
    }

    private void addEnd(double paramDouble) {
        this.x.add(new Double(paramDouble));
        this.freq.add(new Integer(1));
    }

    private void addExisting(int paramInt) {
        int i = ((Integer) this.freq.get(paramInt)).intValue();
        this.freq.set(paramInt, new Integer(++i));
    }

    private void addNew(int paramInt, double paramDouble) {
        this.x.add(paramInt, new Double(paramDouble));
        this.freq.add(paramInt, new Integer(1));
    }

    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            System.out.println("DoubleTally can't clone");
        }
        return object;
    }

    public int getFrequency(int paramInt) {
        return ((Integer) this.freq.get(paramInt)).intValue();
    }

    public double getMax() {
        return ((Double) this.x.lastElement()).doubleValue();
    }

    public int getMaxFreq() {
        int i = 0;

        for (byte b = 0; b < this.freq.size(); b++) {

            int j = ((Integer) this.freq.get(b)).intValue();
            if (j > i) i = j;
        }
        return i;
    }

    public double getMin() {
        return ((Double) this.x.firstElement()).doubleValue();
    }

    public int getN() {
        return this.N;
    }

    public double getProportion(int paramInt) {
        return (this.N > 0) ? (((Integer) this.freq.get(paramInt)).intValue() / this.N) : 0.0D;
    }

    public double getValue(int paramInt) {
        return ((Double) this.x.get(paramInt)).doubleValue();
    }

    public int getValueCount() {
        return this.x.size();
    }

    public int indexOf(double paramDouble) {
        return this.x.indexOf(new Double(paramDouble));
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble = {4.0D, 5.0D, 8.0D, 9.0D, 3.0D, 2.0D, 5.0D, 4.0D, 6.0D, 7.0D, 8.0D, 9.0D, 2.0D, 1.0D, 0.0D, -3.0D, -2.0D, 0.0D, -6.0D, -3.0D};

            DoubleTally doubleTally = new DoubleTally();
            byte b;
            for (b = 0; b < arrayOfDouble.length; ) {
                doubleTally.addValue(arrayOfDouble[b]);
                b++;
            }

            System.out.println("DoubleTally " + doubleTally.getN() + " values, " + "Min = " + doubleTally.getMin() + ", Max = " + doubleTally.getMax());

            for (b = 0; b < doubleTally.getValueCount(); b++) {
                System.out.println(doubleTally.getValue(b) + ", Freq = " + doubleTally.getFrequency(b) + ", p = " + doubleTally.getProportion(b));
            }
            for (b = 0; b < arrayOfDouble.length; b++)
                System.out.println("Index of " + arrayOfDouble[b] + " is " + doubleTally.indexOf(arrayOfDouble[b]));
        }
    }
}

