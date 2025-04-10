package com.zhonglian.server.logger.flow.redis;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.utils.RedisUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;

public class RedisFlowMgr
{
private static RedisFlowMgr instance;

public static RedisFlowMgr getInstance() {
if (instance == null) {
instance = new RedisFlowMgr();
}
return instance;
}

private Map<String, List<String>> logFlows = new HashMap<>();

public void add(JsonObject json) {
String key = json.get("srv").getAsString();
List<String> list = this.logFlows.get(key);
if (list == null) {
list = new ArrayList<>();
this.logFlows.put(key, list);
} 
synchronized (list) {
list.add(json.toString());
} 
}

public void logAll() {
List<List<String>> loggers = new ArrayList<>(this.logFlows.values());
for (List<String> list : loggers) {
if (list.size() <= 0) {
continue;
}
Jedis jedis = RedisUtil.getJedis();
try {
synchronized (list) {
try {
jedis.rpush(System.getProperty("Redis.KEY"), list.<String>toArray(new String[list.size()]));
} catch (Exception e) {
CommLog.warn("日志发送失败:{}", e.toString());
} 
list.clear();
} 
} catch (Exception e) {
CommLog.error("[RedisFlowLoggerMgr]batch save [{}] log error message:{}", new Object[] { "", e.getMessage(), e }); continue;
} finally {
RedisUtil.returnResource(jedis);
} 
} 
}

public void start() {
SyncTaskManager.schedule(200, () -> {
logAll();
return true;
});
}
}

