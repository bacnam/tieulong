package com.mchange.v1.cachedstore;

import java.lang.ref.ReferenceQueue;

class ManualCleanupSoftKeyCachedStore
extends KeyTransformingCachedStore
implements Vacuumable
{
ReferenceQueue queue = new ReferenceQueue();

public ManualCleanupSoftKeyCachedStore(CachedStore.Manager paramManager) {
super(paramManager);
}
protected Object toUserKey(Object paramObject) {
return ((SoftKey)paramObject).get();
}
protected Object toCacheFetchKey(Object paramObject) {
return new SoftKey(paramObject, null);
}
protected Object toCachePutKey(Object paramObject) {
return new SoftKey(paramObject, this.queue);
}

public void vacuum() throws CachedStoreException {
SoftKey softKey;
while ((softKey = (SoftKey)this.queue.poll()) != null)
{

removeByTransformedKey(softKey);
}
}
}

