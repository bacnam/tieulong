/*    */ package com.zhonglian.server.logger.flow.redis;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.utils.RedisUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import redis.clients.jedis.Jedis;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RedisFlowMgr
/*    */ {
/*    */   private static RedisFlowMgr instance;
/*    */   
/*    */   public static RedisFlowMgr getInstance() {
/* 22 */     if (instance == null) {
/* 23 */       instance = new RedisFlowMgr();
/*    */     }
/* 25 */     return instance;
/*    */   }
/*    */   
/* 28 */   private Map<String, List<String>> logFlows = new HashMap<>();
/*    */   
/*    */   public void add(JsonObject json) {
/* 31 */     String key = json.get("srv").getAsString();
/* 32 */     List<String> list = this.logFlows.get(key);
/* 33 */     if (list == null) {
/* 34 */       list = new ArrayList<>();
/* 35 */       this.logFlows.put(key, list);
/*    */     } 
/* 37 */     synchronized (list) {
/* 38 */       list.add(json.toString());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void logAll() {
/* 44 */     List<List<String>> loggers = new ArrayList<>(this.logFlows.values());
/* 45 */     for (List<String> list : loggers) {
/* 46 */       if (list.size() <= 0) {
/*    */         continue;
/*    */       }
/* 49 */       Jedis jedis = RedisUtil.getJedis();
/*    */       try {
/* 51 */         synchronized (list) {
/*    */           try {
/* 53 */             jedis.rpush(System.getProperty("Redis.KEY"), list.<String>toArray(new String[list.size()]));
/* 54 */           } catch (Exception e) {
/* 55 */             CommLog.warn("日志发送失败:{}", e.toString());
/*    */           } 
/* 57 */           list.clear();
/*    */         } 
/* 59 */       } catch (Exception e) {
/* 60 */         CommLog.error("[RedisFlowLoggerMgr]batch save [{}] log error message:{}", new Object[] { "", e.getMessage(), e }); continue;
/*    */       } finally {
/* 62 */         RedisUtil.returnResource(jedis);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 69 */     SyncTaskManager.schedule(200, () -> {
/*    */           logAll();
/*    */           return true;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/flow/redis/RedisFlowMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */