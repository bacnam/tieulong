/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.character.WarSpiritFeature;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWarSpiritTalent;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WarSpiritTalent
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class MeridianNotify
/*    */   {
/*    */     long warspiritTalent;
/*    */     
/*    */     public MeridianNotify(int warspiritTalent) {
/* 24 */       this.warspiritTalent = warspiritTalent;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     int nowLevel = player.getPlayerBO().getWarspiritTalent();
/* 32 */     if (nowLevel >= RefDataMgr.size(RefWarSpiritTalent.class) - 1) {
/* 33 */       throw new WSException(ErrorCode.WarSpiritTalentFull, "天赋已满级");
/*    */     }
/* 35 */     RefWarSpiritTalent ref = (RefWarSpiritTalent)RefDataMgr.get(RefWarSpiritTalent.class, Integer.valueOf(nowLevel + 1));
/*    */ 
/*    */     
/* 38 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 39 */     if (!playerCurrency.check(PrizeType.WarspiritTalentMaterial, ref.Material)) {
/* 40 */       throw new WSException(ErrorCode.NotEnough_WarSpiritTalent, "玩家材料:%s<升级材料:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getWarspiritTalentMaterial()), Integer.valueOf(ref.Material) });
/*    */     }
/*    */     
/* 43 */     playerCurrency.consume(PrizeType.WarspiritTalentMaterial, ref.Material, ItemFlow.WarSpiritTalent);
/*    */ 
/*    */     
/* 46 */     player.getPlayerBO().saveWarspiritTalent(nowLevel + 1);
/*    */     
/* 48 */     ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).updatePower();
/*    */ 
/*    */     
/* 51 */     MeridianNotify notify = new MeridianNotify(player.getPlayerBO().getWarspiritTalent());
/* 52 */     request.response(notify);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WarSpiritTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */