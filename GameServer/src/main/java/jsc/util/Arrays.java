package jsc.util;

import java.text.NumberFormat;
import java.util.Vector;

public class Arrays
{
public static double[] append(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
int i = paramArrayOfdouble1.length;
int j = paramArrayOfdouble2.length;
double[] arrayOfDouble = new double[i + j];
byte b;
for (b = 0; b < j; ) { arrayOfDouble[b] = paramArrayOfdouble2[b]; b++; }
for (b = 0; b < i; ) { arrayOfDouble[b + j] = paramArrayOfdouble1[b]; b++; }
return arrayOfDouble;
}

public static int[] fill(int paramInt1, int paramInt2) {
int[] arrayOfInt = new int[paramInt1];

java.util.Arrays.fill(arrayOfInt, paramInt2);
return arrayOfInt;
}

public static boolean isConstant(int[] paramArrayOfint) {
int i = paramArrayOfint[0];
for (byte b = 1; b < paramArrayOfint.length; ) { if (paramArrayOfint[b] != i) return false;  b++; }
return true;
}

public static boolean isConstant(double[] paramArrayOfdouble) {
double d = paramArrayOfdouble[0];
for (byte b = 1; b < paramArrayOfdouble.length; ) { if (paramArrayOfdouble[b] != d) return false;  b++; }
return true;
}

public static boolean isIntegers(double[] paramArrayOfdouble) {
for (byte b = 0; b < paramArrayOfdouble.length; ) { if (Math.rint(paramArrayOfdouble[b]) != paramArrayOfdouble[b]) return false;  b++; }
return true;
}

public static double[] log(double[] paramArrayOfdouble) {
int i = paramArrayOfdouble.length;
double[] arrayOfDouble = new double[i];
for (byte b = 0; b < i; b++) {

if (paramArrayOfdouble[b] <= 0.0D)
throw new IllegalArgumentException("Element " + b + " not greater than zero."); 
arrayOfDouble[b] = Math.log(paramArrayOfdouble[b]);
} 
return arrayOfDouble;
}

public static double max(double[] paramArrayOfdouble) {
double d = paramArrayOfdouble[0];
for (byte b = 1; b < paramArrayOfdouble.length; b++) {
if (paramArrayOfdouble[b] > d) d = paramArrayOfdouble[b]; 
}  return d;
}

public static double min(double[] paramArrayOfdouble) {
double d = paramArrayOfdouble[0];
for (byte b = 1; b < paramArrayOfdouble.length; b++) {
if (paramArrayOfdouble[b] < d) d = paramArrayOfdouble[b]; 
}  return d;
}

public static long product(int[] paramArrayOfint) {
long l = paramArrayOfint[0];
for (byte b = 1; b < paramArrayOfint.length; ) { l *= paramArrayOfint[b]; b++; }
return l;
}

public static int[] sequence(int paramInt) {
return sequence(0, paramInt - 1);
}

public static int[] sequence(int paramInt1, int paramInt2) {
int[] arrayOfInt = new int[paramInt2 - paramInt1 + 1];
for (int i = paramInt1; i <= paramInt2; ) { arrayOfInt[i - paramInt1] = i; i++; }
return arrayOfInt;
}

public static double[] subtract(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
int i = paramArrayOfdouble1.length;
if (i != paramArrayOfdouble2.length)
throw new IllegalArgumentException("Arrays not equal length."); 
double[] arrayOfDouble = new double[i];
for (byte b = 0; b < i; ) { arrayOfDouble[b] = paramArrayOfdouble1[b] - paramArrayOfdouble2[b]; b++; }
return arrayOfDouble;
}

public static long sum(int[] paramArrayOfint) {
long l = paramArrayOfint[0];
for (byte b = 1; b < paramArrayOfint.length; ) { l += paramArrayOfint[b]; b++; }
return l;
}

public static double sum(double[] paramArrayOfdouble) {
double d = paramArrayOfdouble[0];
for (byte b = 1; b < paramArrayOfdouble.length; ) { d += paramArrayOfdouble[b]; b++; }
return d;
}

public static String[] toStringArray(int[] paramArrayOfint) {
String[] arrayOfString = new String[paramArrayOfint.length];

NumberFormat numberFormat = NumberFormat.getNumberInstance();
for (byte b = 0; b < paramArrayOfint.length; b++)
arrayOfString[b] = numberFormat.format(paramArrayOfint[b]); 
return arrayOfString;
}

public static String[] toStringArray(Vector paramVector) {
String[] arrayOfString = new String[paramVector.size()];
return (String[])paramVector.toArray((Object[])arrayOfString);
}
}

