/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.fight.ArenaFight;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ArenaEndFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Response
/*    */   {
/*    */     Reward reward;
/*    */     int rank;
/*    */     
/*    */     public Response(Integer rank, Reward reward) {
/* 28 */       this.reward = reward;
/* 29 */       this.rank = rank.intValue();
/*    */     }
/*    */   }
/*    */   
/* 33 */   private ArenaManager manager = null;
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 37 */     Fight.End req = (Fight.End)(new Gson()).fromJson(message, Fight.End.class);
/* 38 */     if (this.manager == null) {
/* 39 */       this.manager = ArenaManager.getInstance();
/*    */     }
/* 41 */     ArenaFight fight = (ArenaFight)FightManager.getInstance().popFight(req.fightId);
/* 42 */     if (fight == null) {
/* 43 */       throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(req.fightId) });
/*    */     }
/* 45 */     if (req.result == FightResult.Win) {
/* 46 */       fight.check(req.checks);
/*    */     }
/* 48 */     if (fight.getAtkPid() != player.getPid()) {
/* 49 */       throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能提交战斗[%s]", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(req.fightId) });
/*    */     }
/* 51 */     Reward reward = (Reward)fight.settle(req.result);
/*    */     
/* 53 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes);
/* 54 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes_M1);
/* 55 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ArenaFightTimes_M2);
/*    */     
/* 57 */     int rank = this.manager.getOrCreate(player.getPid()).getRank();
/* 58 */     request.response(new Response(Integer.valueOf(rank), reward));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaEndFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */