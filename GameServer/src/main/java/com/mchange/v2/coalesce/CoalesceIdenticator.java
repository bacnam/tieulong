package com.mchange.v2.coalesce;

import com.mchange.v1.identicator.Identicator;

class CoalesceIdenticator
implements Identicator
{
CoalesceChecker cc;

CoalesceIdenticator(CoalesceChecker paramCoalesceChecker) {
this.cc = paramCoalesceChecker;
}
public boolean identical(Object paramObject1, Object paramObject2) {
return this.cc.checkCoalesce(paramObject1, paramObject2);
}
public int hash(Object paramObject) {
return this.cc.coalesceHash(paramObject);
}
}

