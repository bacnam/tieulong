/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.UnlockType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefUnlockFunction;
/*    */ import core.config.refdata.ref.RefVIP;
/*    */ import core.database.game.bo.ArenaCompetitorBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Arena;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArenaInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int rank;
/*    */     int refreshCD;
/*    */     int fightCD;
/*    */     int challengeTimes;
/*    */     int buyTimes;
/*    */     int remainBuyTimes;
/*    */     int clearFightCDTimes;
/*    */     int clearRefreshCDTimes;
/*    */     List<Arena.CompetitorInfo> opponents;
/*    */     
/*    */     public Response(Competitor competitor, int remains, int buyTimes, int remainBuyTimes, int clearFightCDTimes, int clearRefreshCDTimes, List<Competitor> opponents) {
/* 41 */       ArenaCompetitorBO bo = competitor.getBo();
/* 42 */       this.rank = bo.getRank();
/* 43 */       this.refreshCD = competitor.getRefreshCD();
/* 44 */       this.fightCD = competitor.getFightCD();
/* 45 */       this.challengeTimes = remains;
/* 46 */       this.buyTimes = buyTimes;
/* 47 */       this.remainBuyTimes = remainBuyTimes;
/* 48 */       this.clearFightCDTimes = clearFightCDTimes;
/* 49 */       this.clearRefreshCDTimes = clearRefreshCDTimes;
/* 50 */       this.opponents = new ArrayList<>();
/* 51 */       for (Competitor opp : opponents) {
/* 52 */         this.opponents.add(new Arena.CompetitorInfo(opp));
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 60 */     RefUnlockFunction.checkUnlock(player, UnlockType.Arena);
/* 61 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 63 */     Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
/* 64 */     int remains = ArenaConfig.maxChallengeTimes() - recorder.getValue(ConstEnum.DailyRefresh.ArenaChallenge);
/*    */     
/* 66 */     int times1 = recorder.getValue(ConstEnum.DailyRefresh.ArenaBuyChallengeTimes);
/* 67 */     int remainBuyTimes = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).ArenalTimes - times1;
/* 68 */     int times2 = recorder.getValue(ConstEnum.DailyRefresh.ArenaResetFightCD);
/* 69 */     int times3 = recorder.getValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD);
/*    */     
/* 71 */     request.response(new Response(competitor, remains, times1, remainBuyTimes, times2, times3, competitor.getOpponents()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */