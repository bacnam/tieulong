/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pve.InstanceFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.InstanceType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InstanceCheck
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     InstanceType type;
/*    */   }
/*    */   
/*    */   public static class InstanceInfo {
/*    */     int maxLevel;
/*    */     int challengTimes;
/*    */     int buyTimes;
/*    */     InstanceType type;
/*    */     
/*    */     public InstanceInfo(int maxLevel, int challengTimes, int buyTimes, InstanceType type) {
/* 28 */       this.maxLevel = maxLevel;
/* 29 */       this.challengTimes = challengTimes;
/* 30 */       this.buyTimes = buyTimes;
/* 31 */       this.type = type;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 38 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 39 */     int level = 0;
/* 40 */     int challengTimes = 0;
/* 41 */     InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
/* 42 */     level = feature.getOrCreate().getInstanceMaxLevel(req.type.ordinal());
/* 43 */     challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());
/*    */     
/* 45 */     ConstEnum.DailyRefresh dailyType = null;
/* 46 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 47 */     int times = 0;
/* 48 */     switch (req.type) {
/*    */       case null:
/* 50 */         dailyType = ConstEnum.DailyRefresh.EquipInstanceBuyTimes;
/* 51 */         times = recorder.getValue(dailyType);
/*    */         break;
/*    */       case GemInstance:
/* 54 */         dailyType = ConstEnum.DailyRefresh.GemInstanceBuyTimes;
/* 55 */         times = recorder.getValue(dailyType);
/*    */         break;
/*    */       case MeridianInstance:
/* 58 */         dailyType = ConstEnum.DailyRefresh.MeridianInstanceBuyTimes;
/* 59 */         times = recorder.getValue(dailyType);
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 66 */     request.response(new InstanceInfo(level, challengTimes, times, req.type));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/InstanceCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */