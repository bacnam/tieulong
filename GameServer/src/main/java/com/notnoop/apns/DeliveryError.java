package com.notnoop.apns;

public enum DeliveryError
{
NO_ERROR(0),
PROCESSING_ERROR(1),
MISSING_DEVICE_TOKEN(2),
MISSING_TOPIC(3),
MISSING_PAYLOAD(4),
INVALID_TOKEN_SIZE(5),
INVALID_TOPIC_SIZE(6),
INVALID_PAYLOAD_SIZE(7),
INVALID_TOKEN(8),

NONE(255),
UNKNOWN(254);
private final byte code;

DeliveryError(int code) {
this.code = (byte)code;
}

public int code() {
return this.code;
}

public static DeliveryError ofCode(int code) {
for (DeliveryError e : values()) {
if (e.code == code) {
return e;
}
} 
return UNKNOWN;
}
}

