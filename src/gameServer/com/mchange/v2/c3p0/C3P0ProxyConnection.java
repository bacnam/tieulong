package com.mchange.v2.c3p0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public interface C3P0ProxyConnection
extends Connection
{
public static final Object RAW_CONNECTION = new Object();

Object rawConnectionOperation(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;
}

