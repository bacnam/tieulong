package com.mchange.v1.identicator;

public class StrongIdentityIdenticator
implements Identicator
{
public boolean identical(Object paramObject1, Object paramObject2) {
return (paramObject1 == paramObject2);
}
public int hash(Object paramObject) {
return System.identityHashCode(paramObject);
}
}

