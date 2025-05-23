package com.mchange.v2.coalesce;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

class AbstractWeakCoalescer
implements Coalescer
{
Map wcoalesced;

AbstractWeakCoalescer(Map paramMap) {
this.wcoalesced = paramMap;
}

public Object coalesce(Object paramObject) {
Object object = null;

WeakReference<Object> weakReference = (WeakReference)this.wcoalesced.get(paramObject);
if (weakReference != null) {
object = weakReference.get();
}
if (object == null) {

this.wcoalesced.put(paramObject, new WeakReference(paramObject));
object = paramObject;
} 
return object;
}

public int countCoalesced() {
return this.wcoalesced.size();
}
public Iterator iterator() {
return new CoalescerIterator(this.wcoalesced.keySet().iterator());
}
}

