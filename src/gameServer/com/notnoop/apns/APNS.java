package com.notnoop.apns;

public final class APNS
{
private APNS() {
throw new AssertionError("Uninstantiable class");
}

public static PayloadBuilder newPayload() {
return new PayloadBuilder();
}

public static ApnsServiceBuilder newService() {
return new ApnsServiceBuilder();
}
}

