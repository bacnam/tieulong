/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.treasure.WarSpiritTreasureFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefTreasureWarspirit;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WarspiritFindTreasure
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int times;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     Reward reward;
/*    */     int leftTimes;
/*    */     int leftTentimes;
/*    */     
/*    */     private Response(Reward reward, int leftTimes, int leftTentimes) {
/* 32 */       this.reward = reward;
/* 33 */       this.leftTimes = leftTimes;
/* 34 */       this.leftTentimes = leftTentimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 42 */     WarSpiritTreasureFeature feature = (WarSpiritTreasureFeature)player.getFeature(WarSpiritTreasureFeature.class);
/*    */     
/* 44 */     RefTreasureWarspirit ref = feature.selectRef(player.getLv());
/* 45 */     int cost = 0;
/* 46 */     if (req.times == 1) {
/* 47 */       cost = ref.Price;
/* 48 */     } else if (req.times == 10) {
/* 49 */       cost = ref.TenPrice;
/*    */     } 
/* 51 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 52 */     if (!playerCurrency.check(PrizeType.Crystal, cost)) {
/* 53 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家元宝:%s<所需元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cost) });
/*    */     }
/*    */     
/* 56 */     playerCurrency.consume(PrizeType.Crystal, cost, ItemFlow.FindTreasure);
/* 57 */     Reward reward = feature.findTreasure(req.times);
/* 58 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.FindTreasure);
/* 59 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/WarspiritFindTreasure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */