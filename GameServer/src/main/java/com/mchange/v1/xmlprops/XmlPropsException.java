package com.mchange.v1.xmlprops;

import com.mchange.lang.PotentiallySecondaryException;

public class XmlPropsException
extends PotentiallySecondaryException
{
public XmlPropsException(String paramString, Throwable paramThrowable) {
super(paramString, paramThrowable);
}
public XmlPropsException(Throwable paramThrowable) {
super(paramThrowable);
}
public XmlPropsException(String paramString) {
super(paramString);
}

public XmlPropsException() {}
}

