package com.mchange.io;

import java.io.InvalidClassException;

public class UnknownStreamClassException
extends InvalidClassException
{
public UnknownStreamClassException(ClassNotFoundException paramClassNotFoundException) {
super(paramClassNotFoundException.getMessage());
}
}

