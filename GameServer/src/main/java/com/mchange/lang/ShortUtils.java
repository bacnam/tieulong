package com.mchange.lang;

public class ShortUtils {
    public static final int UNSIGNED_MAX_VALUE = 65535;

    public static int shortFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
        int i = 0;
        i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 8;
        i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 0;
        return (short) i;
    }

    public static byte[] byteArrayFromShort(short paramShort) {
        byte[] arrayOfByte = new byte[2];
        shortIntoByteArray(paramShort, 0, arrayOfByte);
        return arrayOfByte;
    }

    public static void shortIntoByteArray(short paramShort, int paramInt, byte[] paramArrayOfbyte) {
        paramArrayOfbyte[paramInt + 0] = (byte) (paramShort >>> 8 & 0xFF);
        paramArrayOfbyte[paramInt + 1] = (byte) (paramShort >>> 0 & 0xFF);
    }

    public static int toUnsigned(short paramShort) {
        return (paramShort < 0) ? (65536 + paramShort) : paramShort;
    }
}

