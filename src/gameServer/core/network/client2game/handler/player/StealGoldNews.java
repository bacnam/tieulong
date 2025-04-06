/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.pvp.StealGoldFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.StealGoldNewsBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class StealGoldNews
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class StealGoldNewsInfo
/*    */   {
/*    */     String name;
/*    */     int money;
/*    */     int time;
/*    */     
/*    */     private StealGoldNewsInfo(StealGoldNewsBO bo) {
/* 24 */       this.name = PlayerMgr.getInstance().getPlayer(bo.getAtkid()).getName();
/* 25 */       this.money = bo.getMoney();
/* 26 */       this.time = bo.getTime();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 33 */     StealGoldFeature feature = (StealGoldFeature)player.getFeature(StealGoldFeature.class);
/*    */     
/* 35 */     List<StealGoldNewsInfo> list = new ArrayList<>();
/* 36 */     for (StealGoldNewsBO bo : feature.getNews()) {
/* 37 */       list.add(new StealGoldNewsInfo(bo, null));
/*    */     }
/*    */     
/* 40 */     request.response(list);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/StealGoldNews.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */