package com.mchange.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class SmtpMailSender
implements MailSender
{
InetAddress hostAddr;
int port;

public SmtpMailSender(InetAddress paramInetAddress, int paramInt) {
this.hostAddr = paramInetAddress;
this.port = paramInt;
}

public SmtpMailSender(InetAddress paramInetAddress) {
this(paramInetAddress, 25);
}
public SmtpMailSender(String paramString, int paramInt) throws UnknownHostException {
this(InetAddress.getByName(paramString), paramInt);
}
public SmtpMailSender(String paramString) throws UnknownHostException {
this(paramString, 25);
}

public void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3, String paramString4) throws IOException, ProtocolException, UnsupportedEncodingException {
String[] arrayOfString;
if (paramArrayOfString1 == null || paramArrayOfString1.length < 1)
throw new SmtpException("You must specify at least one recipient in the \"to\" field."); 
Properties properties = new Properties();
properties.put("From", paramString1);
properties.put("To", makeRecipientString(paramArrayOfString1));
properties.put("Subject", paramString2);
properties.put("MIME-Version", "1.0");
properties.put("Content-Type", "text/plain; charset=" + MimeUtils.normalEncoding(paramString4));
properties.put("X-Generator", getClass().getName());

if (paramArrayOfString2 != null || paramArrayOfString3 != null) {

int i = paramArrayOfString1.length + ((paramArrayOfString2 != null) ? paramArrayOfString2.length : 0) + ((paramArrayOfString3 != null) ? paramArrayOfString3.length : 0);
arrayOfString = new String[i];
int j = 0;
System.arraycopy(paramArrayOfString1, 0, arrayOfString, j, paramArrayOfString1.length);
j += paramArrayOfString1.length;
if (paramArrayOfString2 != null) {

System.arraycopy(paramArrayOfString2, 0, arrayOfString, j, paramArrayOfString2.length);
j += paramArrayOfString2.length;
properties.put("CC", makeRecipientString(paramArrayOfString2));
} 
if (paramArrayOfString3 != null)
System.arraycopy(paramArrayOfString3, 0, arrayOfString, j, paramArrayOfString3.length); 
} else {
arrayOfString = paramArrayOfString1;
}  SmtpUtils.sendMail(this.hostAddr, this.port, paramString1, arrayOfString, properties, paramString3.getBytes(paramString4));
}

public void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3) throws IOException, ProtocolException {
try {
sendMail(paramString1, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramString2, paramString3, System.getProperty("file.encoding"));
} catch (UnsupportedEncodingException unsupportedEncodingException) {
throw new InternalError("Default encoding [" + System.getProperty("file.encoding") + "] not supported???");
} 
}

private static String makeRecipientString(String[] paramArrayOfString) {
StringBuffer stringBuffer = new StringBuffer(256); byte b; int i;
for (b = 0, i = paramArrayOfString.length; b < i; b++) {

if (b != 0) stringBuffer.append(", "); 
stringBuffer.append(paramArrayOfString[b]);
} 
return stringBuffer.toString();
}

public static void main(String[] paramArrayOfString) {
try {
String[] arrayOfString1 = { "stevewaldman@uky.edu" };
String[] arrayOfString2 = new String[0];
String[] arrayOfString3 = { "stevewaldman@mac.com" };
String str1 = "swaldman@mchange.com";
String str2 = "Test SmtpMailSender Again";
String str3 = "Wheeeee!!!";

SmtpMailSender smtpMailSender = new SmtpMailSender("localhost");
smtpMailSender.sendMail(str1, arrayOfString1, arrayOfString2, arrayOfString3, str2, str3);
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

