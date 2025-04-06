/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.BossFight;
/*    */ import business.global.fight.Fight;
/*    */ import business.global.fight.FightFactory;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DungeonBossBegin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class DungeonBossBeginInfo
/*    */   {
/*    */     Fight.Begin fight;
/*    */     int rebirthTime;
/*    */     
/*    */     public DungeonBossBeginInfo(Fight.Begin fight, int rebirthTime) {
/* 25 */       this.fight = fight;
/* 26 */       this.rebirthTime = rebirthTime;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 33 */     BossFight fight = FightFactory.createFight(player, 0);
/* 34 */     FightManager.getInstance().pushFight((Fight)fight);
/*    */     
/* 36 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 37 */     recorder.setValue(ConstEnum.DailyRefresh.DungeonRebirth, 0);
/*    */     
/* 39 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.DungeonRebirth);
/*    */     
/* 41 */     request.response(new DungeonBossBeginInfo(new Fight.Begin(fight.getId()), curTimes));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DungeonBossBegin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */