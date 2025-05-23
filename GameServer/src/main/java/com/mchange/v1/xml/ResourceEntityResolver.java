package com.mchange.v1.xml;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResourceEntityResolver
implements EntityResolver
{
ClassLoader cl;
String prefix;

public ResourceEntityResolver(ClassLoader paramClassLoader, String paramString) {
this.cl = paramClassLoader;
this.prefix = paramString;
}

public ResourceEntityResolver(Class paramClass) {
this(paramClass.getClassLoader(), classToPrefix(paramClass));
}

public InputSource resolveEntity(String paramString1, String paramString2) throws SAXException, IOException {
if (paramString2 == null) {
return null;
}
int i = paramString2.lastIndexOf('/');
String str = (i >= 0) ? paramString2.substring(i + 1) : paramString2;
InputStream inputStream = this.cl.getResourceAsStream(this.prefix + str);
return (inputStream == null) ? null : new InputSource(inputStream);
}

private static String classToPrefix(Class paramClass) {
String str1 = paramClass.getName();
int i = str1.lastIndexOf('.');
String str2 = (i > 0) ? str1.substring(0, i) : null;
StringBuffer stringBuffer = new StringBuffer(256);

if (str2 != null) {

stringBuffer.append(str2); byte b; int j;
for (b = 0, j = stringBuffer.length(); b < j; b++) {
if (stringBuffer.charAt(b) == '.') stringBuffer.setCharAt(b, '/'); 
}  stringBuffer.append('/');
} 
return stringBuffer.toString();
}
}

