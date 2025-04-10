package jsc.contingencytables;

import jsc.distributions.ChiSquared;
import jsc.distributions.ExtendedHypergeometric;
import jsc.distributions.Hypergeometric;
import jsc.tests.H1;
import jsc.tests.SignificanceTest;

public class FishersExactTest
implements SignificanceTest
{
private double chiSquared;
private double p1;
private double p1x;
private double midP;
private double SP;
private int testStatistic;

public FishersExactTest(ContingencyTable2x2 paramContingencyTable2x2) {
this(paramContingencyTable2x2, H1.NOT_EQUAL);
}

public FishersExactTest(ContingencyTable2x2 paramContingencyTable2x2, H1 paramH1) {
int n, i1, i2;
double d2, d3;
this.p1x = 0.0D;
Hypergeometric hypergeometric = null;

int i = paramContingencyTable2x2.getFrequency(0, 0);
int j = paramContingencyTable2x2.getFrequency(0, 1);
int k = paramContingencyTable2x2.getFrequency(1, 0);
int m = paramContingencyTable2x2.getFrequency(1, 1);
int i5 = i + j + k + m;

if (i * m > j * k) {

if (j < k) {
n = j; i1 = i; i2 = m; int i7 = k;
} else {
n = k; i1 = m; i2 = i; int i7 = j;
}

}
else if (i < m) {
n = i; i1 = j; i2 = k; int i7 = m;
} else {
n = m; i1 = k; i2 = j; int i7 = i;
} 

this.testStatistic = n;
int i3 = n + i1;
int i4 = n + i2;

double d4 = -1.0D;

if (i5 == 0)
{
throw new IllegalArgumentException("All frequencies are zero.");
}

hypergeometric = new Hypergeometric(i3, i5, i4);
this.p1 = hypergeometric.cdf(n);
this.midP = 0.5D * hypergeometric.pdf(n) + hypergeometric.cdf((n - 1));

if (n > 0) {
d3 = hypergeometric.pdf(n);
} else {
d3 = this.p1;
} 

int i6 = Math.min(n + i1, n + i2);
while (i6 >= n + 1) {

if (hypergeometric.pdf(i6) - d3 > 1.0E-16D) {

d4 = hypergeometric.cdf(i6);
break;
} 
i6--;
} 

if (d4 < 0.0D) {
d2 = 1.0D; this.p1x = 1.0D - this.p1;
} else {
this.p1x = 1.0D - d4; d2 = this.p1 + this.p1x;
} 

double d1 = Math.abs(i * m - j * k) - 0.5D * i5;

this.chiSquared = i5 * d1 * d1 / (i + j) * (k + m) * (i + k) * (j + m);

if (paramH1 == H1.NOT_EQUAL) {
this.SP = Math.min(d2, 1.0D);
} else {

ExtendedHypergeometric extendedHypergeometric = new ExtendedHypergeometric(i + k, j + m, i + j, 1.0D);

if (paramH1 == H1.LESS_THAN) {
this.SP = extendedHypergeometric.cdf(i);
} else {
this.SP = 1.0D - extendedHypergeometric.cdf((i - 1));
} 
} 
}

public double getApproxSP() {
return ChiSquared.upperTailProb(this.chiSquared, 1.0D);
}

public double getChiSquared() {
return this.chiSquared;
}

public double getOneTailedMidP() {
return this.midP;
}

public double getOneTailedSP() {
return this.p1;
}

public double getOppositeTailProb() {
return this.p1x;
}

public double getSP() {
return this.SP;
}

public double getTestStatistic() {
return this.testStatistic;
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
byte b1 = 9;
ContingencyTable2x2[] arrayOfContingencyTable2x2 = new ContingencyTable2x2[b1];
arrayOfContingencyTable2x2[0] = new ContingencyTable2x2(3, 1, 1, 3);
arrayOfContingencyTable2x2[1] = new ContingencyTable2x2(8, 2, 3, 5);
arrayOfContingencyTable2x2[2] = new ContingencyTable2x2(2, 6, 18, 14);
arrayOfContingencyTable2x2[3] = new ContingencyTable2x2(2, 3, 4, 5);
arrayOfContingencyTable2x2[4] = new ContingencyTable2x2(8, 1, 4, 5);

arrayOfContingencyTable2x2[5] = new ContingencyTable2x2(100, 210, 310, 410);
arrayOfContingencyTable2x2[6] = new ContingencyTable2x2(200, 410, 620, 820);
arrayOfContingencyTable2x2[7] = new ContingencyTable2x2(400, 410, 420, 420);
arrayOfContingencyTable2x2[8] = new ContingencyTable2x2(1000, 2101, 3104, 4105);
for (byte b2 = 0; b2 < b1; b2++) {

FishersExactTest fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2]);
System.out.println("\n    One tail = " + fishersExactTest.getOneTailedSP());
System.out.println("      opp.tail = " + fishersExactTest.getOppositeTailProb());
System.out.println("            SP = " + fishersExactTest.getSP());

System.out.println("One tail mid-P = " + fishersExactTest.getOneTailedMidP());

System.out.println("     Approx SP = " + fishersExactTest.getApproxSP());
fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2], H1.LESS_THAN);
System.out.println("\"Less than\" SP = " + fishersExactTest.getSP());
fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2], H1.GREATER_THAN);
System.out.println("\"Greater than\" SP = " + fishersExactTest.getSP());
} 
}
}
}

