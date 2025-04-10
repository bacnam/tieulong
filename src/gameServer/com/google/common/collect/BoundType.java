package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public enum BoundType
{
OPEN,

CLOSED;

static BoundType forBoolean(boolean inclusive) {
return inclusive ? CLOSED : OPEN;
}
}

