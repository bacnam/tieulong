package com.mchange.v2.c3p0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;

public interface C3P0ProxyStatement
extends Statement
{
public static final Object RAW_STATEMENT = new Object();

Object rawStatementOperation(Method paramMethod, Object paramObject, Object[] paramArrayOfObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;
}

