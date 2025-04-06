/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.BossFight;
/*    */ import business.global.fight.Fight;
/*    */ import business.global.fight.FightFactory;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefSkipDungeon;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DungeonBossSkipBegin
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Request
/*    */   {
/*    */     int level;
/*    */     boolean isRebirth;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 33 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 35 */     RefSkipDungeon ref = (RefSkipDungeon)RefSkipDungeon.LevelMap.get(Integer.valueOf(req.level));
/* 36 */     if (ref == null) {
/* 37 */       throw new WSException(ErrorCode.Dungeon_NotFound, "副本不存在");
/*    */     }
/* 39 */     if (!req.isRebirth && !((PlayerCurrency)player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Crystal, ref.Cost, ItemFlow.Dungeon_Skip)) {
/* 40 */       throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
/*    */     }
/* 42 */     BossFight fight = FightFactory.createFight(player, req.level);
/* 43 */     FightManager.getInstance().pushFight((Fight)fight);
/* 44 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 46 */     recorder.setValue(ConstEnum.DailyRefresh.DungeonRebirth, 0);
/* 47 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);
/*    */     
/* 49 */     request.response(new DungeonBossBegin.DungeonBossBeginInfo(new Fight.Begin(fight.getId()), curTimes));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DungeonBossSkipBegin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */