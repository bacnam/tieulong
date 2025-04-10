package jsc.descriptive;

public class IrregularFrequencyTable
extends AbstractFrequencyTable
implements DoubleFrequencyTable, Cloneable
{
double[] boundaries;

public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
this(paramString, paramArrayOfdouble1);
for (byte b = 0; b < paramArrayOfdouble2.length; ) { addValue(paramArrayOfdouble2[b]); b++; }
if (this.n < 1) throw new IllegalArgumentException("No data values.");

}

public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble) {
super(paramString);
this.numberOfBins = paramArrayOfdouble.length - 1;

this.boundaries = paramArrayOfdouble;

this.n = 0;
this.freq = new int[this.numberOfBins];
for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }

}

public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble, int[] paramArrayOfint) {
this(paramString, paramArrayOfdouble);

if (paramArrayOfint.length != this.numberOfBins) {
throw new IllegalArgumentException("Number of frequencies should be one less than number of boundaries.");
}

for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
if (this.n < 1) throw new IllegalArgumentException("No data values.");

}

public int addValue(double paramDouble) {
int i;
for (i = 0; i < this.numberOfBins; i++) {
if (this.boundaries[i] <= paramDouble && paramDouble < this.boundaries[i + 1]) {
this.n++; this.freq[i] = this.freq[i] + 1; return i;
} 
}  if (paramDouble == this.boundaries[this.numberOfBins]) {

i = this.numberOfBins - 1;
this.n++;
this.freq[i] = this.freq[i] + 1;
return i;
} 
return -1;
}

public int addValues(double[] paramArrayOfdouble) {
byte b1 = 0;
for (byte b2 = 0; b2 < paramArrayOfdouble.length; b2++) {
if (addValue(paramArrayOfdouble[b2]) >= 0) b1++; 
}  return b1;
}

public double getBoundary(int paramInt) {
return this.boundaries[paramInt];
}

public Object clone() {
Object object = null; try {
object = super.clone();
} catch (CloneNotSupportedException cloneNotSupportedException) {
System.out.println("IrregularFrequencyTable can't clone");
}  return object;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble1 = { 72.2D, 64.0D, 53.4D, 76.8D, 86.3D, 58.1D, 63.2D, 73.1D, 78.0D, 44.3D, 85.1D, 66.6D, 80.4D, 76.0D, 68.8D, 76.8D, 58.9D, 58.1D, 74.9D, 72.2D, 73.1D, 39.3D, 52.8D, 54.2D, 65.3D, 74.0D, 63.2D, 64.7D, 68.8D, 85.1D, 62.2D, 76.0D, 70.5D, 48.9D, 78.0D, 66.6D, 58.1D, 32.5D, 63.2D, 64.0D, 68.8D, 65.3D, 71.9D, 72.2D, 63.2D, 72.2D, 70.5D, 80.4D, 45.4D, 59.6D };

double[] arrayOfDouble2 = { 0.0D, 40.0D, 50.0D, 55.0D, 60.0D, 65.0D, 70.0D, 75.0D, 100.0D };
IrregularFrequencyTable irregularFrequencyTable = new IrregularFrequencyTable("Table", arrayOfDouble2, arrayOfDouble1);

int[] arrayOfInt = { 2, 3, 3, 5, 8, 7, 11, 11 };

System.out.println("Frequency table " + irregularFrequencyTable.getN() + " values");
for (byte b = 0; b < irregularFrequencyTable.getNumberOfBins(); b++)
System.out.println(irregularFrequencyTable.getBoundary(b) + " to " + irregularFrequencyTable.getBoundary(b + 1) + ", Freq = " + irregularFrequencyTable.getFrequency(b) + ", % = " + irregularFrequencyTable.getPercentage(b)); 
}
}
}

