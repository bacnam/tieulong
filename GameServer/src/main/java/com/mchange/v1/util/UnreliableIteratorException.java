package com.mchange.v1.util;

import com.mchange.lang.PotentiallySecondaryException;

public class UnreliableIteratorException
extends PotentiallySecondaryException
{
public UnreliableIteratorException(String paramString, Throwable paramThrowable) {
super(paramString, paramThrowable);
}
public UnreliableIteratorException(Throwable paramThrowable) {
super(paramThrowable);
}
public UnreliableIteratorException(String paramString) {
super(paramString);
}

public UnreliableIteratorException() {}
}

