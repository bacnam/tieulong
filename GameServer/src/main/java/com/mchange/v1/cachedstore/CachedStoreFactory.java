package com.mchange.v1.cachedstore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class CachedStoreFactory
{
public static TweakableCachedStore createNoCleanupCachedStore(CachedStore.Manager paramManager) {
return new NoCleanupCachedStore(paramManager);
}

public static TweakableCachedStore createSoftValueCachedStore(CachedStore.Manager paramManager) {
return new SoftReferenceCachedStore(paramManager);
}

public static TweakableCachedStore createSynchronousCleanupSoftKeyCachedStore(CachedStore.Manager paramManager) {
final ManualCleanupSoftKeyCachedStore inner = new ManualCleanupSoftKeyCachedStore(paramManager);
InvocationHandler invocationHandler = new InvocationHandler()
{

public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
{
inner.vacuum();
return param1Method.invoke(inner, param1ArrayOfObject);
}
};
return (TweakableCachedStore)Proxy.newProxyInstance(CachedStoreFactory.class.getClassLoader(), new Class[] { TweakableCachedStore.class }, invocationHandler);
}

public static TweakableCachedStore createNoCacheCachedStore(CachedStore.Manager paramManager) {
return new NoCacheCachedStore(paramManager);
}

public static WritableCachedStore createDefaultWritableCachedStore(WritableCachedStore.Manager paramManager) {
TweakableCachedStore tweakableCachedStore = createSynchronousCleanupSoftKeyCachedStore(paramManager);
return new SimpleWritableCachedStore(tweakableCachedStore, paramManager);
}

public static WritableCachedStore cacheWritesOnlyWritableCachedStore(WritableCachedStore.Manager paramManager) {
TweakableCachedStore tweakableCachedStore = createNoCacheCachedStore(paramManager);
return new SimpleWritableCachedStore(tweakableCachedStore, paramManager);
}

public static WritableCachedStore createNoCacheWritableCachedStore(WritableCachedStore.Manager paramManager) {
return new NoCacheWritableCachedStore(paramManager);
}
}

