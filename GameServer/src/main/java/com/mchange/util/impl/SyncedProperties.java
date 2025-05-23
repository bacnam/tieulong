package com.mchange.util.impl;

import com.mchange.io.InputStreamUtils;
import com.mchange.io.OutputStreamUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

public class SyncedProperties
{
private static final String[] SA_TEMPLATE = new String[0];

private static final byte H_START_BYTE = 35;
private static final byte[] H_LF_BYTES;
private static final String ASCII = "8859_1";

static {
try {
H_LF_BYTES = System.getProperty("line.separator", "\r\n").getBytes("8859_1");
} catch (UnsupportedEncodingException unsupportedEncodingException) {
throw new InternalError("Encoding 8859_1 not supported ?!?");
} 
}
Properties props;
byte[] headerBytes;
File file;
long last_mod = -1L;

public SyncedProperties(File paramFile, String paramString) throws IOException {
this(paramFile, makeHeaderBytes(paramString));
}
public SyncedProperties(File paramFile, String[] paramArrayOfString) throws IOException {
this(paramFile, makeHeaderBytes(paramArrayOfString));
}
public SyncedProperties(File paramFile) throws IOException {
this(paramFile, (byte[])null);
}

private SyncedProperties(File paramFile, byte[] paramArrayOfbyte) throws IOException {
if (paramFile.exists()) {

if (!paramFile.isFile())
throw new IOException(paramFile.getPath() + ": Properties file can't be a directory or special file!"); 
if (paramArrayOfbyte == null) {

BufferedReader bufferedReader = null;

try {
bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)));

LinkedList<String> linkedList = new LinkedList();
String str = bufferedReader.readLine();
while (str.trim().equals(""))
str = bufferedReader.readLine(); 
while (str.charAt(0) == '#')
linkedList.add(str.substring(1).trim()); 
paramArrayOfbyte = makeHeaderBytes(linkedList.<String>toArray(SA_TEMPLATE));
} finally {

if (bufferedReader != null) bufferedReader.close();

} 
} 
} 

if (!paramFile.canWrite())
throw new IOException("Can't write to file " + paramFile.getPath()); 
this.props = new Properties();
this.headerBytes = paramArrayOfbyte;
this.file = paramFile;
ensureUpToDate();
}

public synchronized String getProperty(String paramString) throws IOException {
ensureUpToDate();
return this.props.getProperty(paramString);
}

public synchronized String getProperty(String paramString1, String paramString2) throws IOException {
String str = this.props.getProperty(paramString1);
return (str == null) ? paramString2 : str;
}

public synchronized void put(String paramString1, String paramString2) throws IOException {
ensureUpToDate();
this.props.put(paramString1, paramString2);
rewritePropsFile();
}

public synchronized void remove(String paramString) throws IOException {
ensureUpToDate();
this.props.remove(paramString);
rewritePropsFile();
}

public synchronized void clear() throws IOException {
ensureUpToDate();
this.props.clear();
rewritePropsFile();
}

public synchronized boolean contains(String paramString) throws IOException {
ensureUpToDate();
return this.props.contains(paramString);
}

public synchronized boolean containsKey(String paramString) throws IOException {
ensureUpToDate();
return this.props.containsKey(paramString);
}

public synchronized Enumeration elements() throws IOException {
ensureUpToDate();
return this.props.elements();
}

public synchronized Enumeration keys() throws IOException {
ensureUpToDate();
return this.props.keys();
}

public synchronized int size() throws IOException {
ensureUpToDate();
return this.props.size();
}

public synchronized boolean isEmpty() throws IOException {
ensureUpToDate();
return this.props.isEmpty();
}

private synchronized void ensureUpToDate() throws IOException {
long l = this.file.lastModified();
if (l > this.last_mod) {

BufferedInputStream bufferedInputStream = null;

try {
bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
this.props.clear();
this.props.load(bufferedInputStream);
this.last_mod = l;
} finally {

InputStreamUtils.attemptClose(bufferedInputStream);
} 
} 
}

private synchronized void rewritePropsFile() throws IOException {
BufferedOutputStream bufferedOutputStream = null;

try {
bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file));
if (this.headerBytes != null) bufferedOutputStream.write(this.headerBytes); 
this.props.store(bufferedOutputStream, (String)null);
bufferedOutputStream.flush();
this.last_mod = this.file.lastModified();
} finally {

OutputStreamUtils.attemptClose(bufferedOutputStream);
} 
}

private static byte[] makeHeaderBytes(String[] paramArrayOfString) {
try {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); byte b; int i;
for (b = 0, i = paramArrayOfString.length; b < i; b++) {

byteArrayOutputStream.write(35);
byteArrayOutputStream.write(paramArrayOfString[b].getBytes());
byteArrayOutputStream.write(H_LF_BYTES);
} 
return byteArrayOutputStream.toByteArray();
}
catch (IOException iOException) {
throw new InternalError("IOException working with ByteArrayOutputStream?!?");
} 
}

private static byte[] makeHeaderBytes(String paramString) {
try {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
byteArrayOutputStream.write(35);
byteArrayOutputStream.write(paramString.getBytes());
byteArrayOutputStream.write(H_LF_BYTES);
return byteArrayOutputStream.toByteArray();
}
catch (IOException iOException) {
throw new InternalError("IOException working with ByteArrayOutputStream?!?");
} 
}
}

