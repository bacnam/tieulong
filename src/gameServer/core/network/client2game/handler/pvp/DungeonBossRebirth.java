/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.BossFight;
/*    */ import business.global.fight.Fight;
/*    */ import business.global.fight.FightFactory;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pve.DungeonFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDungeonRebirth;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DungeonBossRebirth
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Request
/*    */   {
/*    */     int level;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 34 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 35 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 36 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 38 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);
/* 39 */     RefDungeonRebirth ref = (RefDungeonRebirth)RefDataMgr.getOrLast(RefDungeonRebirth.class, Integer.valueOf(curTimes + 1));
/*    */     
/* 41 */     if (!currency.checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Rebirth)) {
/* 42 */       throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
/*    */     }
/*    */     
/* 45 */     BossFight fight = FightFactory.createFight(player, req.level);
/* 46 */     FightManager.getInstance().pushFight((Fight)fight);
/* 47 */     recorder.addValue(ConstEnum.DailyRefresh.DungeonRebirth);
/* 48 */     ((DungeonFeature)player.getFeature(DungeonFeature.class)).setRebirthRef(req.level, ref);
/* 49 */     request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DungeonBossRebirth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */