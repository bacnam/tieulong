package com.mchange.v2.coalesce;

import java.util.Iterator;
import java.util.Map;

class AbstractStrongCoalescer
implements Coalescer
{
Map coalesced;

AbstractStrongCoalescer(Map paramMap) {
this.coalesced = paramMap;
}

public Object coalesce(Object paramObject) {
Object object = this.coalesced.get(paramObject);
if (object == null) {

this.coalesced.put(paramObject, paramObject);
object = paramObject;
} 
return object;
}

public int countCoalesced() {
return this.coalesced.size();
}
public Iterator iterator() {
return new CoalescerIterator(this.coalesced.keySet().iterator());
}
}

