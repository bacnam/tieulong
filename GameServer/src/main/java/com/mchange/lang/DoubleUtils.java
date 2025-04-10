package com.mchange.lang;

public final class DoubleUtils
{
public static byte[] byteArrayFromDouble(double paramDouble) {
long l = Double.doubleToLongBits(paramDouble);
return LongUtils.byteArrayFromLong(l);
}

public static double doubleFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
long l = LongUtils.longFromByteArray(paramArrayOfbyte, paramInt);
return Double.longBitsToDouble(l);
}
}

