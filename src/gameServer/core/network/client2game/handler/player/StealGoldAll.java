/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.pvp.StealGoldFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.config.refdata.ref.RefVIP;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class StealGoldAll
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class StealGoldInfo
/*    */   {
/*    */     int nowTimes;
/*    */     Reward reward;
/*    */     
/*    */     private StealGoldInfo(int nowTimes, Reward reward) {
/* 29 */       this.nowTimes = nowTimes;
/* 30 */       this.reward = reward;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 37 */     StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);
/* 38 */     if (!feature.checkTimes()) {
/* 39 */       throw new WSException(ErrorCode.StealGold_NotEnough, "探金手次数不足");
/*    */     }
/*    */     
/* 42 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 43 */     int nowTime = feature.getTimes();
/* 44 */     int maxTimes = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).StealGold;
/*    */     
/* 46 */     int cost = 0;
/* 47 */     long gain = 0L;
/* 48 */     for (int i = nowTime; i < maxTimes; i++) {
/* 49 */       cost += (RefCrystalPrice.getPrize(i)).StealGoldPrice;
/* 50 */       gain += (RefCrystalPrice.getPrize(i)).StealGoldGain;
/*    */     } 
/*    */     
/* 53 */     if (!currency.check(PrizeType.Crystal, cost)) {
/* 54 */       throw new WSException(ErrorCode.NotEnough_Crystal, "探金手需要钻石%s", new Object[] { Integer.valueOf(cost) });
/*    */     }
/* 56 */     currency.consume(PrizeType.Crystal, cost, ItemFlow.StealGold);
/* 57 */     long get = gain * (10000 + RefDataMgr.getFactor("StealGoldCrit", 5000)) / 10000L;
/*    */ 
/*    */     
/* 60 */     feature.addTimes(maxTimes - nowTime);
/*    */     
/* 62 */     currency.gain(PrizeType.Gold, (int)get, ItemFlow.StealGold);
/*    */     
/* 64 */     for (int j = 0; j < maxTimes - nowTime; j++) {
/*    */       
/* 66 */       Player target = PlayerMgr.getInstance().getPlayer(((Long)feature.getList().get(j % 4)).longValue());
/* 67 */       ((StealGoldFeature)target.getFeature(StealGoldFeature.class)).addNews(player.getPid(), (int)(get / maxTimes - nowTime));
/*    */     } 
/*    */     
/* 70 */     Reward reward = new Reward();
/* 71 */     reward.add(PrizeType.Gold, (int)get);
/* 72 */     request.response(new StealGoldInfo(feature.getTimes(), reward, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/StealGoldAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */