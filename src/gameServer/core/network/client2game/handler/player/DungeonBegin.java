/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pve.DungeonFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDungeon;
/*    */ import core.config.refdata.ref.RefReward;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DungeonBegin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     DungeonFeature dungeon = (DungeonFeature)player.getFeature(DungeonFeature.class);
/*    */ 
/*    */     
/* 27 */     RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(dungeon.getLevel()));
/* 28 */     ((PlayerBase)player.getFeature(PlayerBase.class)).calOnlineTime();
/* 29 */     int onlineSecond = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.OnlineSecond);
/* 30 */     Reward reward = new Reward();
/* 31 */     if (onlineSecond >= RefDataMgr.getFactor("OnlineLimitSecond", 28800)) {
/* 32 */       int num = RefDataMgr.getFactor("OnlineRewardLimit", 5000);
/* 33 */       reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Drop))).genLimitReward(num);
/* 34 */       reward.add(PrizeType.Gold, ref.Gold * num / 10000);
/* 35 */       reward.add(PrizeType.Exp, ref.Exp * num / 10000);
/*    */     } else {
/* 37 */       reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Drop))).genReward();
/* 38 */       reward.add(PrizeType.Gold, ref.Gold);
/* 39 */       reward.add(PrizeType.Exp, ref.Exp);
/*    */     } 
/*    */     
/* 42 */     dungeon.beginFight(reward);
/* 43 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/DungeonBegin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */