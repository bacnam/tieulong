package com.zhonglian.server.http.client;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class IResponseHandler
        implements FutureCallback<HttpResponse> {
    public void completed(HttpResponse httpResponse) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            SyncTaskManager.task(() -> compeleted(paramStringBuilder.toString()));
        } catch (Exception e) {
            CommLog.error("read httpresponse EntityString error : ", e.getMessage(), e);
        }
    }

    public void cancelled() {
        SyncTaskManager.task(() -> failed(new CancelledException()));
    }

    public abstract void compeleted(String paramString);

    public abstract void failed(Exception paramException);

    public class CancelledException
            extends Exception {
        private static final long serialVersionUID = -421378063733917547L;
    }
}

