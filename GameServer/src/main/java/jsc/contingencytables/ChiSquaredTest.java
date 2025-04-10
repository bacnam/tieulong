package jsc.contingencytables;

import jsc.distributions.ChiSquared;
import jsc.tests.SignificanceTest;

public class ChiSquaredTest
implements SignificanceTest
{
private boolean smallE = false;
private int df;
double chiSquared;
private double SP;
private double[][] E;
private double[][] resids;
private ContingencyTable tableCopy;

public ChiSquaredTest(ContingencyTable paramContingencyTable) {
this.tableCopy = paramContingencyTable.copy();

int i = paramContingencyTable.getColumnCount();
if (i < 2) throw new IllegalArgumentException("Less than 2 columns."); 
int j = paramContingencyTable.getRowCount();
if (j < 2) throw new IllegalArgumentException("Less than 2 rows.");

int[] arrayOfInt1 = paramContingencyTable.getColumnTotals();
int[] arrayOfInt2 = paramContingencyTable.getRowTotals();
double d1 = paramContingencyTable.getN();

this.df = (j - 1) * (i - 1);

double d2 = varpear(d1, j, i, arrayOfInt2, arrayOfInt1);

this.E = new double[j][i];
this.resids = new double[j][i];
int[][] arrayOfInt = paramContingencyTable.getFrequencies();

this.chiSquared = 0.0D;
for (byte b = 0; b < j; b++) {
for (byte b1 = 0; b1 < i; b1++) {

this.E[b][b1] = (arrayOfInt2[b] * arrayOfInt1[b1]) / d1;
if (this.E[b][b1] <= 0.0D)
throw new IllegalArgumentException("An expected frequency is zero."); 
if (this.E[b][b1] < 5.0D && d2 >= (this.df + this.df)) this.smallE = true; 
this.resids[b][b1] = (arrayOfInt[b][b1] - this.E[b][b1]) * (arrayOfInt[b][b1] - this.E[b][b1]) / this.E[b][b1];
this.chiSquared += this.resids[b][b1];
} 
} 
this.SP = ChiSquared.upperTailProb(this.chiSquared, this.df);
}

public ContingencyTable getContingencyTable() {
return this.tableCopy;
}

public int getN() {
return this.tableCopy.getN();
}

public int getRowCount() {
return this.tableCopy.getRowCount();
}

public int[] getRowTotals() {
return this.tableCopy.getRowTotals();
}

public int getColumnCount() {
return this.tableCopy.getColumnCount();
}

public int[] getColumnTotals() {
return this.tableCopy.getColumnTotals();
}

public double[][] getExpectedFrequencies() {
return this.E;
}

public double getExpectedFrequency(int paramInt1, int paramInt2) {
return this.E[paramInt1][paramInt2];
}

public int[][] getObservedFrequencies() {
return this.tableCopy.getFrequencies();
}

public int getObservedFrequency(int paramInt1, int paramInt2) {
return this.tableCopy.getFrequency(paramInt1, paramInt2);
}

public double[][] getResiduals() {
return this.resids;
}

public double getResidual(int paramInt1, int paramInt2) {
return this.resids[paramInt1][paramInt2];
}

public int getDegreesOfFreedom() {
return this.df;
}

public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.chiSquared;
}

public boolean hasSmallExpectedFrequency() {
return this.smallE;
}

private double varpear(double paramDouble, int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
double d5 = 0.0D;

if (paramDouble < 4.0D) return Double.MAX_VALUE;

double d1 = (paramDouble - paramInt1) * (paramInt1 - 1.0D) / (paramDouble - 1.0D);
double d3 = (paramDouble - paramInt2) * (paramInt2 - 1.0D) / (paramDouble - 1.0D); byte b;
for (b = 0; b < paramInt1; b++) {
if (paramArrayOfint1[b] <= 0.0D) {
return Double.MAX_VALUE;
}
d5 += 1.0D / paramArrayOfint1[b];
}  double d2 = (d5 - (paramInt1 * paramInt1) / paramDouble) * paramDouble / (paramDouble - 2.0D);
d5 = 0.0D;
for (b = 0; b < paramInt2; b++) {
if (paramArrayOfint2[b] <= 0.0D) {
return Double.MAX_VALUE;
}
d5 += 1.0D / paramArrayOfint2[b];
}  double d4 = (d5 - (paramInt2 * paramInt2) / paramDouble) * paramDouble / (paramDouble - 2.0D);

return 2.0D * paramDouble / (paramDouble - 3.0D) * (d1 - d2) * (d3 - d4) + paramDouble * paramDouble / (paramDouble - 1.0D) * d2 * d4;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
String[] arrayOfString1 = { "Improved", "Same or worse" };
String[] arrayOfString2 = { "Placebo", "Drug 1", "Drug 2", "Drug 3", "Drug 4", "Drug 5" };
int[][] arrayOfInt = { { 8, 12, 21, 15, 14, 19 }, { 22, 18, 9, 15, 16, 11 } };
ContingencyTable contingencyTable = new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
System.out.println(contingencyTable.toString());
ChiSquaredTest chiSquaredTest = new ChiSquaredTest(contingencyTable);
System.out.println("Chi-squared = " + chiSquaredTest.getTestStatistic() + " SP = " + chiSquaredTest.getSP());

int[] arrayOfInt1 = { 2, 1, 2, 2, 2, 3, 3, 2, 1, 1, 2, 3, 2, 3, 2, 3, 1, 2, 3, 2 };
int[] arrayOfInt2 = { 8, 8, 9, 8, 9, 9, 9, 8, 8, 9, 8, 8, 9, 8, 8, 9, 9, 8, 8, 9 };
contingencyTable = new ContingencyTable(arrayOfInt1, arrayOfInt2);
System.out.println(contingencyTable.toString());
chiSquaredTest = new ChiSquaredTest(contingencyTable);
System.out.println("Chi-squared = " + chiSquaredTest.getTestStatistic() + " SP = " + chiSquaredTest.getSP());
}
}
}

