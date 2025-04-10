package com.jolbox.bonecp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PoolUtil
{
public static String fillLogParams(String sql, Map<Object, Object> logParams) {
StringBuilder result = new StringBuilder();
Map<Object, Object> tmpLogParam = (logParams == null) ? new HashMap<Object, Object>() : logParams;

Iterator<Object> it = tmpLogParam.values().iterator();
boolean inQuote = false;
boolean inQuote2 = false;
char[] sqlChar = (sql != null) ? sql.toCharArray() : new char[0];

for (int i = 0; i < sqlChar.length; i++) {
if (sqlChar[i] == '\'') {
inQuote = !inQuote;
}
if (sqlChar[i] == '"') {
inQuote2 = !inQuote2;
}

if (sqlChar[i] == '?' && !inQuote && !inQuote2) {
if (it.hasNext()) {
result.append(prettyPrint(it.next()));
} else {
result.append('?');
} 
} else {
result.append(sqlChar[i]);
} 
} 

return result.toString();
}

protected static String safePrint(Object... o) {
StringBuilder sb = new StringBuilder();
for (Object obj : o) {
sb.append((obj != null) ? obj : "null");
}
return sb.toString();
}

protected static String prettyPrint(Object obj) {
StringBuilder sb = new StringBuilder();
if (obj == null) {
sb.append("NULL");
} else if (obj instanceof Blob) {
sb.append(formatLogParam((Blob)obj));
} else if (obj instanceof Clob) {
sb.append(formatLogParam((Clob)obj));
} else if (obj instanceof Ref) {
sb.append(formatLogParam((Ref)obj));
} else if (obj instanceof Array) {
sb.append(formatLogParam((Array)obj));
} else if (obj instanceof String) {
sb.append("'" + obj.toString() + "'");
} else {
sb.append(obj.toString());
} 
return sb.toString();
}

private static String formatLogParam(Blob obj) {
String result = "";
try {
result = "(blob of length " + obj.length() + ")";
} catch (SQLException e) {
result = "(blob of unknown length)";
} 
return result;
}

private static String formatLogParam(Clob obj) {
String result = "";
try {
result = "(cblob of length " + obj.length() + ")";
} catch (SQLException e) {
result = "(cblob of unknown length)";
} 
return result;
}

private static String formatLogParam(Array obj) {
String result = "";
try {
result = "(array of type" + obj.getBaseTypeName().length() + ")";
} catch (SQLException e) {
result = "(array of unknown type)";
} 
return result;
}

private static String formatLogParam(Ref obj) {
String result = "";
try {
result = "(ref of type" + obj.getBaseTypeName().length() + ")";
} catch (SQLException e) {
result = "(ref of unknown type)";
} 
return result;
}

public static String stringifyException(Throwable t) {
StringWriter sw = new StringWriter();
PrintWriter pw = new PrintWriter(sw);
t.printStackTrace(pw);
String result = "";

result = "------\r\n" + sw.toString() + "------\r\n";

return result;
}
}

