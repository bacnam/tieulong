package com.mchange.v2.ser;

import java.io.InvalidClassException;

public class UnsupportedVersionException
extends InvalidClassException
{
public UnsupportedVersionException(String paramString) {
super(paramString);
}
public UnsupportedVersionException(Object paramObject, int paramInt) {
this(paramObject.getClass().getName() + " -- unsupported version: " + paramInt);
}
}

