package com.mchange.v2.coalesce;

public class CoalesceTest
{
static final int NUM_ITERS = 10000;
static final Coalescer c = CoalescerFactory.createCoalescer(null, true, true);

public static void main(String[] paramArrayOfString) {
doTest();
System.gc();
System.err.println("num coalesced after gc: " + c.countCoalesced());
}

private static void doTest() {
String[] arrayOfString = new String[10000];
for (byte b1 = 0; b1 < '✐'; b1++) {
arrayOfString[b1] = new String("Hello");
}
long l1 = System.currentTimeMillis();
for (byte b2 = 0; b2 < '✐'; b2++) {

String str = arrayOfString[b2];
Object object = c.coalesce(str);
} 

long l2 = System.currentTimeMillis() - l1;
System.out.println("avg time: " + ((float)l2 / 10000.0F) + "ms (" + '✐' + " iterations)");

System.err.println("num coalesced: " + c.countCoalesced());
}
}

