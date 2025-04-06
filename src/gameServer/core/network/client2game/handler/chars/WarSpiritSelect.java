/*    */ package core.network.client2game.handler.chars;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.WarSpirit;
/*    */ import business.player.feature.character.WarSpiritFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.WarSpiritInfo;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class WarSpiritSelect
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int spiritId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 25 */     WarSpirit warSpirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpirit(req.spiritId);
/* 26 */     if (warSpirit == null) {
/* 27 */       throw new WSException(ErrorCode.Char_NotFound, "战灵[%s]不存在或未解锁", new Object[] { Integer.valueOf(req.spiritId) });
/*    */     }
/* 29 */     ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).setWarSpiritNow(warSpirit);
/*    */ 
/*    */     
/* 32 */     warSpirit.onAttrChanged();
/*    */     
/* 34 */     request.response(new WarSpiritInfo(warSpirit));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/chars/WarSpiritSelect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */