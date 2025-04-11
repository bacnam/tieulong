package com.zhonglian.server.http.server;

import BaseCommon.CommLog;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private HttpExchange httpExchange;
    private Map<String, String> params = null;
    private Map<String, List<String>> headMap = new HashMap<>();
    private String requestBody = "";

    public HttpRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headMap.putAll(httpExchange.getRequestHeaders());
        this.params = HttpUtils.abstractHttpParams(getReuestURI().getQuery());
        initRequestBody();
    }

    public URI getReuestURI() {
        return this.httpExchange.getRequestURI();
    }

    public String getParam(String key) {
        return this.params.get(key);
    }

    public String getHeader(String key) {
        List<String> head = this.httpExchange.getRequestHeaders().get(key);
        if (head == null || head.size() == 0) {
            return null;
        }
        return head.get(0);
    }

    public List<String> getHeaderList(String key) {
        return this.httpExchange.getRequestHeaders().get(key);
    }

    private void initRequestBody() {
        InputStream in = this.httpExchange.getRequestBody();
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            String line = null;
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            CommLog.error("[HttpRequest]解析http协议body发生错误");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    CommLog.error("[HttpRequest]解析http关闭输入流时错误");
                }
            }
        }
        this.requestBody = builder.toString();
    }

    public String getRequestBody() {
        return this.requestBody;
    }
}

