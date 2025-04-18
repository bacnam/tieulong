package com.mchange.v1.util;

import java.util.ArrayList;

public abstract class NumberedObjectCache
{
ArrayList al = new ArrayList();

public Object getObject(int paramInt) throws Exception {
Object object = null;
int i = paramInt + 1;
if (i > this.al.size()) {

this.al.ensureCapacity(i * 2);
for (int j = this.al.size(), k = i * 2; j < k; j++)
this.al.add(null); 
object = addToCache(paramInt);
}
else {

object = this.al.get(paramInt);
if (object == null)
object = addToCache(paramInt); 
} 
return object;
}

private Object addToCache(int paramInt) throws Exception {
Object object = findObject(paramInt);
this.al.set(paramInt, object);
return object;
}

protected abstract Object findObject(int paramInt) throws Exception;
}

