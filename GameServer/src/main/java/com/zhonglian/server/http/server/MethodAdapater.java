package com.zhonglian.server.http.server;

import java.util.HashMap;
import java.util.Map;

public class MethodAdapater {
    private Map<String, HttpAdaperter> requests = new HashMap<>();

    public HttpAdaperter getAdaperter(String path) {
        return this.requests.get(path);
    }

    public void addAdapter(String path, HttpAdaperter httpAdaperter) {
        this.requests.put(path, httpAdaperter);
    }
}

