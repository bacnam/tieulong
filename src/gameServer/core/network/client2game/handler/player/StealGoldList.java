/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import business.player.feature.pvp.StealGoldFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.config.refdata.ref.RefVIP;
/*    */ import core.database.game.bo.StealGoldNewsBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class StealGoldList
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class StealGoldInfo
/*    */   {
/*    */     List<StealGoldList.StealGoldEnemy> players;
/*    */     List<Long> money;
/*    */     int nowTimes;
/*    */     int maxTimes;
/*    */     
/*    */     private StealGoldInfo(List<StealGoldList.StealGoldEnemy> players, List<Long> money, List<StealGoldNewsBO> news, int nowTimes, int maxTimes) {
/* 32 */       this.players = players;
/* 33 */       this.money = money;
/* 34 */       this.nowTimes = nowTimes;
/* 35 */       this.maxTimes = maxTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private static class StealGoldEnemy
/*    */   {
/*    */     Player.Summary player;
/*    */     boolean isEnemy;
/*    */     
/*    */     private StealGoldEnemy(Player.Summary player, boolean isEnemy) {
/* 46 */       this.player = player;
/* 47 */       this.isEnemy = isEnemy;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 54 */     StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);
/* 55 */     List<Long> faighters = feature.getList();
/* 56 */     List<StealGoldEnemy> players = new ArrayList<>();
/* 57 */     for (Iterator<Long> iterator = faighters.iterator(); iterator.hasNext(); ) { long i = ((Long)iterator.next()).longValue();
/* 58 */       Player tmp_player = PlayerMgr.getInstance().getPlayer(i);
/* 59 */       if (((DroiyanFeature)player.getFeature(DroiyanFeature.class)).isEnemy(i)) {
/* 60 */         players.add(new StealGoldEnemy(((PlayerBase)tmp_player.getFeature(PlayerBase.class)).summary(), true, null)); continue;
/*    */       } 
/* 62 */       players.add(new StealGoldEnemy(((PlayerBase)tmp_player.getFeature(PlayerBase.class)).summary(), false, null)); }
/*    */ 
/*    */ 
/*    */     
/* 66 */     List<StealGoldNewsBO> news = feature.getNews();
/* 67 */     int nowTime = feature.getTimes();
/* 68 */     int maxTimes = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).StealGold;
/* 69 */     List<Long> money = new ArrayList<>();
/* 70 */     for (Iterator<Integer> iterator1 = feature.getMoneyList().iterator(); iterator1.hasNext(); ) { int ext = ((Integer)iterator1.next()).intValue();
/* 71 */       RefCrystalPrice prize = RefCrystalPrice.getPrize(nowTime);
/* 72 */       money.add(Long.valueOf(prize.StealGoldGain * (1000 + ext) / 1000L)); }
/*    */     
/* 74 */     request.response(new StealGoldInfo(players, money, news, nowTime, maxTimes, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/StealGoldList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */