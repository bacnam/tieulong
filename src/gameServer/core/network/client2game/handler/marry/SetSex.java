/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class SetSex
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     ConstEnum.SexType type;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     ((MarryFeature)player.getFeature(MarryFeature.class)).setSex(req.type);
/* 24 */     request.response(((MarryFeature)player.getFeature(MarryFeature.class)).bo);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/SetSex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */