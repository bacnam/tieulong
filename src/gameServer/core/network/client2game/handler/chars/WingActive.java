/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.RankWing;
/*    */ import business.global.rank.RankManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWingActive;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WingActive
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int charId;
/*    */     int refId;
/*    */   }
/*    */   
/*    */   private static class WingNotify
/*    */   {
/*    */     int charId;
/*    */     long wingLevel;
/*    */     long wingExp;
/*    */     
/*    */     private WingNotify(int charId, long wingLevel, long wingExp) {
/* 38 */       this.charId = charId;
/* 39 */       this.wingLevel = wingLevel;
/* 40 */       this.wingExp = wingExp;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 46 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 47 */     Character character = ((CharFeature)player.getFeature(CharFeature.class)).getCharacter(req.charId);
/* 48 */     if (character == null) {
/* 49 */       throw new WSException(ErrorCode.Char_NotFound, "角色[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.charId) });
/*    */     }
/*    */     
/* 52 */     int wingLevel = 0;
/* 53 */     for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 54 */       wingLevel += charac.getBo().getWing();
/*    */     }
/*    */     
/* 57 */     if (wingLevel != 0) {
/* 58 */       throw new WSException(ErrorCode.Wing_AlreadyActive, "翅膀已激活");
/*    */     }
/*    */     
/* 61 */     RefWingActive ref = (RefWingActive)RefDataMgr.get(RefWingActive.class, Integer.valueOf(req.refId));
/*    */     
/* 63 */     if (ref == null) {
/* 64 */       throw new WSException(ErrorCode.Wing_NotFound, "没有这个档位");
/*    */     }
/*    */     
/* 67 */     if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Crystal, ref.Discount, ItemFlow.WingActive)) {
/* 68 */       throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
/*    */     }
/*    */     
/* 71 */     character.getBo().saveWing(ref.Level);
/*    */     
/* 73 */     int wingLevelnow = 0;
/* 74 */     for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 75 */       wingLevelnow += charac.getBo().getWing();
/*    */     }
/*    */     
/* 78 */     character.onAttrChanged();
/*    */     
/* 80 */     RankManager.getInstance().update(RankType.WingLevel, player.getPid(), wingLevelnow);
/*    */     
/* 82 */     ((RankWing)ActivityMgr.getActivity(RankWing.class)).UpdateMaxRequire_cost(player, wingLevelnow);
/*    */ 
/*    */     
/* 85 */     WingNotify notify = new WingNotify(req.charId, character.getBo().getWing(), character.getBo().getWingExp(), null);
/* 86 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WingActive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */