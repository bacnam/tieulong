package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.C3P0Registry;
import java.io.ObjectStreamException;

public abstract class IdentityTokenResolvable
extends AbstractIdentityTokenized
{
public static Object doResolve(IdentityTokenized itd) {
return C3P0Registry.reregister(itd);
}

protected Object readResolve() throws ObjectStreamException {
Object out = doResolve(this);
verifyResolve(out);

return out;
}

protected void verifyResolve(Object o) throws ObjectStreamException {}
}

