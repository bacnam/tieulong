package com.zhonglian.server.common;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;

public class HeartBeat {
    private static boolean started = false;

    public static void start() {
        if (!started) {
            final String url = Config.GmHeartBeatAddr();
            if (url != null && !url.isEmpty() && url.indexOf("//////") == -1) {
                SyncTaskManager.task(new SyncTask() {
                    @Override
                    public void run() {
                        try {
                            GMParam param = new GMParam();
                            param.put("server_id", Config.ServerID());
                            param.put("gameid", Config.GameID());
                            param.put("world_id", Integer.getInteger("world_sid", 0));
                            HttpUtils.NotifyGM(url, param);
                        } catch (Exception var2) {
                            CommLog.error("给GM后台发送心跳包发生错误", (Throwable)var2);
                        }

                        SyncTaskManager.task(this, 10000);
                    }
                });
            }
        }
    }
}
