/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
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
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.config.refdata.ref.RefVIP;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ArenaBuyFightTime
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int times;
/*    */   }
/*    */   
/*    */   private static class buyTimesInfo {
/*    */     int remainChallengeTimes;
/*    */     int remainBuyTimes;
/*    */     int buyTimes;
/*    */     
/*    */     public buyTimesInfo(int remainChallengeTimes, int remainBuyTimes, int buyTimes) {
/* 36 */       this.remainChallengeTimes = remainChallengeTimes;
/* 37 */       this.remainBuyTimes = remainBuyTimes;
/* 38 */       this.buyTimes = buyTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 45 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 46 */     if (req.times == 0) {
/* 47 */       req.times = 1;
/*    */     }
/* 49 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 50 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 52 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.ArenaChallenge);
/* 53 */     if (curTimes - req.times < 0) {
/* 54 */       throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的挑战次数已满", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 56 */     int times = recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes);
/*    */     
/* 58 */     if (times + req.times > ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).ArenalTimes) {
/* 59 */       throw new WSException(ErrorCode.Arena_ChallengeTimesFull, "玩家[%s]的购买次数已达到最大值", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 61 */     int finalcount = 0;
/* 62 */     for (int i = 0; i < req.times; i++) {
/* 63 */       RefCrystalPrice prize = RefCrystalPrice.getPrize(times + i);
/* 64 */       finalcount += prize.ArenaAddChallenge;
/*    */     } 
/*    */     
/* 67 */     if (!currency.check(PrizeType.Crystal, finalcount)) {
/* 68 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", new Object[] { Integer.valueOf(finalcount) });
/*    */     }
/* 70 */     currency.consume(PrizeType.Crystal, finalcount, ItemFlow.ArenaAddChallenge);
/*    */     
/* 72 */     recorder.addValue(ConstEnum.DailyRefresh.ArenaChallenge, -req.times);
/* 73 */     recorder.addValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes, req.times);
/* 74 */     int remains = ArenaConfig.maxChallengeTimes() - ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.ArenaChallenge);
/* 75 */     int remainBuyTimes = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).ArenalTimes - recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes);
/*    */     
/* 77 */     Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
/* 78 */     competitor.setFightCD(0);
/* 79 */     request.response(new buyTimesInfo(remains, remainBuyTimes, recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaBuyFightTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */