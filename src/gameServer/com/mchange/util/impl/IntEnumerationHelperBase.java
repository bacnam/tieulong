package com.mchange.util.impl;

import com.mchange.util.IntEnumeration;

public abstract class IntEnumerationHelperBase
implements IntEnumeration
{
public abstract boolean hasMoreInts();

public abstract int nextInt();

public final boolean hasMoreElements() {
return hasMoreInts();
}
public final Object nextElement() {
return new Integer(nextInt());
}
}

