package com.mchange.v2.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public final class PropertiesUtils
{
public static int getIntProperty(Properties paramProperties, String paramString, int paramInt) throws NumberFormatException {
String str = paramProperties.getProperty(paramString);
return (str != null) ? Integer.parseInt(str) : paramInt;
}

public static Properties fromString(String paramString1, String paramString2) throws UnsupportedEncodingException {
try {
Properties properties = new Properties();
if (paramString1 != null) {

byte[] arrayOfByte = paramString1.getBytes(paramString2);
properties.load(new ByteArrayInputStream(arrayOfByte));
} 
return properties;
}
catch (UnsupportedEncodingException unsupportedEncodingException) {
throw unsupportedEncodingException;
} catch (IOException iOException) {
throw new Error("Huh? An IOException while working with byte array streams?!", iOException);
} 
}
public static Properties fromString(String paramString) {
try {
return fromString(paramString, "ISO-8859-1");
} catch (UnsupportedEncodingException unsupportedEncodingException) {
throw new Error("Huh? An ISO-8859-1 is an unsupported encoding?!", unsupportedEncodingException);
} 
}

public static String toString(Properties paramProperties, String paramString1, String paramString2) throws UnsupportedEncodingException {
try {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
paramProperties.store(byteArrayOutputStream, paramString1);
byteArrayOutputStream.flush();
return new String(byteArrayOutputStream.toByteArray(), paramString2);
}
catch (UnsupportedEncodingException unsupportedEncodingException) {
throw unsupportedEncodingException;
} catch (IOException iOException) {
throw new Error("Huh? An IOException while working with byte array streams?!", iOException);
} 
}
public static String toString(Properties paramProperties, String paramString) {
try {
return toString(paramProperties, paramString, "ISO-8859-1");
} catch (UnsupportedEncodingException unsupportedEncodingException) {
throw new Error("Huh? An ISO-8859-1 is an unsupported encoding?!", unsupportedEncodingException);
} 
}
}

