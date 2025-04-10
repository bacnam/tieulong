package com.mchange.v1.identicator;

public class IdentityHashCodeIdenticator
implements Identicator
{
public static IdentityHashCodeIdenticator INSTANCE = new IdentityHashCodeIdenticator();

public boolean identical(Object paramObject1, Object paramObject2) {
return (System.identityHashCode(paramObject1) == System.identityHashCode(paramObject2));
}
public int hash(Object paramObject) {
return System.identityHashCode(paramObject);
}
}

