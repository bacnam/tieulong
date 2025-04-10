package com.mchange.v2.c3p0.stmt;

import java.lang.reflect.Method;
import java.sql.Connection;

final class SimpleStatementCacheKey
extends StatementCacheKey
{
static StatementCacheKey _find(Connection pcon, Method stmtProducingMethod, Object[] args) {
int result_set_type, result_set_concurrency, columnIndexes[];
String[] columnNames;
Integer autogeneratedKeys, resultSetHoldability;
String stmtText = (String)args[0];
boolean is_callable = stmtProducingMethod.getName().equals("prepareCall");

if (args.length == 1) {

result_set_type = 1003;
result_set_concurrency = 1007;
columnIndexes = null;
columnNames = null;
autogeneratedKeys = null;
resultSetHoldability = null;
}
else if (args.length == 2) {

Class[] argTypes = stmtProducingMethod.getParameterTypes();
if (argTypes[1].isArray())
{
Class<?> baseType = argTypes[1].getComponentType();
if (baseType == int.class) {

result_set_type = 1003;
result_set_concurrency = 1007;
columnIndexes = (int[])args[1];
columnNames = null;
autogeneratedKeys = null;
resultSetHoldability = null;
}
else if (baseType == String.class) {

result_set_type = 1003;
result_set_concurrency = 1007;
columnIndexes = null;
columnNames = (String[])args[1];
autogeneratedKeys = null;
resultSetHoldability = null;
} else {

throw new IllegalArgumentException("c3p0 probably needs to be updated for some new JDBC spec! As of JDBC3, we expect two arg statement producing methods where the second arg is either an int, int array, or String array.");

}

}
else
{
result_set_type = 1003;
result_set_concurrency = 1007;
columnIndexes = null;
columnNames = null;
autogeneratedKeys = (Integer)args[1];
resultSetHoldability = null;
}

} else if (args.length == 3) {

result_set_type = ((Integer)args[1]).intValue();
result_set_concurrency = ((Integer)args[2]).intValue();
columnIndexes = null;
columnNames = null;
autogeneratedKeys = null;
resultSetHoldability = null;
}
else if (args.length == 4) {

result_set_type = ((Integer)args[1]).intValue();
result_set_concurrency = ((Integer)args[2]).intValue();
columnIndexes = null;
columnNames = null;
autogeneratedKeys = null;
resultSetHoldability = (Integer)args[3];
} else {

throw new IllegalArgumentException("Unexpected number of args to " + stmtProducingMethod.getName());
} 

return new SimpleStatementCacheKey(pcon, stmtText, is_callable, result_set_type, result_set_concurrency, columnIndexes, columnNames, autogeneratedKeys, resultSetHoldability);
}

SimpleStatementCacheKey(Connection physicalConnection, String stmtText, boolean is_callable, int result_set_type, int result_set_concurrency, int[] columnIndexes, String[] columnNames, Integer autogeneratedKeys, Integer resultSetHoldability) {
super(physicalConnection, stmtText, is_callable, result_set_type, result_set_concurrency, columnIndexes, columnNames, autogeneratedKeys, resultSetHoldability);
}

public boolean equals(Object o) {
return StatementCacheKey.equals(this, o);
}
public int hashCode() {
return StatementCacheKey.hashCode(this);
}
}

