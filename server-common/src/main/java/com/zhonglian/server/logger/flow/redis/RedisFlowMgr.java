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

public class RedisFlowMgr {
    private static RedisFlowMgr instance;
    private Map<String, List<String>> logFlows = new HashMap<>();

    private RedisFlowMgr() {
    }

    public static RedisFlowMgr getInstance() {
        if (instance == null) {
            instance = new RedisFlowMgr();
        }

        return instance;
    }

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
        for (List<String> list : new ArrayList<>(this.logFlows.values())) {
            if (list.size() > 0) {
                Jedis jedis = RedisUtil.getJedis();

                try {
                    synchronized (list) {
                        try {
                            jedis.rpush(System.getProperty("Redis.KEY"), list.toArray(new String[list.size()]));
                        } catch (Exception var12) {
                            CommLog.warn("日志发送失败:{}", var12.toString());
                        }

                        list.clear();
                    }
                } catch (Exception var14) {
                    CommLog.error("[RedisFlowLoggerMgr]batch save [{}] log error message:{}", "", var14.getMessage(), var14);
                } finally {
                    RedisUtil.returnResource(jedis);
                }
            }
        }
    }

    public void start() {
        SyncTaskManager.schedule(200, () -> {
            this.logAll();
            return true;
        });
    }
}
