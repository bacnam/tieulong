package com.notnoop.apns;

import com.notnoop.apns.internal.Utilities;
import java.util.Arrays;
import java.util.Date;

public class EnhancedApnsNotification
implements ApnsNotification
{
private static final byte COMMAND = 1;
private static int nextId = 0; private final int identifier;
private final int expiry;
private final byte[] deviceToken;
private final byte[] payload;
public static final int MAXIMUM_EXPIRY = 2147483647;

public static int INCREMENT_ID() {
return ++nextId;
}

public static final Date MAXIMUM_DATE = new Date(2147483647000L);

private byte[] marshall;

public EnhancedApnsNotification(int identifier, int expiryTime, String dtoken, String payload)
{
this.marshall = null; this.identifier = identifier; this.expiry = expiryTime; this.deviceToken = Utilities.decodeHex(dtoken); this.payload = Utilities.toUTF8Bytes(payload); } public EnhancedApnsNotification(int identifier, int expiryTime, byte[] dtoken, byte[] payload) { this.marshall = null;
this.identifier = identifier;
this.expiry = expiryTime;
this.deviceToken = Utilities.copyOf(dtoken);
this.payload = Utilities.copyOf(payload); } public byte[] getDeviceToken() {
return Utilities.copyOf(this.deviceToken);
} public byte[] getPayload() {
return Utilities.copyOf(this.payload);
} public byte[] marshall() {
if (this.marshall == null) {
this.marshall = Utilities.marshallEnhanced((byte)1, this.identifier, this.expiry, this.deviceToken, this.payload);
}

return this.marshall;
}
public int getIdentifier() {
return this.identifier;
}

public int getExpiry() {
return this.expiry;
}

public int length() {
int length = 11 + this.deviceToken.length + 2 + this.payload.length;
assert (marshall()).length == length;
return length;
}

public int hashCode() {
return 21 + 31 * this.identifier + 31 * this.expiry + 31 * Arrays.hashCode(this.deviceToken) + 31 * Arrays.hashCode(this.payload);
}

public boolean equals(Object obj) {
if (!(obj instanceof EnhancedApnsNotification))
return false; 
EnhancedApnsNotification o = (EnhancedApnsNotification)obj;
return (this.identifier == o.identifier && this.expiry == o.expiry && Arrays.equals(this.deviceToken, o.deviceToken) && Arrays.equals(this.payload, o.payload));
}

public String toString() {
String payloadString = "???";
try {
payloadString = new String(this.payload, "UTF-8");
} catch (Exception _) {}
return "Message(Id=" + this.identifier + "; Token=" + Utilities.encodeHex(this.deviceToken) + "; Payload=" + payloadString + ")";
}
}

