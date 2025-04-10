package jsc.datastructures;

public class PairedData
{
private int n;
private double[] x;
private double[] y;

public PairedData(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this.n = paramArrayOfdouble1.length;
if (this.n != paramArrayOfdouble2.length)
throw new IllegalArgumentException("Arrays not equal length."); 
if (this.n < 1)
throw new IllegalArgumentException("No data."); 
this.x = new double[this.n];
this.y = new double[this.n];
System.arraycopy(paramArrayOfdouble1, 0, this.x, 0, this.n);
System.arraycopy(paramArrayOfdouble2, 0, this.y, 0, this.n);
}

public PairedData(double[][] paramArrayOfdouble) {
this.n = paramArrayOfdouble.length;
if (this.n < 1)
throw new IllegalArgumentException("No data."); 
this.x = new double[this.n];
this.y = new double[this.n];
for (byte b = 0; b < this.n; b++) {

this.x[b] = paramArrayOfdouble[b][0];
this.y[b] = paramArrayOfdouble[b][1];
} 
}

public double[] differences() {
double[] arrayOfDouble = new double[this.n];
for (byte b = 0; b < this.n; ) { arrayOfDouble[b] = this.x[b] - this.y[b]; b++; }
return arrayOfDouble;
}

public int getN() {
return this.n;
}

public double[] getX() {
return this.x;
}

public double[] getY() {
return this.y;
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer();
stringBuffer.append("\nPaired data");

stringBuffer.append("\nX\tY");
for (byte b = 0; b < getN(); b++) {
stringBuffer.append("\n" + this.x[b] + "\t" + this.y[b]);
}
return stringBuffer.toString();
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D };
double[] arrayOfDouble2 = { 13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D };
PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
double[] arrayOfDouble3 = pairedData.getX();
double[] arrayOfDouble4 = pairedData.getY();
double[] arrayOfDouble5 = pairedData.differences();

for (byte b = 0; b < pairedData.getN(); b++) {
System.out.println(arrayOfDouble3[b] + " " + arrayOfDouble4[b] + " " + arrayOfDouble5[b]);
}
double[][] arrayOfDouble = { { 17.4D, 13.6D }, { 15.7D, 10.1D }, { 12.9D, 10.3D }, { 9.8D, 9.2D }, { 13.4D, 11.1D }, { 18.7D, 20.4D }, { 13.9D, 10.4D }, { 11.0D, 11.4D }, { 5.4D, 4.9D }, { 10.4D, 8.9D }, { 16.4D, 11.2D }, { 5.6D, 4.8D } };

pairedData = new PairedData(arrayOfDouble);
System.out.println(pairedData.toString());
}
}
}

