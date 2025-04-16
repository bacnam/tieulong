package com.zhonglian.server.common;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;

public class HeartBeat {
    private static boolean started = false;

    public static void start() {
        if (started) return;

        final String url = Config.GmHeartBeatAddr();
        if (url == null || url.isEmpty() || url.indexOf("http") != 0) return;

        started = true;

        SyncTaskManager.task(new Runnable() {
            @Override
            public void run() {
                try {
                    GMParam param = new GMParam();
                    param.put("server_id", Config.ServerID());
                    param.put("gameid", Config.GameID());
                    param.put("world_id", Integer.getInteger("world_sid", 0));

                    HttpUtils.NotifyGM(url, param);
                } catch (Exception e) {
                    CommLog.error("给GM后台发送心跳包发生错误", e);
                }

                // Đăng ký lại task sau 10 giây
                SyncTaskManager.task(this, 10000);
            }
        }, 10000); // Khởi động sau 10 giây
    }
}