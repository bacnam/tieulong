package com.mchange.v1.cachedstore;

import com.mchange.v1.util.WrapperIterator;
import java.lang.ref.ReferenceQueue;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

class ManualCleanupSoftSet
extends AbstractSet
implements Vacuumable
{
HashSet inner = new HashSet();
ReferenceQueue queue = new ReferenceQueue();

public Iterator iterator() {
return (Iterator)new WrapperIterator(this.inner.iterator(), true)
{
protected Object transformObject(Object param1Object)
{
SoftKey softKey = (SoftKey)param1Object;
T t = softKey.get();
return (t == null) ? SKIP_TOKEN : t;
}
};
}

public int size() {
return this.inner.size();
}
public boolean contains(Object paramObject) {
return this.inner.contains(new SoftKey(paramObject, null));
}

private ArrayList toArrayList() {
ArrayList arrayList = new ArrayList(size());
for (Iterator iterator = iterator(); iterator.hasNext();)
arrayList.add(iterator.next()); 
return arrayList;
}

public Object[] toArray() {
return toArrayList().toArray();
}
public Object[] toArray(Object[] paramArrayOfObject) {
return toArrayList().toArray(paramArrayOfObject);
}
public boolean add(Object paramObject) {
return this.inner.add(new SoftKey(paramObject, this.queue));
}
public boolean remove(Object paramObject) {
return this.inner.remove(new SoftKey(paramObject, null));
}
public void clear() {
this.inner.clear();
}

public void vacuum() throws CachedStoreException {
SoftKey softKey;
while ((softKey = (SoftKey)this.queue.poll()) != null)
this.inner.remove(softKey); 
}
}

