package org.apache.http.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class URLEncodedUtils
{
public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
private static final char QP_SEP_A = '&';
private static final char QP_SEP_S = ';';
private static final String NAME_VALUE_SEPARATOR = "=";

public static List<NameValuePair> parse(URI uri, String charset) {
String query = uri.getRawQuery();
if (query != null && !query.isEmpty()) {
return parse(query, Charset.forName(charset));
}
return Collections.emptyList();
}

public static List<NameValuePair> parse(HttpEntity entity) throws IOException {
CharArrayBuffer buf;
ContentType contentType = ContentType.get(entity);
if (contentType == null || !contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
return Collections.emptyList();
}
long len = entity.getContentLength();
Args.check((len <= 2147483647L), "HTTP entity is too large");
Charset charset = (contentType.getCharset() != null) ? contentType.getCharset() : HTTP.DEF_CONTENT_CHARSET;
InputStream instream = entity.getContent();
if (instream == null) {
return Collections.emptyList();
}

try {
buf = new CharArrayBuffer((len > 0L) ? (int)len : 1024);
Reader reader = new InputStreamReader(instream, charset);
char[] tmp = new char[1024];
int l;
while ((l = reader.read(tmp)) != -1) {
buf.append(tmp, 0, l);
}
} finally {

instream.close();
} 
if (buf.length() == 0) {
return Collections.emptyList();
}
return parse(buf, charset, new char[] { '&' });
}

public static boolean isEncoded(HttpEntity entity) {
Header h = entity.getContentType();
if (h != null) {
HeaderElement[] elems = h.getElements();
if (elems.length > 0) {
String contentType = elems[0].getName();
return contentType.equalsIgnoreCase("application/x-www-form-urlencoded");
} 
} 
return false;
}

@Deprecated
public static void parse(List<NameValuePair> parameters, Scanner scanner, String charset) {
parse(parameters, scanner, "[&;]", charset);
}

@Deprecated
public static void parse(List<NameValuePair> parameters, Scanner scanner, String parameterSepartorPattern, String charset) {
scanner.useDelimiter(parameterSepartorPattern);
while (scanner.hasNext()) {

String name, value, token = scanner.next();
int i = token.indexOf("=");
if (i != -1) {
name = decodeFormFields(token.substring(0, i).trim(), charset);
value = decodeFormFields(token.substring(i + 1).trim(), charset);
} else {
name = decodeFormFields(token.trim(), charset);
value = null;
} 
parameters.add(new BasicNameValuePair(name, value));
} 
}

public static List<NameValuePair> parse(String s, Charset charset) {
CharArrayBuffer buffer = new CharArrayBuffer(s.length());
buffer.append(s);
return parse(buffer, charset, new char[] { '&', ';' });
}

public static List<NameValuePair> parse(String s, Charset charset, char... separators) {
if (s == null) {
return Collections.emptyList();
}
CharArrayBuffer buffer = new CharArrayBuffer(s.length());
buffer.append(s);
return parse(buffer, charset, separators);
}

public static List<NameValuePair> parse(CharArrayBuffer buf, Charset charset, char... separators) {
Args.notNull(buf, "Char array buffer");
TokenParser tokenParser = TokenParser.INSTANCE;
BitSet delimSet = new BitSet();
for (char separator : separators) {
delimSet.set(separator);
}
ParserCursor cursor = new ParserCursor(0, buf.length());
List<NameValuePair> list = new ArrayList<NameValuePair>();
while (!cursor.atEnd()) {
delimSet.set(61);
String name = tokenParser.parseToken(buf, cursor, delimSet);
String value = null;
if (!cursor.atEnd()) {
int delim = buf.charAt(cursor.getPos());
cursor.updatePos(cursor.getPos() + 1);
if (delim == 61) {
delimSet.clear(61);
value = tokenParser.parseValue(buf, cursor, delimSet);
if (!cursor.atEnd()) {
cursor.updatePos(cursor.getPos() + 1);
}
} 
} 
if (!name.isEmpty()) {
list.add(new BasicNameValuePair(decodeFormFields(name, charset), decodeFormFields(value, charset)));
}
} 

return list;
}

public static String format(List<? extends NameValuePair> parameters, String charset) {
return format(parameters, '&', charset);
}

public static String format(List<? extends NameValuePair> parameters, char parameterSeparator, String charset) {
StringBuilder result = new StringBuilder();
for (NameValuePair parameter : parameters) {
String encodedName = encodeFormFields(parameter.getName(), charset);
String encodedValue = encodeFormFields(parameter.getValue(), charset);
if (result.length() > 0) {
result.append(parameterSeparator);
}
result.append(encodedName);
if (encodedValue != null) {
result.append("=");
result.append(encodedValue);
} 
} 
return result.toString();
}

public static String format(Iterable<? extends NameValuePair> parameters, Charset charset) {
return format(parameters, '&', charset);
}

public static String format(Iterable<? extends NameValuePair> parameters, char parameterSeparator, Charset charset) {
StringBuilder result = new StringBuilder();
for (NameValuePair parameter : parameters) {
String encodedName = encodeFormFields(parameter.getName(), charset);
String encodedValue = encodeFormFields(parameter.getValue(), charset);
if (result.length() > 0) {
result.append(parameterSeparator);
}
result.append(encodedName);
if (encodedValue != null) {
result.append("=");
result.append(encodedValue);
} 
} 
return result.toString();
}

private static final BitSet UNRESERVED = new BitSet(256);

private static final BitSet PUNCT = new BitSet(256);

private static final BitSet USERINFO = new BitSet(256);

private static final BitSet PATHSAFE = new BitSet(256);

private static final BitSet URIC = new BitSet(256);

private static final BitSet RESERVED = new BitSet(256);

private static final BitSet URLENCODER = new BitSet(256);
private static final int RADIX = 16;

static {
int i;
for (i = 97; i <= 122; i++) {
UNRESERVED.set(i);
}
for (i = 65; i <= 90; i++) {
UNRESERVED.set(i);
}

for (i = 48; i <= 57; i++) {
UNRESERVED.set(i);
}
UNRESERVED.set(95);
UNRESERVED.set(45);
UNRESERVED.set(46);
UNRESERVED.set(42);
URLENCODER.or(UNRESERVED);
UNRESERVED.set(33);
UNRESERVED.set(126);
UNRESERVED.set(39);
UNRESERVED.set(40);
UNRESERVED.set(41);

PUNCT.set(44);
PUNCT.set(59);
PUNCT.set(58);
PUNCT.set(36);
PUNCT.set(38);
PUNCT.set(43);
PUNCT.set(61);

USERINFO.or(UNRESERVED);
USERINFO.or(PUNCT);

PATHSAFE.or(UNRESERVED);
PATHSAFE.set(47);
PATHSAFE.set(59);
PATHSAFE.set(58);
PATHSAFE.set(64);
PATHSAFE.set(38);
PATHSAFE.set(61);
PATHSAFE.set(43);
PATHSAFE.set(36);
PATHSAFE.set(44);

RESERVED.set(59);
RESERVED.set(47);
RESERVED.set(63);
RESERVED.set(58);
RESERVED.set(64);
RESERVED.set(38);
RESERVED.set(61);
RESERVED.set(43);
RESERVED.set(36);
RESERVED.set(44);
RESERVED.set(91);
RESERVED.set(93);

URIC.or(RESERVED);
URIC.or(UNRESERVED);
}

private static String urlEncode(String content, Charset charset, BitSet safechars, boolean blankAsPlus) {
if (content == null) {
return null;
}
StringBuilder buf = new StringBuilder();
ByteBuffer bb = charset.encode(content);
while (bb.hasRemaining()) {
int b = bb.get() & 0xFF;
if (safechars.get(b)) {
buf.append((char)b); continue;
}  if (blankAsPlus && b == 32) {
buf.append('+'); continue;
} 
buf.append("%");
char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
buf.append(hex1);
buf.append(hex2);
} 

return buf.toString();
}

private static String urlDecode(String content, Charset charset, boolean plusAsBlank) {
if (content == null) {
return null;
}
ByteBuffer bb = ByteBuffer.allocate(content.length());
CharBuffer cb = CharBuffer.wrap(content);
while (cb.hasRemaining()) {
char c = cb.get();
if (c == '%' && cb.remaining() >= 2) {
char uc = cb.get();
char lc = cb.get();
int u = Character.digit(uc, 16);
int l = Character.digit(lc, 16);
if (u != -1 && l != -1) {
bb.put((byte)((u << 4) + l)); continue;
} 
bb.put((byte)37);
bb.put((byte)uc);
bb.put((byte)lc); continue;
} 
if (plusAsBlank && c == '+') {
bb.put((byte)32); continue;
} 
bb.put((byte)c);
} 

bb.flip();
return charset.decode(bb).toString();
}

private static String decodeFormFields(String content, String charset) {
if (content == null) {
return null;
}
return urlDecode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, true);
}

private static String decodeFormFields(String content, Charset charset) {
if (content == null) {
return null;
}
return urlDecode(content, (charset != null) ? charset : Consts.UTF_8, true);
}

private static String encodeFormFields(String content, String charset) {
if (content == null) {
return null;
}
return urlEncode(content, (charset != null) ? Charset.forName(charset) : Consts.UTF_8, URLENCODER, true);
}

private static String encodeFormFields(String content, Charset charset) {
if (content == null) {
return null;
}
return urlEncode(content, (charset != null) ? charset : Consts.UTF_8, URLENCODER, true);
}

static String encUserInfo(String content, Charset charset) {
return urlEncode(content, charset, USERINFO, false);
}

static String encUric(String content, Charset charset) {
return urlEncode(content, charset, URIC, false);
}

static String encPath(String content, Charset charset) {
return urlEncode(content, charset, PATHSAFE, false);
}
}

