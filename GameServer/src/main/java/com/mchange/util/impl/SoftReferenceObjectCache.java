package com.mchange.util.impl;

import com.mchange.util.ObjectCache;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class SoftReferenceObjectCache
implements ObjectCache
{
Map store = new HashMap<Object, Object>();

public synchronized Object find(Object paramObject) throws Exception {
Reference<Object> reference = (Reference)this.store.get(paramObject);
Object object;
if (reference == null || (object = reference.get()) == null || isDirty(paramObject, object)) {

object = createFromKey(paramObject);
this.store.put(paramObject, new SoftReference(object));
} 
return object;
}

protected boolean isDirty(Object paramObject1, Object paramObject2) {
return false;
}

protected abstract Object createFromKey(Object paramObject) throws Exception;
}

