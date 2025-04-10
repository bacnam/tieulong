package com.mchange.v2.ser;

public final class IndirectPolicy
{
public static final IndirectPolicy DEFINITELY_INDIRECT = new IndirectPolicy("DEFINITELY_INDIRECT");
public static final IndirectPolicy INDIRECT_ON_EXCEPTION = new IndirectPolicy("INDIRECT_ON_EXCEPTION");
public static final IndirectPolicy DEFINITELY_DIRECT = new IndirectPolicy("DEFINITELY_DIRECT");

String name;

private IndirectPolicy(String paramString) {
this.name = paramString;
}
public String toString() {
return "[IndirectPolicy: " + this.name + ']';
}
}

