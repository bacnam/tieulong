package com.notnoop.apns;

import com.notnoop.apns.internal.Utilities;

import java.util.Arrays;

public class SimpleApnsNotification
        implements ApnsNotification {
    private static final byte COMMAND = 0;
    private final byte[] deviceToken;
    private final byte[] payload;
    private byte[] marshall;

    public SimpleApnsNotification(String dtoken, String payload) {
        this.marshall = null;
        this.deviceToken = Utilities.decodeHex(dtoken);
        this.payload = Utilities.toUTF8Bytes(payload);
    }

    public SimpleApnsNotification(byte[] dtoken, byte[] payload) {
        this.marshall = null;
        this.deviceToken = Utilities.copyOf(dtoken);
        this.payload = Utilities.copyOf(payload);
    }

    public byte[] getDeviceToken() {
        return Utilities.copyOf(this.deviceToken);
    }

    public byte[] getPayload() {
        return Utilities.copyOf(this.payload);
    }

    public byte[] marshall() {
        if (this.marshall == null)
            this.marshall = Utilities.marshall((byte) 0, this.deviceToken, this.payload);
        return this.marshall;
    }

    public int length() {
        int length = 3 + this.deviceToken.length + 2 + this.payload.length;
        assert (marshall()).length == length;
        return length;
    }

    public int hashCode() {
        return 21 + 31 * Arrays.hashCode(this.deviceToken) + 31 * Arrays.hashCode(this.payload);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleApnsNotification))
            return false;
        SimpleApnsNotification o = (SimpleApnsNotification) obj;
        return (Arrays.equals(this.deviceToken, o.deviceToken) && Arrays.equals(this.payload, o.payload));
    }

    public int getIdentifier() {
        return -1;
    }

    public int getExpiry() {
        return -1;
    }

    public String toString() {
        String payloadString = "???";
        try {
            payloadString = new String(this.payload, "UTF-8");
        } catch (Exception _) {
        }
        return "Message(Token=" + Utilities.encodeHex(this.deviceToken) + "; Payload=" + payloadString + ")";
    }
}

