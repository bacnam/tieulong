package com.zhonglian.server.http.server;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zhonglian.server.http.annotation.RequestMapping;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpDispather implements HttpHandler {
    private Map<HttpMethod, MethodAdapater> methodAdapaters = new HashMap<>();

    public void init(String pack) throws Exception {
        for (Class<?> cs : CommClass.getClasses(pack)) {
            Object instance = null;

            Method[] var9;
            for (Method method : var9 = cs.getMethods()) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (mapping != null) {
                    Class[] params = method.getParameterTypes();
                    if (params.length != 2) {
                        throw new IllegalArgumentException("[" + method.getName() + "]不是固定2个参数");
                    }

                    if (params[0] != HttpRequest.class) {
                        throw new IllegalArgumentException("[" + method.getName() + "]第一个参数不是HttpRequest");
                    }

                    if (params[1] != HttpResponse.class) {
                        throw new IllegalArgumentException("[" + method.getName() + "]第二个参数不是HttpResponse");
                    }

                    if (instance == null) {
                        instance = CommClass.forName(cs.getName()).newInstance();
                    }

                    HttpMethod[] var15;
                    for (HttpMethod httpMethod : var15 = mapping.method()) {
                        if (this.methodAdapaters.get(httpMethod) == null) {
                            this.methodAdapaters.put(httpMethod, new MethodAdapater());
                        }

                        this.methodAdapaters.get(httpMethod).addAdapter(mapping.uri(), new HttpAdaperter(instance, method));
                    }
                }
            }
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpResponse response = new HttpResponse(exchange);
        SyncTaskManager.task(() -> {
            try {
                HttpMethod method = null;

                try {
                    String methodname = exchange.getRequestMethod().trim().toUpperCase();
                    method = HttpMethod.valueOf(methodname);
                } catch (Exception var7) {
                    method = HttpMethod.GET;
                }

                MethodAdapater methodAdapater = this.methodAdapaters.get(method);
                if (methodAdapater == null) {
                    response.response(404, "File Not Found");
                    return;
                }

                HttpAdaperter adaperter = methodAdapater.getAdaperter(exchange.getRequestURI().getPath());
                if (adaperter == null) {
                    response.response(404, "File Not Found");
                    return;
                }

                HttpRequest request = new HttpRequest(exchange);
                CommLog.info("[HttpRequest]url:{},Body:{}", request.getReuestURI(), request.getRequestBody());
                adaperter.invoke(request, response);
            } catch (Exception var8) {
                Throwable cause = var8.getCause();
                if (cause != null && cause instanceof RequestException) {
                    RequestException re = (RequestException)cause;
                    response.error(re.getCode(), re.getMessage());
                } else {
                    CommLog.error("{}请求处理失败,异常：", exchange.getRequestURI().getPath(), cause);
                    response.error(300001, "服务器发生未知错误，错误信息：%s", var8.getMessage());
                }
            }
        });
    }
}
