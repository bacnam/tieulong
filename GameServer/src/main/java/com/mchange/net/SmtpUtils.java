package com.mchange.net;

import com.mchange.io.OutputStreamUtils;
import com.mchange.io.ReaderUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Properties;

public final class SmtpUtils
{
private static final String ENC = "8859_1";
private static final String CRLF = "\r\n";
private static final String CHARSET = "charset";
private static final int CHARSET_LEN = "charset".length();

public static final int DEFAULT_SMTP_PORT = 25;

public static void sendMail(InetAddress paramInetAddress, int paramInt, String paramString, String[] paramArrayOfString, Properties paramProperties, byte[] paramArrayOfbyte) throws IOException, SmtpException {
Socket socket = null;
DataOutputStream dataOutputStream = null;
BufferedReader bufferedReader = null;

try {
socket = new Socket(paramInetAddress, paramInt);
dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));
ensureResponse(bufferedReader, 200, 300);
dataOutputStream.writeBytes("HELO " + socket.getLocalAddress().getHostName() + "\r\n");

dataOutputStream.flush();
ensureResponse(bufferedReader, 200, 300);
dataOutputStream.writeBytes("MAIL FROM: " + paramString + "\r\n");

dataOutputStream.flush();
ensureResponse(bufferedReader, 200, 300);
for (int i = paramArrayOfString.length; --i >= 0; ) {

dataOutputStream.writeBytes("RCPT TO: " + paramArrayOfString[i] + "\r\n");

dataOutputStream.flush();
ensureResponse(bufferedReader, 200, 300);
} 
dataOutputStream.writeBytes("DATA\r\n");

dataOutputStream.flush();
ensureResponse(bufferedReader, 300, 400);

for (Enumeration<String> enumeration = paramProperties.keys(); enumeration.hasMoreElements(); ) {

String str1 = enumeration.nextElement();
String str2 = paramProperties.getProperty(str1);
dataOutputStream.writeBytes(str1 + ": " + str2 + "\r\n");
} 
dataOutputStream.writeBytes("\r\n");
dataOutputStream.write(paramArrayOfbyte);
dataOutputStream.writeBytes("\r\n.\r\n");
dataOutputStream.flush();
ensureResponse(bufferedReader, 200, 300);
dataOutputStream.writeBytes("QUIT\r\n");
dataOutputStream.flush();
}
catch (UnsupportedEncodingException unsupportedEncodingException) {

unsupportedEncodingException.printStackTrace();
throw new InternalError("8859_1 not supported???");
}
finally {

OutputStreamUtils.attemptClose(dataOutputStream);
ReaderUtils.attemptClose(bufferedReader);
SocketUtils.attemptClose(socket);
} 
}

private static String encodingFromContentType(String paramString) {
int i = paramString.indexOf("charset");
if (i >= 0) {

String str = paramString.substring(i + CHARSET_LEN);
str = str.trim();

if (str.charAt(0) != '=') return encodingFromContentType(str); 
str = str.substring(1).trim();
int j = str.indexOf(';');
if (j >= 0)
str = str.substring(0, j); 
return str;
} 
return null;
}

private static byte[] bytesFromBodyString(String paramString1, String paramString2) throws UnsupportedEncodingException {
ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, paramString2));
printWriter.print(paramString1);
printWriter.flush();
return byteArrayOutputStream.toByteArray();
}

private static void ensureResponse(BufferedReader paramBufferedReader, int paramInt1, int paramInt2) throws IOException, SmtpException {
String str = paramBufferedReader.readLine();

try {
int i = Integer.parseInt(str.substring(0, 3));
for (; str.charAt(3) == '-'; str = paramBufferedReader.readLine());
if (i < paramInt1 || i >= paramInt2) {
throw new SmtpException(i, str);
}
} catch (NumberFormatException numberFormatException) {
throw new SmtpException("Bad SMTP response while mailing document!");
} 
}

public static void main(String[] paramArrayOfString) {
try {
InetAddress inetAddress = InetAddress.getByName("mailhub.mchange.com");
byte b = 25;
String str = "octavia@mchange.com";
String[] arrayOfString = { "swaldman@mchange.com", "sw-lists@mchange.com" };

Properties properties = new Properties();
properties.put("From", "goolash@mchange.com");
properties.put("To", "garbage@mchange.com");
properties.put("Subject", "Test test test AGAIN...");

byte[] arrayOfByte = "This is a test AGAIN! Imagine that!".getBytes("8859_1");

sendMail(inetAddress, b, str, arrayOfString, properties, arrayOfByte);
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

