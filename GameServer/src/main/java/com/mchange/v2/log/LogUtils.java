package com.mchange.v2.log;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class LogUtils
{
public static String createParamsList(Object[] paramArrayOfObject) {
StringBuffer stringBuffer = new StringBuffer(511);
appendParamsList(stringBuffer, paramArrayOfObject);
return stringBuffer.toString();
}

public static void appendParamsList(StringBuffer paramStringBuffer, Object[] paramArrayOfObject) {
paramStringBuffer.append("[params: ");
if (paramArrayOfObject != null) {
byte b; int i;
for (b = 0, i = paramArrayOfObject.length; b < i; b++) {

if (b != 0) paramStringBuffer.append(", "); 
paramStringBuffer.append(paramArrayOfObject[b]);
} 
} 
paramStringBuffer.append(']');
}

public static String createMessage(String paramString1, String paramString2, String paramString3) {
StringBuffer stringBuffer = new StringBuffer(511);
stringBuffer.append("[class: ");
stringBuffer.append(paramString1);
stringBuffer.append("; method: ");
stringBuffer.append(paramString2);
if (!paramString2.endsWith(")"))
stringBuffer.append("()"); 
stringBuffer.append("] ");
stringBuffer.append(paramString3);
return stringBuffer.toString();
}

public static String createMessage(String paramString1, String paramString2) {
StringBuffer stringBuffer = new StringBuffer(511);
stringBuffer.append("[method: ");
stringBuffer.append(paramString1);
if (!paramString1.endsWith(")"))
stringBuffer.append("()"); 
stringBuffer.append("] ");
stringBuffer.append(paramString2);
return stringBuffer.toString();
}

public static String formatMessage(String paramString1, String paramString2, Object[] paramArrayOfObject) {
if (paramString2 == null) {

if (paramArrayOfObject == null) {
return "";
}
return createParamsList(paramArrayOfObject);
} 

if (paramString1 != null) {

ResourceBundle resourceBundle = ResourceBundle.getBundle(paramString1);
if (resourceBundle != null) {

String str = resourceBundle.getString(paramString2);
if (str != null)
paramString2 = str; 
} 
} 
return (paramArrayOfObject == null) ? paramString2 : MessageFormat.format(paramString2, paramArrayOfObject);
}
}

