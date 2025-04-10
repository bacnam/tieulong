package jsc.descriptive;

import jsc.util.Scale;

public class FrequencyTable
extends AbstractFrequencyTable
implements DoubleFrequencyTable, Cloneable
{
Scale scale;

public FrequencyTable(String paramString, int paramInt, double[] paramArrayOfdouble) {
this(paramString, paramInt, paramArrayOfdouble, false);
}

public FrequencyTable(String paramString, int paramInt, double[] paramArrayOfdouble, boolean paramBoolean) {
super(paramString);
this.numberOfBins = paramInt;

double d1 = Double.POSITIVE_INFINITY;
double d2 = Double.NEGATIVE_INFINITY; byte b;
for (b = 0; b < paramArrayOfdouble.length; b++) {

if (paramArrayOfdouble[b] < d1) d1 = paramArrayOfdouble[b]; 
if (paramArrayOfdouble[b] > d2) d2 = paramArrayOfdouble[b];

} 

this.scale = new Scale(d1, d2, paramInt + 1, true, false);

if (this.scale.getStep() == 0.0D) {
throw new IllegalArgumentException("Constant data");
}
if (paramBoolean) {

double d = 0.5D * this.scale.getStep();
this.scale = new Scale(this.scale.getFirstTickValue() - d, this.scale.getLastTickValue() + d, paramInt, false, false);
} 

this.n = 0;
this.freq = new int[paramInt];
for (b = 0; b < paramInt; ) { this.freq[b] = 0; b++; }
for (b = 0; b < paramArrayOfdouble.length; ) { addValue(paramArrayOfdouble[b]); b++; }
if (this.n < 1) {
throw new IllegalArgumentException("No data values.");
}
}

public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, double paramDouble3) {
super(paramString);
if (paramDouble3 <= 0.0D || paramDouble2 <= paramDouble1) {
throw new IllegalArgumentException("Invalid bins");
}

this.numberOfBins = (int)Math.ceil((paramDouble2 - paramDouble1) / paramDouble3);

this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false, false);

this.n = 0;
this.freq = new int[this.numberOfBins];
for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }

}

public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
super(paramString);
if (paramDouble3 <= 0.0D || paramDouble2 <= paramDouble1) {
throw new IllegalArgumentException("Invalid bins");
}

this.numberOfBins = (int)Math.ceil((paramDouble2 - paramDouble1) / paramDouble3);

this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false, false);

this.n = 0;
this.freq = new int[this.numberOfBins]; byte b;
for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
for (b = 0; b < paramArrayOfdouble.length; ) { addValue(paramArrayOfdouble[b]); b++; }
if (this.n < 1) {
throw new IllegalArgumentException("No data values.");
}
}

public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, int[] paramArrayOfint) {
super(paramString);
if (paramDouble2 <= paramDouble1) {
throw new IllegalArgumentException("Invalid bins");
}

this.numberOfBins = paramArrayOfint.length;

this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false);

this.n = 0;
this.freq = new int[this.numberOfBins];
for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
if (this.n < 1) {
throw new IllegalArgumentException("No data values.");
}
}

public int addValue(double paramDouble) {
if (paramDouble >= this.scale.getFirstTickValue() && paramDouble <= this.scale.getLastTickValue()) {

this.n++;

int i = (int)Math.floor((paramDouble - this.scale.getFirstTickValue()) / this.scale.getStep());

if (i < 0) { i = 0; }
else if (i >= this.numberOfBins) { i = this.numberOfBins - 1; }

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

public double getBinWidth() {
return this.scale.getStep();
}

public double getBoundary(int paramInt) {
return this.scale.getTickValue(paramInt);
}

public Object clone() {
Object object = null; try {
object = super.clone();
} catch (CloneNotSupportedException cloneNotSupportedException) {
System.out.println("FrequencyTable can't clone");
}  return object;
}

public double getMaximumNormalizedFreq() {
int i = getMaximumFreq();
if (i > 0) {

double d = this.n * this.scale.getStep();
if (d > 0.0D) return i / d; 
} 
return 1.0D;
}

public double getNormalizedFrequency(int paramInt) {
double d = this.n * this.scale.getStep();
if (d > 0.0D) {
return this.freq[paramInt] / d;
}
return 0.0D;
}

public Scale getScale() {
return this.scale;
}
public double getScaleMax() {
return this.scale.getLastTickValue();
}
public double getScaleMin() {
return this.scale.getFirstTickValue();
}

public String toString() {
return "FrequencyTable: " + super.toString() + " " + this.scale.toString();
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 39.0D, 41.0D, 41.0D, 41.0D, 41.0D, 43.0D, 43.0D, 45.0D, 45.0D, 48.0D, 69.0D, 83.0D, 83.0D, 83.0D, 83.0D, 91.0D, 179.0D, 238.0D, 241.0D, 253.0D, 47.0D, 274.0D, 280.0D, 394.0D, 501.0D, 503.0D, 509.0D, 511.0D, 513.0D, 515.0D, 518.0D, 518.0D, 520.0D, 522.0D, 522.0D, 522.0D, 525.0D, 527.0D, 527.0D, 527.0D, 529.0D, 529.0D, 531.0D, 531.0D, 533.0D, 535.0D, 538.0D, 538.0D, 541.0D, 543.0D, 545.0D, 547.0D, 547.0D, 549.0D, 549.0D, 549.0D, 549.0D, 551.0D, 553.0D, 555.0D, 555.0D, 555.0D, 557.0D, 560.0D, 560.0D, 562.0D, 564.0D, 568.0D, 570.0D, 572.0D, 574.0D, 576.0D, 579.0D, 581.0D, 583.0D, 585.0D, 578.0D, 578.0D, 589.0D, 590.0D, 592.0D, 592.0D, 596.0D, 598.0D, 598.0D, 598.0D, 301.0D, 601.0D, 606.0D, 609.0D, 611.0D, 615.0D, 617.0D, 617.0D, 619.0D, 621.0D, 623.0D, 623.0D, 626.0D, 626.0D, 628.0D, 630.0D, 630.0D, 630.0D, 630.0D, 630.0D, 632.0D, 634.0D, 636.0D, 638.0D, 640.0D, 640.0D, 642.0D, 644.0D, 644.0D, 646.0D, 648.0D, 648.0D, 648.0D, 650.0D, 652.0D, 655.0D, 657.0D, 659.0D, 661.0D, 661.0D, 663.0D, 665.0D, 670.0D, 673.0D, 675.0D, 677.0D, 679.0D, 681.0D, 683.0D, 909.0D, 911.0D, 907.0D, 555.0D, 555.0D, 692.0D, 694.0D, 657.0D };

FrequencyTable frequencyTable = new FrequencyTable("Table", 9, arrayOfDouble, false);

System.out.println("Frequency table " + frequencyTable.getN() + " values");
for (byte b = 0; b < frequencyTable.getNumberOfBins(); b++)
System.out.println(frequencyTable.getBoundary(b) + " to " + frequencyTable.getBoundary(b + 1) + ", Freq = " + frequencyTable.getFrequency(b) + ", % = " + frequencyTable.getPercentage(b)); 
}
}
}

