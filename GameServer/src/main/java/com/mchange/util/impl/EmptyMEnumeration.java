package com.mchange.util.impl;

import com.mchange.util.MEnumeration;
import java.util.NoSuchElementException;

public class EmptyMEnumeration
implements MEnumeration
{
public static MEnumeration SINGLETON = new EmptyMEnumeration();

public Object nextElement() {
throw new NoSuchElementException(); } public boolean hasMoreElements() {
return false;
}
}

