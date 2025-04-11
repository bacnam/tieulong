package jsc.swt.virtualgraphics;

public class PolygonBars
        extends Path {
    public PolygonBars(double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
        super(2 + paramArrayOfdouble.length + paramArrayOfdouble.length);
        int i = paramArrayOfdouble.length;

        float f = (float) paramDouble1;
        this.path.moveTo(f, 0.0F);

        for (byte b = 0; b < i; b++) {

            f = (float) (paramDouble1 + b * paramDouble2);
            float f1 = (float) paramArrayOfdouble[b];
            this.path.lineTo(f, f1);
            this.path.lineTo((float) (f + paramDouble2), f1);
        }
        this.path.lineTo((float) (f + paramDouble2), 0.0F);
        this.path.closePath();
    }
}

