package com.mchange.v1.cachedstore;

import java.lang.ref.SoftReference;

class SoftReferenceCachedStore
extends ValueTransformingCachedStore
{
public SoftReferenceCachedStore(CachedStore.Manager paramManager) {
super(paramManager);
}
protected Object toUserValue(Object paramObject) {
return (paramObject == null) ? null : ((SoftReference)paramObject).get();
}
protected Object toCacheValue(Object paramObject) {
return (paramObject == null) ? null : new SoftReference(paramObject);
}
}

