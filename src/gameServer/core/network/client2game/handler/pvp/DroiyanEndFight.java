/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.DroiyanFight;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.DroiyanRecordBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DroiyanEndFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Response
/*    */   {
/*    */     int point;
/*    */     int gold;
/*    */     int exp;
/*    */     int rob;
/*    */     int treasure;
/*    */     int red;
/*    */     
/*    */     public Response(DroiyanRecordBO reward, int red) {
/* 31 */       this.point = reward.getPoint();
/* 32 */       this.gold = reward.getGold();
/* 33 */       this.exp = reward.getExp();
/* 34 */       this.rob = reward.getRob();
/* 35 */       this.treasure = reward.getTreasure();
/* 36 */       this.red = red;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 42 */     Fight.End fightend = (Fight.End)(new Gson()).fromJson(message, Fight.End.class);
/* 43 */     DroiyanFight fight = (DroiyanFight)FightManager.getInstance().popFight(fightend.fightId);
/* 44 */     if (fight == null) {
/* 45 */       throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(fightend.fightId) });
/*    */     }
/* 47 */     if (fightend.result == FightResult.Win) {
/* 48 */       fight.check(fightend.checks);
/*    */     }
/* 50 */     if (fight.getAtkPid() != player.getPid()) {
/* 51 */       throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能提交战斗[%s]", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(fightend.fightId) });
/*    */     }
/* 53 */     DroiyanRecordBO reward = (DroiyanRecordBO)fight.settle(fightend.result);
/*    */ 
/*    */     
/* 56 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanFightTimes);
/* 57 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanFightTimes_M1);
/* 58 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanFightTimes_M2);
/* 59 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanFightTimes_M3);
/* 60 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanFightTimes_M4);
/*    */     
/* 62 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.DroiyanAttack);
/*    */     
/* 64 */     request.response(new Response(reward, fight.getAtkRed()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanEndFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */