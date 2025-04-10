package com.zhonglian.server.http.client;

import BaseCommon.CommLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpSyncClient
{
public static String sendGet(String url, String param) {
String result = "";
BufferedReader in = null;
try {
String urlNameString = String.valueOf(url) + "?" + param;
URL realUrl = new URL(urlNameString);

URLConnection connection = realUrl.openConnection();

connection.setRequestProperty("accept", "*connection.setRequestProperty("connection", "Keep-Alive");
connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

connection.connect();

in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
String line;
while ((line = in.readLine()) != null) {
result = String.valueOf(result) + line;
}
} catch (Exception e) {
CommLog.error("发送GET请求[{}]出现异常！", url, e);
} finally {
try {
if (in != null) {
in.close();
}
} catch (Exception exception) {}
} 

return result;
}

public static String sendPost(String url, String param) {
PrintWriter out = null;
BufferedReader in = null;
String result = "";
try {
URL realUrl = new URL(url);

URLConnection conn = realUrl.openConnection();

conn.setRequestProperty("accept", "*conn.setRequestProperty("connection", "Keep-Alive");
conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

conn.setDoOutput(true);
conn.setDoInput(true);

out = new PrintWriter(conn.getOutputStream());

out.print(param);

out.flush();

in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
String line;
while ((line = in.readLine()) != null) {
result = String.valueOf(result) + line;
}
} catch (Exception e) {
CommLog.error("发送 POST请求[{}]出现异常！", url, e);
} finally {
try {
if (out != null) {
out.close();
}
if (in != null) {
in.close();
}
} catch (IOException iOException) {}
} 

return result;
}

public static String sendPost(String url, Map<String, Object> param) {
StringBuilder sBuilder = new StringBuilder();
for (Map.Entry<String, Object> pair : param.entrySet()) {
try {
sBuilder.append(pair.getKey()).append('=').append(URLEncoder.encode(pair.getValue().toString(), "utf-8")).append('&');
} catch (UnsupportedEncodingException unsupportedEncodingException) {}
} 

if (sBuilder.length() > 0) {
sBuilder.deleteCharAt(sBuilder.length() - 1);
}
return sendPost(url, sBuilder.toString());
}
}

