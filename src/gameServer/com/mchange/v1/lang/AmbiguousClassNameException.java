package com.mchange.v1.lang;

public class AmbiguousClassNameException
extends Exception
{
AmbiguousClassNameException(String paramString, Class paramClass1, Class paramClass2) {
super(paramString + " could refer either to " + paramClass1.getName() + " or " + paramClass2.getName());
}
}

