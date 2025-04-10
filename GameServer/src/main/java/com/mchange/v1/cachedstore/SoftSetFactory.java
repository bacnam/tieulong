package com.mchange.v1.cachedstore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

public final class SoftSetFactory
{
public static Set createSynchronousCleanupSoftSet() {
final ManualCleanupSoftSet inner = new ManualCleanupSoftSet();
InvocationHandler invocationHandler = new InvocationHandler()
{

public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
{
inner.vacuum();
return param1Method.invoke(inner, param1ArrayOfObject);
}
};
return (Set)Proxy.newProxyInstance(SoftSetFactory.class.getClassLoader(), new Class[] { Set.class }, invocationHandler);
}
}

