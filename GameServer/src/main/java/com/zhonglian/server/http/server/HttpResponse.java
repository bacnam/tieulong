package com.zhonglian.server.http.server;

import BaseCommon.CommLog;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse
{
private HttpExchange httpExchange;
private boolean responsed = false;

public HttpResponse(HttpExchange httpExchange) {
this.httpExchange = httpExchange;
}

public void response(String result) {
response(200, result);
}

public void response(int code, String result) {
if (this.responsed) {
CommLog.error("重复response", new Throwable());
return;
} 
try {
this.httpExchange.sendResponseHeaders(code, (result.getBytes()).length);
OutputStream out = this.httpExchange.getResponseBody();
out.write(result.getBytes());
out.flush();
this.httpExchange.close();
this.responsed = true;
} catch (IOException e) {
CommLog.error("回写Http数据response时发生错误", e);
} 
}

public void response(File file) {
if (this.responsed) {
CommLog.error("重复response", new Throwable()); return;
} 
try {
Exception exception2, exception1 = null;

}
catch (IOException e) {
CommLog.error("回写Http数据response时发生错误", e);
} 
}

public void error(int code, String format, Object... param) {
String msg = String.format(format, param);
String rep = String.format("{\"state\":%d,\"msg\":\"%s\"}", new Object[] { Integer.valueOf(code), encodeString(msg) });
response(rep);
CommLog.error("{}请求处理失败,错误码:{},msg:{}", new Object[] { this.httpExchange.getRequestURI(), Integer.valueOf(code), msg });
}

private String encodeString(String str) {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < str.length(); i++) {
char ch = str.charAt(i);
switch (ch) {
case '\\':
sb.append("\\\\");
break;
case '/':
sb.append("\\/");
break;
case '"':
sb.append("\\\"");
break;
case '\t':
sb.append("\\t");
break;
case '\f':
sb.append("\\f");
break;
case '\b':
sb.append("\\b");
break;
case '\n':
sb.append("\\n");
break;
case '\r':
sb.append("\\r");
break;
default:
sb.append(ch);
break;
} 

} 
return sb.toString();
}
}

