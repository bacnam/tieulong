/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HeartBeat
/*    */   extends BaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/* 13 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/HeartBeat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */