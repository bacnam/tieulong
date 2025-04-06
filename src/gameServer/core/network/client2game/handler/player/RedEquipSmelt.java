/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.character.Equip;
/*    */ import business.player.feature.character.EquipFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.common.enums.Quality;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefSmelt;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class RedEquipSmelt
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long equipSid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 30 */     EquipFeature feature = (EquipFeature)player.getFeature(EquipFeature.class);
/* 31 */     Equip equip = feature.getEquip(req.equipSid);
/* 32 */     if (equip == null) {
/* 33 */       throw new WSException(ErrorCode.Equip_NotFound, "装备[%s]不存在", new Object[] { Long.valueOf(req.equipSid) });
/*    */     }
/* 35 */     if (equip.getRef().getQuality() != Quality.Red) {
/* 36 */       throw new WSException(ErrorCode.Equip_NotRed, "装备[%s]不是神装", new Object[] { Long.valueOf(req.equipSid) });
/*    */     }
/* 38 */     if (equip.getOwner() != null) {
/* 39 */       throw new WSException(ErrorCode.Equip_Equiped, "装备在身上不能熔炼", new Object[] { Long.valueOf(req.equipSid) });
/*    */     }
/* 41 */     feature.consume(equip);
/* 42 */     RefSmelt refsmelt = (RefSmelt)RefDataMgr.get(RefSmelt.class, String.valueOf(equip.getRef().getQuality().toString()) + equip.getLevel());
/* 43 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 44 */     playerCurrency.gain(PrizeType.RedPiece, refsmelt.RedPiece, ItemFlow.Smelt);
/* 45 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/RedEquipSmelt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */