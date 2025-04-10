package com.mchange.v2.util;

public final class XORShiftRandomUtils
{
public static long nextLong(long paramLong) {
paramLong ^= paramLong << 21L;
paramLong ^= paramLong >>> 35L;
paramLong ^= paramLong << 4L;
return paramLong;
}

public static void main(String[] paramArrayOfString) {
long l = System.currentTimeMillis();
byte b1 = 100;
int[] arrayOfInt = new int[b1];
byte b2;
for (b2 = 0; b2 < 1000000; b2++) {

l = nextLong(l);
arrayOfInt[(int)(Math.abs(l) % b1)] = arrayOfInt[(int)(Math.abs(l) % b1)] + 1;
if (b2 % 10000 == 0)
System.out.println(l); 
} 
for (b2 = 0; b2 < b1; b2++) {

if (b2 != 0) System.out.print(", "); 
System.out.print(b2 + " -> " + arrayOfInt[b2]);
} 
System.out.println();
}
}

