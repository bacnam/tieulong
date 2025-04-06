/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefAchievement;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AchievePickUpPrize
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int achieveId;
/*    */     int achieveCount;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 26 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 27 */     int achieveID = req.achieveId;
/* 28 */     RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
/*    */ 
/*    */ 
/*    */     
/* 32 */     AchievementFeature achievementContainer = (AchievementFeature)player.getFeature(AchievementFeature.class);
/* 33 */     Reward reward = achievementContainer.cmd_pickUpPrize(achieveID, req.achieveCount);
/* 34 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/AchievePickUpPrize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */