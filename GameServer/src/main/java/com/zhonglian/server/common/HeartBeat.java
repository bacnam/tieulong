package com.zhonglian.server.common;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.http.server.GMParam;
import com.zhonglian.server.http.server.HttpUtils;

public class HeartBeat
{
private static boolean started = false;

public static void start() {
if (started) {
return;
}
final String url = Config.GmHeartBeatAddr();
if (url == null || url.isEmpty() || url.indexOf("
return;
}

SyncTaskManager.task(new SyncTask()
{
public void run() {
try {
GMParam param = new GMParam();
param.put("server_id", Integer.valueOf(Config.ServerID()));
param.put("gameid", Integer.valueOf(Config.GameID()));
param.put("world_id", Integer.getInteger("world_sid", 0));
HttpUtils.NotifyGM(url, param);
} catch (Exception e) {
CommLog.error("给GM后台发送心跳包发生错误", e);
} 
SyncTaskManager.task(this, 10000);
}
});
}
}

