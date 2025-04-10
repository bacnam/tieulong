package com.mchange.v1.lang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;

public final class Synchronizer
{
public static Object createSynchronizedWrapper(final Object o) {
InvocationHandler invocationHandler = new InvocationHandler()
{

public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
{
synchronized (param1Object) {
return param1Method.invoke(o, param1ArrayOfObject);
}  }
};
Class<?> clazz = o.getClass();
return Proxy.newProxyInstance(clazz.getClassLoader(), recurseFindInterfaces(clazz), invocationHandler);
}

private static Class[] recurseFindInterfaces(Class paramClass) {
HashSet<Class<?>> hashSet = new HashSet();
while (paramClass != null) {

Class[] arrayOfClass1 = paramClass.getInterfaces(); byte b; int i;
for (b = 0, i = arrayOfClass1.length; b < i; b++)
hashSet.add(arrayOfClass1[b]); 
paramClass = paramClass.getSuperclass();
} 
Class[] arrayOfClass = new Class[hashSet.size()];
hashSet.toArray((Class<?>[][])arrayOfClass);
return arrayOfClass;
}
}

