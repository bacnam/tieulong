package com.mchange.v1.cachedstore;

import com.mchange.lang.PotentiallySecondaryError;

public class CachedStoreError
extends PotentiallySecondaryError
{
public CachedStoreError(String paramString, Throwable paramThrowable) {
super(paramString, paramThrowable);
}
public CachedStoreError(Throwable paramThrowable) {
super(paramThrowable);
}
public CachedStoreError(String paramString) {
super(paramString);
}

public CachedStoreError() {}
}

