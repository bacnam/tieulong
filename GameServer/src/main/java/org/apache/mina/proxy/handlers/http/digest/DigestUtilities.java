package org.apache.mina.proxy.handlers.http.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.security.sasl.AuthenticationException;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.apache.mina.proxy.utils.StringUtilities;

public class DigestUtilities
{
public static final String SESSION_HA1 = DigestUtilities.class + ".SessionHA1";

private static MessageDigest md5;

static {
try {
md5 = MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException e) {
throw new RuntimeException(e);
} 
}

public static final String[] SUPPORTED_QOPS = new String[] { "auth", "auth-int" };

public static String computeResponseValue(IoSession session, HashMap<String, String> map, String method, String pwd, String charsetName, String body) throws AuthenticationException, UnsupportedEncodingException {
byte[] hA1, hA2, hFinal;
boolean isMD5Sess = "md5-sess".equalsIgnoreCase(StringUtilities.getDirectiveValue(map, "algorithm", false));

if (!isMD5Sess || session.getAttribute(SESSION_HA1) == null) {

StringBuilder stringBuilder = new StringBuilder();
stringBuilder.append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "username", true))).append(':');

String realm = StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "realm", false));
if (realm != null) {
stringBuilder.append(realm);
}

stringBuilder.append(':').append(pwd);

if (isMD5Sess) {
byte[] prehA1;
synchronized (md5) {
md5.reset();
prehA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
} 

stringBuilder = new StringBuilder();
stringBuilder.append(ByteUtilities.asHex(prehA1));
stringBuilder.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "nonce", true)));

stringBuilder.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "cnonce", true)));

synchronized (md5) {
md5.reset();
hA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
} 

session.setAttribute(SESSION_HA1, hA1);
} else {
synchronized (md5) {
md5.reset();
hA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
} 
} 
} else {
hA1 = (byte[])session.getAttribute(SESSION_HA1);
} 

StringBuilder sb = new StringBuilder(method);
sb.append(':');
sb.append(StringUtilities.getDirectiveValue(map, "uri", false));

String qop = StringUtilities.getDirectiveValue(map, "qop", false);
if ("auth-int".equalsIgnoreCase(qop)) {
byte[] hEntity; ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);

synchronized (md5) {
md5.reset();
hEntity = md5.digest(body.getBytes(proxyIoSession.getCharsetName()));
} 
sb.append(':').append(hEntity);
} 

synchronized (md5) {
md5.reset();
hA2 = md5.digest(sb.toString().getBytes(charsetName));
} 

sb = new StringBuilder();
sb.append(ByteUtilities.asHex(hA1));
sb.append(':').append(StringUtilities.getDirectiveValue(map, "nonce", true));
sb.append(":00000001:");

sb.append(StringUtilities.getDirectiveValue(map, "cnonce", true));
sb.append(':').append(qop).append(':');
sb.append(ByteUtilities.asHex(hA2));

synchronized (md5) {
md5.reset();
hFinal = md5.digest(sb.toString().getBytes(charsetName));
} 

return ByteUtilities.asHex(hFinal);
}
}

