/*    */ package core.network.world2game.handler.player;
/*    */ 
/*    */ import business.global.chat.ChatMgr;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.world2game.handler.WBaseHandler;
/*    */ import proto.gameworld.ChatMessage;
/*    */ 
/*    */ public class WorldChat
/*    */   extends WBaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws WSException {
/* 16 */     ChatMessage req = (ChatMessage)(new Gson()).fromJson(message, ChatMessage.class);
/* 17 */     ChatMgr.getInstance().addAllWorldChat(req);
/* 18 */     for (Player p : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 19 */       p.pushProto("chat", req);
/*    */     }
/* 21 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/world2game/handler/player/WorldChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */