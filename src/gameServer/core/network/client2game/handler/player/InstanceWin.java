/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.feature.pve.InstanceFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.InstanceType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InstanceWin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     InstanceType type;
/*    */     int level;
/*    */     boolean sweep;
/*    */   }
/*    */   
/*    */   public static class WinReward {
/*    */     Reward reward;
/*    */     InstanceType type;
/*    */     int instanceMaxLevel;
/*    */     int challengTimes;
/*    */     
/*    */     public WinReward(Reward reward, InstanceType type, int instanceMaxLevel, int challengTimes) {
/* 31 */       this.reward = reward;
/* 32 */       this.type = type;
/* 33 */       this.instanceMaxLevel = instanceMaxLevel;
/* 34 */       this.challengTimes = challengTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 42 */     WinReward winReward = null;
/* 43 */     Reward reward = null;
/* 44 */     int level = 0;
/* 45 */     int challengTims = 0;
/* 46 */     InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
/* 47 */     feature.fightInstance(req.level, req.type);
/* 48 */     reward = feature.getReward(req.level, req.sweep, req.type);
/* 49 */     level = feature.getOrCreate().getInstanceMaxLevel(req.type.ordinal());
/* 50 */     challengTims = feature.getOrCreate().getChallengTimes(req.type.ordinal());
/*    */ 
/*    */     
/* 53 */     switch (req.type) {
/*    */       case null:
/* 55 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.EquipInstance);
/* 56 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.EquipInstance_M1);
/* 57 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.EquipInstance_M2);
/* 58 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.EquipInstance_M3);
/* 59 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.EquipInstance_M4);
/*    */         break;
/*    */ 
/*    */ 
/*    */       
/*    */       case GemInstance:
/* 65 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemInstance);
/* 66 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemInstance_M1);
/* 67 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.GemInstance_M2);
/*    */         break;
/*    */       case MeridianInstance:
/* 70 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianInstance);
/* 71 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianInstance_M1);
/* 72 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianInstance_M2);
/* 73 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianInstance_M3);
/* 74 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.MeridianInstance_M4);
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 80 */     winReward = new WinReward(reward, req.type, level, challengTims);
/* 81 */     request.response(winReward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/InstanceWin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */