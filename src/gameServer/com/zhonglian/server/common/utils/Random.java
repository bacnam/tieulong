package com.zhonglian.server.common.utils;

import java.util.UUID;

public class Random
{
public static int nextInt(int n) {
if (n == 0) {
return 0;
}
int res = Math.abs(UUID.randomUUID().hashCode()) % n;
return res;
}

public static int nextInt(int n, int offset) {
if (n == 0) {
return offset;
}
int res = Math.abs(UUID.randomUUID().hashCode()) % n;
return res + offset;
}

public static boolean nextBoolean() {
return (nextInt(2) == 1);
}

public static byte nextByte() {
return (byte)nextInt(256);
}

public static long nextLong(long n) {
if (n == 0L) {
return 0L;
}
long head = nextInt(2147483647);
long l = nextInt(2147483647);

long dividend = (head << 32L) + l;

long remain = dividend - dividend / n * n;

if (n < 0L) {
return 0L - remain;
}
return remain;
}

public static boolean isTrue(float ratio) {
float fl = (float)Math.random();
if (fl <= ratio)
return true; 
return false;
}
}

