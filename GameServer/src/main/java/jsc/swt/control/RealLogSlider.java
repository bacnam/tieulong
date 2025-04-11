package jsc.swt.control;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Hashtable;

public class RealLogSlider
        extends JSlider {
    double minVal;
    double logRange;

    public RealLogSlider(double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double[] paramArrayOfdouble, String paramString) {
        super(0);
        if (paramDouble1 <= 0.0D || paramDouble2 <= paramDouble1)
            throw new IllegalArgumentException("Illegal range for logarithmic slider.");
        this.minVal = paramDouble1;
        this.logRange = Math.log(paramDouble2 / paramDouble1);
        setMinimum(0);
        setMaximum(paramInt);
        setValue(getSliderValue(paramDouble3));

        setPaintTicks(false);

        int i = paramArrayOfdouble.length;
        Hashtable hashtable = new Hashtable(i);
        DecimalFormat decimalFormat = new DecimalFormat(paramString);

        for (byte b = 0; b < i; b++) {

            double d = paramArrayOfdouble[b];
            if (d >= paramDouble1 && d <= paramDouble2)
                hashtable.put(new Integer(getSliderValue(d)), new JLabel(decimalFormat.format(d)));
        }
        setLabelTable(hashtable);
        setPaintLabels(true);
    }

    public double getRealValue() {
        return getRealValue(getValue());
    }

    public void setRealValue(double paramDouble) {
        setValue(getSliderValue(paramDouble));
    }

    double getRealValue(int paramInt) {
        return this.minVal * Math.exp(paramInt * this.logRange / getMaximum());
    }

    int getSliderValue(double paramDouble) {
        double d = getMaximum() * Math.log(paramDouble / this.minVal) / this.logRange;
        return (int) Math.round(d);
    }
}

