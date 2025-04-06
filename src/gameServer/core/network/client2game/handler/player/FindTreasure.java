/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.achievement.AchievementFeature;
/*    */ import business.player.feature.treasure.FindTreasureFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefTreasure;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class FindTreasure
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
/* 36 */       this.reward = reward;
/* 37 */       this.leftTimes = leftTimes;
/* 38 */       this.leftTentimes = leftTentimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 45 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 46 */     FindTreasureFeature feature = (FindTreasureFeature)player.getFeature(FindTreasureFeature.class);
/*    */     
/* 48 */     ConstEnum.FindTreasureType find = null;
/* 49 */     int times = 0;
/* 50 */     if (req.times == 1) {
/* 51 */       find = ConstEnum.FindTreasureType.single;
/* 52 */       times = RefDataMgr.getFactor("findTreasureTimes", 20);
/*    */     } 
/* 54 */     if (req.times == 10) {
/* 55 */       find = ConstEnum.FindTreasureType.Ten;
/* 56 */       times = RefDataMgr.getFactor("findTreasureTentimes", 2);
/*    */     } 
/*    */     
/* 59 */     if (times != -1 && feature.getLeftTimes(find) <= 0) {
/* 60 */       throw new WSException(ErrorCode.NotEnoughFindTimes, "玩家寻宝次数不足");
/*    */     }
/*    */     
/* 63 */     RefTreasure ref = feature.selectRef(player.getLv());
/* 64 */     int cost = 0;
/* 65 */     if (req.times == 1) {
/* 66 */       cost = ref.Price;
/* 67 */     } else if (req.times == 10) {
/* 68 */       cost = ref.TenPrice;
/*    */     } 
/* 70 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 71 */     if (!playerCurrency.check(PrizeType.Crystal, cost)) {
/* 72 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家元宝:%s<所需元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cost) });
/*    */     }
/*    */     
/* 75 */     playerCurrency.consume(PrizeType.Crystal, cost, ItemFlow.FindTreasure);
/* 76 */     Reward reward = feature.findTreasure(req.times);
/* 77 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.FindTreasure);
/*    */ 
/*    */     
/* 80 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.FindTreasureTimes, Integer.valueOf(req.times));
/* 81 */     ((AchievementFeature)player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.FindTreasureTimes_M1, Integer.valueOf(req.times));
/*    */     
/* 83 */     request.response(new Response(reward, feature.getLeftTimes(ConstEnum.FindTreasureType.single), feature.getLeftTimes(ConstEnum.FindTreasureType.Ten), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/FindTreasure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */