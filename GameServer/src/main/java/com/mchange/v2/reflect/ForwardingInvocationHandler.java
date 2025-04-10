package com.mchange.v2.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ForwardingInvocationHandler
implements InvocationHandler
{
Object inner;

public ForwardingInvocationHandler(Object paramObject) {
this.inner = paramObject;
}
public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
return paramMethod.invoke(this.inner, paramArrayOfObject);
}
}

