package com.mchange.v1.lang;

public final class NullUtils
{
public static boolean equalsOrBothNull(Object paramObject1, Object paramObject2) {
if (paramObject1 == paramObject2)
return true; 
if (paramObject1 == null) {
return false;
}
return paramObject1.equals(paramObject2);
}
}

