package jsc.util;

import java.util.Vector;

public class Sort
{
public static Vector getLabels(String[] paramArrayOfString) {
int i = paramArrayOfString.length;

String[] arrayOfString = new String[i];
System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, i);

sortStrings(arrayOfString, null, 0, i - 1, true);
Vector vector = new Vector();
for (byte b = 0; b < i; b++) {
if (!vector.contains(arrayOfString[b])) vector.addElement(arrayOfString[b]); 
}  return vector;
}

public static void sort(double[] paramArrayOfdouble, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
if (paramArrayOfdouble == null || paramArrayOfdouble.length < 2) {
return;
}

int i = paramInt1, j = paramInt2;
Double double_ = new Double(paramArrayOfdouble[(paramInt1 + paramInt2) / 2]);
while (true) {
if (paramBoolean) {
for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble[i])) > 0; i++);
for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble[j])) < 0; j--);
} else {
for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble[i])) < 0; i++);
for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble[j])) > 0; j--);
} 
if (i < j) {
double d = paramArrayOfdouble[i]; paramArrayOfdouble[i] = paramArrayOfdouble[j]; paramArrayOfdouble[j] = d;
if (paramArrayOfint != null) { int k = paramArrayOfint[i]; paramArrayOfint[i] = paramArrayOfint[j]; paramArrayOfint[j] = k; }

}  if (i <= j) { i++; j--; }
if (i > j) {
if (paramInt1 < j) sort(paramArrayOfdouble, paramArrayOfint, paramInt1, j, paramBoolean); 
if (i < paramInt2) sort(paramArrayOfdouble, paramArrayOfint, i, paramInt2, paramBoolean);

return;
} 
} 
}

public static void sort(Double[] paramArrayOfDouble, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
if (paramArrayOfDouble == null || paramArrayOfDouble.length < 2) {
return;
}

int i = paramInt1, j = paramInt2;
Double double_ = paramArrayOfDouble[(paramInt1 + paramInt2) / 2];
while (true) {
if (paramBoolean) {
for (; i < paramInt2 && double_.compareTo(paramArrayOfDouble[i]) > 0; i++);
for (; j > paramInt1 && double_.compareTo(paramArrayOfDouble[j]) < 0; j--);
} else {
for (; i < paramInt2 && double_.compareTo(paramArrayOfDouble[i]) < 0; i++);
for (; j > paramInt1 && double_.compareTo(paramArrayOfDouble[j]) > 0; j--);
} 
if (i < j) {
Double double_1 = paramArrayOfDouble[i]; paramArrayOfDouble[i] = paramArrayOfDouble[j]; paramArrayOfDouble[j] = double_1;
if (paramArrayOfint != null) { int k = paramArrayOfint[i]; paramArrayOfint[i] = paramArrayOfint[j]; paramArrayOfint[j] = k; }

}  if (i <= j) { i++; j--; }
if (i > j) {
if (paramInt1 < j) sort(paramArrayOfDouble, paramArrayOfint, paramInt1, j, paramBoolean); 
if (i < paramInt2) sort(paramArrayOfDouble, paramArrayOfint, i, paramInt2, paramBoolean);

return;
} 
} 
}

public static void sortDoubles(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt1, int paramInt2, boolean paramBoolean) {
if (paramArrayOfdouble1 == null || paramArrayOfdouble1.length < 2) {
return;
}

int i = paramInt1, j = paramInt2;
Double double_ = new Double(paramArrayOfdouble1[(paramInt1 + paramInt2) / 2]);
while (true) {
if (paramBoolean) {
for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble1[i])) > 0; i++);
for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble1[j])) < 0; j--);
} else {
for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble1[i])) < 0; i++);
for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble1[j])) > 0; j--);
} 
if (i < j) {
double d = paramArrayOfdouble1[i]; paramArrayOfdouble1[i] = paramArrayOfdouble1[j]; paramArrayOfdouble1[j] = d;
if (paramArrayOfdouble2 != null) { d = paramArrayOfdouble2[i]; paramArrayOfdouble2[i] = paramArrayOfdouble2[j]; paramArrayOfdouble2[j] = d; }

}  if (i <= j) { i++; j--; }
if (i > j) {
if (paramInt1 < j) sortDoubles(paramArrayOfdouble1, paramArrayOfdouble2, paramInt1, j, paramBoolean); 
if (i < paramInt2) sortDoubles(paramArrayOfdouble1, paramArrayOfdouble2, i, paramInt2, paramBoolean);

return;
} 
} 
}

public static void sortInts(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2, boolean paramBoolean) {
if (paramArrayOfint1 == null || paramArrayOfint1.length < 2) {
return;
}

int i = paramInt1, j = paramInt2;
Integer integer = new Integer(paramArrayOfint1[(paramInt1 + paramInt2) / 2]);
while (true) {
if (paramBoolean) {
for (; i < paramInt2 && integer.compareTo(new Integer(paramArrayOfint1[i])) > 0; i++);
for (; j > paramInt1 && integer.compareTo(new Integer(paramArrayOfint1[j])) < 0; j--);
} else {
for (; i < paramInt2 && integer.compareTo(new Integer(paramArrayOfint1[i])) < 0; i++);
for (; j > paramInt1 && integer.compareTo(new Integer(paramArrayOfint1[j])) > 0; j--);
} 
if (i < j) {
int k = paramArrayOfint1[i]; paramArrayOfint1[i] = paramArrayOfint1[j]; paramArrayOfint1[j] = k;
if (paramArrayOfint2 != null) { int m = paramArrayOfint2[i]; paramArrayOfint2[i] = paramArrayOfint2[j]; paramArrayOfint2[j] = m; }

}  if (i <= j) { i++; j--; }
if (i > j) {
if (paramInt1 < j) sortInts(paramArrayOfint1, paramArrayOfint2, paramInt1, j, paramBoolean); 
if (i < paramInt2) sortInts(paramArrayOfint1, paramArrayOfint2, i, paramInt2, paramBoolean);

return;
} 
} 
}

public static void sortStrings(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2, boolean paramBoolean) {
if (paramArrayOfString1 == null || paramArrayOfString1.length < 2) {
return;
}

int i = paramInt1, j = paramInt2;
String str = paramArrayOfString1[(paramInt1 + paramInt2) / 2];
while (true) {
if (paramBoolean) {
for (; i < paramInt2 && compareStrings(str, paramArrayOfString1[i]) > 0; i++);
for (; j > paramInt1 && compareStrings(str, paramArrayOfString1[j]) < 0; j--);
} else {
for (; i < paramInt2 && compareStrings(str, paramArrayOfString1[i]) < 0; i++);
for (; j > paramInt1 && compareStrings(str, paramArrayOfString1[j]) > 0; j--);
} 
if (i < j) {
String str1 = paramArrayOfString1[i]; paramArrayOfString1[i] = paramArrayOfString1[j]; paramArrayOfString1[j] = str1;
if (paramArrayOfString2 != null) { str1 = paramArrayOfString2[i]; paramArrayOfString2[i] = paramArrayOfString2[j]; paramArrayOfString2[j] = str1; }

}  if (i <= j) { i++; j--; }
if (i > j) {
if (paramInt1 < j) sortStrings(paramArrayOfString1, paramArrayOfString2, paramInt1, j, paramBoolean); 
if (i < paramInt2) sortStrings(paramArrayOfString1, paramArrayOfString2, i, paramInt2, paramBoolean);

return;
} 
} 
}

public static int compare(Object paramObject1, Object paramObject2, boolean paramBoolean) {
if (paramObject1 != null && paramObject2 == null) return paramBoolean ? -1 : 1; 
if (paramObject1 == null && paramObject2 == null) return 0; 
if (paramObject1 == null && paramObject2 != null) return paramBoolean ? 1 : -1; 
if (paramObject1 instanceof Double && paramObject2 instanceof Double) {
Double double_1 = (Double)paramObject1, double_2 = (Double)paramObject2; return double_1.compareTo(double_2);
}  if (paramObject1 instanceof Integer && paramObject2 instanceof Integer) {
Integer integer1 = (Integer)paramObject1, integer2 = (Integer)paramObject2; return integer1.compareTo(integer2);
}  return compareStrings(paramObject1.toString(), paramObject2.toString());
}

public static int compareStrings(String paramString1, String paramString2) {
try {
Double double_1 = new Double(Double.parseDouble(paramString1));
Double double_2 = new Double(Double.parseDouble(paramString2));
return double_1.compareTo(double_2);
} catch (NumberFormatException numberFormatException) {

return paramString1.compareToIgnoreCase(paramString2);
} 
}

static class Test
{
public static void main(String[] param1ArrayOfString) {
double[] arrayOfDouble = { 7.0D, 3.0D, -4.0D, 3.0D, 1.0D, 8.0D, 2.0D, 1.0D, 6.0D, -5.0D, -2.0D, 0.0D, 7.0D };
Sort.sortDoubles(arrayOfDouble, null, 0, arrayOfDouble.length - 1, true); byte b;
for (b = 0; b < arrayOfDouble.length; b++)
System.out.println(arrayOfDouble[b] + ", "); 
String[] arrayOfString = { "X-ray", "1211", "0", "Golf", "99", "x-ray", "111", "Hotel", "Alpha", "Zero", "25", "Juliette", "£", "Foxtrot", "Tango", "Romeo", "1111", "Bravo", "November", "Charlie", "*", "-10", "Victor", "10", "romeo", "12", "11", "121", "1", "-100", "-1", "-0" };

Sort.sortStrings(arrayOfString, null, 0, arrayOfString.length - 1, true);
for (b = 0; b < arrayOfString.length; b++)
System.out.println(arrayOfString[b] + ", "); 
}
}
}

