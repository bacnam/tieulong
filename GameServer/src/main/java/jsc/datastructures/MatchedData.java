package jsc.datastructures;

import java.util.Vector;
import jsc.util.Arrays;
import jsc.util.Rank;
import jsc.util.Sort;

public class MatchedData
implements Cloneable
{
private int n;
private int blockCount;
private int treatmentCount;
private Vector blockLabels;
private Vector treatmentLabels;
private double[][] data;

public MatchedData(double[] paramArrayOfdouble, String[] paramArrayOfString1, String[] paramArrayOfString2) {
this.n = paramArrayOfdouble.length;
if (this.n < 1)
throw new IllegalArgumentException("No data."); 
if (this.n != paramArrayOfString1.length || this.n != paramArrayOfString2.length) {
throw new IllegalArgumentException("Arrays not equal length.");
}

this.blockLabels = Sort.getLabels(paramArrayOfString1);
this.treatmentLabels = Sort.getLabels(paramArrayOfString2);

this.blockCount = this.blockLabels.size();
this.treatmentCount = this.treatmentLabels.size();
this.data = new double[this.blockCount][this.treatmentCount];
for (byte b = 0; b < this.n; b++) {
this.data[this.blockLabels.indexOf(paramArrayOfString1[b])][this.treatmentLabels.indexOf(paramArrayOfString2[b])] = paramArrayOfdouble[b];
}
}

public MatchedData(double[] paramArrayOfdouble, int[] paramArrayOfint1, int[] paramArrayOfint2) {
this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
}

public MatchedData(double[][] paramArrayOfdouble, String[] paramArrayOfString1, String[] paramArrayOfString2) {
this.blockCount = paramArrayOfString1.length;
this.treatmentCount = paramArrayOfString2.length;

if (this.blockCount < 1)
throw new IllegalArgumentException("No blocks labels."); 
if (this.treatmentCount < 1)
throw new IllegalArgumentException("No treatment labels."); 
if (this.blockCount != paramArrayOfdouble.length)
throw new IllegalArgumentException("Number of block labels and data values do not match.");  byte b;
for (b = 0; b < this.blockCount; b++) {
if ((paramArrayOfdouble[b]).length != this.treatmentCount)
throw new IllegalArgumentException("Number of treatment labels and data values do not match."); 
} 
this.blockLabels = new Vector(this.blockCount);
this.treatmentLabels = new Vector(this.treatmentCount);
for (b = 0; b < this.blockCount; ) { this.blockLabels.add(paramArrayOfString1[b]); b++; }
for (b = 0; b < this.treatmentCount; ) { this.treatmentLabels.add(paramArrayOfString2[b]); b++; }

this.data = paramArrayOfdouble;
this.n = this.blockCount * this.treatmentCount;
}

public MatchedData(double[][] paramArrayOfdouble, int[] paramArrayOfint1, int[] paramArrayOfint2) {
this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
}

public MatchedData(double[][] paramArrayOfdouble) {
this(paramArrayOfdouble, Arrays.sequence(0, paramArrayOfdouble.length - 1), Arrays.sequence(0, (paramArrayOfdouble[0]).length - 1));
} public Object clone() {
return copy();
}

public MatchedData copy() {
String[] arrayOfString1 = new String[this.blockCount];
String[] arrayOfString2 = new String[this.treatmentCount];
double[][] arrayOfDouble = new double[this.blockCount][this.treatmentCount];
byte b;
for (b = 0; b < this.blockCount; ) { arrayOfString1[b] = this.blockLabels.get(b); b++; }
for (b = 0; b < this.treatmentCount; ) { arrayOfString2[b] = this.treatmentLabels.get(b); b++; }
for (b = 0; b < this.blockCount; b++) {
for (byte b1 = 0; b1 < this.treatmentCount; b1++)
arrayOfDouble[b][b1] = this.data[b][b1]; 
} 
return new MatchedData(arrayOfDouble, arrayOfString1, arrayOfString2);
}

public double[][] getData() {
return this.data;
}

public double getDatum(int paramInt1, int paramInt2) {
return this.data[paramInt1][paramInt2];
}

public int getN() {
return this.n;
}

public int getBlockCount() {
return this.blockCount;
}

public double[] getBlockData(String paramString) {
int i = this.blockLabels.indexOf(paramString);
if (i < 0) return null; 
return this.data[i];
}

public double[] getBlockData(int paramInt) {
return this.data[paramInt];
}

public String getBlockLabel(int paramInt) {
return this.blockLabels.get(paramInt);
}

public Vector getBlockLabels() {
return this.blockLabels;
}

public double getBlockMean(int paramInt) {
double d = 0.0D;
for (byte b = 0; b < this.treatmentCount; ) { d += this.data[paramInt][b]; b++; }
return d / this.treatmentCount;
}

public double[] getBlockPackedCopy() {
double[] arrayOfDouble = new double[this.blockCount * this.treatmentCount];
for (byte b = 0; b < this.blockCount; b++) {
for (byte b1 = 0; b1 < this.treatmentCount; b1++) {
arrayOfDouble[b * this.treatmentCount + b1] = this.data[b][b1];
}
} 
return arrayOfDouble;
}

public boolean hasBlockLabel(String paramString) {
return this.blockLabels.contains(paramString);
}

public int indexOfBlock(String paramString) {
return this.blockLabels.indexOf(paramString);
}

public int getTreatmentCount() {
return this.treatmentCount;
}

public double[] getTreatmentData(String paramString) {
int i = this.treatmentLabels.indexOf(paramString);
if (i < 0) return null; 
return getTreatmentData(i);
}

public double[] getTreatmentData(int paramInt) {
double[] arrayOfDouble = new double[this.blockCount];
for (byte b = 0; b < this.blockCount; ) { arrayOfDouble[b] = this.data[b][paramInt]; b++; }
return arrayOfDouble;
}

public String getTreatmentLabel(int paramInt) {
return this.treatmentLabels.get(paramInt);
}

public Vector getTreatmentLabels() {
return this.treatmentLabels;
}

public double getTreatmentMean(int paramInt) {
double d = 0.0D;
for (byte b = 0; b < this.blockCount; ) { d += this.data[b][paramInt]; b++; }
return d / this.blockCount;
}

public double[] getTreatmentPackedCopy() {
double[] arrayOfDouble = new double[this.blockCount * this.treatmentCount];
for (byte b = 0; b < this.blockCount; b++) {
for (byte b1 = 0; b1 < this.treatmentCount; b1++) {
arrayOfDouble[b + b1 * this.blockCount] = this.data[b][b1];
}
} 
return arrayOfDouble;
}

public boolean hasTreatmentLabel(String paramString) {
return this.treatmentLabels.contains(paramString);
}

public int indexOfTreatment(String paramString) {
return this.treatmentLabels.indexOf(paramString);
}

public int rankByBlocks(double paramDouble) {
int i = 0;

for (byte b = 0; b < this.blockCount; b++) {

Rank rank = new Rank(getBlockData(b), paramDouble);
i += rank.getCorrectionFactor1();
for (byte b1 = 0; b1 < this.treatmentCount; b1++)
this.data[b][b1] = rank.getRank(b1); 
} 
return i;
}

public double sweepByBlocks() {
double d = 0.0D;
for (byte b = 0; b < this.blockCount; b++) {

double d1 = getBlockMean(b);
for (byte b1 = 0; b1 < this.treatmentCount; b1++) {

this.data[b][b1] = this.data[b][b1] - d1;
d += this.data[b][b1] * this.data[b][b1];
} 
} 
return d;
}

public double sweepByTreatments() {
double d = 0.0D;
for (byte b = 0; b < this.treatmentCount; b++) {

double d1 = getTreatmentMean(b);
for (byte b1 = 0; b1 < this.blockCount; b1++) {

this.data[b1][b] = this.data[b1][b] - d1;
d += this.data[b1][b] * this.data[b1][b];
} 
} 
return d;
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer();
stringBuffer.append("\nMatched data\n"); byte b1;
for (b1 = 0; b1 < this.treatmentCount; ) { stringBuffer.append("\t" + getTreatmentLabel(b1)); b1++; }
for (byte b2 = 0; b2 < this.blockCount; b2++) {

stringBuffer.append("\n" + getBlockLabel(b2));
for (b1 = 0; b1 < this.treatmentCount; ) { stringBuffer.append("\t" + this.data[b2][b1]); b1++; }

}  stringBuffer.append("\n");
return stringBuffer.toString();
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[][] arrayOfDouble = { { 3.93D, 3.99D, 4.08D }, { 3.78D, 3.96D, 3.94D }, { 3.88D, 3.96D, 4.02D }, { 3.93D, 4.03D, 4.06D }, { 3.84D, 4.1D, 3.94D }, { 3.75D, 4.02D, 4.09D }, { 3.98D, 4.06D, 4.17D }, { 3.84D, 3.92D, 4.12D } };

MatchedData matchedData = new MatchedData(arrayOfDouble);

double d = matchedData.sweepByBlocks();
System.out.print(matchedData.toString());
System.out.print("rss = " + d);
d = matchedData.sweepByTreatments();
System.out.print(matchedData.toString());
System.out.print("rss = " + d);
}
}
}

