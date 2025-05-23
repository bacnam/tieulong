package com.mchange.io.impl;

import com.mchange.io.IOStringEnumeration;
import java.io.IOException;

public abstract class IOStringEnumerationHelperBase
implements IOStringEnumeration
{
public abstract boolean hasMoreStrings() throws IOException;

public abstract String nextString() throws IOException;

public final boolean hasMoreElements() throws IOException {
return hasMoreStrings();
}
public final Object nextElement() throws IOException {
return nextString();
}
}

