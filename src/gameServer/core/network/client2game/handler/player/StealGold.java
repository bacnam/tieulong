/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.pvp.StealGoldFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.common.utils.Random;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class StealGold
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request {
/*    */     long targetPid;
/*    */   }
/*    */   
/*    */   private static class StealGoldInfo {
/*    */     int nowTimes;
/*    */     Reward reward;
/*    */     List<Long> money;
/*    */     
/*    */     private StealGoldInfo(int nowTimes, Reward reward, List<Long> money) {
/* 36 */       this.nowTimes = nowTimes;
/* 37 */       this.reward = reward;
/* 38 */       this.money = money;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 45 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 46 */     StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);
/* 47 */     if (!feature.getList().contains(Long.valueOf(req.targetPid))) {
/* 48 */       throw new WSException(ErrorCode.StealGold_NotFound, "人物不存在");
/*    */     }
/* 50 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/*    */     
/* 52 */     int times = feature.getTimes();
/* 53 */     if (!feature.checkTimes()) {
/* 54 */       throw new WSException(ErrorCode.StealGold_NotEnough, "探金手次数不足");
/*    */     }
/*    */     
/* 57 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
/* 58 */     if (!currency.check(PrizeType.Crystal, prize.StealGoldPrice)) {
/* 59 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次探金手需要钻石%s", new Object[] { Integer.valueOf(times), Integer.valueOf(prize.StealGoldPrice) });
/*    */     }
/* 61 */     currency.consume(PrizeType.Crystal, prize.StealGoldPrice, ItemFlow.StealGold);
/* 62 */     long get = 0L;
/* 63 */     int index = feature.getList().indexOf(Long.valueOf(req.targetPid));
/* 64 */     long money = prize.StealGoldGain * (1000 + ((Integer)feature.getMoneyList().get(index)).intValue()) / 1000L;
/* 65 */     if (Random.nextInt(10000) < RefDataMgr.getFactor("StealGoldCrit", 5000)) {
/* 66 */       get = money * 2L;
/*    */     } else {
/* 68 */       get = money;
/*    */     } 
/*    */     
/* 71 */     int gain = currency.gain(PrizeType.Gold, (int)get, ItemFlow.StealGold);
/*    */ 
/*    */     
/* 74 */     feature.addTimes();
/*    */ 
/*    */     
/* 77 */     Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
/* 78 */     ((StealGoldFeature)target.getFeature(StealGoldFeature.class)).addNews(player.getPid(), gain);
/* 79 */     Reward reward = new Reward();
/* 80 */     reward.add(PrizeType.Gold, gain);
/*    */     
/* 82 */     int nowTime = feature.getTimes();
/* 83 */     List<Long> moneylist = new ArrayList<>();
/* 84 */     for (Iterator<Integer> iterator = feature.getMoneyList().iterator(); iterator.hasNext(); ) { int ext = ((Integer)iterator.next()).intValue();
/* 85 */       RefCrystalPrice tmp_prize = RefCrystalPrice.getPrize(nowTime);
/* 86 */       moneylist.add(Long.valueOf(tmp_prize.StealGoldGain * (1000 + ext) / 1000L)); }
/*    */ 
/*    */     
/* 89 */     request.response(new StealGoldInfo(feature.getTimes(), reward, moneylist, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/StealGold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */