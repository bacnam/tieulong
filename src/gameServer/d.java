/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.AsynTask.AsyncTaskManager;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.http.client.HttpUtil;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefAchievement;
/*    */ import core.database.game.bo.PlayerBO;
/*    */ import core.server.GameServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class d
/*    */ {
/*    */   public static void time() {
/* 24 */     System.out.println(CommTime.getTodayZeroClockS());
/* 25 */     System.out.println(CommTime.getTodayZeroClockS() + 2592000);
/* 26 */     System.out.println(CommTime.getTodayZeroClockS() + 2678400);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void t() throws Exception {
/* 31 */     int MB = 1049600;
/* 32 */     long total = Runtime.getRuntime().totalMemory() / MB;
/* 33 */     long free = Runtime.getRuntime().freeMemory() / MB;
/* 34 */     long max = Runtime.getRuntime().maxMemory() / MB;
/* 35 */     long usable = max - total + free;
/* 36 */     CommLog.info(String.format("MemoryMonitor total：%10sMB free：%10sMB maxavail：%10sMB useable：%10sMB", new Object[] { Long.valueOf(total), Long.valueOf(free), Long.valueOf(max), Long.valueOf(usable) }));
/*    */   }
/*    */ 
/*    */   
/*    */   public static void gc(int time) throws Exception {
/* 41 */     PlayerMgr.getInstance().releasPlayer(time);
/* 42 */     System.gc();
/* 43 */     t();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void t2(int achieveID, int count) throws Exception {
/* 48 */     Player player = PlayerMgr.getInstance().getPlayer(2750000000000082L);
/* 49 */     if (player == null) {
/*    */       return;
/*    */     }
/* 52 */     RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
/* 53 */     if (ref == null) {
/*    */       return;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static void send() {
/* 60 */     JsonObject param = new JsonObject();
/* 61 */     param.addProperty("cid", "11");
/* 62 */     param.addProperty("buntime", "-1");
/* 63 */     HttpUtil.sendHttpPost2Web(3000, 3000, "http://127.0.0.1:9888/game/player/bannedLogin", param.toString(), "");
/*    */   }
/*    */   
/*    */   public static void taskamount() {
/* 67 */     StringBuilder sBuilder = new StringBuilder();
/* 68 */     sBuilder.append("ThreadMonitor\n");
/* 69 */     sBuilder.append(SyncTaskManager.getInstance().toString());
/* 70 */     sBuilder.append(AsyncTaskManager.getInstance().toString());
/* 71 */     CommLog.warn(sBuilder.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void reload() {
/* 78 */     GameServer.reload();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void player() {
/* 83 */     PlayerBO newBO = new PlayerBO();
/* 84 */     newBO.setOpenId("new" + CommTime.nowMS());
/* 85 */     newBO.setSid(1);
/* 86 */     newBO.setName("123");
/* 87 */     PlayerMgr.getInstance().initPlayerBO(newBO, 1);
/* 88 */     PlayerMgr.getInstance().createPlayer(newBO);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void player(int cid) {
/* 93 */     CommLog.error(PlayerMgr.getInstance().getOnlinePlayers().size());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/d.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */