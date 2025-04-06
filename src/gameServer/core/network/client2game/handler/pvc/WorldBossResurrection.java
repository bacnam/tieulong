/*    */ package core.network.client2game.handler.pvc;
/*    */ 
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.worldboss.WorldBossFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.database.game.bo.WorldBossBO;
/*    */ import core.database.game.bo.WorldBossChallengeBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WorldBossResurrection
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 30 */     WorldBossChallengeBO challenBO = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate();
/*    */     
/* 32 */     int crystalCost = RefDataMgr.getFactor("WorldBossResurrection", 20) + 
/* 33 */       RefDataMgr.getFactor("WorldBossResurrectionAdd", 10) * challenBO.getResurrection();
/*    */ 
/*    */     
/* 36 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 37 */     if (!playerCurrency.check(PrizeType.Crystal, crystalCost)) {
/* 38 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝:%s<复活需要元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(crystalCost) });
/*    */     }
/*    */ 
/*    */     
/* 42 */     playerCurrency.consume(PrizeType.Crystal, crystalCost, ItemFlow.WorldBoss_Resurrection);
/*    */     
/* 44 */     challenBO.saveResurrection(challenBO.getResurrection() + 1);
/* 45 */     challenBO.saveLeaveFightTime(challenBO.getLeaveFightTime() - RefDataMgr.getFactor("WorldBossAttackCD", 30));
/* 46 */     WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
/* 47 */     if ((WorldBossMgr.getInstance()).fightingPlayers.get(boss) != null && !((List)(WorldBossMgr.getInstance()).fightingPlayers.get(boss)).contains(player))
/* 48 */       ((List<Player>)(WorldBossMgr.getInstance()).fightingPlayers.get(boss)).add(player); 
/* 49 */     request.response(challenBO);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossResurrection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */