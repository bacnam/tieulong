package org.apache.thrift;

public class EncodingUtils {

    public static final void encodeBigEndian(final int integer, final byte[] buf) {
        encodeBigEndian(integer, buf, 0);
    }

    public static final void encodeBigEndian(final int integer, final byte[] buf, int offset) {
        buf[offset] = (byte) (0xff & (integer >> 24));
        buf[offset + 1] = (byte) (0xff & (integer >> 16));
        buf[offset + 2] = (byte) (0xff & (integer >> 8));
        buf[offset + 3] = (byte) (0xff & (integer));
    }

    public static final int decodeBigEndian(final byte[] buf) {
        return decodeBigEndian(buf, 0);
    }

    public static final int decodeBigEndian(final byte[] buf, int offset) {
        return ((buf[offset] & 0xff) << 24) | ((buf[offset + 1] & 0xff) << 16)
                | ((buf[offset + 2] & 0xff) << 8) | ((buf[offset + 3] & 0xff));
    }

}
