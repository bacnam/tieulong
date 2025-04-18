package com.mchange.v1.identicator;

final class StrongIdHashKey
extends IdHashKey
{
Object keyObj;

public StrongIdHashKey(Object paramObject, Identicator paramIdenticator) {
super(paramIdenticator);
this.keyObj = paramObject;
}

public Object getKeyObj() {
return this.keyObj;
}

public boolean equals(Object paramObject) {
if (paramObject instanceof StrongIdHashKey) {
return this.id.identical(this.keyObj, ((StrongIdHashKey)paramObject).keyObj);
}
return false;
}

public int hashCode() {
return this.id.hash(this.keyObj);
}
}

