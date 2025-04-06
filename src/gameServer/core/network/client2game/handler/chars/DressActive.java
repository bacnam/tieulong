/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.character.DressFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.UnlockType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDress;
/*    */ import core.config.refdata.ref.RefUnlockFunction;
/*    */ import core.database.game.bo.DressBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DressActive
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int dressId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 31 */     RefUnlockFunction refunlock = (RefUnlockFunction)RefDataMgr.get(RefUnlockFunction.class, UnlockType.Dress);
/* 32 */     if (refunlock.UnlockLevel > player.getLv()) {
/* 33 */       throw new WSException(ErrorCode.NotEnough_UnlockCond, "解锁条件不足");
/*    */     }
/* 35 */     DressFeature feature = (DressFeature)player.getFeature(DressFeature.class);
/* 36 */     DressBO bo = feature.getDressByDressId(req.dressId);
/* 37 */     if (bo != null) {
/* 38 */       throw new WSException(ErrorCode.Dress_AlreadyActive, "已激活");
/*    */     }
/*    */     
/* 41 */     RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(req.dressId));
/* 42 */     if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(ref.Material, ref.Count, ItemFlow.ActiveDress)) {
/* 43 */       throw new WSException(ErrorCode.NotEnough_Crystal, "材料不足");
/*    */     }
/* 45 */     feature.gainAndEquip(req.dressId, 1, ItemFlow.ActiveDress);
/* 46 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/DressActive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */