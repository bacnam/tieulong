package jsc.swt.virtualgraphics;

public class VPolygon
        extends Path {
    public VPolygon(VPoint[] paramArrayOfVPoint) {
        super(paramArrayOfVPoint.length);
        this.path.moveTo((float) (paramArrayOfVPoint[0]).x, (float) (paramArrayOfVPoint[0]).y);
        for (byte b = 1; b < paramArrayOfVPoint.length; ) {
            this.path.lineTo((float) (paramArrayOfVPoint[b]).x, (float) (paramArrayOfVPoint[b]).y);
            b++;
        }
        this.path.closePath();
    }

    public VPolygon(VPoint[] paramArrayOfVPoint, int paramInt) {
        super(paramInt);
        this.path.moveTo((float) (paramArrayOfVPoint[0]).x, (float) (paramArrayOfVPoint[0]).y);
        for (byte b = 1; b <= paramInt; ) {
            this.path.lineTo((float) (paramArrayOfVPoint[b]).x, (float) (paramArrayOfVPoint[b]).y);
            b++;
        }
        this.path.closePath();
    }
}

