package com.mchange.v1.lang;

public final class TVLUtils
{
public static final boolean isDefinitelyTrue(Boolean paramBoolean) {
return (paramBoolean != null && paramBoolean.booleanValue());
}
public static final boolean isDefinitelyFalse(Boolean paramBoolean) {
return (paramBoolean != null && !paramBoolean.booleanValue());
}
public static final boolean isPossiblyTrue(Boolean paramBoolean) {
return (paramBoolean == null || paramBoolean.booleanValue());
}
public static final boolean isPossiblyFalse(Boolean paramBoolean) {
return (paramBoolean == null || !paramBoolean.booleanValue());
}
public static final boolean isUnknown(Boolean paramBoolean) {
return (paramBoolean == null);
}
}

