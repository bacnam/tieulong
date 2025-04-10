package com.mchange.v1.identicator;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class WeakIdHashKey
extends IdHashKey
{
Ref keyRef;
int hash;

public WeakIdHashKey(Object paramObject, Identicator paramIdenticator, ReferenceQueue paramReferenceQueue) {
super(paramIdenticator);

if (paramObject == null) {
throw new UnsupportedOperationException("Collection does not accept nulls!");
}
this.keyRef = new Ref(paramObject, paramReferenceQueue);
this.hash = paramIdenticator.hash(paramObject);
}

public Ref getInternalRef() {
return this.keyRef;
}
public Object getKeyObj() {
return this.keyRef.get();
}

public boolean equals(Object paramObject) {
if (paramObject instanceof WeakIdHashKey) {

WeakIdHashKey weakIdHashKey = (WeakIdHashKey)paramObject;
if (this.keyRef == weakIdHashKey.keyRef) {
return true;
}

T t1 = this.keyRef.get();
T t2 = weakIdHashKey.keyRef.get();
if (t1 == null || t2 == null) {
return false;
}
return this.id.identical(t1, t2);
} 

return false;
}

public int hashCode() {
return this.hash;
}

class Ref extends WeakReference {
public Ref(Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
super((T)param1Object, param1ReferenceQueue);
}
WeakIdHashKey getKey() {
return WeakIdHashKey.this;
}
}
}

