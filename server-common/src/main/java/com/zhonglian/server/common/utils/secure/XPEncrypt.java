package com.zhonglian.server.common.utils.secure;

import java.io.ByteArrayInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class XPEncrypt {
    public static final int ALIGN = 8;
    public static final int MAGIC = 1164408404;
    private byte[] eData;
    private ByteBuffer eBuffer;
    private int eSize;
    private int key;

    public XPEncrypt(int key) {
        this.key = key;
    }

    public ByteBuffer encrypt(ByteBuffer data) {
        return this.encrypt(data.array());
    }

    public ByteBuffer encrypt(byte[] data) {
        if (data == null) {
            return null;
        } else {
            this.eData = data;
            this.paddingAndMakeBuffer();
            return this.doEncrypt();
        }
    }

    public ByteBuffer decrypt(byte[] data) {
        ByteBuffer dst = ByteBuffer.allocate(data.length);
        ByteBuffer src = ByteBuffer.wrap(data);
        XPEncryptGroup group = new XPEncryptGroup(this.key);
        if (data.length % 8 != 0) {
            return null;
        } else {
            while (src.hasRemaining()) {
                dst.putLong(group.decode(src.getLong()));
            }

            int dataSize = dst.getInt(data.length - 12);
            int sum = 0;
            ((Buffer)dst).rewind();

            while (dst.remaining() > 8) {
                sum += dst.getInt();
            }

            if (dst.getInt(data.length - 8) == 1164408404 && dst.getInt(data.length - 4) == sum) {
                ((Buffer)dst).limit(dataSize);
                ((Buffer)dst).rewind();
                return dst;
            } else {
                return null;
            }
        }
    }

    public ByteArrayInputStream decrypt(ByteArrayInputStream input) throws Exception {
        int length = input.available();
        if (length % 8 != 0) {
            throw new Exception("消息字节流错误，非刚好8位字节流");
        } else {
            ByteBuffer dst = ByteBuffer.allocate(length);
            XPEncryptGroup group = new XPEncryptGroup(this.key);
            byte[] ibuff = new byte[4];

            while (input.available() > 0) {
                input.read(ibuff);
                int hv = this.toInt(ibuff);
                input.read(ibuff);
                int lv = this.toInt(ibuff);
                dst.putLong(group.decode(hv, lv));
            }

            int dataSize = dst.getInt(length - 12);
            int sum = 0;
            ((Buffer)dst).rewind();

            while (dst.remaining() > 8) {
                sum += dst.getInt();
            }

            if (dst.getInt(length - 8) == 1164408404 && dst.getInt(length - 4) == sum) {
                ((Buffer)dst).limit(dataSize);
                ((Buffer)dst).rewind();
                return new ByteArrayInputStream(dst.array(), 0, dataSize);
            } else {
                throw new Exception("消息字节流错误，check sum错误");
            }
        }
    }

    private ByteBuffer doEncrypt() {
        ByteBuffer ret = ByteBuffer.allocate(this.eSize);
        XPEncryptGroup group = new XPEncryptGroup(this.key);
        ((Buffer)this.eBuffer).rewind();

        while (this.eBuffer.hasRemaining()) {
            ret.putLong(group.encode(this.eBuffer.getLong()));
        }

        ((Buffer)ret).rewind();
        return ret;
    }

    private void paddingAndMakeBuffer() {
        int dataSize = this.eData.length;
        int append = 0;
        append = 11 - ((dataSize & 7) + 3 & 7);
        int zeros = append - 4;
        this.eSize = dataSize + append + 8;
        this.eBuffer = ByteBuffer.allocate(this.eSize);
        this.eBuffer.put(this.eData);

        for (int i = 0; i < zeros; i++) {
            this.eBuffer.put((byte)0);
        }

        this.eBuffer.putInt(dataSize);
        ((Buffer)this.eBuffer).rewind();
        int checksum = 0;

        while (this.eBuffer.remaining() > 8) {
            checksum += this.eBuffer.getInt();
        }

        this.eBuffer.putInt(this.eSize - 8, 1164408404);
        this.eBuffer.putInt(this.eSize - 4, checksum);
    }

    private int toInt(byte[] src) {
        return src[3] & 0xFF | (src[2] & 0xFF) << 8 | (src[1] & 0xFF) << 16 | (src[0] & 0xFF) << 24;
    }
}
