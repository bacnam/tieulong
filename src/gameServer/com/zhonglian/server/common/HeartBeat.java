/*    */ package com.zhonglian.server.common;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTask;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.zhonglian.server.http.server.GMParam;
/*    */ import com.zhonglian.server.http.server.HttpUtils;
/*    */ 
/*    */ 
/*    */ public class HeartBeat
/*    */ {
/*    */   private static boolean started = false;
/*    */   
/*    */   public static void start() {
/* 15 */     if (started) {
/*    */       return;
/*    */     }
/* 18 */     final String url = Config.GmHeartBeatAddr();
/* 19 */     if (url == null || url.isEmpty() || url.indexOf("//////") != -1) {
/*    */       return;
/*    */     }
/*    */     
/* 23 */     SyncTaskManager.task(new SyncTask()
/*    */         {
/*    */           public void run() {
/*    */             try {
/* 27 */               GMParam param = new GMParam();
/* 28 */               param.put("server_id", Integer.valueOf(Config.ServerID()));
/* 29 */               param.put("gameid", Integer.valueOf(Config.GameID()));
/* 30 */               param.put("world_id", Integer.getInteger("world_sid", 0));
/* 31 */               HttpUtils.NotifyGM(url, param);
/* 32 */             } catch (Exception e) {
/* 33 */               CommLog.error("给GM后台发送心跳包发生错误", e);
/*    */             } 
/* 35 */             SyncTaskManager.task(this, 10000);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/HeartBeat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */