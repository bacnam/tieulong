package com.zhonglian.server.http.server;

import BaseCommon.CommLog;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpResponse {
    private HttpExchange httpExchange;
    private boolean responsed = false;

    public HttpResponse(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void response(String result) {
        this.response(200, result);
    }

    public void response(int code, String result) {
        if (this.responsed) {
            CommLog.error("重复response", new Throwable());
        } else {
            try {
                this.httpExchange.sendResponseHeaders(code, result.getBytes().length);
                OutputStream out = this.httpExchange.getResponseBody();
                out.write(result.getBytes());
                out.flush();
                this.httpExchange.close();
                this.responsed = true;
            } catch (IOException var4) {
                CommLog.error("回写Http数据response时发生错误", (Throwable)var4);
            }
        }
    }

    public void response(File file) {
        if (this.responsed) {
            CommLog.error("重复response", new Throwable());
        } else {
            try {
                try (InputStream input = new FileInputStream(file)) {
                    byte[] byt = new byte[input.available()];
                    input.read(byt);
                    this.httpExchange.sendResponseHeaders(200, byt.length);
                    OutputStream out = this.httpExchange.getResponseBody();
                    out.write(byt);
                    out.flush();
                    this.httpExchange.close();
                }
            } catch (IOException var15) {
                CommLog.error("回写Http数据response时发生错误", (Throwable)var15);
            }
        }
    }

    public void error(int code, String format, Object... param) {
        String msg = String.format(format, param);
        String rep = String.format("{\"state\":%d,\"msg\":\"%s\"}", code, this.encodeString(msg));
        this.response(rep);
        CommLog.error("{}请求处理失败,错误码:{},msg:{}", this.httpExchange.getRequestURI(), code, msg);
    }

    private String encodeString(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(ch);
            }
        }

        return sb.toString();
    }
}
