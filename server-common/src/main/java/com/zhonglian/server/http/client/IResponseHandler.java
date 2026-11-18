package com.zhonglian.server.http.client;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public abstract class IResponseHandler implements FutureCallback<HttpResponse> {
    public abstract void compeleted(String var1);

    public void completed(HttpResponse httpResponse) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            SyncTaskManager.task(() -> this.compeleted(sb.toString()));
        } catch (Exception var5) {
            CommLog.error("read httpresponse EntityString error : ", var5.getMessage(), var5);
        }
    }

    @Override
    public abstract void failed(Exception var1);

    @Override
    public void cancelled() {
        SyncTaskManager.task(() -> this.failed(new IResponseHandler.CancelledException()));
    }

    public class CancelledException extends Exception {
        private static final long serialVersionUID = -421378063733917547L;
    }
}
