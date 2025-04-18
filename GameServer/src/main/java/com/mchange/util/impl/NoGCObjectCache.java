package com.mchange.util.impl;

import com.mchange.util.ObjectCache;
import java.util.Hashtable;

public abstract class NoGCObjectCache
implements ObjectCache
{
Hashtable store = new Hashtable<Object, Object>();

public Object find(Object paramObject) throws Exception {
Object object = this.store.get(paramObject);
if (object == null || isDirty(paramObject, object)) {

object = createFromKey(paramObject);
this.store.put(paramObject, object);
} 
return object;
}

protected boolean isDirty(Object paramObject1, Object paramObject2) {
return false;
}

protected abstract Object createFromKey(Object paramObject) throws Exception;
}

